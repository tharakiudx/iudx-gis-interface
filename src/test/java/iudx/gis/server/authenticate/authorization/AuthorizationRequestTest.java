package iudx.gis.server.authenticate.authorization;

import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import iudx.gis.server.authenticator.authorization.Api;
import iudx.gis.server.authenticator.authorization.AuthorizationRequest;
import iudx.gis.server.authenticator.authorization.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({VertxExtension.class, MockitoExtension.class})
public class AuthorizationRequestTest {
    @Test
    @DisplayName("AuthRequest should not Null")
    public void authRequestShouldNotNull(VertxTestContext vertxTestContext) {
        AuthorizationRequest authR1= new AuthorizationRequest(Method.GET, Api.ENTITIES);
        assertNotNull(authR1);
        vertxTestContext.completeNow();
    }

    @Test
    @DisplayName("authRequest should have same hashcode")

    public void authRequestShouldhaveSameHash(VertxTestContext vertxTestContext) {
        AuthorizationRequest authR1= new AuthorizationRequest(Method.GET, Api.ENTITIES);
        AuthorizationRequest authR2= new AuthorizationRequest(Method.GET, Api.ENTITIES);
        assertEquals(authR1.hashCode(), authR2.hashCode());
        vertxTestContext.completeNow();
    }
}
