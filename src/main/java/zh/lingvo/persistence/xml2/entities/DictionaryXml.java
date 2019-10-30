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

@JacksonXmlRootElement(localName = "dictionary")
@JsonInclude(Include.NON_NULL)
public class DictionaryXml implements XmlEntity {
    @JacksonXmlProperty(isAttribute = true)
    private String lang;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "word")
    private List<WordXml> words;

    public DictionaryXml() {
    }

    public DictionaryXml(String lang, List<WordXml> words) {
        this.lang = lang;
        this.words = words;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<WordXml> getWords() {
        return words;
    }

    public void setWords(List<WordXml> words) {
        this.words = words;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DictionaryXml that = (DictionaryXml) o;
        return Objects.equal(lang, that.lang) &&
                Objects.equal(words, that.words);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(lang, words);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("lang", lang)
                .add("words", words == null ? "null" : words.size())
                .toString();
    }
}
