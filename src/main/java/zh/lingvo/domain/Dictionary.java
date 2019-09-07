package zh.lingvo.domain;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import zh.lingvo.domain.words.Word;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Dictionary {
    private final Language language;

    private final Map<UUID, Word> words;

    public Dictionary(Language language) {
        this.language = language;
        this.words = new HashMap<>();
    }

    public void add(Word word) {
        words.put(word.getId(), word);
    }

    public void set(Word word) {
        Preconditions.checkArgument(
                words.containsKey(word.getId()),
                "Word with id [%s] not found in [%s] dictionary", word.getId(), language.getCode());
        words.put(word.getId(), word);
    }

    public void setWords(Collection<Word> words) {
        this.words.clear();
        words.forEach(word -> this.words.put(word.getId(), word));
    }

    public Language getLanguage() {
        return language;
    }

    public List<Word> getWords() {
        return words.values().stream()
                .sorted(Comparator.comparing(Word::getName))
                .collect(ImmutableList.toImmutableList());
    }

    public Word get(UUID id) {
        return words.get(id);
    }
}
