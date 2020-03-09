package zh.lingvo.rest.entities.forms;

import zh.lingvo.rest.entities.JsonEntity;

import java.util.List;

public class WordFormsEntityMapping implements JsonEntity {
    private List<WordFormEntityEntry> wordForms;
    private int changeModelIndex;

    public WordFormsEntityMapping() {
    }

    public WordFormsEntityMapping(List<WordFormEntityEntry> wordForms, int changeModelIndex) {
        this.wordForms = wordForms;
        this.changeModelIndex = changeModelIndex;
    }

    public List<WordFormEntityEntry> getWordForms() {
        return wordForms;
    }

    public void setWordForms(List<WordFormEntityEntry> wordForms) {
        this.wordForms = wordForms;
    }

    public int getChangeModelIndex() {
        return changeModelIndex;
    }

    public void setChangeModelIndex(int changeModelIndex) {
        this.changeModelIndex = changeModelIndex;
    }
}
