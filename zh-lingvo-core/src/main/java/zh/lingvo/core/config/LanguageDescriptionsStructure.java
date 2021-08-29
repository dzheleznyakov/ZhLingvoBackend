package zh.lingvo.core.config;

import zh.config.parser.SyntaxError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LanguageDescriptionsStructure {
    public final Map<String, String> partsOfSpeech = new LinkedHashMap<>();
    public final List<LanguageSpec> languageSpecs = new ArrayList<>();
    public List<SyntaxError> errors = new ArrayList<>();

    public static class LanguageSpec {
        public String name;
        public String nativeName;
        public String code;
        public Map<String, String> partsOfSpeech_shortNames = new HashMap<>();
        public Map<String, String> partsOfSpeech_nativeNames = new HashMap<>();
    }
}
