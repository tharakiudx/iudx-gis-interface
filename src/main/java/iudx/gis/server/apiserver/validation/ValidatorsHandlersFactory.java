package iudx.gis.server.apiserver.validation;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import iudx.gis.server.apiserver.util.RequestType;
import iudx.gis.server.apiserver.validation.types.IdTypeValidator;
import iudx.gis.server.apiserver.validation.types.StringTypeValidator;
import iudx.gis.server.apiserver.validation.types.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static iudx.gis.server.apiserver.util.Constants.NGSILDQUERY_ID;

public class ValidatorsHandlersFactory {

  private static final Logger LOGGER = LogManager.getLogger(ValidatorsHandlersFactory.class);

  public List<Validator> build(final Vertx vertx, RequestType type, final MultiMap parameters,
      final MultiMap headers) {
    List<Validator> validator = new ArrayList<>();
    LOGGER.debug("getValidation4Context() started for :" + type);
    switch (type) {
      case ENTITY_PATH:
        validator.add(new StringTypeValidator(parameters.get("domain"), true));
        validator.add(new StringTypeValidator(parameters.get("userSha"), true));
        validator.add(new StringTypeValidator(parameters.get("resourceServer"), true));
        validator.add(new StringTypeValidator(parameters.get("resourceGroup"), true));
        validator.add(new StringTypeValidator(parameters.get("resourceName"), true));
        break;
      case ENTITY_QUERY:
        validator.add(new IdTypeValidator(parameters.get(NGSILDQUERY_ID), true));
        break;
      default:
        break;
    }
    return validator;
  }
}
