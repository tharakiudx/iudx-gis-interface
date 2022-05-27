package iudx.gis.server.apiserver.validation;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import iudx.gis.server.apiserver.util.RequestType;
import iudx.gis.server.apiserver.validation.types.IdTypeValidator;
import iudx.gis.server.apiserver.validation.types.StringTypeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith({VertxExtension.class, MockitoExtension.class})
public class ValidatorsHandlersFactoryTest {
ValidatorsHandlersFactory validatorsHandlersFactory;
    Map<String, String> jsonSchemaMap = new HashMap<>();
    @Mock
    Vertx vertx;
    @Mock
    IdTypeValidator idTypeValidator;

    @BeforeEach
    public void setUp(){
    validatorsHandlersFactory = new ValidatorsHandlersFactory();
    }

    @Test
    @DisplayName("getAdminCrudPathDeleteValidations Test")
    public void getAdminCrudPathDeleteValidationsTest(VertxTestContext vertxTestContext){
    MultiMap params = MultiMap.caseInsensitiveMultiMap();
    MultiMap headers = MultiMap.caseInsensitiveMultiMap();
    JsonObject jsonObject = mock(JsonObject.class);

    var validator =validatorsHandlersFactory.build(vertx, RequestType.ADMIN_CRUD_PATH_DELETE,params,headers,jsonObject);

    assertEquals(1,validator.size());
    vertxTestContext.completeNow();
    }

    @Test
    @DisplayName("getEntityQueryValidations Test")
    public void getEntityQueryValidationsTest(VertxTestContext vertxTestContext){
        MultiMap params = MultiMap.caseInsensitiveMultiMap();
        MultiMap headers = MultiMap.caseInsensitiveMultiMap();
        JsonObject jsonObject = mock(JsonObject.class);

        var validator =validatorsHandlersFactory.build(vertx, RequestType.ENTITY_QUERY,params,headers,jsonObject);

        assertEquals(1,validator.size());
        vertxTestContext.completeNow();
    }

    @Test
    @DisplayName("getAdminCrudPathValidations Test")
    public void getAdminCrudPathValidationsTest(VertxTestContext vertxTestContext){
        MultiMap params = MultiMap.caseInsensitiveMultiMap();
        MultiMap headers = MultiMap.caseInsensitiveMultiMap();
        JsonObject jsonObject = mock(JsonObject.class);

        var validator =validatorsHandlersFactory.build(vertx, RequestType.ADMIN_CRUD_PATH,params,headers,jsonObject);
        assertEquals(1,validator.size());
        vertxTestContext.completeNow();
    }
    @Test
    @DisplayName("getEntityPathValidations Test")
    public void getEntityPathValidationsTest(VertxTestContext vertxTestContext){
        MultiMap params = MultiMap.caseInsensitiveMultiMap();
        MultiMap headers = MultiMap.caseInsensitiveMultiMap();
        JsonObject jsonObject = mock(JsonObject.class);

        var validator =validatorsHandlersFactory.build(vertx, RequestType.ENTITY_PATH,params,headers,jsonObject);
        assertEquals(5,validator.size());
        vertxTestContext.completeNow();
    }
}
