package zh.lingvo.core.config;

import com.google.common.collect.ImmutableMap;
import zh.config.parser.Builder;
import zh.config.parser.SyntaxError;
import zh.config.parser.syntax.ConfigValue;
import zh.lingvo.core.config.LanguageDescriptionsStructure.LanguageSpec;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static zh.config.parser.SyntaxError.Type.STRUCTURAL_ERROR;

public class LanguageDescriptionsBuilder implements Builder<LanguageDescriptionsStructure> {
    private static final String PARTS_OF_SPEECH_KEY = "parts_of_speech";
    private static final String LANGUAGES_DESCRIPTIONS_CONFIG_KEY = "language_descriptions";

    private LanguageDescriptionsStructure structure;
    private ConfigValue value;

    private final LanguageSpecBuilder languageSpecBuilder = new LanguageSpecBuilder();

    @Override
    public LanguageDescriptionsStructure build(ConfigValue value, List<SyntaxError> errors) {
        structure = new LanguageDescriptionsStructure();
        structure.errors = errors;
        this.value = value;

        build();

        return structure;
    }

    private void build() {
        buildPartsOfSpeech();
        buildLanguageDescriptions();
    }

    private void buildPartsOfSpeech() {
        ConfigValue posList = value.getMap().get(PARTS_OF_SPEECH_KEY);
        final Set<String> processedPoses = new HashSet<>();
        posList.getList().stream()
                .filter(this::verifyPartOfSpeechList)
                .map(this::toListOfStrings)
                .forEach(pair -> buildPartOfSpeechEntry(processedPoses, pair));
    }

    private boolean verifyPartOfSpeechList(ConfigValue configValue) {
        List<ConfigValue> list = configValue.getList();
        if (list.size() >= 2)
            return true;

        SyntaxError error = list.isEmpty()
                ? new SyntaxError(STRUCTURAL_ERROR, "PoS not defined")
                : new SyntaxError(STRUCTURAL_ERROR, String.format("PoS [%s] default short name not defined.", list.get(0).getString()));
        structure.errors.add(error);
        return false;
    }

    private List<String> toListOfStrings(ConfigValue configValue) {
        return configValue.getList()
                .stream()
                .map(ConfigValue::getString)
                .collect(Collectors.toList());
    }

    private void buildPartOfSpeechEntry(Set<String> processedPoses, List<String> pair) {
        String pos = pair.get(0);
        if (processedPoses.contains(pos))
            structure.errors.add(new SyntaxError(
                    STRUCTURAL_ERROR,
                    String.format("Part of speech [%s] appears more than once.", pos)));
        else {
            processedPoses.add(pos);
            String defaultShortName = pair.get(1);
            structure.partsOfSpeech.put(pos, defaultShortName);
        }
    }

    private void buildLanguageDescriptions() {
        ConfigValue langDescriptions = value.getMap()
                .getOrDefault(LANGUAGES_DESCRIPTIONS_CONFIG_KEY, ConfigValue.NULL);
        langDescriptions.getMap().forEach(languageSpecBuilder::build);
    }

    private class LanguageSpecBuilder {
        private static final String NATIVE_NAME_CONFIG_KEY = "name";
        private static final String CODE_CONFIG_KEY = "code";
        private static final int SHORT_NAMES_INDEX = 1;
        private static final int NATIVE_NAMES_INDEX = 2;

        void build(String name, ConfigValue specConfig) {
            Map<String, ConfigValue> specMap = specConfig.getMap();
            LanguageSpec spec = new LanguageSpec();
            buildLanguageSpecMetadata(name, specMap, spec);
            buildLanguagePosSpec(name, specMap, spec);
            structure.languageSpecs.add(spec);
        }

        private void buildLanguageSpecMetadata(String name, Map<String, ConfigValue> specMap, LanguageSpec spec) {
            spec.name = name;
            buildLanguageSpecNativeName(specMap, spec, name);
            buildLanguageSpecCode(specMap, spec, name);
        }

        private void buildLanguageSpecNativeName(Map<String, ConfigValue> specMap, LanguageSpec spec, String languageName) {
            ConfigValue configValue = specMap.get(NATIVE_NAME_CONFIG_KEY);
            if (configValue == null || configValue.getString().equals(""))
                structure.errors.add(new SyntaxError(STRUCTURAL_ERROR,
                        String.format("Language [%s] native name is not defined.", languageName)));
            else
                spec.nativeName = configValue.getString();
        }

        private void buildLanguageSpecCode(Map<String, ConfigValue> specMap, LanguageSpec spec, String languageName) {
            ConfigValue configValue = specMap.get(CODE_CONFIG_KEY);
            if (configValue == null || configValue.getString().equals(""))
                structure.errors.add(new SyntaxError(STRUCTURAL_ERROR,
                        String.format("Language [%s] code is not defined.", languageName)));
            else
                spec.code = configValue.getString();
        }

        private void buildLanguagePosSpec(String name, Map<String, ConfigValue> specMap, LanguageSpec spec) {
            ConfigValue langPosSpec = specMap.get(PARTS_OF_SPEECH_KEY);
            evaluatePos(langPosSpec, name);
            spec.partsOfSpeech_nativeNames = buildPosNativeNames(langPosSpec);
            spec.partsOfSpeech_shortNames = buildPosShortNames(langPosSpec);
        }

        private Map<String, String> buildPosNativeNames(ConfigValue config) {
            return buildPos(config, NATIVE_NAMES_INDEX);
        }

        private Map<String, String> buildPosShortNames(ConfigValue config) {
            return buildPos(config, SHORT_NAMES_INDEX);
        }

        private ImmutableMap<String, String> buildPos(ConfigValue config, int valIndex) {
            return config.getList()
                    .stream()
                    .filter(configValue -> configValue.getList().size() >= 3)
                    .collect(ImmutableMap.toImmutableMap(
                            cv -> cv.getList().get(0).getString(),
                            cv -> cv.getList().get(valIndex).getString()));
        }

        private void evaluatePos(ConfigValue config, String languageName) {
            config.getList()
                    .stream()
                    .map(ConfigValue::getList)
                    .filter(list -> list.size() < 3)
                    .forEach(list -> processLangSpecPosDefinitionError(languageName, list));
        }

        private void processLangSpecPosDefinitionError(String languageName, List<ConfigValue> list) {
            SyntaxError error;
            if (list.size() == 2)
                error = new SyntaxError(STRUCTURAL_ERROR,
                        String.format("PoS [%s] in language [%s] native name is not defined.", list.get(0).getString(), languageName));
            else if (list.size() == 1)
                error = new SyntaxError(STRUCTURAL_ERROR,
                        String.format("PoS [%s] in language [%s] short name is not defined.", list.get(0).getString(), languageName));
            else
                error = new SyntaxError(STRUCTURAL_ERROR,
                        String.format("PoS in language [%s] is not defined.", languageName));
            structure.errors.add(error);
        }
    }
}
