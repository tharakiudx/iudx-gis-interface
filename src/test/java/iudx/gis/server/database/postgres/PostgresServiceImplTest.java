package iudx.gis.server.database.postgres;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import iudx.gis.server.apiserver.query.PgsqlQueryBuilder;
import iudx.gis.server.configuration.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.PostgreSQLContainer;

import static iudx.gis.server.common.Constants.*;
import static iudx.gis.server.database.util.Constants.ID;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Arrays;

@ExtendWith(VertxExtension.class)
public class PostgresServiceImplTest {
  private static final Logger LOGGER = LogManager.getLogger(PostgresServiceImplTest.class);
  static PostgresServiceImpl postgresService;
  static PostgreSQLContainer<?> postgresContainer;
  public static String CONTAINER = "postgres:12.11";
  public static String database = "iudx";
  private static Configuration config;
  private static JsonObject dbConfig;

  @BeforeAll
  static void setup(Vertx vertx, VertxTestContext vertxTestContext) {
    config = new Configuration();
    dbConfig = config.configLoader(2, vertx);

    dbConfig.put("databaseIp", "localhost");
    dbConfig.put("databasePort", 5432);
    dbConfig.put("databaseName", database);
    dbConfig.put("databaseUserName", "iudx_user");
    dbConfig.put("databasePassword", "pg@postgres.dk");
    dbConfig.put("poolSize", 25);

    postgresContainer = new PostgreSQLContainer<>(CONTAINER).withInitScript("pg_test_schema.sql");

    postgresContainer.withUsername(dbConfig.getString("databaseUserName"));
    postgresContainer.withPassword(dbConfig.getString("databasePassword"));
    postgresContainer.withDatabaseName(dbConfig.getString("databaseName"));
    postgresContainer.withExposedPorts(dbConfig.getInteger("databasePort"));
    postgresContainer.start();

    if (postgresContainer.isRunning()) {
      dbConfig.put("databasePort", postgresContainer.getFirstMappedPort());

      PgConnectOptions connectOptions =
          new PgConnectOptions()
              .setPort(dbConfig.getInteger("databasePort"))
              .setHost(dbConfig.getString("databaseIp"))
              .setDatabase(dbConfig.getString("databaseName"))
              .setUser(dbConfig.getString("databaseUserName"))
              .setPassword(dbConfig.getString("databasePassword"))
              .setReconnectAttempts(2)
              .setReconnectInterval(1000);
      PoolOptions poolOptions = new PoolOptions().setMaxSize(dbConfig.getInteger("poolSize"));
      PgPool pool = PgPool.pool(vertx, connectOptions, poolOptions);

      postgresService = new PostgresServiceImpl(pool);
      vertxTestContext.completeNow();
    }
  }


  @Test
  public void getAllDetails(VertxTestContext vertxTestContext) {
    String query = PgsqlQueryBuilder.getAdminDetailsQuery(
        "iisc.ac.in/89a36273d77dac4cf38114fca1bbe64392547f86/rs.iudx.io/varanasi/varanasi_test");

    postgresService.executeQuery(query, handler -> {
      if (handler.succeeded()) {
        JsonObject result = handler.result();
        JsonArray rows = result.getJsonArray("result");
        JsonObject json = rows.getJsonObject(0);
        assertEquals("https://map.varanasismartcity.gov.in/varanasismartcity",
            json.getString("url"));
        vertxTestContext.completeNow();
      } else {
        vertxTestContext.failNow(handler.cause());
      }
    });
  }

  @Test
  public void getAllDetailsFailure(VertxTestContext vertxTestContext) {
    String wrongQuery = "select * from gis1";

    postgresService.executeQuery(wrongQuery, handler -> {
      if (handler.succeeded()) {
        vertxTestContext.failNow(handler.cause());
      } else {
        String resultM = handler.cause().getMessage();
        JsonObject json=new JsonObject(resultM);
        assertEquals("urn:dx:rs:DatabaseError", json.getString("type"));
        assertEquals(400, json.getInteger("status"));
        vertxTestContext.completeNow();


      }
    });
  }
  
}
