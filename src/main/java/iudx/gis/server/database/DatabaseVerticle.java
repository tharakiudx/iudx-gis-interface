package iudx.gis.server.database;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.serviceproxy.ServiceBinder;
import io.vertx.sqlclient.PoolOptions;

public class DatabaseVerticle extends AbstractVerticle {
  private DatabaseService database;
  private static final String DATABASE_SERVICE_ADDRESS = "iudx.gis.database.service";
  private ServiceBinder binder;
  private MessageConsumer<JsonObject> consumer;

  /* Database Properties */
  private String databaseIP;
  private int databasePort;
  private String databaseName;
  private String databaseUserName;
  private String databasePassword;
  private int poolSize;
  private PgPool pgclient;
  private PoolOptions poolOptions;
  private PgConnectOptions connectOptions;
  private PostgresClient pgClient;
  
  @Override
  public void start() throws Exception {

    databaseIP = config().getString("databaseIP");
    databasePort = config().getInteger("databasePort");
    databaseName = config().getString("databaseName");
    databaseUserName = config().getString("databaseUserName");
    databasePassword = config().getString("databasePassword");
    poolSize = config().getInteger("dbClientPoolSize");

    /* Set Connection Object */
    if (connectOptions == null) {
      connectOptions = new PgConnectOptions().setPort(databasePort).setHost(databaseIP)
          .setDatabase(databaseName).setUser(databaseUserName).setPassword(databasePassword);
    }

    /* Pool options */
    if (poolOptions == null) {
      poolOptions = new PoolOptions().setMaxSize(poolSize);
    }

    /* Create the client pool */
    pgclient = PgPool.pool(vertx, connectOptions, poolOptions);

    pgClient = new PostgresClient(vertx, connectOptions, poolOptions);

    binder = new ServiceBinder(vertx);
    database = new DatabaseServiceImpl(pgClient);
    consumer =
        binder.setAddress(DATABASE_SERVICE_ADDRESS).register(DatabaseService.class, database);
  }


  @Override
  public void stop() {
    binder.unregister(consumer);
  }
}