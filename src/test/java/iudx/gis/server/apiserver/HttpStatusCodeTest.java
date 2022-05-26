package iudx.gis.server.apiserver;

import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import iudx.gis.server.apiserver.util.HttpStatusCode;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(VertxExtension.class)
public class HttpStatusCodeTest {
    @ParameterizedTest
    @EnumSource
    public void test(HttpStatusCode httpStatusCode, VertxTestContext testContext){
        assertNotNull(httpStatusCode);
        testContext.completeNow();
    }
}
