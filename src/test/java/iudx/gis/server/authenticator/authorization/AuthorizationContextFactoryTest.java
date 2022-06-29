package iudx.gis.server.authenticator.authorization;

import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(VertxExtension.class)
class AuthorizationContextFactoryTest {

AuthorizationContextFactory authorizationContextFactory;

@Test
    public void test(VertxTestContext vertxTestContext){
    assertThrows(IllegalArgumentException.class,()->AuthorizationContextFactory.create(IudxRole.PROVIDER));
    vertxTestContext.completeNow();
}

    @Test
    public void test2(VertxTestContext vertxTestContext){
        assertThrows(IllegalArgumentException.class,()->AuthorizationContextFactory.create(null));
        vertxTestContext.completeNow();
    }
}