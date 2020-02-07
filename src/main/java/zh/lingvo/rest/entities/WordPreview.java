package zh.lingvo.rest.entities;

import com.google.common.base.MoreObjects;
import zh.lingvo.domain.words.Word;

import java.util.UUID;

public class WordPreview implements JsonEntity {
    private UUID id;
    private String word;

    public WordPreview() {
    }

    public WordPreview(Word word) {
        this.id = word.getId();
        this.word = word.getName().getValue();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("word", word)
                .toString();
    }
}
