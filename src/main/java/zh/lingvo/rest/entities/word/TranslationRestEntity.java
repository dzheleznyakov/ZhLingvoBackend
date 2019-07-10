package zh.lingvo.rest.entities.word;

import com.google.common.base.MoreObjects;
import zh.lingvo.rest.entities.JsonEntity;
import zh.lingvo.util.json.Jsonable;

public class TranslationRestEntity implements JsonEntity {
    @Jsonable
    private String translation;

    @Jsonable
    private String elaboration;

    public TranslationRestEntity() {
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getElaboration() {
        return elaboration;
    }

    public void setElaboration(String elaboration) {
        this.elaboration = elaboration;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("translation", translation)
                .add("elaboration", elaboration)
                .omitNullValues()
                .toString();
    }
}
