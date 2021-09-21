package zh.lingvo.core.config.generators;

import zh.lingvo.core.config.LanguageDescriptionsStructure;

import java.util.Map;
import java.util.stream.Collectors;

public class PosCodeGenerator implements Generator {
    public static final String CLASS_NAME = "CLASS_NAME";

    private static final String INDENT = "    ";
    private final Map<String, String> partsOfSpeech;
    private final String className;
    private final StringBuilder contentBuilder = new StringBuilder();

    public PosCodeGenerator(LanguageDescriptionsStructure structure, Map<String, String> flags) {
        this.partsOfSpeech = structure.partsOfSpeech;
        this.className = flags.get(CLASS_NAME);
    }

    @Override
    public String generate() {
        generateImports();
        generateBody();
        return contentBuilder.toString();
    }

    private void generateImports() {
        contentBuilder.append("import com.google.common.collect.ImmutableMap;\n")
                .append("import java.util.Arrays;\n")
                .append("import java.util.Map;\n")
                .append("import java.util.function.Function;\n\n");
    }

    private void generateBody() {
        contentBuilder.append("public enum ")
                .append(className)
                .append(" {\n");
        generateValues();
        generateFields();
        generateConstructor();
        generateGetters();
        generateIndices();
        generateIndexLookups();
        contentBuilder.append("}\n");
    }

    private void generateValues() {
        String values = partsOfSpeech.entrySet().stream()
                .map(this::getValueDefinition)
                .collect(Collectors.joining(",\n", "", ";\n\n"));
        contentBuilder.append(values);
    }

    private String getValueDefinition(Map.Entry<String, String> e) {
        String pos = e.getKey();
        String shortName = e.getValue();
        return String.format("%s%s(\"%s\")", INDENT, pos, shortName);
    }

    private void generateFields() {
        contentBuilder.append(INDENT)
                .append("private final String shortName;\n\n");
    }

    private void generateConstructor() {
        contentBuilder
                .append(INDENT).append(className).append("(String shortName) {\n")
                .append(INDENT.repeat(2)).append("this.shortName = shortName;\n")
                .append(INDENT).append("}\n\n");
    }

    private void generateGetters() {
        contentBuilder
                .append(INDENT).append("public String getShortName() {\n")
                .append(INDENT.repeat(2)).append("return this.shortName;\n")
                .append(INDENT).append("}\n\n");
    }

    private void generateIndices() {
        contentBuilder
                .append(INDENT)
                .append("private static final Map<String, ")
                .append(className)
                .append("> BY_SHORT_NAME = Arrays.stream(")
                .append(className)
                .append(".values())\n")
                .append(INDENT.repeat(3))
                .append(".collect(ImmutableMap.toImmutableMap(")
                .append(className)
                .append("::getShortName, Function.identity()));\n\n");
    }

    private void generateIndexLookups() {
        contentBuilder
                .append(INDENT).append("public static ").append(className).append(" fromShortName(String shortName) {\n")
                .append(INDENT.repeat(2)).append("return BY_SHORT_NAME.get(shortName);\n")
                .append(INDENT).append("}\n");
    }
}
