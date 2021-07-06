package iudx.gis.server.apiserver.validation;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import iudx.gis.server.apiserver.validation.types.IdTypeValidator;
import iudx.gis.server.apiserver.validation.types.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static iudx.gis.server.apiserver.Constants.NGSILDQUERY_ID;

public class ValidatorsHandlersFactory {

    private static final Logger LOGGER =
            LogManager.getLogger(ValidatorsHandlersFactory.class);

    public Validator build(final Vertx vertx,
                                 final MultiMap parameters,
                                 final MultiMap headers) {
        Validator validator = new IdTypeValidator(parameters.get(NGSILDQUERY_ID), true);
        return validator;
    }
}
