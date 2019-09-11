package zh.lingvo.domain.words;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

public class Translation implements WordEntity {
    private String translation;
    private String elaboration;

    public Translation() {
    }

    public Translation(String translation) {
        this.translation = translation;
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

    public boolean isVoid() {
        return Strings.isNullOrEmpty(translation) && Strings.isNullOrEmpty(elaboration);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("translation", translation)
                .add("elaboration", elaboration)
                .toString();
    }
}
