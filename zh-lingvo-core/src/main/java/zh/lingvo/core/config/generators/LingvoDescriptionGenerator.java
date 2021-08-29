package zh.lingvo.core.config.generators;

import zh.args.Args;
import zh.args.ArgsException;
import zh.config.parser.Builder;
import zh.config.parser.ConfigParser;
import zh.config.parser.SyntaxError;
import zh.config.parser.lexer.Lexer;
import zh.lingvo.core.config.LanguageDescriptionsBuilder;
import zh.lingvo.core.config.LanguageDescriptionsStructure;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

        private final String[] args;
        private final Args argParser;
        private String outputDirectory = null;
        private String outputPackage = null;
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
                outputPackage = argParser.getString('p');
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
        }

        private void generatePosCode(LanguageDescriptionsStructure structure) throws IOException {
            Path baseOutputDirectory = new File("").getAbsoluteFile().toPath().resolve(outputDirectory);
            Path absoluteOutputDirectory = baseOutputDirectory.resolve(outputPackage.replace(".", File.separator));
            File outputFile = absoluteOutputDirectory.resolve(POS_FILENAME).toFile();
            String className = getClassName(outputFile);
            String posFileContent = new PosCodeGenerator(className, structure.partsOfSpeech).generate();
            String fullContent = generateFullContent(posFileContent);
            Files.write(outputFile.toPath(), fullContent.getBytes(StandardCharsets.UTF_8));
        }

        private String generateFullContent(String fileContent) {
            return "/*\n" + GeneratorText.DISCLAIMER + "*/\n\n" +
                    "package " + outputPackage + ";\n\n" +
                    fileContent;
        }

        private String getClassName(File outputFile) {
            String fileName = outputFile.getName();
            int endIndex = fileName.lastIndexOf(".");
            return fileName.substring(0, endIndex);
        }
    }
}
