package zh.lingvo.core.config.generators;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.core.config.LanguageDescriptionsStructure;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DisplayName("Test Parts of speech code generator")
class PosCodeGeneratorTest {
    @Test
    @DisplayName("Test generating of the PoS file context")
    void testGenerate() {
        LanguageDescriptionsStructure structure = new LanguageDescriptionsStructure();
        structure.partsOfSpeech.putAll(ImmutableMap.of(
                "POS_A", "a",
                "POS_B", "b",
                "POS_C", "c"));
        Map<String, String> flags = ImmutableMap.of(PosCodeGenerator.CLASS_NAME, "PartOfSpeech");
        PosCodeGenerator generator = new PosCodeGenerator(structure, flags);

        String actual = generator.generate();

        assertThat(actual, is("" +
                "import com.google.common.collect.ImmutableMap;\n" +
                "import java.util.Arrays;\n" +
                "import java.util.Map;\n" +
                "import java.util.function.Function;\n" +
                "\n" +
                "public enum PartOfSpeech {\n" +
                "    POS_A(\"a\"),\n" +
                "    POS_B(\"b\"),\n" +
                "    POS_C(\"c\");\n" +
                "\n" +
                "    private final String shortName;\n" +
                "\n" +
                "    PartOfSpeech(String shortName) {\n" +
                "        this.shortName = shortName;\n" +
                "    }\n" +
                "\n" +
                "    public String getShortName() {\n" +
                "        return this.shortName;\n" +
                "    }\n" +
                "\n" +
                "    private static final Map<String, PartOfSpeech> BY_SHORT_NAME = Arrays.stream(PartOfSpeech.values())\n" +
                "            .collect(ImmutableMap.toImmutableMap(PartOfSpeech::getShortName, Function.identity()));\n" +
                "\n" +
                "    public static PartOfSpeech fromShortName(String shortName) {\n" +
                "        return BY_SHORT_NAME.get(shortName);\n" +
                "    }\n" +
                "}\n"
        ));
    }
}