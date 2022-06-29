package iudx.gis.server.apiserver.validation.types;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.ValidationException;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import iudx.gis.server.apiserver.exceptions.DxRuntimeException;
import iudx.gis.server.apiserver.util.RequestType;
import iudx.gis.server.apiserver.validation.ValidatorsHandlersFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import io.vertx.json.schema.Schema;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(VertxExtension.class)
@ExtendWith(MockitoExtension.class)
class JsonSchemaTypeValidatorTest {
    JsonSchemaTypeValidator jsonSchemaTypeValidator;
    @Mock
    Schema schema;
    @Mock
    JsonObject jsonObject;
    @Mock
    Vertx vertx;
    @Mock
    ValidationException validationException;


    @BeforeEach
    public void setup(Vertx vertx, VertxTestContext testContext){
        jsonSchemaTypeValidator= new JsonSchemaTypeValidator(jsonObject,schema);
        testContext.completeNow();
    }

    @Test
    public void test(VertxTestContext vertxTestContext){
        assertTrue(jsonSchemaTypeValidator.isValid());
        vertxTestContext.completeNow();
    }

    @Test
    public void test2(VertxTestContext vertxTestContext){
        assertNotNull(jsonSchemaTypeValidator.failureCode());
        vertxTestContext.completeNow();
    }

    @Test
    public void test3(VertxTestContext vertxTestContext){
        assertNotNull(jsonSchemaTypeValidator.failureMessage());
        vertxTestContext.completeNow();
    }
    /*static Stream<Arguments> invalidValues() {
        // Add any valid value which will pass successfully.
        String random600Id = RandomStringUtils.random(600);
        return Stream.of(
                Arguments.of(false)
                //,
                //Arguments.of("  ", true)
                );
    }*/
    /*@ParameterizedTest
    @MethodSource("invalidValues")*/
    /*@Test
    public void test2(VertxTestContext vertxTestContext){
        JsonObject jsonObject1=new JsonObject().put("asd",null);
        MultiMap params = MultiMap.caseInsensitiveMultiMap();
        MultiMap headers = MultiMap.caseInsensitiveMultiMap();

        ValidatorsHandlersFactory validatorsHandlersFactory=new ValidatorsHandlersFactory();
        validatorsHandlersFactory.build(vertx, RequestType.ADMIN_CRUD_PATH,params,headers,null);
        assertThrows(ValidationException.class,()->jsonSchemaTypeValidator.isValid());
        //assertThrows(DxRuntimeException.class,()->jsonSchemaTypeValidator.isValid());
        vertxTestContext.completeNow();
    }*/
    /*@Test
    public void test2(VertxTestContext vertxTestContext){
        *//*doAnswer(invocation -> {

        });*//*
        JsonObject jsonObject1=new JsonObject().put("body",null);
        MultiMap params = MultiMap.caseInsensitiveMultiMap();
        MultiMap headers = MultiMap.caseInsensitiveMultiMap();

        ValidatorsHandlersFactory validatorsHandlersFactory=new ValidatorsHandlersFactory();
        validatorsHandlersFactory.build(vertx, RequestType.ADMIN_CRUD_PATH,params,headers,jsonObject1);
        //assertThrows(ValidationException.class,()->jsonSchemaTypeValidator.isValid());
        assertThrows(DxRuntimeException.class,()->jsonSchemaTypeValidator.isValid());
        vertxTestContext.completeNow();
    }*/
}