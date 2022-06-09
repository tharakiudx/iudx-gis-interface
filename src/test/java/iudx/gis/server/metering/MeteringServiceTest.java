package iudx.gis.server.metering;

import static iudx.gis.server.metering.util.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static iudx.gis.server.metering.util.Constants.API;
import static iudx.gis.server.metering.util.Constants.ID;
import static iudx.gis.server.metering.util.Constants.IID;
import static iudx.gis.server.metering.util.Constants.RESPONSE_SIZE;
import static iudx.gis.server.metering.util.Constants.USER_ID;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import iudx.gis.server.apiserver.response.ResponseUrn;
import iudx.gis.server.configuration.Configuration;
import java.util.UUID;

import iudx.gis.server.metering.util.ResponseBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({VertxExtension.class})
public class MeteringServiceTest {

  private static final Logger LOGGER = LogManager.getLogger(MeteringServiceTest.class);
  public static String userId;
  public static String id;
  private static MeteringService meteringService;
  private static Vertx vertxObj;
  private static String databaseIP;
  private static int databasePort;
  private static String databaseName;
  private static String databaseUserName;
  private static String databasePassword;
  private static String databaseTableName;
  private static int databasePoolSize;
  private static Configuration config;

  @BeforeAll
  @DisplayName("Deploying Verticle")
  static void startVertex(Vertx vertx, VertxTestContext vertxTestContext) {
    vertxObj = vertx;
    config = new Configuration();
    JsonObject dbConfig = config.configLoader(3, vertx);
    databaseIP = dbConfig.getString("meteringDatabaseIP");
    databasePort = dbConfig.getInteger("meteringDatabasePort");
    databaseName = dbConfig.getString("meteringDatabaseName");
    databaseUserName = dbConfig.getString("meteringDatabaseUserName");
    databasePassword = dbConfig.getString("meteringDatabasePassword");
    databasePoolSize = dbConfig.getInteger("meteringPoolSize");
    databaseTableName = dbConfig.getString("meteringDatabaseTableName");
    meteringService = new MeteringServiceImpl(dbConfig, vertxObj);
    userId = UUID.randomUUID().toString();
    id = "89a36273d77dac4cf38114fca1bbe64392547f86";
    vertxTestContext.completeNow();
  }

  @Test
  @DisplayName("Testing Write Query")
  void writeData(VertxTestContext vertxTestContext) {
    JsonObject request = new JsonObject();
    request.put(USER_ID, "15c7506f-c800-48d6-adeb-0542b03947c6");
    request.put(ID, "15c7506f-c800-48d6-adeb-0542b03947c6/integration-test-alias/");
    request.put(API, "/ngsi-ld/v1/entities");
    request.put(RESPONSE_SIZE, 12345);
    meteringService.executeWriteQuery(
        request,
        vertxTestContext.succeeding(
            response ->
                vertxTestContext.verify(
                    () -> {
                      assertTrue(response.getString("title").equals("Success"));
                      vertxTestContext.completeNow();
                    })));
  }

  @Test
  @DisplayName("Testing Write Query for admin api.")
  void writeAdminData(VertxTestContext vertxTestContext) {
    JsonObject request = new JsonObject();
    request.put(USER_ID, "15c7506f-c800-48d6-adeb-0542b03947c6");
    request.put(IID, "rs.iudx.io");
    request.put(API, "/admin/gis/serverInfo");
    request.put(RESPONSE_SIZE, 0);
    meteringService.executeWriteQuery(
        request,
        vertxTestContext.succeeding(
            response ->
                vertxTestContext.verify(
                    () -> {
                      assertTrue(response.getString("title").equals("Success"));
                      vertxTestContext.completeNow();
                    })));
  }
  @Test
  @DisplayName("Set Type And Title Test")
  public void setTypeAndTitleTest(VertxTestContext vertxTestContext){
    ResponseBuilder responseBuilder= new ResponseBuilder("200");
    responseBuilder.setTypeAndTitle(200);
    assertEquals("Successful operation",ResponseUrn.SUCCESS.getMessage());
    responseBuilder.setTypeAndTitle(204);
    assertEquals("Success",SUCCESS);
    responseBuilder.setTypeAndTitle(400);
    assertEquals("bad request parameter",ResponseUrn.BAD_REQUEST_URN.getMessage());
    vertxTestContext.completeNow();
  }
}
