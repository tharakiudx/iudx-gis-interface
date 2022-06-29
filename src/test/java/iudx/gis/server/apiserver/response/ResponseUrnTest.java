package iudx.gis.server.apiserver.response;

import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(VertxExtension.class)
class ResponseUrnTest {
    /*ResponseUrn responseUrn;
    @Test
    public void test1(VertxTestContext vertxTestContext){
        assertNull(responseUrn.toString());
        vertxTestContext.completeNow();
    }*/
    @Test
    public void test2(VertxTestContext vertxTestContext){
        assertNotNull(ResponseUrn.fromCode("urn:dx:rs:backend"));
        vertxTestContext.completeNow();
    }
}