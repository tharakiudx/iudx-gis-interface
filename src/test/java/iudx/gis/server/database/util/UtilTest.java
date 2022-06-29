package iudx.gis.server.database.util;

import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import iudx.gis.server.apiserver.util.HttpStatusCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static iudx.gis.server.database.util.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(VertxExtension.class)
class UtilTest {
Util util;

JsonObject jsonObject= new JsonObject().put(TYPE,HttpStatusCode.BAD_REQUEST)
        .put(TITLE,"urn:dx:rs:badRequest").put(ERROR_MESSAGE,"Bad Request");
@Test
    public void test(VertxTestContext vertxTestContext){
    assertNotNull(Util.getResponse(HttpStatusCode.BAD_REQUEST,"urn:dx:rs:badRequest","Bad Request"));
    vertxTestContext.completeNow();
}
}