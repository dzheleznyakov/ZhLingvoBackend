package zh.lingvo.rest.entities.word;

import com.google.common.base.MoreObjects;
import zh.lingvo.rest.entities.JsonEntity;
import zh.lingvo.util.json.Jsonable;

import java.util.List;

public class MeaningRestEntity implements JsonEntity {
    @Jsonable
    private String remark;

    @Jsonable
    private List<TranslationRestEntity> translations;

    @Jsonable
    private List<ExampleRestEntity> examples;

    public MeaningRestEntity() {
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
