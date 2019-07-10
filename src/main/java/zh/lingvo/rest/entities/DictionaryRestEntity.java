package zh.lingvo.rest.entities;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import zh.lingvo.rest.entities.word.WordRestEntity;
import zh.lingvo.domain.Dictionary;
import zh.lingvo.util.json.Jsonable;

import java.util.List;

public class DictionaryRestEntity implements JsonEntity {
    @Jsonable
    private List<WordRestEntity> words;

    public DictionaryRestEntity() {
    }

    public DictionaryRestEntity(Dictionary dictionary) {
        this.words = dictionary.getWords().stream()
                .map(e -> new WordRestEntity())
//                .map(WordRestEntity::new)
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
