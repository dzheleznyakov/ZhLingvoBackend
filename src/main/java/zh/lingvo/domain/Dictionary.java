package zh.lingvo.domain;

import com.google.common.collect.ImmutableList;
import zh.lingvo.domain.words.Word;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Dictionary {
    private final Language language;

    private final Set<Word> words;

    public Dictionary(Language language) {
        this.language = language;
        this.words = new TreeSet<>(Comparator.comparing(Word::getWord));
    }

    public void add(Word word) {
        this.words.add(word);
    }

    public void addAll(Collection<Word> newWords) {
        this.words.addAll(newWords);
    }

    public void addAll(Word... newWords) {
        this.words.addAll(Arrays.asList(newWords));
    }

    public Language getLanguage() {
        return language;
    }

    public List<Word> getWords() {
        return ImmutableList.copyOf(words);
    }
}
