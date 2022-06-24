package iudx.gis.server.authenticate;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import io.micrometer.core.ipc.http.HttpSender.Method;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import iudx.gis.server.authenticator.AuthenticationVerticle;
import iudx.gis.server.authenticator.JwtAuthenticationServiceImpl;
import iudx.gis.server.authenticator.authorization.Api;
import iudx.gis.server.authenticator.model.JwtData;
import iudx.gis.server.cache.CacheService;
import iudx.gis.server.configuration.Configuration;

@ExtendWith(VertxExtension.class)
public class JwtAuthServiceImplTest {
  private static final Logger LOGGER = LogManager.getLogger(JwtAuthServiceImplTest.class);
  private static JsonObject authConfig;
  private static JwtAuthenticationServiceImpl jwtAuthenticationService;
  private static Configuration config;
  private static String openId;
  private static String closeId;
  private static String invalidId;

  private static CacheService cacheServiceMock;

  @BeforeAll
  @DisplayName("Initialize Vertx and deploy Auth Verticle")
  static void init(Vertx vertx, VertxTestContext testContext) {
    config = new Configuration();
    authConfig = config.configLoader(1, vertx);

    authConfig.put("audience", "rs.iudx.io");
    authConfig.put("authServerHost", "authvertx.iudx.io");
    LOGGER.info("config : {}", authConfig);

    cacheServiceMock = Mockito.mock(CacheService.class);


    JWTAuthOptions jwtAuthOptions = new JWTAuthOptions();
    jwtAuthOptions.addPubSecKey(
        new PubSecKeyOptions().setAlgorithm("ES256").setBuffer("-----BEGIN PUBLIC KEY-----\n" +
            "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE8BKf2HZ3wt6wNf30SIsbyjYPkkTS\n" +
            "GGyyM2/MGF/zYTZV9Z28hHwvZgSfnbsrF36BBKnWszlOYW0AieyAUKaKdg==\n" +
            "-----END PUBLIC KEY-----\n" +
            ""));
    jwtAuthOptions
        .getJWTOptions()
        .setIgnoreExpiration(true); // ignore token expiration only for test

    JWTAuth jwtAuth = JWTAuth.create(vertx, jwtAuthOptions);
    jwtAuthenticationService =

        new JwtAuthenticationServiceImpl(vertx, jwtAuth, authConfig, cacheServiceMock);

    // since test token doesn't contain valid id's, so forcibly put some dummy id in cache
    // for test.
    openId =
        "iisc.ac.in/89a36273d77dac4cf38114fca1bbe64392547f86/rs.iudx.io/pune-env-flood";
    closeId =
        "iisc.ac.in/89a36273d77dac4cf38114fca1bbe64392547f86/rs.iudx.io/surat-itms-realtime-information";
    invalidId = "example.com/79e7bfa62fad6c765bac69154c2f24c94c95220a/resource-group1";

    jwtAuthenticationService.resourceIdCache.put(openId, "OPEN");
    jwtAuthenticationService.resourceIdCache.put(closeId, "CLOSED");
    jwtAuthenticationService.resourceIdCache.put(invalidId, "CLOSED");

    LOGGER.info("Auth tests setup complete");
    testContext.completeNow();

  }

  @Test
  @DisplayName("Testing setup")
  public void shouldSucceed(VertxTestContext testContext) {
    LOGGER.info("Default test is passing");
    testContext.completeNow();
  }

  @Test
  @DisplayName("success - allow access to all open endpoints")
  public void allow4OpenEndpoint(VertxTestContext testContext) {
    JsonObject authInfo = new JsonObject();

    authInfo.put("id", openId);
    authInfo.put("apiEndpoint", Api.ENTITIES.getApiEndpoint());
    authInfo.put("method", Method.GET);

    JwtData jwtData = new JwtData();
    jwtData.setIss("auth.test.com");
    jwtData.setAud("rs.iudx.io");
    jwtData.setExp(1627408865);
    jwtData.setIat(1627408865);
    jwtData.setIid("ri:foobar.iudx.io");
    jwtData.setRole("consumer");
    jwtData.setCons(new JsonObject().put("access", new JsonArray().add("api")));

    jwtAuthenticationService
        .validateAccess(jwtData, true, authInfo)
        .onComplete(
            handler -> {
              if (handler.succeeded()) {
                testContext.completeNow();
              } else {
                testContext.failNow("invalid access");
              }
            });
  }

  @Test
  @DisplayName("success - disallow access to closed endpoint for different id")
  public void disallow4ClosedEndpoint(VertxTestContext testContext) {
    JsonObject authInfo = new JsonObject();

    authInfo.put("token", JwtTokenHelper.closedConsumerApiToken);
    authInfo.put("id", invalidId);
    authInfo.put("apiEndpoint", Api.ENTITIES.getApiEndpoint());
    authInfo.put("method", Method.GET);

    JsonObject request = new JsonObject();


    AsyncResult<JsonObject> asyncResult = mock(AsyncResult.class);
    when(asyncResult.succeeded()).thenReturn(false);


    Mockito.doAnswer(new Answer<AsyncResult<JsonObject>>() {
      @SuppressWarnings("unchecked")
      @Override
      public AsyncResult<JsonObject> answer(InvocationOnMock arg0) throws Throwable {
        ((Handler<AsyncResult<JsonObject>>) arg0.getArgument(1)).handle(asyncResult);
        return null;
      }
    }).when(cacheServiceMock).get(any(), any());

    jwtAuthenticationService.tokenIntrospect(
        request,
        authInfo,
        handler -> {
          if (handler.succeeded()) {
            testContext.failNow("invalid access");
          } else {
            testContext.completeNow();
          }
        });
  }

  @Test
  @DisplayName("success - allow consumer access to /entities endpoint")
  public void success4ConsumerTokenEntitiesAPI(VertxTestContext testContext) {

    JsonObject request = new JsonObject();
    JsonObject authInfo = new JsonObject();

    authInfo.put("token", JwtTokenHelper.closedConsumerApiToken);
    authInfo.put("id", closeId);
    authInfo.put("apiEndpoint", Api.ENTITIES.getApiEndpoint());
    authInfo.put("method", Method.GET);

    jwtAuthenticationService.tokenIntrospect(
        request,
        authInfo,
        handler -> {
          if (handler.succeeded()) {
            testContext.completeNow();
          } else {
            testContext.failNow(handler.cause());
          }
        });
  }

  @Test
  @DisplayName("success - allow consumer access to /subscription endpoint")
  public void success4AdminTokenAPI(VertxTestContext testContext) {

    JsonObject request = new JsonObject();
    JsonObject authInfo = new JsonObject();

    authInfo.put("token", JwtTokenHelper.AdminToken);
    authInfo.put("apiEndpoint", "/admin/gis/serverInfo");
    authInfo.put("method", Method.POST);


    AsyncResult<JsonObject> asyncResult = mock(AsyncResult.class);
    when(asyncResult.succeeded()).thenReturn(false);


    Mockito.doAnswer(new Answer<AsyncResult<JsonObject>>() {
      @SuppressWarnings("unchecked")
      @Override
      public AsyncResult<JsonObject> answer(InvocationOnMock arg0) throws Throwable {
        ((Handler<AsyncResult<JsonObject>>) arg0.getArgument(1)).handle(asyncResult);
        return null;
      }
    }).when(cacheServiceMock).get(any(), any());

    jwtAuthenticationService.tokenIntrospect(
        request,
        authInfo,
        handler -> {
          if (handler.succeeded()) {
            testContext.completeNow();
          } else {
            testContext.failNow(handler.cause());
          }
        });
  }

  @Test
  @DisplayName("failure - admin-> consumer api access")
  public void failure4AdminTokenEntitiesAPI(VertxTestContext testContext) {

    JsonObject request = new JsonObject();
    JsonObject authInfo = new JsonObject();

    authInfo.put("token", JwtTokenHelper.AdminToken);
    authInfo.put("id", closeId);
    authInfo.put("apiEndpoint", Api.ENTITIES.getApiEndpoint());
    authInfo.put("method", Method.GET);

    jwtAuthenticationService.tokenIntrospect(
        request,
        authInfo,
        handler -> {
          if (handler.succeeded()) {
            testContext.failNow(handler.cause());
          } else {
            testContext.completeNow();
          }
        });
  }

  @Test
  @DisplayName("failure - consumer role -> admin access")
  public void consumerTokenAPI(VertxTestContext testContext) {

    JsonObject request = new JsonObject();
    JsonObject authInfo = new JsonObject();

    authInfo.put("token", JwtTokenHelper.closedConsumerApiToken);
    authInfo.put("id", closeId);
    authInfo.put("apiEndpoint", "/admin/gis/serverInfo");
    authInfo.put("method", Method.POST);

    jwtAuthenticationService.tokenIntrospect(
        request,
        authInfo,
        handler -> {
          if (handler.succeeded()) {
            testContext.failNow(handler.cause());
          } else {
            testContext.completeNow();
          }
        });
  }

  @Test
  @DisplayName("decode valid jwt - consumer")
  public void decodeJwtConsumerSuccess(VertxTestContext testContext) {
    jwtAuthenticationService
        .decodeJwt(JwtTokenHelper.closedConsumerApiToken)
        .onComplete(
            handler -> {
              if (handler.succeeded()) {
                assertEquals("consumer", handler.result().getRole());
                testContext.completeNow();
              } else {
                testContext.failNow(handler.cause());
              }
            });
  }

  @Test
  @DisplayName("decode invalid jwt")
  public void decodeJwtFailure(VertxTestContext testContext) {
    String jwt =
        "eyJ0eXAiOiJKV1QiLCJbGciOiJFUzI1NiJ9.eyJzdWIiOiJhM2U3ZTM0Yy00NGJmLTQxZmYtYWQ4Ni0yZWUwNGE5NTQ0MTgiLCJpc3MiOiJhdXRoLnRlc3QuY29tIiwiYXVkIjoiZm9vYmFyLml1ZHguaW8iLCJleHAiOjE2Mjc2ODk5NDAsImlhdCI6MTYyNzY0Njc0MCwiaWlkIjoicmc6ZXhhbXBsZS5jb20vNzllN2JmYTYyZmFkNmM3NjViYWM2OTE1NGMyZjI0Yzk0Yzk1MjIwYS9yZXNvdXJjZS1ncm91cCIsInJvbGUiOiJkZWxlZ2F0ZSIsImNvbnMiOnt9fQ.eJjCUvWuGD3L3Dn2fKj8Ydl1byGoyRS59VfL6ZJcdKR3_eIhm6SOY-CW3p5XDSYVhRTlWvlPLjfXYo9t_PxgnA";
    jwtAuthenticationService
        .decodeJwt(jwt)
        .onComplete(
            handler -> {
              if (handler.succeeded()) {
                testContext.failNow(handler.cause());
              } else {
                testContext.completeNow();
              }
            });
  }

  @Test
  @DisplayName("success - allow consumer access to /entities endpoint for access [api,subs]")
  public void access4ConsumerTokenEntitiesAPI(VertxTestContext testContext) {

    JsonObject authInfo = new JsonObject();

    authInfo.put("token", JwtTokenHelper.openConsumerApiToken);
    authInfo.put(
        "id",
        "datakaveri.org/04a15c9960ffda227e9546f3f46e629e1fe4132b/rs.iudx.io/pune-env-flood/FWR053");
    authInfo.put("apiEndpoint", "/ngsi-ld/v1/entities");
    authInfo.put("method", "GET");

    JwtData jwtData = new JwtData();
    jwtData.setIss("auth.test.com");
    jwtData.setAud("rs.iudx.io");
    jwtData.setExp(1627408865);
    jwtData.setIat(1627408865);
    jwtData.setIid(
        "rg:datakaveri.org/04a15c9960ffda227e9546f3f46e629e1fe4132b/rs.iudx.io/pune-env-flood/FWR053");
    jwtData.setRole("consumer");
    jwtData.setCons(new JsonObject().put("access", new JsonArray().add("sub")));

    jwtAuthenticationService
        .validateAccess(jwtData, true, authInfo)
        .onComplete(
            handler -> {
              if (handler.succeeded()) {
                testContext.completeNow();
              } else {
                testContext.failNow("invalid access");
              }
            });
  }

  @Test
  @DisplayName("failure - consumer access to /entities endpoint for access [subs]")
  public void access4ConsumerTokenEntitiesPostAPI(VertxTestContext testContext) {

    JsonObject authInfo = new JsonObject();

    authInfo.put("token", JwtTokenHelper.openConsumerApiToken);
    authInfo.put(
        "id",
        "datakaveri.org/04a15c9960ffda227e9546f3f46e629e1fe4132b/rs.iudx.io/pune-env-flood/FWR053");
    authInfo.put("apiEndpoint", "/ngsi-ld/v1/entities");
    authInfo.put("method", "GET");

    JwtData jwtData = new JwtData();
    jwtData.setIss("auth.test.com");
    jwtData.setAud("rs.iudx.io");
    jwtData.setExp(1627408865);
    jwtData.setIat(1627408865);
    jwtData.setIid(
        "rg:datakaveri.org/04a15c9960ffda227e9546f3f46e629e1fe4132b/rs.iudx.io/pune-env-flood/FWR053");
    jwtData.setRole("consumer");
    jwtData.setCons(new JsonObject().put("access", new JsonArray().add("subs")));

    jwtAuthenticationService
        .validateAccess(jwtData, false, authInfo)
        .onComplete(
            handler -> {
              if (handler.succeeded()) {
                testContext.failNow("invalid access provided");
              } else {
                testContext.completeNow();
              }
            });
  }

  @Test
  @DisplayName("failure - consumer access to /admin endpoint for access [api]")
  public void access4ConsumerTokenIngestAPI(VertxTestContext testContext) {

    JsonObject request = new JsonObject();
    JsonObject authInfo = new JsonObject();

    authInfo.put("token", JwtTokenHelper.closedConsumerApiToken);
    authInfo.put("id", closeId);
    authInfo.put("apiEndpoint", "/admin/gis/serverInfo");
    authInfo.put("method", Method.POST);

    jwtAuthenticationService.tokenIntrospect(
        request,
        authInfo,
        handler -> {
          if (handler.succeeded()) {
            testContext.failNow(handler.cause());
          } else {
            testContext.completeNow();
          }
        });
  }

  @Test
  @DisplayName("failure - provider access to /entities endpoint for access [api]")
  public void access4ProviderTokenEntitiesAPI(VertxTestContext testContext) {

    JsonObject authInfo = new JsonObject();

    authInfo.put("token", JwtTokenHelper.closedProviderApiToken);
    authInfo.put("id", "example.com/79e7bfa62fad6c765bac69154c2f24c94c95220a/resource-group");
    authInfo.put("apiEndpoint", "/ngsi-ld/v1/entities");
    authInfo.put("method", "GET");

    JwtData jwtData = new JwtData();
    jwtData.setIss("auth.test.com");
    jwtData.setAud("rs.iudx.io");
    jwtData.setExp(1627408865);
    jwtData.setIat(1627408865);
    jwtData.setIid("rg:example.com/79e7bfa62fad6c765bac69154c2f24c94c95220a/resource-group");
    jwtData.setRole("provider");
    jwtData.setCons(new JsonObject().put("access", new JsonArray().add("api")));

    jwtAuthenticationService
        .validateAccess(jwtData, false, authInfo)
        .onComplete(
            handler -> {
              if (handler.succeeded()) {

                testContext.failNow("provider not provided access to API");
              } else {
                testContext.completeNow();
              }
            });
  }

  @Test
  @DisplayName("success - validId check")
  public void validIdCheck4JwtToken(VertxTestContext testContext) {
    JwtData jwtData = new JwtData();
    jwtData.setIss("auth.test.com");
    jwtData.setAud("rs.iudx.io");
    jwtData.setExp(1627408865);
    jwtData.setIat(1627408865);
    jwtData.setIid(
        "rg:datakaveri.org/04a15c9960ffda227e9546f3f46e629e1fe4132b/rs.iudx.io/pune-env-flood/FWR053");
    jwtData.setRole("consumer");
    jwtData.setCons(new JsonObject().put("access", new JsonArray().add("ingest")));

    jwtAuthenticationService
        .isValidId(
            jwtData,
            "datakaveri.org/04a15c9960ffda227e9546f3f46e629e1fe4132b/rs.iudx.io/pune-env-flood/FWR053")
        .onComplete(
            handler -> {
              if (handler.succeeded()) {
                testContext.completeNow();
              } else {
                testContext.failNow("fail");
              }
            });
  }

  @Test
  @DisplayName("failure - invalid audience")
  public void invalidAudienceCheck(VertxTestContext testContext) {
    JwtData jwtData = new JwtData();
    jwtData.setIss("auth.test.com");
    jwtData.setAud("abc.iudx.io1");
    jwtData.setExp(1627408865);
    jwtData.setIat(1627408865);
    jwtData.setIid(
        "rg:datakaveri.org/04a15c9960ffda227e9546f3f46e629e1fe4132b/rs.iudx.io/pune-env-flood/FWR053");
    jwtData.setRole("provider");
    jwtData.setCons(new JsonObject().put("access", new JsonArray().add("ingest")));
    jwtAuthenticationService
        .isValidAudienceValue(jwtData)
        .onComplete(
            handler -> {
              if (handler.failed()) {
                testContext.completeNow();
              } else {
                testContext.failNow("fail");
              }
            });
  }

  @Test
  @DisplayName("failure - invalid validId check")
  public void invalidIdCheck4JwtToken(VertxTestContext testContext) {
    JwtData jwtData = new JwtData();
    jwtData.setIss("auth.test.com");
    jwtData.setAud("rs.iudx.io");
    jwtData.setExp(1627408865);
    jwtData.setIat(1627408865);
    jwtData.setIid(
        "rg:datakaveri.org/04a15c9960ffda227e9546f3f46e629e1fe4132b/rs.iudx.io/pune-env-flood/FWR053");
    jwtData.setRole("consumer");
    jwtData.setCons(new JsonObject().put("access", new JsonArray().add("ingest")));

    jwtAuthenticationService
        .isValidId(
            jwtData,
            "datakaveri.org/04a15c9960ffda227e9546f3f46e629e1fe4132b/rs.iudx.io/pune-env-flood/FWR055")
        .onComplete(
            handler -> {
              if (handler.succeeded()) {
                testContext.failNow("fail");
              } else {
                testContext.completeNow();
              }
            });
  }

  @Disabled
  @Test
  @DisplayName("Success Case for Resource Exist")
  public void isResourceExistTest(VertxTestContext testContext) {
    String orginal_id =
        "datakaveri.org/04a15c9960ffda227e9546f3f46e629e1fe4132b/rs.iudx.io/pune-env-flood/FWR053";
    String duplicate_id =
        "rg:datakaveri.org/04a15c9960ffda227e9546f3f46e629e1fe4132b/rs.iudx.io/pune-env-flood/FWR053";
    jwtAuthenticationService.isResourceExist(orginal_id, "OPEN")
        .onComplete(handle -> {
          if (handle.succeeded()) {
            testContext.completeNow();
          } else {
            testContext.failNow("Resource Doesn't exist");
          }
        });
  }

  @Disabled
  @Test
  @DisplayName("Fail Case for Resource Exist")
  public void isResourceNotExistTest(VertxTestContext testContext) {
    String orginal_id =
        "datakaveri.org/04a15c9960ffda227e9546f3f46e629e1fe4132b/rs.iudx.io/pune-env-flood/FWR053";
    String duplicate_id =
        "rg:datakaveri.org/04a15c9960ffda227e9546f3f46e629e1fe4132b/rs.iudx.io/pune-env-flood/FWR053";
    jwtAuthenticationService.isResourceExist(duplicate_id, "OPEN")
        .onComplete(handle -> {
          if (handle.succeeded()) {
            testContext.failNow("Resource exist");
          } else {
            testContext.completeNow();
          }
        });
  }

  @Disabled
  @Test
  @DisplayName("Success case for Group Access Policy")
  public void isgetGroupAccessPolicyTest(VertxTestContext testContext) {
    String gId = "iisc.ac.in/89a36273d77dac4cf38114fca1bbe64392547f86/rs.iudx.io/pune-env-flood";
    jwtAuthenticationService.getGroupAccessPolicy(gId)
        .onComplete(handle -> {
          if (handle.succeeded()) {
            testContext.completeNow();
          } else {
            testContext.failNow("Cannot get Group Access Policy");
          }
        });
  }

  @Disabled
  @Test
  @DisplayName("Failed case for Group Access Policy")
  public void isgetGroupAccessPolicyFailTest(VertxTestContext testContext) {
    String wrong_gId =
        "rg:iisc.ac.in/89a36273d77dac4cf38114fca1bbe64392547f86/rs.iudx.io/pune-env-flood";
    jwtAuthenticationService.getGroupAccessPolicy(wrong_gId)
        .onComplete(handle -> {
          if (handle.succeeded()) {
            testContext.failNow("Cannot get Group Access Policy");
          } else {
            testContext.completeNow();
          }
        });
  }


}
