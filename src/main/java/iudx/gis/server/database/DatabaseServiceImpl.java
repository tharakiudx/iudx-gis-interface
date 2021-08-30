package iudx.gis.server.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public class DatabaseServiceImpl implements DatabaseService {

  private static final Logger LOGGER = LogManager.getLogger(DatabaseServiceImpl.class);
  private PostgresClient pgSQLClient;
  public static final String SELECT_GIS_SERVER_URL =
      "SELECT * FROM gis WHERE iudx_resource_id='$1'";
 
  public DatabaseServiceImpl(PostgresClient pgClient) {
    // TODO Auto-generated constructor stub
    this.pgSQLClient = pgClient;
  }

  @Override
  public DatabaseService searchQuery(JsonObject request, Handler<AsyncResult<JsonObject>> handler) {
    Future<JsonObject> getGISURL = getURLInDb(request.getString("id"));
    getGISURL.onComplete(getUserApiKeyHandler -> {
      if (getUserApiKeyHandler.succeeded()) {
        LOGGER.info("DATABASE_READ_SUCCESS");
        handler.handle(Future.succeededFuture(getUserApiKeyHandler.result()));
      } else {
        LOGGER.info("DATABASE_READ_FAILURE");
      }
    });
    return this;
  }

  @Override
  public DatabaseService insertIntoDb(JsonObject request,
      Handler<AsyncResult<JsonObject>> handler) {
    handler.handle(Future.succeededFuture(new JsonObject().put("a", "b")));
    return this;
  }

  Future<JsonObject> getURLInDb(String id) {

    LOGGER.debug("Info : PSQLClient#getUserInDb() started");
    Promise<JsonObject> promise = Promise.promise();
    JsonObject response = new JsonObject();
    String query = SELECT_GIS_SERVER_URL.replace("$1", id);
    LOGGER.debug("Info : " + query);
    // Check in DB, get username and password
    pgSQLClient.executeAsync(query).onComplete(db -> {
      LOGGER.debug("Info : PSQLClient#getUserInDb()executeAsync completed");
      if (db.succeeded()) {
        LOGGER.debug("Info : PSQLClient#getUserInDb()executeAsync success");
        String url = null;
        // Get the apiKey
        RowSet<Row> result = db.result();
        if (db.result().size() > 0) {
          for (Row row : result) {
            url = row.getString(2);
          }
        }
        response.put("type", "success");
        response.put("title", "Successfully fetched the GIS server URL");
        response.put("results", new JsonArray().add(new JsonObject().put("URL", url)));
        promise.complete(response);
      } else {
        LOGGER.fatal("Fail : PSQLClient#getUserInDb()executeAsync failed");
        promise.fail("Error : Get ID from database failed");
      }
    });
    return promise.future();
  }


}
