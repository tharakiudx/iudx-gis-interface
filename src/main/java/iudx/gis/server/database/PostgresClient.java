package iudx.gis.server.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;

public class PostgresClient {
  private static final Logger LOGGER = LogManager.getLogger(PostgresClient.class);

  private static PgPool pgPool;

  public PostgresClient(Vertx vertx, PgConnectOptions pgConnectOptions,
      PoolOptions connectionPoolOptions) {
      pgPool = PgPool.pool(vertx, pgConnectOptions, connectionPoolOptions);
  }

  public Future<RowSet<Row>> executeAsync(String preparedQuerySQL) {
    LOGGER.debug("Info : PostgresQLClient#executeAsync() started");
    Promise<RowSet<Row>> promise = Promise.promise();
    pgPool.withConnection(connection -> connection.query(preparedQuerySQL).execute())
        .onSuccess(ar -> {
          LOGGER.debug("Info : connectionHandler.succeeded()");
          promise.complete(ar);
        })
        .onFailure(ar -> {
          LOGGER.error("Could not execute query due to: {}", ar.getLocalizedMessage());
          promise.fail(ar);
        });
    
    return promise.future();
  }
}
