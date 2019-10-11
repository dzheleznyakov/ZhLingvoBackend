package zh.lingvo.domain.changepatterns.helpers;

import zh.lingvo.domain.words.Word;

import java.util.Map;

public interface WordFormsHelper {
    Map<Enum<?>[], String> getForms(Word word);
}
