package zh.lingvo.rest.entities;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.rest.entities.word.WordRestEntity;

import java.util.List;

public class DictionaryRestEntity implements JsonEntity {
    private List<WordRestEntity> words;

    public DictionaryRestEntity() {
    }

    public DictionaryRestEntity(Dictionary dictionary) {
        this.words = dictionary.getWords().stream()
                .map(word -> new WordRestEntity(word, dictionary.getLanguage()))
                .collect(ImmutableList.toImmutableList());
    }

    public List<WordRestEntity> getWords() {
        return words;
    }

    public void setWords(List<WordRestEntity> words) {
        this.words = words;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("#OfWords", words.size())
                .add("firstWord", words.isEmpty() ? null : words.get(0))
                .toString();
    }
}
