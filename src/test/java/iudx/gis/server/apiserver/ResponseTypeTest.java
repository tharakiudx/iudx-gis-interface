package iudx.gis.server.apiserver;

import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import iudx.gis.server.apiserver.response.ResponseType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(VertxExtension.class)
public class ResponseTypeTest {
    @ParameterizedTest
    @EnumSource
    public void test(ResponseType responseType, VertxTestContext testContext){
        assertNotNull(responseType);
        testContext.completeNow();
    }
    @Test
    @DisplayName("Test for a single enum")
    public void testEnumInternalError( VertxTestContext vertxTestContext)
    {
        assertEquals("Internal error",ResponseType.InternalError.getMessage());
        assertEquals(500,ResponseType.InternalError.getCode());
        vertxTestContext.completeNow();
    }
}
