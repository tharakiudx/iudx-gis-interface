package iudx.gis.server.apiserver.handlers;

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import iudx.gis.server.apiserver.validation.ValidatorsHandlersFactory;
import iudx.gis.server.apiserver.validation.types.Validator;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class ValidationHandler implements Handler<RoutingContext> {

    private static final Logger LOGGER = LogManager.getLogger(ValidationHandler.class);
    private Vertx vertx;


    public ValidationHandler(Vertx vertx) {
        this.vertx=vertx;
    }

    @Override
    public void handle(RoutingContext context) {
        ValidatorsHandlersFactory validationFactory = new ValidatorsHandlersFactory();
        MultiMap parameters = context.request().params();
        MultiMap headers = context.request().headers();
        Map<String, String> pathParams = context.pathParams();
        parameters.addAll(pathParams);

        Validator validator = validationFactory.build(vertx, parameters, headers);

        if (!validator.isValid()) {
            error(context);
            return;
        }
        context.next();
        return;
    }

    private void error(RoutingContext context) {
        context.response().putHeader("content-type", "application/json")
                .setStatusCode(HttpStatus.SC_BAD_REQUEST)
                .end(getBadRequestMessage().toString());
    }

    private JsonObject getBadRequestMessage() {
        return new JsonObject()
                .put("type", 400)
                .put("title", "Bad Request")
                .put("details", "Bad query");
    }
}
