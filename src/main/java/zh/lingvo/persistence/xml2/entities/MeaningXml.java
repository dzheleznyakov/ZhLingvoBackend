package zh.lingvo.persistence.xml2.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import zh.lingvo.persistence.xml.entities.XmlEntity;

import java.util.List;

@JacksonXmlRootElement(localName = "meaning")
@JsonInclude(Include.NON_NULL)
public class MeaningXml implements XmlEntity {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "translation")
    private List<TranslationXml> translations;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "example")
    private List<ExampleXml> examples;

    public MeaningXml() {
    }

    public MeaningXml(List<TranslationXml> translations, List<ExampleXml> examples) {
        this.translations = translations;
        this.examples = examples;
    }

    public List<TranslationXml> getTranslations() {
        return translations;
    }

    public void setTranslations(List<TranslationXml> translations) {
        this.translations = translations;
    }

    public List<ExampleXml> getExamples() {
        return examples;
    }

    public void setExamples(List<ExampleXml> examples) {
        this.examples = examples;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeaningXml that = (MeaningXml) o;
        return Objects.equal(translations, that.translations) &&
                Objects.equal(examples, that.examples);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(translations, examples);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("translations", translations)
                .add("examples", examples)
                .toString();
    }
}
