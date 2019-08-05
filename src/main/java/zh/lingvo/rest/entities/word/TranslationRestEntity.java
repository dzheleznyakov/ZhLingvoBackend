package zh.lingvo.rest.entities.word;

import com.google.common.base.MoreObjects;
import zh.lingvo.domain.words.Translation;
import zh.lingvo.rest.entities.JsonEntity;

public class TranslationRestEntity implements JsonEntity {
    private String translation;

    private String elaboration;

    public TranslationRestEntity() {
    }

    public TranslationRestEntity(Translation tr) {
        translation = tr.getTranslation();
        elaboration = tr.getElaboration();
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
