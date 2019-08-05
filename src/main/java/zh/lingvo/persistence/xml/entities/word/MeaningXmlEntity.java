package zh.lingvo.persistence.xml.entities.word;

import com.google.common.base.MoreObjects;
import zh.lingvo.domain.words.Meaning;
import zh.lingvo.util.CollectionUtils;
import zh.lingvo.persistence.xml.entities.XmlEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "meaning")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class MeaningXmlEntity implements XmlEntity {
    private String remark;
    private List<TranslationXmlEntity> translations;
    private List<ExampleXmlEntity> examples;

    public MeaningXmlEntity() {
    }

    public MeaningXmlEntity(Meaning meaning) {
        remark = meaning.getRemark();
        translations = CollectionUtils.transform(meaning::getTranslations, TranslationXmlEntity::new);
        examples = CollectionUtils.transform(meaning::getExamples, ExampleXmlEntity::new);
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @XmlElementWrapper(name = "translations")
    @XmlElement(name = "translation")
    public List<TranslationXmlEntity> getTranslations() {
        return translations;
    }

    public void setTranslations(List<TranslationXmlEntity> translations) {
        this.translations = translations;
    }

    @XmlElementWrapper(name = "examples")
    @XmlElement(name = "example")
    public List<ExampleXmlEntity> getExamples() {
        return examples;
    }

    public void setExamples(List<ExampleXmlEntity> examples) {
        this.examples = examples;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("remark", remark)
                .add("#OfTranslations", translations == null ? null : translations.size())
                .add("#OfExamples", examples == null ? null : examples.size())
                .toString();
    }
}
