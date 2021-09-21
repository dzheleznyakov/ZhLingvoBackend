package zh.lingvo.core.config.generators;

import zh.lingvo.core.LanguageDescriptor;
import zh.lingvo.core.config.LanguageDescriptionsStructure;

import java.util.Map;

public class LanguageDescriptorCodeGenerator implements Generator {
    public static final String CLASS_NAME = "CLASS_NAME";
    public static final String POS_PACKAGE = "POS_PACKAGE";
    public static final String POS_CLASS_NAME = "POS_CLASS_NAME";

    private static final String INDENT = "    ";
    private final LanguageDescriptionsStructure.LanguageSpec languageSpec;
    private final Map<String, String> flags;
    private final StringBuilder contentBuilder = new StringBuilder();

    public LanguageDescriptorCodeGenerator(LanguageDescriptionsStructure.LanguageSpec languageSpec, Map<String, String> flags) {
        this.languageSpec = languageSpec;
        this.flags = flags;
    }

    @Override
    public String generate() {
        generateImports();
        generateBody();
        return contentBuilder.toString();
    }

    private void generateImports() {
        contentBuilder.append("import com.google.common.collect.ImmutableList;\n")
                .append("import com.google.common.collect.ImmutableMap;\n")
                .append("import ").append(flags.get(POS_PACKAGE)).append('.').append(flags.get(POS_CLASS_NAME)).append(";\n\n")
                .append("import java.util.List;\n")
                .append("import java.util.Map;\n\n");
    }

    private void generateBody() {
        contentBuilder.append("public class ").append(flags.get(CLASS_NAME))
                .append(" implements ").append(LanguageDescriptor.class.getCanonicalName()).append(" {\n");
        generatePosData();
        overrideConstantValueGetter("getLanguageName", "String", '"' + languageSpec.name + '"');
        overrideConstantValueGetter("getLanguageNativeName", "String", '"' + languageSpec.nativeName + '"');
        overrideConstantValueGetter("getLanguageCode", "String", '"' + languageSpec.code + '"');
        overrideConstantValueGetter("getPartsOfSpeech", "List<PartOfSpeech>", "ImmutableList.copyOf(POS_DATA.keySet())");
        overridePosDataGetter("getPartOfSpeechNativeShortName", 0);
        overridePosDataGetter("getPartOfSpeechNativeName", 1);
        generatePosDataGetterBase();
        contentBuilder.append("}\n");
    }

    private void generatePosData() {
        contentBuilder.append(INDENT)
                .append("private static final Map<PartOfSpeech, String[]> POS_DATA = ImmutableMap.<PartOfSpeech, String[]>builder()\n");
        languageSpec.partsOfSpeech_nativeNames.keySet().forEach(pos -> contentBuilder.append(INDENT.repeat(3))
                .append(".put(").append(flags.get(POS_CLASS_NAME)).append('.').append(pos)
                .append(", new String[]{\"")
                .append(languageSpec.partsOfSpeech_shortNames.get(pos))
                .append("\", \"")
                .append(languageSpec.partsOfSpeech_nativeNames.get(pos))
                .append("\"})\n"));
        contentBuilder.append(INDENT.repeat(3)).append(".build();\n");
    }

    private void overrideConstantValueGetter(String name, String type, String value) {
        generateOverride()
                .append(INDENT).append("public ").append(type).append(' ').append(name).append("() {\n")
                .append(INDENT.repeat(2)).append("return ").append(value).append(";\n")
                .append(INDENT).append("}\n");
    }

    private void overridePosDataGetter(String name, int index) {
        generateOverride()
                .append(INDENT).append("public String ").append(name).append("(").append(flags.get(POS_CLASS_NAME)).append(" pos) {\n")
                .append(INDENT.repeat(2)).append("return getPosData(pos, ").append(index).append(");\n")
                .append(INDENT).append("}\n");
    }

    private void generatePosDataGetterBase() {
        contentBuilder.append('\n')
                .append(INDENT).append("private String getPosData(").append(flags.get(POS_CLASS_NAME)).append(" pos, int index) {\n")
                .append(INDENT.repeat(2)).append("if (POS_DATA.containsKey(pos)) {\n")
                .append(INDENT.repeat(3)).append("return POS_DATA.get(pos)[index];\n")
                .append(INDENT.repeat(2)).append("}\n")
                .append(INDENT.repeat(2)).append("return \"\";\n")
                .append(INDENT).append("}\n");
    }

    private StringBuilder generateOverride() {
        return contentBuilder.append('\n').append(INDENT).append("@Override\n");
    }
}
