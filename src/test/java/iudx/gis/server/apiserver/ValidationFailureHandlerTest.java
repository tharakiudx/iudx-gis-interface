package iudx.gis.server.apiserver;
import io.vertx.core.*;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.*;
import io.vertx.junit5.VertxExtension;
import iudx.gis.server.apiserver.handlers.ValidationFailureHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith({VertxExtension.class, MockitoExtension.class})
public class ValidationFailureHandlerTest {
    ValidationFailureHandler validationFailureHandler;
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
   /* @Test
    @DisplayName("DxRuntime exception test case")
    public void dxruntimeExceptiontest() {
        RoutingContext routingContextMock = mock(RoutingContext.class);
        HttpServerResponse httpResponseMock = mock(HttpServerResponse.class);
        Future<Void> voidFutureMock = mock(Future.class);
        DxRuntimeException dxRuntimeExceptionMock = mock(DxRuntimeException.class);
        ResponseUrn responseUrnMock= mock(ResponseUrn.class);
        HttpStatusCode httpStatusCodeMock= mock(HttpStatusCode.class);

        when(dxRuntimeExceptionMock.getUrn()).thenReturn(responseUrnMock);
        when(responseUrnMock.getUrn()).thenReturn("Dummy URN");

        //when(httpResponseMock.getStatusCode()).thenReturn(httpStatusCodeMock);

        when(routingContextMock.failure()).thenReturn(dxRuntimeExceptionMock);

        when(httpResponseMock.putHeader(anyString(),anyString())).thenReturn(httpResponseMock);
        when(httpResponseMock.setStatusCode(anyInt())).thenReturn(httpResponseMock);
        when(httpResponseMock.end(anyString())).thenReturn(voidFutureMock);
        validationFailureHandler.handle(routingContextMock);
    }*/
}
