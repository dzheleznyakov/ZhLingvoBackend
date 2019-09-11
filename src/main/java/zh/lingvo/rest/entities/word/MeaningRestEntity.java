package zh.lingvo.rest.entities.word;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.MoreObjects;
import zh.lingvo.domain.words.Meaning;
import zh.lingvo.rest.entities.JsonEntity;
import zh.lingvo.util.CollectionUtils;

import java.util.List;

public class MeaningRestEntity implements JsonEntity {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String remark;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TranslationRestEntity> translations;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ExampleRestEntity> examples;

    public MeaningRestEntity() {
    }

    public MeaningRestEntity(Meaning meaning) {
        remark = meaning.getRemark();
        translations = CollectionUtils.transform(meaning::getTranslations, TranslationRestEntity::new);
        examples = CollectionUtils.transform(meaning::getExamples, ExampleRestEntity::new);
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<TranslationRestEntity> getTranslations() {
        return translations;
    }

    public void setTranslations(List<TranslationRestEntity> translations) {
        this.translations = translations;
    }

    public List<ExampleRestEntity> getExamples() {
        return examples;
    }

    public void setExamples(List<ExampleRestEntity> examples) {
        this.examples = examples;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("remark", remark)
                .add("translations", translations)
                .add("examples", examples)
                .omitNullValues()
                .toString();
    }
}
