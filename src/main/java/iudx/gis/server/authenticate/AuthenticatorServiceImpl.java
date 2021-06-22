package iudx.gis.server.authenticate;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

public class AuthenticatorServiceImpl implements AuthenticatorService {


    public AuthenticatorServiceImpl(Vertx vertx, WebClient client, JsonObject config) {}

    public AuthenticatorServiceImpl(Vertx vertx, WebClient webClient) {
    }

    @Override
    public AuthenticatorService tokenInterospect(JsonObject request, JsonObject authenticationInfo, Handler<AsyncResult<JsonObject>> handler) {
        return null;
    }
}
