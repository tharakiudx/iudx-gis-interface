package iudx.gis.server.apiserver;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.junit5.VertxExtension;
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

import static iudx.gis.server.apiserver.util.Constants.HEADER_TOKEN;
import static iudx.gis.server.apiserver.util.Constants.NGSILD_ENTITIES_URL;
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
    public void processAuthFailureNotFound(){
        RoutingContext routingContextMock= mock(RoutingContext.class);
        //HttpStatusCode httpStatusCodeMock= mock(HttpStatusCode.class);
        HttpServerResponse httpServerResponseMock = mock(HttpServerResponse.class);
        Future<Void> voidFutureMock = mock(Future.class);

        when(routingContextMock.response()).thenReturn(httpServerResponseMock);
        when(httpServerResponseMock.putHeader(anyString(),anyString())).thenReturn(httpServerResponseMock);
        when(httpServerResponseMock.setStatusCode(anyInt())).thenReturn(httpServerResponseMock);
        when(httpServerResponseMock.end(anyString())).thenReturn(voidFutureMock);
        authHandler.processAuthFailure(routingContextMock,"Not Found");

    }
    @Test
    @DisplayName("Process AuthFailure Except Found")
    public void processAuthFailureExceptFound(){
        RoutingContext routingContextMock= mock(RoutingContext.class);
        HttpServerResponse httpServerResponseMock = mock(HttpServerResponse.class);
        Future<Void> voidFutureMock = mock(Future.class);

        when(routingContextMock.response()).thenReturn(httpServerResponseMock);
        when(httpServerResponseMock.putHeader(anyString(),anyString())).thenReturn(httpServerResponseMock);
        when(httpServerResponseMock.setStatusCode(anyInt())).thenReturn(httpServerResponseMock);
        when(httpServerResponseMock.end(anyString())).thenReturn(voidFutureMock);
        authHandler.processAuthFailure(routingContextMock,"");

    }

    @Test
    public void getNormalizedPathTest(){
        authHandler.getNormalizedPath(NGSILD_ENTITIES_URL);
    }
}
