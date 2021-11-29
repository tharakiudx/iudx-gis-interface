package iudx.gis.server.apiserver.validation.types;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.Schema;

public final class JsonSchemaTypeValidator implements Validator {

  private static final Logger LOGGER = LogManager.getLogger(JsonSchemaTypeValidator.class);

  private final JsonObject value;
  private final Schema schema;

  public JsonSchemaTypeValidator(final JsonObject value, final Schema schema) {
    this.value = value;
    this.schema = schema;
  }

  @Override
  public boolean isValid() {
    try {
      schema.validateSync(value);
    } catch (Exception e) {
      LOGGER.error("Json schema validation failed due to: {}", e.getMessage());
      return false;
    }
    LOGGER.debug("Json request body validated");
    return true;
//    try {
//      schema.validateSync(value);
//    } catch (ValidationException e) {
//      LOGGER.error("Validation error :" + e.getMessage());
//      throw new Exception();
//    } catch (NoSyncValidationException e) {
//      LOGGER.error("Validation error :" + e.getMessage());
//      throw new DxRuntimeException(failureCode(), INVALID_PAYLOAD_FORMAT, failureMessage(value.toString()));
//    }
  }

  @Override
  public int failureCode() {
//    return HttpStatusCode.BAD_REQUEST.getValue();
    return 404;
  }

  @Override
  public String failureMessage() {
return "Bad request";
//    return INVALID_PAYLOAD_FORMAT.getMessage();
  }

}
