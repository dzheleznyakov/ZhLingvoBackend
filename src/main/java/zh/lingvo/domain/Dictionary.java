package zh.lingvo.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import zh.lingvo.domain.languages.Language;
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
    private final Map<String, Word> wordsByName;

    public Dictionary(Language language) {
        this.language = language;
        this.words = new HashMap<>();
        this.wordsByName = new HashMap<>();
    }

    public void add(Word word) {
        put(word);
    }

    public void set(Word word) {
        Preconditions.checkArgument(
                words.containsKey(word.getId()),
                "Word with id [%s] not found in [%s] dictionary", word.getId(), language.getCode());
        put(word);
    }

    public void setWords(Collection<Word> words) {
        this.words.clear();
        words.forEach(this::put);
    }

    private void put(Word word) {
        words.put(word.getId(), word);
        wordsByName.put(word.getName().getValue(), word);
    }

    public Language getLanguage() {
        return language;
    }

    public List<Word> getWords() {
        return words.values().stream()
                .sorted(Comparator.comparing(word -> word.getName().getValue()))
                .collect(ImmutableList.toImmutableList());
    }

    public boolean contains(String wordName) {
        return wordsByName.containsKey(wordName);
    }

    public Word get(UUID id) {
        return words.get(id);
    }

    public Word get(String wordName) {
        return wordsByName.get(wordName);
    }

    public Word remove(UUID uuid) {
        return words.remove(uuid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dictionary that = (Dictionary) o;
        return Objects.equal(language, that.language) &&
                Objects.equal(words, that.words) &&
                Objects.equal(wordsByName, that.wordsByName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(language, words, wordsByName);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("language", language)
                .toString();
    }
}
