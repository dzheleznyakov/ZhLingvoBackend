package zh.lingvo.config.generators;

import com.google.common.collect.ImmutableMap;
import zh.args.Args;
import zh.args.ArgsException;
import zh.lingvo.config.LanguageDescriptionsBuilder;
import zh.lingvo.config.LanguageDescriptionsStructure;
import zh.lingvo.config.parser.Builder;
import zh.lingvo.config.parser.ConfigParser;
import zh.lingvo.config.parser.SyntaxError;
import zh.lingvo.config.parser.lexer.Lexer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class LingvoDescriptionGenerator {
    private static final String ARGS_SCHEMA = "p*,o*";

    public static void main(String[] args) throws IOException {
        try {
            Args argsParser = new Args(ARGS_SCHEMA, args);
            new DescriptionCompiler(argsParser, args).run();
        } catch (ArgsException e) {
            System.out.printf("Usage: %s file%n", ARGS_SCHEMA);
            System.out.println(e.errorMessage());
            System.exit(0);
        }
    }

    private static class DescriptionCompiler {
        public static final String POS_FILENAME = "PartOfSpeech.java";
        public static final String LANG_DESCR_FILENAME_TEMPLATE = "%sLanguageDescriptor.java";

        public static final String DOMAIN_SUBPACKAGE = "domain";
        public static final String DESCR_SUBPACKAGE = "descriptors";

        private final String[] args;
        private final Args argParser;
        private String outputDirectory = null;
        private String outputBasePackage = null;
        private Builder<LanguageDescriptionsStructure> builder;
        private ConfigParser parser;
        private Lexer lexer;

        private DescriptionCompiler(Args argParser, String[] args) {
            this.args = args;
            this.argParser = argParser;
        }

        public void run() throws IOException {
            extractCommandLineArguments();

            LanguageDescriptionsStructure structure = compile(getSourceCode());
            int syntaxErrorCount = reportSyntaxErrors(structure);
            if (syntaxErrorCount == 0)
                generateCode(structure);
        }

        private void extractCommandLineArguments() {
            if (argParser.has('o'))
                outputDirectory = argParser.getString('o');
            if (argParser.has('p'))
                outputBasePackage = argParser.getString('p');
        }

        private String getSourceCode() throws IOException {
            String sourceFileName = args[argParser.nextArgument()];
            return new String(Files.readAllBytes(Paths.get(sourceFileName)));
        }

        private LanguageDescriptionsStructure compile(String sourceCode) {
            builder = new LanguageDescriptionsBuilder();
            parser = new ConfigParser();
            lexer = new Lexer(parser);
            lexer.lex(sourceCode);
            return parser.build(builder);
        }

        private int reportSyntaxErrors(LanguageDescriptionsStructure structure) {
            int syntaxErrorCount = structure.errors.size();
            System.out.printf(
                    "Complied with %d syntax error%s.%n",
                    syntaxErrorCount,
                    (syntaxErrorCount == 1 ? "" : "s"));

            for (SyntaxError error : structure.errors)
                System.out.println(error);

            return syntaxErrorCount;
        }

        private void generateCode(LanguageDescriptionsStructure structure) throws IOException {
            generatePosCode(structure);
            generateLanguageDescriptors(structure);
        }

        private void generatePosCode(LanguageDescriptionsStructure structure) throws IOException {
            String fullPackage = outputBasePackage + "." + DOMAIN_SUBPACKAGE;
            File outputFile = getOutputFile(POS_FILENAME, fullPackage);
            Map<String, String> flags = ImmutableMap.of(PosCodeGenerator.CLASS_NAME, getClassName(outputFile));
            String posFileContent = new PosCodeGenerator(structure, flags).generate();
            writeContent(posFileContent, fullPackage, outputFile);
        }

        private void generateLanguageDescriptors(LanguageDescriptionsStructure structure) throws IOException {
            for (LanguageDescriptionsStructure.LanguageSpec languageSpec : structure.languageSpecs)
                generateLanguageDescriptor(languageSpec);
        }

        private void generateLanguageDescriptor(LanguageDescriptionsStructure.LanguageSpec languageSpec) throws IOException {
            String fullPackage = outputBasePackage + "." + DESCR_SUBPACKAGE;
            String fileName = String.format(LANG_DESCR_FILENAME_TEMPLATE, languageSpec.name);
            File outputFile = getOutputFile(fileName, fullPackage);
            Map<String, String> flags = ImmutableMap.of(
                    LanguageDescriptorCodeGenerator.CLASS_NAME, getClassName(outputFile),
                    LanguageDescriptorCodeGenerator.POS_PACKAGE, outputBasePackage + "." + DOMAIN_SUBPACKAGE,
                    LanguageDescriptorCodeGenerator.POS_CLASS_NAME, getClassName(getOutputFile(POS_FILENAME, outputBasePackage))
            );
            String descrFileContent = new LanguageDescriptorCodeGenerator(languageSpec, flags).generate();
            writeContent(descrFileContent, fullPackage, outputFile);
        }

        private File getOutputFile(String fileName, String fullPackage) {
            Path baseOutputDirectory = new File("").getAbsoluteFile().toPath().resolve(outputDirectory);
            Path absoluteOutputDirectory = baseOutputDirectory.resolve(fullPackage.replace(".", File.separator));
            return absoluteOutputDirectory.resolve(fileName).toFile();
        }

        private String generateFullContent(String fileContent, String fullPackage) {
            return "/*\n" + GeneratorText.DISCLAIMER + "*/\n\n" +
                    "package " + fullPackage + ";\n\n" +
                    fileContent;
        }

        private String getClassName(File outputFile) {
            String fileName = outputFile.getName();
            int endIndex = fileName.lastIndexOf(".");
            return fileName.substring(0, endIndex);
        }

        private void writeContent(String descrFileContent, String fullPackage, File outputFile) throws IOException {
            String fullContent = generateFullContent(descrFileContent, fullPackage);
            ensureParentsExist(outputFile);
            Files.write(outputFile.toPath(), fullContent.getBytes(StandardCharsets.UTF_8));
        }

        private void ensureParentsExist(File file) throws IOException {
            File parentFile = file.getParentFile();
            if (!parentFile.exists())
                parentFile.mkdirs();
        }
    }
}
