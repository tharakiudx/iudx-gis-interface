package iudx.gis.server.database.postgres;

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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.PostgreSQLContainer;

import static iudx.gis.server.database.util.Constants.ID;
import static iudx.gis.server.database.util.Constants.SELECT_ADMIN_DETAILS_QUERY;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class PostGresTest {
    private static final Logger LOGGER = LogManager.getLogger(PostGresTest.class);
    static PostgresServiceImpl postgresService;
    static PostgreSQLContainer<?> postgresContainer;
    public static String CONTAINER = "postgres:12.11";
    public static String database = "iudx";
    private static Configuration config;
    private static JsonObject dbConfig;

    @BeforeAll
    static void setup(Vertx vertx, VertxTestContext vertxTestContext){
        config= new Configuration();
        dbConfig = config.configLoader(2,vertx);

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
        LOGGER.debug(postgresContainer.getLogs());
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

    /*@Test
    @DisplayName("Execute Prepaid Query Passed Case")
    public void executePreQueryPassedCase(VertxTestContext testContext){
        JsonObject query= (JsonObject) new JsonObject();
        postgresService.executePreparedQuery(SELECT_ADMIN_DETAILS_QUERY, query , handler->{
            if(handler.succeeded())
            {
                JsonObject response = handler.result();
                assertTrue(response.containsKey("result"));
                assertEquals(1,response.size());
                testContext.completeNow();
            }
            else {
                testContext.failNow(handler.cause());
            }
        });
    }*/
    /*@Test
    @DisplayName("Execute Prepaid Query Failed Case")
    public void executePreQueryFailedCase(VertxTestContext testContext){
        JsonObject queryParams= (JsonObject) new JsonObject().put(ID,"resId1");
        postgresService.executePreparedQuery(SELECT_ADMIN_DETAILS_QUERY, queryParams ,handler->{
            if(handler.succeeded())
            {
                testContext.failNow(handler.cause());
                JsonObject failureCause = new JsonObject(handler.cause().getMessage());
                assertTrue(failureCause.containsKey(ID));
            }
            else {
                testContext.completeNow();
            }
        });
    }
*/

}
