package iudx.gis.server.apiserver.util;


public class Constants {
  public static final String API_ENDPOINT = "apiEndpoint";
  public static final String API_METHOD = "method";
  public static final String ID = "id";
  public static final String EPOCH_TIME = "epochTime";
  public static final String ISO_TIME = "isoTime";

  // NGSI-LD endpoints
  public static final String NGSILD_ENTITIES_URL =  "/entities";

  // path regex
  public static final String ENTITITES_URL_REGEX = NGSILD_ENTITIES_URL + "(.*)";

  /** API Documentation endpoint */
  public static final String ROUTE_STATIC_SPEC = "/apis/spec";

  public static final String ROUTE_DOC = "/apis";
  /** Accept Headers and CORS */
  public static final String MIME_APPLICATION_JSON = "application/json";

  public static final String MIME_TEXT_HTML = "text/html";
  // ngsi-ld/IUDX query paramaters
  public static final String NGSILDQUERY_ID = "id";
  public static final String NGSILDQUERY_IDPATTERN = "idpattern";
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
  // request/response params
  public static final String CONTENT_TYPE = "content-type";
  public static final String APPLICATION_JSON = "application/json";
  // json fields
  public static final String ERROR_MESSAGE = "errorMessage";
  public static final String JSON_TYPE = "type";
  public static final String JSON_TITLE = "title";
  public static final String JSON_DETAIL = "detail";
  public static final String JSON_RESULT = "results";
  public static final String EXPIRY = "expiry";
  public static final String IID = "iid";
  public static final String RESPONSE_SIZE = "response_size";
  // messages (Error, Exception, messages..)
  public static final String MSG_BAD_QUERY = "Bad query";
  // Validations
  public static final int VALIDATION_ID_MAX_LEN = 512;
  /** Accept Headers and CORS */
  public static final String AUTH_INFO = "authInfo";
}
