package iudx.gis.server.authenticator.authorization;

import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import iudx.gis.server.common.Api;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({VertxExtension.class, MockitoExtension.class})
public class AuthorizationRequestTest {

    private String dxApiBasePath;
    private String adminBasePath;
    private Api api;
    @BeforeEach
    public void init(VertxTestContext vertxTestContext)
    {
        dxApiBasePath = "/ngsi-ld/v1";
        adminBasePath = "/admin/gis";
        api = Api.getInstance(dxApiBasePath,adminBasePath);
        vertxTestContext.completeNow();
    }
    @Test
    @DisplayName("AuthRequest should not Null")
    public void authRequestShouldNotNull(VertxTestContext vertxTestContext) {
        AuthorizationRequest authR1= new AuthorizationRequest(Method.GET, api.getEntitiesEndpoint());
        assertNotNull(authR1);
        vertxTestContext.completeNow();
    }

    @Test
    @DisplayName("authRequest should have same hashcode")

    public void authRequestShouldhaveSameHash(VertxTestContext vertxTestContext) {
        AuthorizationRequest authR1= new AuthorizationRequest(Method.GET, api.getEntitiesEndpoint());
        AuthorizationRequest authR2= new AuthorizationRequest(Method.GET, api.getEntitiesEndpoint());
        assertEquals(authR1.hashCode(), authR2.hashCode());
        vertxTestContext.completeNow();
    }

    @Test
    @DisplayName("authRequest Equals")

    public void authRequestEquals(VertxTestContext vertxTestContext) {
        AuthorizationRequest authR1= new AuthorizationRequest(Method.GET, api.getEntitiesEndpoint());
        Object obj= new Object();
        assertNotNull(authR1.equals(obj));
        vertxTestContext.completeNow();
    }

    @Test
    @DisplayName("authRequest Equals null")

    public void authRequestEquals2(VertxTestContext vertxTestContext) {
        AuthorizationRequest authR1= new AuthorizationRequest(Method.GET, api.getEntitiesEndpoint());
        Object obj= new Object();
        assertNotNull(authR1.equals(null));
        vertxTestContext.completeNow();
    }
}
