package iudx.gis.server.authenticate;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

@VertxGen
@ProxyGen
public interface AuthenticatorService {

    @GenIgnore
    static AuthenticatorService createProxy(Vertx vertx, String address) {
        return new AuthenticatorServiceVertxEBProxy(vertx, address);
    }

    @Fluent
    AuthenticatorService tokenInterospect(JsonObject request, JsonObject authenticationInfo,
                                           Handler<AsyncResult<JsonObject>> handler);
}
