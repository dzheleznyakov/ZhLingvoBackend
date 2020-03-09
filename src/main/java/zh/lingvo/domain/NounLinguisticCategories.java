package zh.lingvo.domain;

import zh.lingvo.domain.forms.NounWordFormCategory;

public interface NounLinguisticCategories {
    LinguisticCategory[] SINGULAR_NOMINATIVE = new LinguisticCategory[] { Number.SINGULAR, NounWordFormCategory.NOMINATIVE };
    LinguisticCategory[] PLURAL_NOMINATIVE = new LinguisticCategory[] { Number.PLURAL, NounWordFormCategory.NOMINATIVE };
    LinguisticCategory[] SINGULAR_POSSESSIVE = new LinguisticCategory[] { Number.SINGULAR, NounWordFormCategory.POSSESSIVE };
    LinguisticCategory[] PLURAL_POSSESSIVE = new LinguisticCategory[] { Number.PLURAL, NounWordFormCategory.POSSESSIVE };
}
