package zh.lingvo.domain.forms;

import zh.lingvo.domain.linguisticcategories.LinguisticCategory;
import zh.lingvo.domain.linguisticcategories.NounCase;
import zh.lingvo.domain.linguisticcategories.Number;

public interface NounDeclensionsCategory {
    LinguisticCategory[] SINGULAR_NOMINATIVE = new LinguisticCategory[] { Number.SINGULAR, NounCase.NOMINATIVE };
    LinguisticCategory[] PLURAL_NOMINATIVE = new LinguisticCategory[] { Number.PLURAL, NounCase.NOMINATIVE };
    LinguisticCategory[] SINGULAR_POSSESSIVE = new LinguisticCategory[] { Number.SINGULAR, NounCase.POSSESSIVE };
    LinguisticCategory[] PLURAL_POSSESSIVE = new LinguisticCategory[] { Number.PLURAL, NounCase.POSSESSIVE };
}
