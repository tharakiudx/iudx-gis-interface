package iudx.gis.server.authenticator.authorization;

import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import iudx.gis.server.common.Api;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(VertxExtension.class)
class AuthorizationContextFactoryTest {

AuthorizationContextFactory authorizationContextFactory;
@Mock
private Api api;

@Test
    public void test(VertxTestContext vertxTestContext){
    assertThrows(IllegalArgumentException.class,()->AuthorizationContextFactory.create(IudxRole.PROVIDER,api));
    vertxTestContext.completeNow();
}

    @Test
    public void test2(VertxTestContext vertxTestContext){
        assertThrows(IllegalArgumentException.class,()->AuthorizationContextFactory.create(null,api));
        vertxTestContext.completeNow();
    }
}