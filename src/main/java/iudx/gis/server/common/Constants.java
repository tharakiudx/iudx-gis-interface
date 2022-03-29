package iudx.gis.server.common;

public class Constants {

  /** service proxy addresses **/
  public static final String PG_SERVICE_ADDRESS = "iudx.gis.pgsql.service";
  public static final String CACHE_SERVICE_ADDRESS = "iudx.gis.cache.service";
  public static final String AUTHENTICATION_SERVICE_ADDRESS = "iudx.gis.authentication.service";
  public static final String DATABASE_SERVICE_ADDRESS = "iudx.gis.database.service";
  public static final String DATABROKER_SERVICE_ADDRESS = "iudx.gis.broker.service";
  public static final String GIS_INVALID_SUB="invalid-tokens";

  // postgres queries
  public static String SELECT_REVOKE_TOKEN_SQL = "SELECT * FROM revoked_tokens";

}
