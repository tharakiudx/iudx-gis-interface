package iudx.gis.server.apiserver.handlers;

import static iudx.gis.server.apiserver.response.ResponseUrn.INVALID_TOKEN;
import static iudx.gis.server.apiserver.response.ResponseUrn.RESOURCE_NOT_FOUND;
import static iudx.gis.server.apiserver.util.Constants.ADMIN_BASE_PATH;
import static iudx.gis.server.apiserver.util.Constants.API_ENDPOINT;
import static iudx.gis.server.apiserver.util.Constants.API_METHOD;
import static iudx.gis.server.apiserver.util.Constants.APPLICATION_JSON;
import static iudx.gis.server.apiserver.util.Constants.AUTH_INFO;
import static iudx.gis.server.apiserver.util.Constants.CONTENT_TYPE;
import static iudx.gis.server.apiserver.util.Constants.EXPIRY;
import static iudx.gis.server.apiserver.util.Constants.HEADER_TOKEN;
import static iudx.gis.server.apiserver.util.Constants.ID;
import static iudx.gis.server.apiserver.util.Constants.IID;
import static iudx.gis.server.apiserver.util.Constants.JSON_DETAIL;
import static iudx.gis.server.apiserver.util.Constants.JSON_TITLE;
import static iudx.gis.server.apiserver.util.Constants.JSON_TYPE;
import static iudx.gis.server.apiserver.util.Constants.NGSILD_ENTITIES_URL;
import static iudx.gis.server.apiserver.util.Constants.USER_ID;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import iudx.gis.server.apiserver.response.ResponseUrn;
import iudx.gis.server.apiserver.util.HttpStatusCode;
import iudx.gis.server.authenticator.AuthenticationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuthHandler implements Handler<RoutingContext> {

  private static final String AUTH_SERVICE_ADDRESS = "iudx.gis.authentication.service";
  private static final Logger LOGGER = LogManager.getLogger(AuthHandler.class);
  private static AuthenticationService authenticator;
  private HttpServerRequest request;

  public static AuthHandler create(Vertx vertx) {
    authenticator = AuthenticationService.createProxy(vertx, AUTH_SERVICE_ADDRESS);
    return new AuthHandler();
  }

  @Override
  public void handle(RoutingContext context) {
    request = context.request();

    JsonObject requestJson = context.getBodyAsJson();

    if (requestJson == null) {
      requestJson = new JsonObject();
    }

    LOGGER.debug("Info : path " + request.path());

    String token = request.headers().get(HEADER_TOKEN);
    final String path = getNormalizedPath(request.path());
    final String method = context.request().method().toString();

    if (token == null) token = "public";

    String paramId = getId4rmRequest();

    String id = null;

    if (paramId != null && !paramId.isBlank()) {
      id = paramId;
    }

    JsonObject authInfo =
        new JsonObject()
            .put(API_ENDPOINT, path)
            .put(HEADER_TOKEN, token)
            .put(API_METHOD, method)
            .put(ID, id);

    LOGGER.debug("Info :" + context.request().path());
    LOGGER.debug("Info :" + context.request().path().split("/").length);

    authenticator.tokenIntrospect(
        requestJson,
        authInfo,
        authHandler -> {
          if (authHandler.succeeded()) {
            authInfo.put(IID, authHandler.result().getValue(IID));
            authInfo.put(USER_ID, authHandler.result().getValue(USER_ID));
            authInfo.put(EXPIRY, authHandler.result().getValue(EXPIRY));
            context.data().put(AUTH_INFO, authInfo);
          } else {
            processAuthFailure(context, authHandler.cause().getMessage());
            return;
          }
          context.next();
        });
  }

  public void processAuthFailure(RoutingContext ctx, String result) {
    if (result.contains("Not Found")) {
      LOGGER.error("Error : Item Not Found");
      HttpStatusCode statusCode = HttpStatusCode.getByValue(404);
      ctx.response()
          .putHeader(CONTENT_TYPE, APPLICATION_JSON)
          .setStatusCode(statusCode.getValue())
          .end(generateResponse(RESOURCE_NOT_FOUND, statusCode).toString());
    } else {
      LOGGER.error("Error : Authentication Failure");
      HttpStatusCode statusCode = HttpStatusCode.getByValue(401);
      ctx.response()
          .putHeader(CONTENT_TYPE, APPLICATION_JSON)
          .setStatusCode(statusCode.getValue())
          .end(generateResponse(INVALID_TOKEN, statusCode).toString());
    }
  }

  public String getNormalizedPath(String url) {
    LOGGER.debug("URL : {}", url);
    String path = null;
    if (url.matches(NGSILD_ENTITIES_URL)) path = NGSILD_ENTITIES_URL;
    else if (url.matches(ADMIN_BASE_PATH)) path = ADMIN_BASE_PATH;
    return path;
  }


  private String getId4rmRequest() {
    return request.getParam(ID);
  }

  private JsonObject generateResponse(ResponseUrn urn, HttpStatusCode statusCode) {
    return new JsonObject()
        .put(JSON_TYPE, urn.getUrn())
        .put(JSON_TITLE, statusCode.getDescription())
        .put(JSON_DETAIL, statusCode.getDescription());
  }
}
