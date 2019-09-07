package zh.lingvo.rest.entities;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import zh.lingvo.domain.Dictionary;

import java.util.List;

public class DictionaryRestEntity implements JsonEntity {
    private List<WordPreview> words;

    public DictionaryRestEntity() {
    }

    public DictionaryRestEntity(Dictionary dictionary) {
        this.words = dictionary.getWords().stream()
                .map(WordPreview::new)
                .collect(ImmutableList.toImmutableList());
    }

    public List<WordPreview> getWords() {
        return words;
    }

    public void setWords(List<WordPreview> words) {
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
