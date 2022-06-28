package iudx.gis.server.apiserver.validation.types;

import io.vertx.core.Vertx;
import io.vertx.core.cli.annotations.Description;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import iudx.gis.server.apiserver.exceptions.DxRuntimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(VertxExtension.class)
class StringTypeValidatorTest {
    StringTypeValidator stringTypeValidator;
    @BeforeEach
    public  void setup(Vertx vertx, VertxTestContext testContext){
        testContext.completeNow();
    }
    static Stream<Arguments> invalidValues() {
        return Stream.of(
                Arguments.of("  ", false),
                Arguments.of(null,true));
    }

    static Stream<Arguments> allowedValues() {
        // Add any valid value for which validation will pass successfully.
        return Stream.of(
                Arguments.of("within", true),
                Arguments.of("intersects", true),
                Arguments.of("near", true),
                Arguments.of(null, false));
    }


    @ParameterizedTest
    @MethodSource("allowedValues")
    /*@Description("String type parameter valid values.")*/
    public void testValidGeoRelValue(String value, boolean required, Vertx vertx,
                                     VertxTestContext testContext) {
        StringTypeValidator stringTypeValidator = new StringTypeValidator(value, required);
        assertTrue(stringTypeValidator.isValid());
        testContext.completeNow();
    }
    @ParameterizedTest
    @MethodSource("invalidValues")
    @Description("id type parameter invalid values.")
    public void testInvalidIDTypeValue(String value, boolean required, Vertx vertx,
                                       VertxTestContext testContext) {
        stringTypeValidator = new StringTypeValidator(value, required);
        assertThrows(DxRuntimeException.class, () -> stringTypeValidator.isValid());
        testContext.completeNow();
    }
}