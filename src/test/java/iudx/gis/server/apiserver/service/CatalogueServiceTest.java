package iudx.gis.server.apiserver.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import iudx.gis.server.authenticator.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith({VertxExtension.class, MockitoExtension.class})
public class CatalogueServiceTest {
    @Mock
    Vertx vertxObj;
    JsonObject config;
    CatalogueService catalogueService;

    @Mock
    HttpRequest<Buffer> httpRequest;
    @Mock
    AsyncResult<HttpResponse<Buffer>> asyncResult;
    @Mock
    HttpResponse<Buffer> httpResponse;

    @BeforeEach
    public void setup(VertxTestContext vertxTestContext){
        config = new JsonObject();
        config.put("catServerHost","guest");
        config.put("catServerPort",8443);
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        JsonArray jsonArray1 = new JsonArray();
        JsonObject jsonObject1 = new JsonObject();

        jsonObject1.put("id", "abcd/abcd/abcd/abcd");
        jsonObject1.put("iudxResourceAPIs", jsonArray1);
        jsonArray.add(jsonObject1);
        jsonObject.put("results", jsonArray);
        CatalogueService.catWebClient = mock(WebClient.class);

        when(CatalogueService.catWebClient.get(anyInt(),anyString(),anyString())).thenReturn(httpRequest);
        when(httpRequest.addQueryParam(anyString(),anyString())).thenReturn(httpRequest);
        when(httpRequest.expect(any())).thenReturn(httpRequest);
        when(asyncResult.succeeded()).thenReturn(true);
        when(asyncResult.result()).thenReturn(httpResponse);
        when(httpResponse.bodyAsJsonObject()).thenReturn(jsonObject);
        doAnswer(new Answer<AsyncResult<HttpResponse<Buffer>>>() {
            @Override
            public AsyncResult<HttpResponse<Buffer>> answer(InvocationOnMock arg0) throws Throwable {

                ((Handler<AsyncResult<HttpResponse<Buffer>>>)arg0.getArgument(0)).handle(asyncResult);
                return null;
            }
        }).when(httpRequest).send(any());
        catalogueService = new CatalogueService(vertxObj,config);
        vertxTestContext.completeNow();
    }

    @Test
    @DisplayName("Testing Success for isItemExist method with String IDs")
    public void testIsItemExistSuccess(VertxTestContext vertxTestContext)
    {
        JsonObject responseJSonObject = new JsonObject();
        responseJSonObject.put("type","urn:dx:cat:Success");
        responseJSonObject.put("totalHits", 10);
        /*responseJSonObject.put("results", "iudx:Resource");*/
        when(httpResponse.bodyAsJsonObject()).thenReturn(responseJSonObject);

        catalogueService.isItemExist("asd/asd").onComplete(handler -> {
            if (handler.succeeded())
            {
                assertTrue(handler.result());
                vertxTestContext.completeNow();
            }
            else
            {
                vertxTestContext.failNow(handler.cause());
            }
        });
        verify(CatalogueService.catWebClient,times(2)).get(anyInt(),anyString(),anyString());
        verify(httpRequest,times(4)).addQueryParam(anyString(),anyString());
        verify(httpRequest,times(2)).send(any());
    }
    @Test
    @DisplayName("Testing Failed for isItemExist method with String IDs")
    public void testIsItemExistFail(VertxTestContext vertxTestContext)
    {

        JsonObject responseJSonObject = new JsonObject();
        responseJSonObject.put("type","urn:dx:cat:Success");
        responseJSonObject.put("totalHits", 0);
        when(httpResponse.bodyAsJsonObject()).thenReturn(responseJSonObject);

        catalogueService.isItemExist("asd/asd").onComplete(handler -> {
            if (handler.succeeded())
            {
                vertxTestContext.failNow(handler.cause());
                vertxTestContext.completeNow();
            }
            else
            {
                vertxTestContext.completeNow();
            }
        });
        verify(CatalogueService.catWebClient,times(2)).get(anyInt(),anyString(),anyString());
        verify(httpRequest,times(2)).send(any());
    }

    @Test
    public void testResourceCache(VertxTestContext vertxTestContext){

        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        JsonArray jsonArray1 = new JsonArray();
        JsonObject jsonObject1 = new JsonObject();

        jsonObject1.put("id", "abcd/abcd/abcd/abcd");
        jsonObject1.put("iudxResourceAPIs", jsonArray1);
        jsonArray.add(jsonObject1);
        jsonObject.put("results", jsonArray);

        when(CatalogueService.catWebClient.get(anyInt(),anyString(),anyString())).thenReturn(httpRequest);
        when(httpRequest.addQueryParam(anyString(),anyString())).thenReturn(httpRequest);
        when(httpRequest.addQueryParam(anyString(),anyString())).thenReturn(httpRequest);
        when(httpRequest.addQueryParam(anyString(),anyString())).thenReturn(httpRequest);
        when(httpRequest.expect(any())).thenReturn(httpRequest);

        when(asyncResult.succeeded()).thenReturn(true);
        when(asyncResult.result()).thenReturn(httpResponse);
        when(httpResponse.bodyAsJsonObject()).thenReturn(jsonObject);

        doAnswer(new Answer<AsyncResult<HttpResponse<Buffer>>>() {
            @Override
            public AsyncResult<HttpResponse<Buffer>> answer(InvocationOnMock arg0) throws Throwable {

                ((Handler<AsyncResult<HttpResponse<Buffer>>>)arg0.getArgument(0)).handle(asyncResult);
                return null;
            }
        }).when(httpRequest).send(any());

        catalogueService.populateResourceCache(CatalogueService.catWebClient).onComplete(handler->{
                if (handler.succeeded()){
                    vertxTestContext.completeNow();
                }
                else {
                    handler.failed();
                }
                });
    }
    /*@Test
    public void testCallCatalogItemApi(VertxTestContext vertxTestContext){

        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        JsonArray jsonArray1 = new JsonArray();
        JsonObject jsonObject1 = new JsonObject();

        jsonObject1.put("id", "abcd/abcd/abcd/abcd");
        jsonObject1.put("iudxResourceAPIs", jsonArray1);
        jsonArray.add(jsonObject1);
        jsonObject.put("results", jsonArray);

        when(CatalogueService.catWebClient.get(anyInt(),anyString(),anyString())).thenReturn(httpRequest);
        when(httpRequest.addQueryParam(anyString(),anyString())).thenReturn(httpRequest);

        when(asyncResult.succeeded()).thenReturn(true);
        when(asyncResult.result()).thenReturn(httpResponse);
        when(httpResponse.bodyAsJsonObject()).thenReturn(jsonObject);

        doAnswer(new Answer<AsyncResult<HttpResponse<Buffer>>>() {
            @Override
            public AsyncResult<HttpResponse<Buffer>> answer(InvocationOnMock arg0) throws Throwable {

                ((Handler<AsyncResult<HttpResponse<Buffer>>>)arg0.getArgument(0)).handle(asyncResult);
                return null;
            }
        }).when(httpRequest).send(any());

        catalogueService.callCatalogueItemApi("CatalogueService/catWebClient").onComplete(handler->{
            if (handler.succeeded()){
                vertxTestContext.completeNow();
            }
            else {
                handler.failed();
            }
        });
    }*/



    /*@Test
    @DisplayName("Populate Group Cache")
    public void populateGroupCacheTest(Vertx vertx, VertxTestContext testContext){
        Future<Boolean> booleanFuture = catalogueService.populateGroupCache(webClient);
        testContext.completeNow();
    }*/

   /* @Test
    @DisplayName("Populate Group Cache")
    public void populateGroupCacheTest(VertxTestContext vertxTestContext){
        when(webClient.get(anyInt(),anyString(),anyString())).thenReturn(httpRequest);
        when(httpRequest.addQueryParam(anyString(),anyString())).thenReturn(httpRequest);
        when(httpRequest.addQueryParam(anyString(),anyString())).thenReturn(httpRequest);
        when(httpRequest.addQueryParam(anyString(),anyString())).thenReturn(httpRequest);
        when(httpRequest.expect(any())).thenReturn(httpRequest);

        JsonObject jsonObject= new JsonObject().put("id","asbc/iudx/resoruce")
                        .put("accessPolicy","SECURE");
        JsonArray jsonArray= new JsonArray().add(new JsonObject().put("result",jsonObject));

        when(asyncResultMock.succeeded()).thenReturn(true);
        when(asyncResultMock.result()).thenReturn(httpResponse);
        when(httpResponse.bodyAsJsonObject()).thenReturn(jsonObject);
        when(jsonObject.getJsonArray("result")).thenReturn(any());

        Mockito.doAnswer(new Answer<AsyncResult<HttpResponse<Buffer>>>() {
            @SuppressWarnings("unchecked")
            @Override
            public AsyncResult<HttpResponse<Buffer>> answer(InvocationOnMock arg0) throws Throwable {
                ((Handler<AsyncResult<HttpResponse<Buffer>>>) arg0.getArgument(0)).handle(asyncResultMock);
                return null;
            }
        }).when(httpRequest).send(any());

        catalogueService.populateGroupCache(webClient).onComplete(handler->{
           if(handler.succeeded()){
               vertxTestContext.completeNow();
           }
           else{
               vertxTestContext.failNow("Failed");
           }
        });
    }*/

    /*@Test
    @DisplayName("Populate Resource Cache")
    public void populateResourceCache(Vertx vertx, VertxTestContext testContext){
        var variable = catalogueService.populateResourceCache(webClient).onComplete(
                handle->{
                    if (handle.succeeded())
                        testContext.completeNow();
                    else
                        testContext.failNow("Failed to populate");
                }
        );
       // testContext.completeNow();
    }*/

  /*  @Test
    @DisplayName("Item successful test case")
    public void isItemExistTestSuccess(Vertx vertx, VertxTestContext testContext){
       lenient().doReturn(httpRequest).when(webClient).get(anyInt(),anyString(),anyString());
        lenient().doReturn(httpRequest).when(httpRequest).addQueryParam(any(),any());
        lenient().doReturn(httpRequest).when(httpRequest).expect(any());

        AsyncResult<HttpResponse<Buffer>> asyncResult= mock(AsyncResult.class);
        lenient().when(asyncResult.succeeded()).thenReturn(true);
        lenient().when(asyncResult.result()).thenReturn(httpResponse);
        lenient().when(httpResponse.bodyAsJsonObject()).thenReturn(new JsonObject()
                .put("type","urn:dx:cat:Success")
                .put("totalHits",1)
                .put("results","iudx:Resource"));

        Mockito.lenient().doAnswer(new Answer<AsyncResult<HttpResponse<Buffer>>>() {
            @Override
            public AsyncResult<HttpResponse<Buffer>> answer(InvocationOnMock arg0) throws Throwable {
                ((Handler<AsyncResult<HttpResponse<Buffer>>>)arg0.getArgument(0)).handle(asyncResult);
                return null;
            }
        }).when(httpRequest).send(any());
        catalogueService.isItemExist("anyId");
        testContext.completeNow();
    }*/

    /*@Test
    @DisplayName("Item Present Test Case")
    public void isItemPresents(VertxTestContext testContext){
        String id="abcdef/xyz.usd/asd";
        catalogueService.isItemExist(id);
        testContext.completeNow();
    }*/

    /*@Test
    @DisplayName("success - is Item exists ")
    public void isItemExistTestSuccess(Vertx vertx) {

        doReturn(httpRequest).when(webClient).get(anyInt(), anyString(), anyString());
        doReturn(httpRequest).when(httpRequest).addQueryParam(any(), any());
        doReturn(httpRequest).when(httpRequest).expect(any());
        JsonObject jsonObject= new JsonObject();
        JsonArray jsonArray= new JsonArray();

        AsyncResult<HttpResponse<Buffer>> asyncResult = mock(AsyncResult.class);
        when(asyncResult.succeeded()).thenReturn(true);
        when(asyncResult.result()).thenReturn(httpResponse);
        when(httpResponse.bodyAsJsonObject()).thenReturn(jsonObject
                .put("type", "urn:dx:cat:Success")
                .put("totalHits", 1)
                .put("results","iudx:Resource"));
        //when(jsonObject.getString("type")).thenReturn("urn:dx:cat:Success");
        //when(jsonObject.getInteger("total"))
        //when(jsonObject.getJsonArray("results")).thenReturn(jsonArray.add(jsonObject));
        //when(jsonArray.getJsonObject(0)).thenReturn(jsonArray.add(jsonObject));

        Mockito.doAnswer(new Answer<AsyncResult<HttpResponse<Buffer>>>() {
            @SuppressWarnings("unchecked")
            @Override
            public AsyncResult<HttpResponse<Buffer>> answer(InvocationOnMock arg0) throws Throwable {
                ((Handler<AsyncResult<HttpResponse<Buffer>>>) arg0.getArgument(0)).handle(asyncResult);
                return null;
            }
        }).when(httpRequest).send(any());

        Future<Boolean> call = catalogueService.isItemExist("adadasd:iudx:Resource");

        assertFalse(call.succeeded());
        //verify(httpRequest, times(0)).addQueryParam(any(), any());
        //verify(httpRequest, times(0)).expect(any());
        //verify(httpRequest, times(0)).send(any());
    }*/

    /*@Test
    @DisplayName("fail - is Item exists ")
    public void isItemExistTestFailure(Vertx vertx) {

        doReturn(httpRequest).when(webClient).get(anyInt(), anyString(), anyString());
        doReturn(httpRequest).when(httpRequest).addQueryParam(any(), any());
        doReturn(httpRequest).when(httpRequest).expect(any());

        AsyncResult<HttpResponse<Buffer>> asyncResult = mock(AsyncResult.class);
        when(asyncResult.succeeded()).thenReturn(false);
        when(asyncResult.cause()).thenReturn(new Throwable(""));

        Mockito.doAnswer(new Answer<AsyncResult<HttpResponse<Buffer>>>() {
            @SuppressWarnings("unchecked")
            @Override
            public AsyncResult<HttpResponse<Buffer>> answer(InvocationOnMock arg0) throws Throwable {
                ((Handler<AsyncResult<HttpResponse<Buffer>>>) arg0.getArgument(0)).handle(asyncResult);
                return null;
            }
        }).when(httpRequest).send(any());

        Future<Boolean> call = catalogueService.isItemExist("adadasd");

        assertFalse(call.succeeded());
        //verify(httpRequest, times(1)).addQueryParam(any(), any());
        //verify(httpRequest, times(1)).expect(any());
        //verify(httpRequest, times(1)).send(any());
    }*/
    /*@Test
    @DisplayName("Call catalogue Item Api Test Case")
    public void iscallCatalogueItemApi(VertxTestContext testContext){
        String id="abcdef/xyz.usd/asd";
        catalogueService.callCatalogueItemApi(id);
        testContext.completeNow();
    }*/
    /*@Test
    @DisplayName("Call catalogue Item Api Test Case")
    public void isisIdPresent(VertxTestContext testContext){
        String id="abcdef/xyz.usd/asd";
        catalogueService.isIdPresent(id);
        testContext.completeNow();
    }*/

   /* @Test
    public void testCallCatalogueItemApi(VertxTestContext vertxTestContext){

        JsonObject responseJSonObject = new JsonObject();
        responseJSonObject.put("id","accessPolicy");

        //when(httpResponse.bodyAsJsonObject()).thenReturn(responseJSonObject);


        catalogueService.callCatalogueItemApi("asd/zxc/qwe/asd").onComplete(handler->{
            if (handler.succeeded()){

                vertxTestContext.completeNow();
            }
            else
                vertxTestContext.failNow(handler.cause());
        });
        vertxTestContext.completeNow();
    }
*/
}
