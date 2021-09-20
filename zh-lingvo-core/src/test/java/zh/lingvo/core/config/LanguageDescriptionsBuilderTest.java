package zh.lingvo.core.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import zh.config.parser.Builder;
import zh.config.parser.ConfigParser;
import zh.config.parser.SyntaxError;
import zh.config.parser.lexer.Lexer;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static zh.config.parser.SyntaxError.Type.STRUCTURAL_ERROR;

@DisplayName("Test LanguageDescriptions Builder")
class LanguageDescriptionsBuilderTest {
    private static final String PARTS_OF_SPEECH_CONFIG_KEY = "parts_of_speech";
    private static final String LANGUAGES_DESCRIPTIONS_CONFIG_KEY = "language_descriptions";

    private final ConfigParser parser = new ConfigParser();
    private final Lexer lexer = new Lexer(parser);
    private final Builder<LanguageDescriptionsStructure> builder = new LanguageDescriptionsBuilder();

    private void assertError(Collection<SyntaxError> errors, SyntaxError expectedError) {
        List<String> errorsStr = errors.stream()
                .map(Objects::toString)
                .collect(Collectors.toList());
        assertThat(errorsStr, contains(expectedError.toString()));
    }

    @Nested
    class PartsOfSpeech {
        @Test
        @DisplayName("Should parse parts of speech from the config")
        void parsePartsOfSpeech()  {
            lexer.lex(PARTS_OF_SPEECH_CONFIG_KEY + " [[AA aa] [BB bb] [CC cc]]");

            LanguageDescriptionsStructure structure = parser.build(builder);

            Map<String, String> expectedPartsOfSpeech = ImmutableMap.of(
                    "AA", "aa",
                    "BB", "bb",
                    "CC", "cc");
            assertThat(structure.partsOfSpeech, is(equalTo(expectedPartsOfSpeech)));
        }

        @Test
        @DisplayName("Should preserve the order in which parts of speech appear in the config")
        void preserveOrder() {
            lexer.lex(PARTS_OF_SPEECH_CONFIG_KEY + " [\n" +
                    "\t[ZZ zz] [AA aa] [PP pp]\n" +
                    "\t[BB bb] [KK kk] [CC cc]\n" +
                    "\t[DD dd] [EE ee] [FF ff]\n" +
                    "]");

            LanguageDescriptionsStructure structure = parser.build(builder);
            Set<String> actualPosValues = structure.partsOfSpeech.keySet();

            List<String> expectedPosValuesInOrder = ImmutableList.of(
                    "ZZ", "AA", "PP", "BB", "KK", "CC", "DD", "EE", "FF");

            assertThat(actualPosValues, hasSize(expectedPosValuesInOrder.size()));

            Iterator<String> posIterator = actualPosValues.iterator();
            for (String expectedPos : expectedPosValuesInOrder)
                assertThat(posIterator.next(), is(equalTo(expectedPos)));
        }

        @Test
        @DisplayName("Should detect an error if a part of speech appears more than once")
        void posAppearsTwice() {
            lexer.lex(PARTS_OF_SPEECH_CONFIG_KEY + " [[AA aa] [GG gg] [AA aa]]");

            LanguageDescriptionsStructure structure = parser.build(builder);

            assertError(structure.errors, new SyntaxError(STRUCTURAL_ERROR,
                    "Part of speech [AA] appears more than once."));
        }

        @Test
        @DisplayName("Should detect an error if a pos entry does not contain a default name")
        void posEntry_NoDefaultNameTooShort() {
            lexer.lex(PARTS_OF_SPEECH_CONFIG_KEY + " [[AA aa] [SS] [ZZ zz]]");

            LanguageDescriptionsStructure structure = parser.build(builder);

            assertError(structure.errors, new SyntaxError(
                    STRUCTURAL_ERROR,
                    "PoS [SS] default short name not defined."));
        }

        @Test
        @DisplayName("Should detect an error if a pos entry does not contain pos")
        void posEntry_PosNotDefined() {
            lexer.lex(PARTS_OF_SPEECH_CONFIG_KEY + " [[AA aa] [] [ZZ zz]]");

            LanguageDescriptionsStructure structure = parser.build(builder);

            assertError(structure.errors, new SyntaxError(
                    STRUCTURAL_ERROR,
                    "PoS not defined"));
        }
    }

    @Nested
    class LanguageDescriptions {
        private static final String INPUT_POS = PARTS_OF_SPEECH_CONFIG_KEY + "[[AA aa] [BB bb] [CC cc]]\n";

        private LanguageDescriptionsStructure buildLanguageDescriptionStructure(String input) {
            lexer.lex(INPUT_POS + input);
            return parser.build(builder);
        }

        @Test
        @DisplayName("Should have empty language descriptions list if none is present in the config")
        void noLangDescr() {
            LanguageDescriptionsStructure structure = buildLanguageDescriptionStructure("");

            assertThat(structure.languageSpecs, is(empty()));
        }

        @Test
        @DisplayName("Should parse all language descriptions")
        void testLangDescr() {
            LanguageDescriptionsStructure structure = buildLanguageDescriptionStructure(LANGUAGES_DESCRIPTIONS_CONFIG_KEY + " " +
                    "{\n" +
                    "  Foo {\n" +
                    "    name Fooish\n" +
                    "    code Fo\n" +
                    "    " + PARTS_OF_SPEECH_CONFIG_KEY + " [\n" +
                    "      [AA a aAa]\n" +
                    "      [BB b bBb]\n" +
                    "      [CC c cCc]\n" +
                    "    ]\n" +
                    "  }\n" +
                    "\n" +
                    "  Bar {\n" +
                    "    name Barbar\n" +
                    "    code Br\n" +
                    "    " + PARTS_OF_SPEECH_CONFIG_KEY + "[\n" +
                    "      [AA A \"a A\"]\n" +
                    "      [BB B \"b B\"]\n" +
                    "      [CC C \"c C\"]\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}");

            List<LanguageDescriptionsStructure.LanguageSpec> specs = structure.languageSpecs;

            assertThat(specs, hasSize(2));

            LanguageDescriptionsStructure.LanguageSpec fooSpec = specs.get(0);
            assertThat(fooSpec.name, is("Foo"));
            assertThat(fooSpec.nativeName, is("Fooish"));
            assertThat(fooSpec.code, is("Fo"));
            assertThat(fooSpec.partsOfSpeech_nativeNames, is(equalTo(ImmutableMap.of(
                    "AA", "aAa", "BB", "bBb", "CC", "cCc"
            ))));
            assertThat(fooSpec.partsOfSpeech_shortNames, is(equalTo(ImmutableMap.of(
                    "AA", "a", "BB", "b", "CC", "c"
            ))));

            LanguageDescriptionsStructure.LanguageSpec barSpec = specs.get(1);
            assertThat(barSpec.name, is("Bar"));
            assertThat(barSpec.nativeName, is("Barbar"));
            assertThat(barSpec.code, is("Br"));
            assertThat(barSpec.partsOfSpeech_nativeNames, is(equalTo(ImmutableMap.of(
                    "AA", "a A", "BB", "b B", "CC", "c C"
            ))));
            assertThat(barSpec.partsOfSpeech_shortNames, is(equalTo(ImmutableMap.of(
                    "AA", "A", "BB", "B", "CC", "C"
            ))));
        }

        @Test
        @DisplayName("Should detect an error if a PoS in a language spec does not have a short name")
        void languageSpec_PosDoesNotHaveShortName() {
            LanguageDescriptionsStructure structure = buildLanguageDescriptionStructure(LANGUAGES_DESCRIPTIONS_CONFIG_KEY + " " +
                    "{\n" +
                    "  Foo {\n" +
                    "    name Fooish\n" +
                    "    code Fo\n" +
                    "    " + PARTS_OF_SPEECH_CONFIG_KEY + " [\n" +
                    "      [AA a aAa]\n" +
                    "      [BB]\n" +
                    "      [CC c cCc]\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}");

            assertError(structure.errors, new SyntaxError(STRUCTURAL_ERROR,
                    "PoS [BB] in language [Foo] short name is not defined."));
        }

        @Test
        @DisplayName("Should detect an error if a PoS in a language spec does not have a native name")
        void languageSpec_PosDoesNotHaveNativeName() {
            LanguageDescriptionsStructure structure = buildLanguageDescriptionStructure(LANGUAGES_DESCRIPTIONS_CONFIG_KEY + " " +
                    "{\n" +
                    "  Foo {\n" +
                    "    name Fooish\n" +
                    "    code Fo\n" +
                    "    " + PARTS_OF_SPEECH_CONFIG_KEY + " [\n" +
                    "      [AA a aAa]\n" +
                    "      [BB b]\n" +
                    "      [CC c cCc]\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}");

            assertError(structure.errors, new SyntaxError(STRUCTURAL_ERROR,
                    "PoS [BB] in language [Foo] native name is not defined."));
        }

        @Test
        @DisplayName("Should detect an error if a PoS in a language spec is not defined")
        void languageSpec_PosIsNotDefined() {
            LanguageDescriptionsStructure structure = buildLanguageDescriptionStructure(LANGUAGES_DESCRIPTIONS_CONFIG_KEY + " " +
                    "{\n" +
                    "  Foo {\n" +
                    "    name Fooish\n" +
                    "    code Fo\n" +
                    "    " + PARTS_OF_SPEECH_CONFIG_KEY + " [\n" +
                    "      [AA a aAa]\n" +
                    "      []\n" +
                    "      [CC c cCc]\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}");

            assertError(structure.errors, new SyntaxError(STRUCTURAL_ERROR,
                    "PoS in language [Foo] is not defined."));
        }

        @Test
        @DisplayName("Should detect an error if a language spec is missing the native language name")
        void languageSpec_NoNativeName() {
            LanguageDescriptionsStructure structure = buildLanguageDescriptionStructure(LANGUAGES_DESCRIPTIONS_CONFIG_KEY + " " +
                    "{\n" +
                    "  Foo {\n" +
                    "    code Fo\n" +
                    "    " + PARTS_OF_SPEECH_CONFIG_KEY + " [\n" +
                    "      [AA a aAa]\n" +
                    "      [BB b bBb]\n" +
                    "      [CC c cCc]\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}");

            assertError(structure.errors, new SyntaxError(STRUCTURAL_ERROR,
                    "Language [Foo] native name is not defined."));
        }

        @Test
        @DisplayName("Should detect an error if a language spec native name is an empty string")
        void languageSpec_EmptyNativeName() {
            LanguageDescriptionsStructure structure = buildLanguageDescriptionStructure(LANGUAGES_DESCRIPTIONS_CONFIG_KEY + " " +
                    "{\n" +
                    "  Foo {\n" +
                    "    name \"\"\n" +
                    "    code Fo\n" +
                    "    " + PARTS_OF_SPEECH_CONFIG_KEY + " [\n" +
                    "      [AA a aAa]\n" +
                    "      [BB b bBb]\n" +
                    "      [CC c cCc]\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}");

            assertError(structure.errors, new SyntaxError(STRUCTURAL_ERROR,
                    "Language [Foo] native name is not defined."));
        }

        @Test
        @DisplayName("Should detect an error if a language spec is missing the language code")
        void languageSpec_NoCode() {
            LanguageDescriptionsStructure structure = buildLanguageDescriptionStructure(LANGUAGES_DESCRIPTIONS_CONFIG_KEY + " " +
                    "{\n" +
                    "  Foo {\n" +
                    "    name Fooish\n" +
                    "    " + PARTS_OF_SPEECH_CONFIG_KEY + " [\n" +
                    "      [AA a aAa]\n" +
                    "      [BB b bBb]\n" +
                    "      [CC c cCc]\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}");

            assertError(structure.errors, new SyntaxError(STRUCTURAL_ERROR,
                    "Language [Foo] code is not defined."));
        }

        @Test
        @DisplayName("Should detect an error if a language spec code is an empty string")
        void languageSpec_EmptyCode() {
            LanguageDescriptionsStructure structure = buildLanguageDescriptionStructure(LANGUAGES_DESCRIPTIONS_CONFIG_KEY + " " +
                    "{\n" +
                    "  Foo {\n" +
                    "    name Fooish\n" +
                    "    code \"\"\n" +
                    "    " + PARTS_OF_SPEECH_CONFIG_KEY + " [\n" +
                    "      [AA a aAa]\n" +
                    "      [BB b bBb]\n" +
                    "      [CC c cCc]\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}");

            assertError(structure.errors, new SyntaxError(STRUCTURAL_ERROR,
                    "Language [Foo] code is not defined."));
        }

        @Test
        @DisplayName("Should detect an error if the PoS in a language spec is not defined in PoS spec")
        void languageSpec_PosNotDefined() {
            LanguageDescriptionsStructure structure = buildLanguageDescriptionStructure(LANGUAGES_DESCRIPTIONS_CONFIG_KEY + " " +
                    "{\n" +
                    "  Foo {\n" +
                    "    name Fooish\n" +
                    "    code \"Fo\"\n" +
                    "    " + PARTS_OF_SPEECH_CONFIG_KEY + " [\n" +
                    "      [AA a aAa]\n" +
                    "      [BB b bBb]\n" +
                    "      [DD d dDd]\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}");

            assertError(structure.errors, new SyntaxError(STRUCTURAL_ERROR,
                    "Part of speech [DD] in language [Foo] is not defined."));
        }
    }
}