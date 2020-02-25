package zh.lingvo.domain;

import zh.lingvo.domain.forms.NounWordForm;

public interface NounLinguisticCategories {
    LinguisticCategory[] SINGULAR_NOMINATIVE = new LinguisticCategory[] { Number.SINGULAR, NounWordForm.NOMINATIVE };
    LinguisticCategory[] PLURAL_NOMINATIVE = new LinguisticCategory[] { Number.PLURAL, NounWordForm.NOMINATIVE };
    LinguisticCategory[] SINGULAR_POSSESSIVE = new LinguisticCategory[] { Number.SINGULAR, NounWordForm.POSSESSIVE };
    LinguisticCategory[] PLURAL_POSSESSIVE = new LinguisticCategory[] { Number.PLURAL, NounWordForm.POSSESSIVE };
}
