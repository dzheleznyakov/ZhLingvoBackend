package zh.lingvo.persistence.xml.entities;

import com.google.common.base.MoreObjects;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.persistence.xml.entities.word.WordXmlEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement(name = "dictionary")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DictionaryXmlEntity implements XmlEntity {
    private String languageCode;
    private List<WordXmlEntity> words;

    public DictionaryXmlEntity() {
    }

    public DictionaryXmlEntity(Dictionary dictionary) {
        this.languageCode = dictionary.getLanguage().getCode();
        this.words = dictionary.getWords().stream()
                .map(WordXmlEntity::new)
                .collect(Collectors.toList());
    }

    @XmlAttribute(name = "code")
    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @XmlElementWrapper(name = "words")
    @XmlElement(name = "word")
    public List<WordXmlEntity> getWords() {
        return words;
    }

    public void setWords(List<WordXmlEntity> words) {
        this.words = words;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("languageCode", languageCode)
                .add("#OfWords", words.size())
                .add("firstWord", words.isEmpty() ? null : words.iterator().next())
                .toString();
    }
}
