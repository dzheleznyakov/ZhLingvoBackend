package zh.lingvo.domain.changepatterns.helpers;

import zh.lingvo.domain.LinguisticCategory;
import zh.lingvo.domain.words.Word;

import java.util.Map;

public interface WordFormsHelper {
    Map<LinguisticCategory[], String> getForms(Word word);
}
