package iudx.gis.server.apiserver.handlers;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
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
import org.mockito.junit.jupiter.MockitoExtension;

import static iudx.gis.server.apiserver.util.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith({VertxExtension.class, MockitoExtension.class})
public class AuthHandlerTest {
    private static final String AUTH_SERVICE_ADDRESS = "iudx.gis.authentication.service";
    private static final Logger LOGGER = LogManager.getLogger(AuthHandlerTest.class);
    AuthHandler authHandler;
    @BeforeEach
    public void setUp(){
        authHandler=new AuthHandler();
    }
/*
    @Test
    public void test(){
        RoutingContext routingContextMock= mock(RoutingContext.class);
        HttpServerRequest httpServerRequestMock = mock(HttpServerRequest.class);
        JsonObject jsonObjectMock = mock(JsonObject.class);
        MultiMap multiMapMock= mock(MultiMap.class);
        HttpMethod httpMethodMock= mock(HttpMethod.class);
        AuthenticationService authenticationServiceMock= mock(AuthenticationService.class);

        when(routingContextMock.request()).thenReturn(httpServerRequestMock);
        when(routingContextMock.getBodyAsJson()).thenReturn(jsonObjectMock);
        when(httpServerRequestMock.path()).thenReturn("Some path");

        when(httpServerRequestMock.headers()).thenReturn(multiMapMock);
        when(multiMapMock.get(anyString())).thenReturn("token");

        when(routingContextMock.request()).thenReturn(httpServerRequestMock);
        when(httpServerRequestMock.method()).thenReturn(httpMethodMock);
        when(httpMethodMock.toString()).thenReturn("");

        authenticationServiceMock.tokenIntrospect(jsonObjectMock,jsonObjectMock,handler->{});
        authHandler.handle(routingContextMock);
    }*/

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
