package iudx.gis.server.apiserver.handlers;

import io.vertx.core.*;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import iudx.gis.server.apiserver.handlers.AuthHandler;
import iudx.gis.server.apiserver.util.HttpStatusCode;
import iudx.gis.server.authenticator.AuthenticationService;
import iudx.gis.server.authenticator.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.HashMap;
import java.util.Map;

import static iudx.gis.server.apiserver.util.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith({VertxExtension.class, MockitoExtension.class})
public class AuthHandlerTest {
    private static final String AUTH_SERVICE_ADDRESS = "iudx.gis.authentication.service";
    private static final Logger LOGGER = LogManager.getLogger(AuthHandlerTest.class);
    AuthHandler authHandler;
    @Mock
    RoutingContext routingContextMock;
    @Mock
    HttpServerResponse httpServerResponse;
    @Mock
    HttpServerRequest httpServerRequest;
    @BeforeEach
    public void setUp(VertxTestContext vertxTestContext, Vertx vertx){
        authHandler= AuthHandler.create(vertx);
        lenient().doReturn(httpServerRequest).when(routingContextMock).request();
        lenient().doReturn(httpServerResponse).when(routingContextMock).response();
        vertxTestContext.completeNow();
    }

    @Test
    public void testHandleSuccess(VertxTestContext vertxTestContext){
        JsonObject jsonObjectMock = new JsonObject().put("id","iddd");
        MultiMap multiMapMock= mock(MultiMap.class);
        HttpMethod httpMethodMock= mock(HttpMethod.class);
        Map map = new HashMap<String,Object>() ;
        AuthenticationService authenticationServiceMock= mock(AuthenticationService.class);
        AsyncResult<JsonObject> asyncResult = mock(AsyncResult.class);
        when(routingContextMock.request()).thenReturn(httpServerRequest);
        when(routingContextMock.getBodyAsJson()).thenReturn(jsonObjectMock);
        doReturn(NGSILD_ENTITIES_URL).when(httpServerRequest).path();
        when(httpServerRequest.headers()).thenReturn(multiMapMock);
        when(multiMapMock.get(HEADER_TOKEN)).thenReturn("asd.asd.sad.sad");
        when(routingContextMock.request()).thenReturn(httpServerRequest);
        when(httpServerRequest.method()).thenReturn(httpMethodMock);
        when(httpMethodMock.toString()).thenReturn("POST");
        when(httpServerRequest.getParam(ID)).thenReturn("qeret/dfasfa/zxcvvb");
        JsonObject authinfo = new JsonObject();
        lenient().when(asyncResult.succeeded()).thenReturn(true);
        lenient().when(asyncResult.result()).thenReturn(new JsonObject().put(IID,"aasadas")
                .put(USER_ID,"aasadastyui")
                .put(EXPIRY,"25 05 2022"));
        Mockito.lenient().doAnswer(new Answer<AsyncResult<JsonObject>>() {
            @SuppressWarnings("unchecked")
            @Override
            public AsyncResult<JsonObject> answer(InvocationOnMock arg0) throws Throwable {
                ((Handler<AsyncResult<JsonObject>>) arg0.getArgument(2 )).handle(asyncResult);
                return null;
            }
        }).when(authenticationServiceMock).tokenIntrospect(any(),any(),any());

        authHandler.handle(routingContextMock);
        //verify(routingContextMock, times(1)).next();

        vertxTestContext.completeNow();
    }
    @Test
    public void testHandleFail(VertxTestContext vertxTestContext){
        JsonObject jsonObjectMock = new JsonObject().put("id","iddd");
        MultiMap multiMapMock= mock(MultiMap.class);
        HttpMethod httpMethodMock= mock(HttpMethod.class);
        Map map = new HashMap<String,Object>() ;
        AuthenticationService authenticationServiceMock= mock(AuthenticationService.class);
        AsyncResult<JsonObject> asyncResult = mock(AsyncResult.class);
        when(routingContextMock.request()).thenReturn(httpServerRequest);
        when(routingContextMock.getBodyAsJson()).thenReturn(jsonObjectMock);
        doReturn(NGSILD_ENTITIES_URL).when(httpServerRequest).path();
        when(httpServerRequest.headers()).thenReturn(multiMapMock);
        when(multiMapMock.get(HEADER_TOKEN)).thenReturn("asd.asd.sad.sad");
        when(routingContextMock.request()).thenReturn(httpServerRequest);
        when(httpServerRequest.method()).thenReturn(httpMethodMock);
        when(httpMethodMock.toString()).thenReturn("POST");
        when(httpServerRequest.getParam(ID)).thenReturn("qeret/dfasfa/zxcvvb");
        JsonObject authinfo = new JsonObject();
        lenient().when(asyncResult.succeeded()).thenReturn(false);
        lenient().when(asyncResult.cause()).thenReturn(new Throwable("fail"));

        Mockito.lenient().doAnswer(new Answer<AsyncResult<JsonObject>>() {
            @SuppressWarnings("unchecked")
            @Override
            public AsyncResult<JsonObject> answer(InvocationOnMock arg0) throws Throwable {
                ((Handler<AsyncResult<JsonObject>>) arg0.getArgument(2)).handle(asyncResult);
                return null;
            }
        }).when(authenticationServiceMock).tokenIntrospect(any(),any(),any());

        authHandler.handle(routingContextMock);
        //Mockito.verify(httpServerResponse, times(0)).putHeader(anyString(), anyString());
        //Mockito.verify(httpServerResponse, times(0)).setStatusCode(anyInt());
        //Mockito.verify(httpServerResponse, times(0)).end(anyString());
        vertxTestContext.completeNow();
    }

    @Test
    @DisplayName("Process AuthFailure NotFound")
    public void processAuthFailureNotFound(VertxTestContext vertxTestContext){
        RoutingContext routingContextMock= mock(RoutingContext.class);

        HttpServerResponse httpServerResponseMock = mock(HttpServerResponse.class);
        Future<Void> voidFutureMock = mock(Future.class);

        when(routingContextMock.response()).thenReturn(httpServerResponseMock);
        when(httpServerResponseMock.putHeader(anyString(),anyString())).thenReturn(httpServerResponseMock);
        when(httpServerResponseMock.setStatusCode(anyInt())).thenReturn(httpServerResponseMock);
        when(httpServerResponseMock.end(anyString())).thenReturn(voidFutureMock);
        authHandler.processAuthFailure(routingContextMock,"Not Found");

        verify(httpServerResponseMock, times(1)).setStatusCode(anyInt());
        verify(httpServerResponseMock, times(1)).putHeader(anyString(),anyString());
        verify(httpServerResponseMock, times(1)).end(anyString());

        vertxTestContext.completeNow();

    }
    @Test
    @DisplayName("Process AuthFailure Except Found")
    public void processAuthFailureExceptFound(VertxTestContext vertxTestContext){
        RoutingContext routingContextMock= mock(RoutingContext.class);
        HttpServerResponse httpServerResponseMock = mock(HttpServerResponse.class);
        Future<Void> voidFutureMock = mock(Future.class);

        when(routingContextMock.response()).thenReturn(httpServerResponseMock);
        when(httpServerResponseMock.putHeader(anyString(),anyString())).thenReturn(httpServerResponseMock);
        when(httpServerResponseMock.setStatusCode(anyInt())).thenReturn(httpServerResponseMock);
        when(httpServerResponseMock.end(anyString())).thenReturn(voidFutureMock);
        authHandler.processAuthFailure(routingContextMock,"");
        verify(httpServerResponseMock, times(1)).setStatusCode(anyInt());
        verify(httpServerResponseMock, times(1)).putHeader(anyString(),anyString());
        verify(httpServerResponseMock, times(1)).end(anyString());

        vertxTestContext.completeNow();

    }

    @Test
    public void getNormalizedPathTest(VertxTestContext vertxTestContext){
        String authString= authHandler.getNormalizedPath(NGSILD_ENTITIES_URL);
        assertEquals(authString , NGSILD_ENTITIES_URL);
        String authString2= authHandler.getNormalizedPath(ADMIN_BASE_PATH);
        assertEquals(authString2 , ADMIN_BASE_PATH);
        vertxTestContext.completeNow();
    }
    @Test
    @DisplayName("Test static method: create")
    public void testCreate(VertxTestContext vertxTestContext)
    {
        AuthHandler res =  AuthHandler.create(Vertx.vertx());
        assertNotNull(res);
        vertxTestContext.completeNow();
    }
}
