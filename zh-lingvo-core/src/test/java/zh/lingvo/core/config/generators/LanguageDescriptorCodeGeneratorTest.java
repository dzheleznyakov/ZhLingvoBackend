package zh.lingvo.core.config.generators;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.core.config.LanguageDescriptionsStructure;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DisplayName("Test language descriptor code generator")
class LanguageDescriptorCodeGeneratorTest {
    @Test
    @DisplayName("Test generating a language descriptor file content")
    void testGenerate() {
        LanguageDescriptionsStructure.LanguageSpec spec = new LanguageDescriptionsStructure.LanguageSpec();
        spec.name = "Foo";
        spec.nativeName = "Fooish";
        spec.code = "Fo";
        spec.partsOfSpeech_shortNames.putAll(ImmutableMap.of(
                "POS_A", "a",
                "POS_B", "b",
                "POS_C", "c"
        ));
        spec.partsOfSpeech_nativeNames.putAll(ImmutableMap.of(
                "POS_A", "AA",
                "POS_B", "BB",
                "POS_C", "CC"
        ));

        Map<String, String> flags = ImmutableMap.of(
                LanguageDescriptorCodeGenerator.CLASS_NAME, "FooLanguageDescriptor",
                LanguageDescriptorCodeGenerator.POS_PACKAGE, "zh.lingvo.core.domain",
                LanguageDescriptorCodeGenerator.POS_CLASS_NAME, "PartOfSpeech"
        );

        LanguageDescriptorCodeGenerator generator = new LanguageDescriptorCodeGenerator(spec, flags);

        String actual = generator.generate();

        assertThat(actual, is("" +
                "import com.google.common.collect.ImmutableList;\n" +
                "import com.google.common.collect.ImmutableMap;\n" +
                "import zh.lingvo.core.domain.PartOfSpeech;\n" +
                "\n" +
                "import java.util.List;\n" +
                "import java.util.Map;\n" +
                "\n" +
                "public class FooLanguageDescriptor implements zh.lingvo.core.LanguageDescriptor {\n" +
                "    private static final Map<PartOfSpeech, String[]> POS_DATA = ImmutableMap.<PartOfSpeech, String[]>builder()\n" +
                "            .put(PartOfSpeech.POS_A, new String[]{\"a\", \"AA\"})\n" +
                "            .put(PartOfSpeech.POS_B, new String[]{\"b\", \"BB\"})\n" +
                "            .put(PartOfSpeech.POS_C, new String[]{\"c\", \"CC\"})\n" +
                "            .build();\n" +
                "\n" +
                "    @Override\n" +
                "    public String getLanguageName() {\n" +
                "        return \"Foo\";\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String getLanguageNativeName() {\n" +
                "        return \"Fooish\";\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String getLanguageCode() {\n" +
                "        return \"Fo\";\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public List<PartOfSpeech> getPartsOfSpeech() {\n" +
                "        return ImmutableList.copyOf(POS_DATA.keySet());\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String getPartOfSpeechNativeShortName(PartOfSpeech pos) {\n" +
                "        return getPosData(pos, 0);\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String getPartOfSpeechNativeName(PartOfSpeech pos) {\n" +
                "        return getPosData(pos, 1);\n" +
                "    }\n" +
                "\n" +
                "    private String getPosData(PartOfSpeech pos, int index) {\n" +
                "        if (POS_DATA.containsKey(pos)) {\n" +
                "            return POS_DATA.get(pos)[index];\n" +
                "        }\n" +
                "        return \"\";\n" +
                "    }\n" +
                "}\n"));
    }
}