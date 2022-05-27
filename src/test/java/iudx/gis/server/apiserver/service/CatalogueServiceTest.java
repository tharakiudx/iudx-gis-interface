package iudx.gis.server.apiserver.service;

import io.netty.util.Constant;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import iudx.gis.server.apiserver.service.CatalogueService;
import iudx.gis.server.authenticator.Constants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith({VertxExtension.class, MockitoExtension.class})
public class CatalogueServiceTest {
    @Mock
    WebClient webClient;
    @Mock
    HttpRequest<Buffer> httpRequest;
    @Mock
    HttpResponse<Buffer> response;
    @Mock
    JsonObject json,config;
    /*@Mock
    WebClientFactory webClientFactory;*/
    @Mock
    AsyncResult<HttpResponse<Buffer>> asyncResultMock;
    @Mock
    WebClientOptions webClientOptions;
    CatalogueService catalogueService;

    String catSearchPath=Constants.CAT_RSG_PATH;
    String catItemPath= Constants.CAT_ITEM_PATH;

    @BeforeEach
    public void setup(Vertx vertx){
        config = new JsonObject();
        config.put("catServerHost","anyhost");
        config.put("catServerPort",12345);

        //when(webClientOptions.setTrustAll(true)).thenReturn(webClientOptions);
        //when(webClientOptions.setVerifyHost(false)).thenReturn(webClientOptions);
        //when(webClientOptions.setSsl(true)).thenReturn(webClientOptions);

        //when(WebClient.create(vertx,webClientOptions)).thenReturn(webClient);
        catalogueService= new CatalogueService(vertx,config);
    }

    @Test
    @DisplayName("Populate Group Cache")
    public void populateGroupCacheTest(Vertx vertx, VertxTestContext testContext){
        Future<Boolean> booleanFuture = catalogueService.populateGroupCache(webClient);
        testContext.completeNow();
    }
    @Test
    @DisplayName("Populate Resource Cache")
    public void populateResourceCache(Vertx vertx, VertxTestContext testContext){
        var variable = catalogueService.populateResourceCache(webClient);
        testContext.completeNow();
    }

    /*@Test
    @DisplayName("Item successful test case")
    public void isItemExistTestSuccess(Vertx vertx, VertxTestContext testContext){
        doReturn(httpRequest).when(webClient).get(anyInt(),anyString(),anyString());
        doReturn(httpRequest).when(httpRequest).addQueryParam(any(),any());
        doReturn(httpRequest).when(httpRequest).expect(any());

        AsyncResult<HttpResponse<Buffer>> asyncResult= mock(AsyncResult.class);
        when(asyncResult.succeeded()).thenReturn(true);
        when(asyncResult.result()).thenReturn(response);
        when(response.bodyAsJsonObject()).thenReturn(new JsonObject()
                .put("type","urn:dx:cat:success")
                .put("totalhits",1));
        //when(asyncResult.cause()).thenReturn(new Throwable(""));

        Mockito.doAnswer(new Answer<AsyncResult<HttpResponse<Buffer>>>() {
            @Override
            public AsyncResult<HttpResponse<Buffer>> answer(InvocationOnMock arg0) throws Throwable {
                ((Handler<AsyncResult<HttpResponse<Buffer>>>)arg0.getArgument(0)).handle(asyncResult);
                return null;
            }
        }).when(httpRequest).send(any());
        var call= catalogueService.isItemExist("anyId");
    }*/

    @Test
    @DisplayName("Item Present Test Case")
    public void isItemPresents(VertxTestContext testContext){
        AsyncResult<HttpResponse<Buffer>> asyncResult = mock(AsyncResult.class);
        String id="abcdef/xyz.usd/asd";
        catalogueService.isItemExist(id);
        /*when(webClient.get(anyInt(),anyString(),anyString())).thenReturn(httpRequest);
        when(httpRequest.addQueryParam(anyString(),anyString())).thenReturn(httpRequest);
        when(httpRequest.expect(any())).thenReturn(httpRequest);
        when(httpRequest.send()).thenReturn(Future.succeededFuture());
*/
        /*when(asyncResult.succeeded()).thenReturn(true);
        when(asyncResult.result()).thenReturn(response);
        when(response.bodyAsJsonObject()).thenReturn(new JsonObject()
                .put("type", "urn:dx:cat:Success")
                .put("totalHits", 1));
        Mockito.doAnswer(new Answer<AsyncResult<HttpResponse<Buffer>>>() {
            @SuppressWarnings("unchecked")
            @Override
            public AsyncResult<HttpResponse<Buffer>> answer(InvocationOnMock arg0) throws Throwable {
                ((Handler<AsyncResult<HttpResponse<Buffer>>>) arg0.getArgument(0)).handle(asyncResult);
                return null;
            }
        }).when(httpRequest).send(any());*/
        //Future<Boolean> call = catalogueService.isItemExist("adadasd");

        //assertTrue(call.succeeded());
        //verify(httpRequest, times(1)).addQueryParam(any(), any());
        //verify(httpRequest, times(1)).expect(any());
        //verify(httpRequest, times(1)).send(any());

        testContext.completeNow();
    }

    @Test
    @DisplayName("Call catalogue Item Api Test Case")
    public void iscallCatalogueItemApi(VertxTestContext testContext){
        String id="abcdef/xyz.usd/asd";
        catalogueService.callCatalogueItemApi(id);
        testContext.completeNow();
    }
    @Test
    @DisplayName("Call catalogue Item Api Test Case")
    public void isisIdPresent(VertxTestContext testContext){
        String id="abcdef/xyz.usd/asd";
        catalogueService.isIdPresent(id);
        testContext.completeNow();
    }
}
