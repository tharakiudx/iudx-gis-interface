package iudx.gis.server.apiserver;
import io.vertx.core.*;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.*;
import io.vertx.junit5.VertxExtension;
import iudx.gis.server.apiserver.exceptions.DxRuntimeException;
import iudx.gis.server.apiserver.handlers.ValidationFailureHandler;
import iudx.gis.server.apiserver.response.ResponseUrn;
import iudx.gis.server.apiserver.util.HttpStatusCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith({VertxExtension.class, MockitoExtension.class})
public class ValidationFailureHandlerTest {
    ValidationFailureHandler validationFailureHandler;

    ResponseUrn responseUrn;
    @BeforeEach
    public void setUp(){
        validationFailureHandler = new ValidationFailureHandler();
    }
        @Test
        @DisplayName("Runtime exception test case")
        public void runtimeExceptiontest() {
        RoutingContext routingContextMock = mock(RoutingContext.class);
        HttpServerResponse httpResponseMock = mock(HttpServerResponse.class);
        Future<Void> voidFutureMock = mock(Future.class);
        RuntimeException runtimeExceptionMock = mock(RuntimeException.class);
        when(routingContextMock.response()).thenReturn(httpResponseMock);
        when(routingContextMock.failure()).thenReturn(runtimeExceptionMock);
        when(httpResponseMock.putHeader(anyString(),anyString())).thenReturn(httpResponseMock);
        when(httpResponseMock.setStatusCode(anyInt())).thenReturn(httpResponseMock);
        when(httpResponseMock.end(anyString())).thenReturn(voidFutureMock);
        validationFailureHandler.handle(routingContextMock);
    }
/*    @Test
    @DisplayName("DxRuntime exception test case")
    public void dxruntimeExceptiontest() {
        RoutingContext routingContextMock = mock(RoutingContext.class);
        HttpServerResponse httpServerResponseMock = mock(HttpServerResponse.class);
        Future<Void> voidFutureMock = mock(Future.class);
        DxRuntimeException dxRuntimeExceptionMock = mock(DxRuntimeException.class);

        HttpStatusCode httpStatusCodeMock=  HttpStatusCode.BAD_REQUEST;

        when(routingContextMock.failure()).thenReturn(dxRuntimeExceptionMock);
        when(dxRuntimeExceptionMock.getUrn()).thenReturn(responseUrn);
        when(responseUrn.getUrn()).thenReturn("dummy urn");

        when(dxRuntimeExceptionMock.getStatusCode()).thenReturn(400);
        when(routingContextMock.response()).thenReturn(httpServerResponseMock);

        when(httpServerResponseMock.putHeader(anyString(),anyString())).thenReturn(httpServerResponseMock);
        when(httpServerResponseMock.setStatusCode(anyInt())).thenReturn(httpServerResponseMock);
        when(httpServerResponseMock.end(anyString())).thenReturn(voidFutureMock);
        validationFailureHandler.handle(routingContextMock);
    }*/
}
