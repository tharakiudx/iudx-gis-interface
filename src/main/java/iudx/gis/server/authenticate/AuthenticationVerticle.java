package iudx.gis.server.authenticate;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.serviceproxy.ServiceBinder;

public class AuthenticationVerticle extends AbstractVerticle {

  private static final String AUTH_SERVICE_ADDRESS = "iudx.gis.authentication.service";
  private AuthenticatorService authenticator;
  private ServiceBinder binder;
  private MessageConsumer<JsonObject> consumer;
  private WebClientOptions webClientOptions = new WebClientOptions();

  @Override
  public void start() throws Exception {
    binder = new ServiceBinder(vertx);
    authenticator = new AuthenticatorServiceImpl(vertx, WebClient.create(vertx, webClientOptions));

    /* Publish the Authentication service with the Event Bus against an address. */

    consumer =
        binder.setAddress(AUTH_SERVICE_ADDRESS).register(AuthenticatorService.class, authenticator);
  }

  @Override
  public void stop() {
    binder.unregister(consumer);
  }
}
