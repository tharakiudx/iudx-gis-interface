package iudx.gis.server.apiserver.validation.types;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import io.vertx.json.schema.Schema;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(VertxExtension.class)
@ExtendWith(MockitoExtension.class)
class JsonSchemaTypeValidatorTest {
    JsonSchemaTypeValidator jsonSchemaTypeValidator;
    @Mock
    Schema schema;
    @Mock
    JsonObject jsonObject;

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

}