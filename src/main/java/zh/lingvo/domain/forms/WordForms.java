package zh.lingvo.domain.forms;

import zh.lingvo.domain.linguisticcategories.LinguisticCategory;
import zh.lingvo.domain.changepatterns.ChangeModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WordForms {
    private List<WordFormsToChangeModelMapping> allWordForms = new ArrayList<>();

    public List<WordFormsToChangeModelMapping> getAllWordForms() {
        return allWordForms;
    }

    public void put(Map<LinguisticCategory[], String> wordForms, ChangeModel changeModel) {
        put(wordForms, changeModel, "");
    }

    public void put(Map<LinguisticCategory[], String> wordForms, ChangeModel changeModel, String description) {
        WordFormsToChangeModelMapping mapping = new WordFormsToChangeModelMapping(wordForms, changeModel);
        mapping.setDescription(description);
        allWordForms.add(mapping);
    }

    public static class WordFormsToChangeModelMapping {
        private String description;
        private Map<LinguisticCategory[], String> wordForms;
        private ChangeModel changeModel;

        public WordFormsToChangeModelMapping(Map<LinguisticCategory[], String> wordForms, ChangeModel changeModel) {
            this.wordForms = wordForms;
            this.changeModel = changeModel;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Map<LinguisticCategory[], String> getWordForms() {
            return wordForms;
        }

        public void setWordForms(Map<LinguisticCategory[], String> wordForms) {
            this.wordForms = wordForms;
        }

        public ChangeModel getChangeModel() {
            return changeModel;
        }

        public void setChangeModel(ChangeModel changeModel) {
            this.changeModel = changeModel;
        }
    }
}
