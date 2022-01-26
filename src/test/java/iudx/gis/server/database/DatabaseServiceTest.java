package iudx.gis.server.database;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import iudx.gis.server.configuration.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static iudx.gis.server.database.util.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseServiceTest {
  private static DatabaseService database;

  private static String resId1;
  private static String resId2;
  private static String username;
  private static String password;
  private static int serverPort;
  public static String tokenUrl;
  private static String serverUrl;
  private static JsonObject accessInfo;

  /* Database Properties */
  private static String databaseIP;
  private static int databasePort;
  private static String databaseName;
  private static String databaseUserName;
  private static String databasePassword;
  private static int poolSize;
  private static PgConnectOptions connectOptions;
  private static PoolOptions poolOptions;
  private static PgPool pgPool;
  private static PostgresClient pgClient;

  private static Configuration dbConfig;

  private static final Logger LOGGER = LogManager.getLogger(DatabaseServiceTest.class);

  @BeforeAll
  @DisplayName("Deploy Database Verticle")
  static void startVertx(Vertx vertx, VertxTestContext vertxTestContext) {

    dbConfig = new Configuration();
    JsonObject config = dbConfig.configLoader(2, vertx);

    try {
      databaseIP = config.getString("databaseIP");
      databasePort = config.getInteger("databasePort");
      databaseName = config.getString("databaseName");
      databaseUserName = config.getString("databaseUserName");
      databasePassword = config.getString("databasePassword");
      poolSize = config.getInteger("dbClientPoolSize");
    } catch (Exception e) {
      LOGGER.fatal("Could not set db properties due to: {}", e.getMessage());
      vertxTestContext.failNow(e);
    }

    /* Set data for requests */
    resId1 = UUID.randomUUID().toString();
    resId2 = UUID.randomUUID().toString();
    username = UUID.randomUUID().toString();
    password = UUID.randomUUID().toString();
    serverUrl = UUID.randomUUID().toString();
    tokenUrl=UUID.randomUUID().toString();
    serverPort = ThreadLocalRandom.current().nextInt(1, 5000);
    accessInfo = new JsonObject()
        .put(USERNAME, username)
        .put(PASSWORD, password)
        .put(TOKEN_URL,tokenUrl);

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
    pgPool = PgPool.pool(vertx, connectOptions, poolOptions);

    pgClient = new PostgresClient(vertx, connectOptions, poolOptions);
    database = new DatabaseServiceImpl(pgClient);

    vertxTestContext.completeNow();
  }

  @Test
  @DisplayName("Insert Admin Details into DB without accessInfo")
  @Order(1)
  void successfullyInsertAdminDetailsWithoutAccessInfo(VertxTestContext testContext) {
    JsonObject request = new JsonObject()
        .put(ID, resId1)
        .put(SERVER_URL, serverUrl)
        .put(SERVER_PORT, serverPort)
        .put(SECURE, false);

    JsonObject expected = new JsonObject()
        .put(DETAIL, SUCCESS);

    database.insertAdminDetails(request, ar -> {
      if (ar.succeeded()) {
        JsonObject response = ar.result();
        LOGGER.debug("Insert admin details without access info response is: {}", response.toString());
        assertEquals(expected, response);
        testContext.completeNow();
      } else {
        testContext.failNow(ar.cause());
      }
    });
  }

  @Test
  @DisplayName("Insert Admin Details into DB with accessInfo")
  @Order(2)
  void successfullyInsertAdminDetailsWithAccessInfo(VertxTestContext testContext) {
    JsonObject request = new JsonObject()
        .put(ID, resId2)
        .put(SERVER_URL, serverUrl)
        .put(SERVER_PORT, serverPort)
        .put(SECURE, true)
        .put(ACCESS_INFO, accessInfo);

    JsonObject expected = new JsonObject()
        .put(DETAIL, SUCCESS);

    database.insertAdminDetails(request, ar -> {
      if (ar.succeeded()) {
        JsonObject response = ar.result();
        LOGGER.debug("Insert admin details with access info response is: {}", response.toString());
        assertEquals(expected, response);
        testContext.completeNow();
      } else {
        testContext.failNow(ar.cause());
      }
    });
  }

  @Test
  @DisplayName("Update Admin Details without access info")
  @Order(3)
  void successfullyUpdateAdminDetailsWithoutAccessInfo(VertxTestContext testContext) {
    JsonObject request = new JsonObject()
        .put(ID, resId2)
        .put(SERVER_URL, serverUrl)
        .put(SERVER_PORT, serverPort)
        .put(SECURE, false);

    JsonObject expected = new JsonObject()
        .put(DETAIL, SUCCESS);

    database.updateAdminDetails(request, ar -> {
      if (ar.succeeded()) {
        JsonObject response = ar.result();
        LOGGER.debug("Update admin details without access info response is: {}", response.toString());
        assertEquals(expected, response);
        testContext.completeNow();
      } else {
        testContext.failNow(ar.cause());
      }
    });
  }

  @Test
  @DisplayName("Update Admin Details without access info")
  @Order(4)
  void successfullyUpdateAdminDetailsWithAccessInfo(VertxTestContext testContext) {
    JsonObject request = new JsonObject()
        .put(ID, resId1)
        .put(SERVER_URL, serverUrl)
        .put(SERVER_PORT, serverPort)
        .put(SECURE, true)
        .put(ACCESS_INFO, accessInfo);

    JsonObject expected = new JsonObject()
        .put(DETAIL, SUCCESS);

    database.updateAdminDetails(request, ar -> {
      if (ar.succeeded()) {
        JsonObject response = ar.result();
        LOGGER.debug("Update admin details with access info response is: {}", response.toString());
        assertEquals(expected, response);
        testContext.completeNow();
      } else {
        testContext.failNow(ar.cause());
      }
    });
  }

  @Test
  @DisplayName("Delete Admin Details case 1")
  @Order(5)
  void successfullyDeleteAdminDetails1(VertxTestContext testContext) {
    String request = resId1;

    JsonObject expected = new JsonObject()
        .put(DETAIL, SUCCESS);

    database.deleteAdminDetails(request, ar -> {
      if (ar.succeeded()) {
        JsonObject response = ar.result();
        LOGGER.debug("Delete admin details response is: {}", response.toString());
        assertEquals(expected, response);
        testContext.completeNow();
      } else {
        testContext.failNow(ar.cause());
      }
    });
  }

  @Test
  @DisplayName("Delete Admin Details case 2")
  @Order(6)
  void successfullyDeleteAdminDetails2(VertxTestContext testContext) {
    String request = resId2;

    JsonObject expected = new JsonObject()
        .put(DETAIL, SUCCESS);

    database.deleteAdminDetails(request, ar -> {
      if (ar.succeeded()) {
        JsonObject response = ar.result();
        LOGGER.debug("Delete admin details response is: {}", response.toString());
        assertEquals(expected, response);
        testContext.completeNow();
      } else {
        testContext.failNow(ar.cause());
      }
    });
  }
}
