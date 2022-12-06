package iudx.gis.server.metering.util;

import static iudx.gis.server.metering.util.Constants.ADMIN;
import static iudx.gis.server.metering.util.Constants.ADMIN_BASE_PATH;
import static iudx.gis.server.metering.util.Constants.API;
import static iudx.gis.server.metering.util.Constants.ID;
import static iudx.gis.server.metering.util.Constants.IID;
import static iudx.gis.server.metering.util.Constants.ORIGIN;
import static iudx.gis.server.metering.util.Constants.ORIGIN_SERVER;
import static iudx.gis.server.metering.util.Constants.PRIMARY_KEY;
import static iudx.gis.server.metering.util.Constants.PROVIDER_ID;
import static iudx.gis.server.metering.util.Constants.USER_ID;

import io.vertx.core.json.JsonObject;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QueryBuilder {

  private static final Logger LOGGER = LogManager.getLogger(QueryBuilder.class);

  public JsonObject buildMessageForRMQ(JsonObject request) {
    String primaryKey = UUID.randomUUID().toString().replace("-", "");
    String api = request.getString(API);
    String resourceId =
        api.equals(ADMIN_BASE_PATH) ? request.getString(IID) : request.getString(ID);
    String providerID =
        api.equals(ADMIN_BASE_PATH)
            ? ADMIN
            : resourceId.substring(0, resourceId.indexOf('/', resourceId.indexOf('/') + 1));
    request.remove(IID);
    request.put(ID,resourceId);
    request.put(PRIMARY_KEY, primaryKey);
    request.put(PROVIDER_ID, providerID);
    request.put(ORIGIN, ORIGIN_SERVER);

    LOGGER.trace("Info: Request " + request);
    return request;
  }
}
