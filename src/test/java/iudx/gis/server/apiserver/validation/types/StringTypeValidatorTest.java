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
                Arguments.of("12323324893438348585478424248284248274274284278427437483458734578345873427942974128472142323783783783123233248934383485854784242482842482742742842784274374834587345783458734279429741284721423237831232332489343834858547842424828424827427428427842743748345873457834587342794297412847214232378378378347537485347537853874573457857873834eyuefhdhfth78378347537485347537853874573457857873834eyuefhdhfth47537485347537853874573457857873834eyuefhdhfth3fe83t7h34873y2y429y423984374297447  ", false),
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