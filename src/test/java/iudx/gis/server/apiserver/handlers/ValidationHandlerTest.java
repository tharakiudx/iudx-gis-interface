package iudx.gis.server.apiserver.handlers;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import iudx.gis.server.apiserver.exceptions.DxRuntimeException;
import iudx.gis.server.apiserver.util.Constants;
import iudx.gis.server.apiserver.util.RequestType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith({VertxExtension.class, MockitoExtension.class})
public class ValidationHandlerTest {
    ValidationHandler validationHandler,validationHandler2;
    Vertx vertx;
    RequestType requestTypeMock;
    MultiMap parameters;

   @BeforeEach
    public void setUp(){
        vertx= mock(Vertx.class);
        validationHandler =new ValidationHandler(vertx,RequestType.ENTITY_QUERY);
   }

    @Test
    @DisplayName("Validation Successful")
    public void testHandle(VertxTestContext vertxTestContext){
        MultiMap multiMapMock= mock(MultiMap.class);
        RoutingContext routingContextMock= mock(RoutingContext.class);
        HttpServerRequest httpServerRequestMock = mock(HttpServerRequest.class);
        JsonObject jsonObjectMock= mock(JsonObject.class);

        when(routingContextMock.request()).thenReturn(httpServerRequestMock);

        parameters = MultiMap.caseInsensitiveMultiMap();
        parameters.set(Constants.ID, "asdasd/asdasd/adasd/adasd/adasd");

        when(httpServerRequestMock.params()).thenReturn(parameters);
        when(httpServerRequestMock.headers()).thenReturn(multiMapMock);

        when(routingContextMock.getBodyAsJson()).thenReturn(jsonObjectMock);

       validationHandler.handle(routingContextMock);
       vertxTestContext.completeNow();
    }

    /*@Test
    @DisplayName("Validation Failure")
    public void testHandle2(){
        MultiMap multiMapMock= mock(MultiMap.class);
        RoutingContext routingContextMock= mock(RoutingContext.class);
        HttpServerRequest httpServerRequestMock = mock(HttpServerRequest.class);
        JsonObject jsonObjectMock= mock(JsonObject.class);
        parameters = MultiMap.caseInsensitiveMultiMap();
        parameters.set(Constants.ID, "asdasd/asdasd");
        when(httpServerRequestMock.params()).thenReturn(parameters);
        when(httpServerRequestMock.headers()).thenReturn(multiMapMock);

        //when(routingContextMock.getBodyAsJson()).thenReturn(jsonObjectMock);
        //validationHandler2 = new ValidationHandler(vertx,RequestType.ENTITY_WRONG_PATH);
        assertThrows(DxRuntimeException.class, ()-> validationHandler.handle(routingContextMock));
    }*/


}
