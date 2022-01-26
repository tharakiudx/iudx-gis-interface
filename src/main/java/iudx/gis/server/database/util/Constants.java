package iudx.gis.server.database.util;

public class Constants {

  public static final String TYPE = "type";
  public static final String TITLE = "title";
  public static final String SUCCESS = "Success";
  public static final String ERROR_MESSAGE = "errorMessage";
  public static final String DETAIL="detail";

  public static final String ID = "id";
  public static final String SERVER_URL = "server-url";
  public static final String SERVER_PORT = "server-port";
  public static final String SECURE = "isSecure";

  public static final String ACCESS_INFO = "accessInfo";
  public static final String USERNAME = "username";
  public static final String PASSWORD = "password";
  public static final String TOKEN_URL = "tokenURL";

  public static final String TABLE_NAME = "gis";

  public static final String SELECT_ADMIN_DETAILS_QUERY =
      "SELECT * FROM " + TABLE_NAME + " WHERE iudx_resource_id = '$1'";

  public static final String INSERT_ADMIN_DETAILS_QUERY =
      "INSERT INTO " + TABLE_NAME + "(iudx_resource_id, url, port, isOpen, username, password,tokenurl) " +
          "VALUES ('$1', '$2', '$3', '$4', '$5', '$6','$7')";

  public static final String UPDATE_ADMIN_DETAILS_QUERY =
      "UPDATE " + TABLE_NAME + " SET url='$1', port='$2', isOpen='$3', username='$4', password='$5', tokenurl='$7' " +
          "WHERE iudx_resource_id='$6'";

  public static final String DELETE_ADMIN_DETAILS_QUERY =
      "DELETE FROM " + TABLE_NAME + " WHERE iudx_resource_id = '$1'";
}
