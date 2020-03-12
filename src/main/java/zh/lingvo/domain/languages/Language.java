package zh.lingvo.domain.languages;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import zh.lingvo.domain.PartOfSpeech;
import zh.lingvo.domain.changepatterns.BasicNounChangeModel;
import zh.lingvo.domain.changepatterns.ChangeModel;
import zh.lingvo.domain.changepatterns.helpers.WordFormsHelper;
import zh.lingvo.domain.forms.WordFormCategory;
import zh.lingvo.domain.forms.WordForms;
import zh.lingvo.domain.linguisticcategories.Gender;
import zh.lingvo.domain.linguisticcategories.LinguisticCategory;
import zh.lingvo.domain.linguisticcategories.Number;
import zh.lingvo.domain.linguisticcategories.Person;
import zh.lingvo.domain.words.Name;
import zh.lingvo.domain.words.Word;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class Language {
    private final String code;
    private final String name;

    protected Map<PartOfSpeech, String> posNamings;
    protected Map<PartOfSpeech, List<WordFormCategory>> wordFormsMappings;
    protected Map<PartOfSpeech, ChangeModel> changePatternsMap;
    protected Map<PartOfSpeech, WordFormsHelper> wordFormHelpers;
    protected Map<Number, String> numberNamings;
    protected Map<Gender, String> genderNamings;
    protected Map<Person, String> personEncodings;
    protected Map<LinguisticCategory[], String> conjugationEncodings;
    protected Map<WordFormCategory, String> wordFormNamings;

    protected Language(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @NotNull
    public String getPartsOfSpeechName(PartOfSpeech partOfSpeech) {
        return getPosNamings().getOrDefault(partOfSpeech, "");
    }

    public List<PartOfSpeech> getPartsOfSpeech() {
        return getKeyEnumsInOrder(getPosNamings());
    }

    @NotNull
    public Map<PartOfSpeech, String> getPosNamings() {
        if (posNamings == null)
            loadPosNamings();
        return posNamings;
    }

    @Nullable
    public PartOfSpeech getPos(String posName) {
        return getPosNamings().entrySet()
                .stream()
                .filter((entry -> Objects.equals(entry.getValue(), posName)))
                .map(Map.Entry::getKey)
                .findAny()
                .orElse(null);
    }

    protected abstract void loadPosNamings();

    public String getNumberName(Number num) {
        return getNumberNamings().getOrDefault(num, "");
    }

    public List<Number> getNumbers() {
        return getKeyEnumsInOrder(getNumberNamings());
    }

    @NotNull
    private Map<Number, String> getNumberNamings() {
        if (numberNamings == null)
            loadNumberNamings();
        return numberNamings;
    }

    protected abstract void loadNumberNamings();

    private <E extends Enum<E>> List<E> getKeyEnumsInOrder(Map<E, ?> map) {
        return map.keySet()
                .stream()
                .sorted(Comparator.comparing(E::ordinal))
                .collect(ImmutableList.toImmutableList());
    }

    public List<WordFormCategory> getForms(PartOfSpeech pos) {
        return getWordFormsMappings().get(pos);
    }

    @NotNull
    private Map<PartOfSpeech, List<WordFormCategory>> getWordFormsMappings() {
        if (wordFormsMappings == null)
            loadWordFormsMappings();
        return wordFormsMappings;
    }

    protected abstract void loadWordFormsMappings();

    public WordForms getWordForms(@NotNull Word word, @NotNull PartOfSpeech pos) {
        WordFormsHelper wordFormsHelper = getWordFormsHelpers().get(pos);
        List<Name> formExceptions = word.getFormExceptions() == null
                ? ImmutableList.of()
                : word.getFormExceptions().get(pos);
        return wordFormsHelper.getForms(word, formExceptions);
    }

    public LinguisticCategory[] getForm(PartOfSpeech pos, String formName) {
        WordFormsHelper wordFormsHelper = getWordFormsHelpers().get(pos);
        return wordFormsHelper.getForm(formName);
    }

    private Map<PartOfSpeech, WordFormsHelper> getWordFormsHelpers() {
        if (wordFormHelpers == null)
            loadWordFormHelpers();
        return wordFormHelpers;
    }

    protected abstract void loadWordFormHelpers();

    public String getFormName(WordFormCategory wordFormCategory) {
        return getWordFormsNamings().getOrDefault(wordFormCategory, "");
    }

    private Map<WordFormCategory, String> getWordFormsNamings() {
        if (wordFormNamings == null)
            loadWordFormNamings();
        return wordFormNamings;
    }

    protected abstract void loadWordFormNamings();

    public List<Gender> getGenders() {
        return getKeyEnumsInOrder(getGenderNamings());
    }

    public String getGenderName(Gender gender) {
        return getGenderNamings().getOrDefault(gender, "");
    }

    @NotNull
    public Map<Gender, String> getGenderNamings() {
        if (genderNamings == null)
            loadGenderNamings();
        return genderNamings;
    }

    protected abstract void loadGenderNamings();

    public String getPersonEncoding(Person person) {
        return getPersonEndodings().getOrDefault(person, "");
    }

    @NotNull
    public Map<Person, String> getPersonEndodings() {
        if (personEncodings == null)
            loadPersonEncodings();
        return personEncodings;
    }

    protected abstract void loadPersonEncodings();

    @NotNull
    public Map<LinguisticCategory[], String> getConjugationEncodings() {
        if (conjugationEncodings == null)
            loadConjugationEncodings();
        return conjugationEncodings;
    }

    protected abstract void loadConjugationEncodings();

    public ChangeModel getChangeModel(PartOfSpeech pos) {
        return getChangePatternsMap().getOrDefault(pos, ChangeModel.EMPTY);
    }

    private Map<PartOfSpeech, ChangeModel> getChangePatternsMap() {
        if (changePatternsMap == null)
            loadChangePatternMap();
        return changePatternsMap;
    }

    protected void loadChangePatternMap() {
        changePatternsMap = ImmutableMap.of(
                PartOfSpeech.NOUN, new BasicNounChangeModel(this)
        );
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("code", code)
                .add("name", name)
                .toString();
    }

}
