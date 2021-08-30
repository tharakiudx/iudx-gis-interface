package iudx.gis.server.database;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

public class DatabaseServiceImpl implements DatabaseService {

  @Override
  public DatabaseService searchQuery(JsonObject request, Handler<AsyncResult<JsonObject>> handler) {
    handler.handle(Future.succeededFuture(new JsonObject().put("a", "b")));
    return this;
  }

  @Override
  public DatabaseService insertIntoDb(JsonObject request,
      Handler<AsyncResult<JsonObject>> handler) {
    handler.handle(Future.succeededFuture(new JsonObject().put("a", "b")));
    return this;
  }
}
