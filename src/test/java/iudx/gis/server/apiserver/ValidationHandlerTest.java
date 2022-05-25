package iudx.gis.server.apiserver;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.junit5.VertxExtension;
import iudx.gis.server.apiserver.handlers.ValidationHandler;
import iudx.gis.server.apiserver.util.RequestType;
import iudx.gis.server.apiserver.validation.ValidatorsHandlersFactory;
import iudx.gis.server.apiserver.validation.types.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith({VertxExtension.class, MockitoExtension.class})
public class ValidationHandlerTest {
    ValidationHandler validationHandler;
    Vertx vertx;
    RequestType requestTypeMock;

   @BeforeEach
    public void setUp(){
        vertx= mock(Vertx.class);
        //requestTypeMock= mock(RequestType.class);
        //validationHandler =new ValidationHandler(vertx,requestTypeMock);
        validationHandler= mock(ValidationHandler.class);
   }

    @Test
    public void testHandle(){
        ValidatorsHandlersFactory validatorsHandlersFactoryMock
                =mock(ValidatorsHandlersFactory.class);
        MultiMap multiMapMock= mock(MultiMap.class);
        RoutingContext routingContextMock= mock(RoutingContext.class);
        HttpServerRequest httpServerRequestMock = mock(HttpServerRequest.class);
        Map mapMock= mock(Map.class);
        JsonObject jsonObjectMock= mock(JsonObject.class);
        List listMock= mock(List.class);
        Validator validatorMock= mock(Validator.class);

        when(routingContextMock.request()).thenReturn(httpServerRequestMock);
        when(httpServerRequestMock.params()).thenReturn(multiMapMock);

        when(routingContextMock.request()).thenReturn(httpServerRequestMock);
        when(httpServerRequestMock.headers()).thenReturn(multiMapMock);

        when(routingContextMock.pathParams()).thenReturn(mapMock);
        when(routingContextMock.getBodyAsJson()).thenReturn(jsonObjectMock);

        //validatorsHandlersFactoryMock.build(vertx,requestTypeMock,multiMapMock,multiMapMock,jsonObjectMock);
        when(validatorsHandlersFactoryMock.build(any(),any(),any(),any(),any())).thenReturn(listMock);
        validationHandler.handle(routingContextMock);

    }



}
