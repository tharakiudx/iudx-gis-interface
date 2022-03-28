package iudx.gis.server.database.postgres;

import static iudx.gis.server.common.Constants.DATABASE_SERVICE_ADDRESS;
import static iudx.gis.server.common.Constants.PG_SERVICE_ADDRESS;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.serviceproxy.ServiceBinder;
import io.vertx.sqlclient.PoolOptions;
import iudx.gis.server.database.DatabaseService;
import iudx.gis.server.database.DatabaseServiceImpl;
import iudx.gis.server.database.PostgresClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PostgresVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LogManager.getLogger(PostgresService.class);

  private MessageConsumer<JsonObject> consumer;
  private MessageConsumer<JsonObject> consumer2;

  private ServiceBinder binder;
  private DatabaseService database;
  private PgConnectOptions connectOptions;
  private PoolOptions poolOptions;
  private PgPool pool;
  private PostgresClient pgClient;

  /* DataBase properties */
  private String databaseIP;
  private int databasePort;
  private String databaseName;
  private String databaseUserName;
  private String databasePassword;
  private int poolSize;

  private PostgresService pgService;

  @Override
  public void start() throws Exception {

    databaseIP = config().getString("databaseIp");
    databasePort = config().getInteger("databasePort");
    databaseName = config().getString("databaseName");
    databaseUserName = config().getString("databaseUserName");
    databasePassword = config().getString("databasePassword");
    poolSize = config().getInteger("poolSize");

    this.connectOptions =
        new PgConnectOptions()
            .setPort(databasePort)
            .setHost(databaseIP)
            .setDatabase(databaseName)
            .setUser(databaseUserName)
            .setPassword(databasePassword)
            .setReconnectAttempts(2)
            .setReconnectInterval(1000);

    this.poolOptions = new PoolOptions().setMaxSize(poolSize);
    this.pool = PgPool.pool(vertx, connectOptions, poolOptions);

    pool = PgPool.pool(vertx, connectOptions, poolOptions);

    pgClient = new PostgresClient(vertx, connectOptions, poolOptions);
    binder = new ServiceBinder(vertx);
    database = new DatabaseServiceImpl(pgClient);

    pgService = new PostgresServiceImpl(this.pool);

    consumer = binder.setAddress(DATABASE_SERVICE_ADDRESS).register(DatabaseService.class, database);

    consumer2 = binder.setAddress(PG_SERVICE_ADDRESS).register(PostgresService.class, pgService);
    LOGGER.info("Postgres verticle started.");
  }

  @Override
  public void stop() {
    binder.unregister(consumer);
  }
}
