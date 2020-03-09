package zh.lingvo.rest.entities.forms;

import com.google.common.collect.ImmutableList;
import zh.lingvo.domain.changepatterns.ChangeModel;
import zh.lingvo.domain.forms.WordForms;
import zh.lingvo.rest.entities.JsonEntity;

import java.util.ArrayList;
import java.util.List;

public class WordFormsEntity implements JsonEntity {
    private List<ChangeModelEntity> changeModels;
    private List<WordFormsEntityMapping> wordFormsMappings;

    public WordFormsEntity() {
    }

    public WordFormsEntity(WordForms wordForms) {
        this.changeModels = new ArrayList<>();
        this.wordFormsMappings = new ArrayList<>();
        wordForms.getAllWordForms().forEach(formToModelMapping -> {
            ChangeModel changeModel = formToModelMapping.getChangeModel();
            changeModels.add(new ChangeModelEntity(changeModel));
            int index = this.changeModels.size() - 1;

            ImmutableList<WordFormEntityEntry> wordFormEntries = formToModelMapping.getWordForms().entrySet()
                    .stream()
                    .map(entry -> new WordFormEntityEntry(entry.getKey(), entry.getValue()))
                    .collect(ImmutableList.toImmutableList());
            WordFormsEntityMapping wordFormsEntityMapping = new WordFormsEntityMapping(wordFormEntries, index);

            this.wordFormsMappings.add(wordFormsEntityMapping);
        });
    }

    public List<ChangeModelEntity> getChangeModels() {
        return changeModels;
    }

    public void setChangeModels(List<ChangeModelEntity> changeModels) {
        this.changeModels = changeModels;
    }

    public List<WordFormsEntityMapping> getWordFormsMappings() {
        return wordFormsMappings;
    }

    public void setWordFormsMappings(List<WordFormsEntityMapping> wordFormsMappings) {
        this.wordFormsMappings = wordFormsMappings;
    }

}
