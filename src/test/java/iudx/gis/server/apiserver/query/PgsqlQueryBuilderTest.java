package iudx.gis.server.apiserver.query;

import static iudx.gis.server.database.util.Constants.ACCESS_INFO;
import static iudx.gis.server.database.util.Constants.ID;
import static iudx.gis.server.database.util.Constants.PASSWORD;
import static iudx.gis.server.database.util.Constants.SECURE;
import static iudx.gis.server.database.util.Constants.SERVER_PORT;
import static iudx.gis.server.database.util.Constants.SERVER_URL;
import static iudx.gis.server.database.util.Constants.TOKEN_URL;
import static iudx.gis.server.database.util.Constants.USERNAME;
import static org.junit.Assert.assertTrue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;

@ExtendWith(VertxExtension.class)
public class PgsqlQueryBuilderTest {

  private static final Logger LOGGER = LogManager.getLogger(PgsqlQueryBuilder.class);

  @BeforeAll
  static void init(VertxTestContext testContext) {
    testContext.completeNow();
  }

  @Test
  public void getAdminDetailsQueryTest(VertxTestContext testContext) {
    String query = PgsqlQueryBuilder.getAdminDetailsQuery("abc").toLowerCase();
    String expected = "select * from gis where iudx_resource_id = 'abc'";
    assertTrue(expected.equalsIgnoreCase(query));
    testContext.completeNow();
  }

  @Test
  public void insertAdminDetailsQueryTest(VertxTestContext testContext) {

    JsonObject queryParams = new JsonObject();

    queryParams.put(ID, "abc");
    queryParams.put(SERVER_URL, "www.abc.in/test");
    queryParams.put(SERVER_PORT, 1234L);
    queryParams.put(SECURE, false);

    String query = PgsqlQueryBuilder.insertAdminDetailsQuery(queryParams).toLowerCase();
    String expected =
        "insert into gis(iudx_resource_id, url, port, isOpen, username, password,tokenurl) " +
            "values ('abc', 'www.abc.in/test', '1234', 'false', '', '','')";
    assertTrue(expected.equalsIgnoreCase(query));
    testContext.completeNow();

  }


  @Test
  public void insertAdminDetailsQueryWithAccessInfoTest(VertxTestContext testContext) {

    JsonObject queryParams = new JsonObject();
    JsonObject accessInfo = new JsonObject();

    queryParams.put(ID, "abc");
    queryParams.put(SERVER_URL, "www.abc.in/test");
    queryParams.put(SERVER_PORT, 1234L);
    queryParams.put(SECURE, false);
    queryParams.put(ACCESS_INFO, accessInfo);

    accessInfo.put(USERNAME, "username");
    accessInfo.put(PASSWORD, "password");
    accessInfo.put(TOKEN_URL, "www.tokenurl.in");

    String query = PgsqlQueryBuilder.insertAdminDetailsQuery(queryParams).toLowerCase();

    String expected =
        "insert into gis(iudx_resource_id, url, port, isOpen, username, password,tokenurl) " +
            "values ('abc', 'www.abc.in/test', '1234', 'false', 'username', 'password','www.tokenurl.in')";

    assertTrue(expected.equalsIgnoreCase(query));
    testContext.completeNow();

  }

  @Test
  public void updateAdminDetailsQueryTest(VertxTestContext testContext) {

    JsonObject queryParams = new JsonObject();

    queryParams.put(ID, "abc");
    queryParams.put(SERVER_URL, "www.abc.in/test");
    queryParams.put(SERVER_PORT, 1234L);
    queryParams.put(SECURE, false);

    String query = PgsqlQueryBuilder.updateAdminDetailsQuery(queryParams).toLowerCase();

    String expected =
        "update gis set url='www.abc.in/test', port='1234', isOpen='false', username='', password='', tokenurl='' "
            +
            "where iudx_resource_id='abc'";
    assertTrue(expected.equalsIgnoreCase(query));
    testContext.completeNow();

  }


  @Test
  public void updateAdminDetailsQueryWithAccessInfoTest(VertxTestContext testContext) {

    JsonObject queryParams = new JsonObject();
    JsonObject accessInfo = new JsonObject();

    queryParams.put(ID, "abc");
    queryParams.put(SERVER_URL, "www.abc.in/test");
    queryParams.put(SERVER_PORT, 1234L);
    queryParams.put(SECURE, false);
    queryParams.put(ACCESS_INFO, accessInfo);

    accessInfo.put(USERNAME, "username");
    accessInfo.put(PASSWORD, "password");
    accessInfo.put(TOKEN_URL, "www.tokenurl.in");

    String query = PgsqlQueryBuilder.updateAdminDetailsQuery(queryParams).toLowerCase();
    String expected =
        "update gis set url='www.abc.in/test', port='1234', isOpen='false', username='username', password='password', tokenurl='www.tokenurl.in' "
            +
            "where iudx_resource_id='abc'";
    assertTrue(expected.equalsIgnoreCase(query));
    testContext.completeNow();

  }

  @Test
  public void deleteAdminDetailsQueryTest(VertxTestContext testContext) {
    String query = PgsqlQueryBuilder.deleteAdminDetailsQuery("abc").toLowerCase();
    String expected = "delete from gis where iudx_resource_id = 'abc'";
    assertTrue(expected.equalsIgnoreCase(query));
    testContext.completeNow();
  }

}
