package iudx.gis.server.database;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceBinder;

public class DatabaseVerticle extends AbstractVerticle {
  private DatabaseService database;
  private static final String DATABASE_SERVICE_ADDRESS = "iudx.gis.database.service";
  private ServiceBinder binder;
  private MessageConsumer<JsonObject> consumer;

  @Override
  public void start() throws Exception {

    binder = new ServiceBinder(vertx);
    database = new DatabaseServiceImpl();
    consumer =
        binder.setAddress(DATABASE_SERVICE_ADDRESS).register(DatabaseService.class, database);
  }


  @Override
  public void stop() {
    binder.unregister(consumer);
  }
}
