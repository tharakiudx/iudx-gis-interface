package iudx.gis.server.apiserver;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import iudx.gis.server.authenticate.AuthenticatorService;
import iudx.gis.server.database.DatabaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

import static iudx.gis.server.apiserver.Constants.*;

public class ApiServerVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LogManager.getLogger(ApiServerVerticle.class);

    /** Service addresses */
    private static final String DATABASE_SERVICE_ADDRESS = "iudx.gis.database.service";
    private static final String AUTH_SERVICE_ADDRESS = "iudx.gis.authentication.service";

    private HttpServer server;
    private Router router;
    private int port = 8443;
    private boolean isSSL, isProduction;
    private String keystore;
    private String keystorePassword;
    // private CatalogueService catalogueService;

    private DatabaseService database;
    private AuthenticatorService authenticator;
    // private ParamsValidator validator;


    @Override
    public void start() throws Exception {
        Set<String> allowedHeaders = new HashSet<>();
        allowedHeaders.add(HEADER_ACCEPT);
        allowedHeaders.add(HEADER_TOKEN);
        allowedHeaders.add(HEADER_CONTENT_LENGTH);
        allowedHeaders.add(HEADER_CONTENT_TYPE);
        allowedHeaders.add(HEADER_HOST);
        allowedHeaders.add(HEADER_ORIGIN);
        allowedHeaders.add(HEADER_REFERER);
        allowedHeaders.add(HEADER_ALLOW_ORIGIN);

        Set<HttpMethod> allowedMethods = new HashSet<>();
        allowedMethods.add(HttpMethod.GET);
        allowedMethods.add(HttpMethod.POST);
        allowedMethods.add(HttpMethod.PATCH);
        allowedMethods.add(HttpMethod.PUT);

        router = Router.router(vertx);

        router = Router.router(vertx);
        router.route().handler(
                CorsHandler.create("*").allowedHeaders(allowedHeaders).allowedMethods(allowedMethods));

        router.route().handler(requestHandler -> {
            requestHandler.response()
                    .putHeader("Cache-Control", "no-cache, no-store,  must-revalidate,max-age=0")
                    .putHeader("Pragma", "no-cache")
                    .putHeader("Expires", "0")
                    .putHeader("X-Content-Type-Options", "nosniff");
            requestHandler.next();
        });

        router.route().handler(BodyHandler.create());

        /* Read ssl configuration. */
        isSSL = config().getBoolean("ssl");

        /* Read server deployment configuration. */
        isProduction = config().getBoolean("production");

        HttpServerOptions serverOptions = new HttpServerOptions();

        if (isSSL) {
            LOGGER.debug("Info: Starting HTTPs server");

            /* Read the configuration and set the HTTPs server properties. */

            keystore = config().getString("keystore");
            keystorePassword = config().getString("keystorePassword");

            /* Setup the HTTPs server properties, APIs and port. */

            serverOptions.setSsl(true)
                    .setKeyStoreOptions(new JksOptions().setPath(keystore).setPassword(keystorePassword));

        } else {
            LOGGER.debug("Info: Starting HTTP server");

            /* Setup the HTTP server properties, APIs and port. */

            serverOptions.setSsl(false);
            if (isProduction) {
                port = 80;
            } else {
                port = 8080;
            }
        }

        serverOptions.setCompressionSupported(true).setCompressionLevel(5);
        server = vertx.createHttpServer(serverOptions);
        server.requestHandler(router).listen(port);

        /* Get a handler for the Service Discovery interface. */

        database = DatabaseService.createProxy(vertx, DATABASE_SERVICE_ADDRESS);
        authenticator = AuthenticatorService.createProxy(vertx, AUTH_SERVICE_ADDRESS);
    }
}
