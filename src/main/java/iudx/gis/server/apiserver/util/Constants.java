package iudx.gis.server.apiserver.util;

import java.util.List;

public class Constants {
  // date-time format
  public static final String APP_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss[.SSSSSS]'Z'";
  public static final String APP_NAME_REGEX = "[a-zA-Z0-9._\\-]*$";

  public static final String API_ENDPOINT = "apiEndpoint";
  public static final String API_METHOD = "method";
  public static final String ID = "id";
  public static final String IDS = "ids";

  // config
  public static final String CONFIG_FILE = "config.properties";

  // NGSI-LD endpoints
  public static final String NGSILD_BASE_PATH = "/ngsi-ld/v1";
  public static final String NGSILD_ENTITIES_URL = NGSILD_BASE_PATH + "/entities";
  // path regex
  public static final String ENTITITES_URL_REGEX = NGSILD_ENTITIES_URL + "(.*)";
  // admin API endpoints
  public static final String ADMIN_BASE_PATH = "/admin/gis/serverInfo";
  /** API Documentation endpoint */
  public static final String ROUTE_STATIC_SPEC = "/apis/spec";
  public static final String ROUTE_DOC = "/apis";
  public static final List<String> bypassEndpoint = List.of(ROUTE_STATIC_SPEC, ROUTE_DOC);
  public static final List<String> openEndPoints =
      List.of(
          "/ngsi-ld/v1/temporal/entities",
          "/ngsi-ld/v1/entities",
          "/ngsi-ld/v1/entityOperations/query");
  public static String PG_SERVICE_ADD="iudx.rs.pgsql.service";
  /** Accept Headers and CORS */
  public static final String MIME_APPLICATION_JSON = "application/json";

  public static final String MIME_TEXT_HTML = "text/html";

  // ngsi-ld/IUDX query paramaters
  public static final String NGSILDQUERY_ID = "id";
  public static final String NGSILDQUERY_IDPATTERN = "idpattern";
  public static final String NGSILDQUERY_TYPE = "type";
  public static final String IUDXQUERY_OPTIONS = "options";
  public static final String NGSILDQUERY_ENTITIES = "entities";
  public static final String USER_ID = "userid";
  public static final String API = "api";

  // Header params
  public static final String HEADER_TOKEN = "token";
  public static final String HEADER_HOST = "Host";
  public static final String HEADER_ACCEPT = "Accept";
  public static final String HEADER_CONTENT_LENGTH = "Content-Length";
  public static final String HEADER_CONTENT_TYPE = "Content-Type";
  public static final String HEADER_ORIGIN = "Origin";
  public static final String HEADER_REFERER = "Referer";
  public static final String HEADER_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
  public static final String HEADER_OPTIONS = "options";

  // request/response params
  public static final String CONTENT_TYPE = "content-type";
  public static final String APPLICATION_JSON = "application/json";

  // json fields
  public static final String ERROR_MESSAGE = "errorMessage";
  public static final String JSON_INSTANCEID = "instanceID";
  public static final String JSON_TYPE = "type";
  public static final String JSON_DOMAIN = "domain";
  public static final String JSON_USERSHA = "userSHA";
  public static final String JSON_NAME = "name";
  public static final String JSON_ENTITIES = "entities";
  public static final String JSON_ID = "id";
  public static final String JSON_VALUE = "value";
  public static final String JSON_TITLE = "title";
  public static final String JSON_DETAIL = "detail";
  public static final String JSON_RESULT = "results";
  public static final String JSON_RESOURCE_SERVER = "resourceServer";
  public static final String JSON_RESOURCE_GROUP = "resourceGroup";
  public static final String JSON_RESOURCE_NAME = "resourceName";
  public static final String EXPIRY = "expiry";
  public static final String IID = "iid";


  // messages (Error, Exception, messages..)
  public static final String MSG_BAD_QUERY = "Bad query";

  // Validations
  public static final int VALIDATION_ID_MAX_LEN = 512;
  /** Accept Headers and CORS */
  public static final String DOMAIN = "domain";
  public static final String USERSHA = "userSha";
  public static final String JSON_ALIAS = "alias";
  public static final String RESOURCE_SERVER = "resourceServer";
  public static final String RESOURCE_GROUP = "resourceGroup";
  public static final String RESOURCE_NAME = "resourceName";
  public static final String AUTH_INFO = "authInfo";

}
