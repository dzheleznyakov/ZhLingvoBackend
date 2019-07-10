package persistence.xml.entities.word;

import com.google.common.base.MoreObjects;
import zh.lingvo.domain.words.Translation;
import persistence.xml.entities.XmlEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "translation")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class TranslationXmlEntity implements XmlEntity {
    private String translation;
    private String elaboration;

    public TranslationXmlEntity() {
    }

    public TranslationXmlEntity(Translation tr) {
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
