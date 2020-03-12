package zh.lingvo.domain.forms;

import zh.lingvo.domain.linguisticcategories.LinguisticCategory;
import zh.lingvo.domain.linguisticcategories.Number;
import zh.lingvo.domain.linguisticcategories.Person;
import zh.lingvo.domain.linguisticcategories.VerbTimeTense;

public interface VerbConjugationCategory {
    LinguisticCategory[] PRESENT_SIMPLE_SINGULAR_FIRST = new LinguisticCategory[] {VerbTimeTense.PRESENT_SIMPLE, Number.SINGULAR, Person.FIRST };
    LinguisticCategory[] PRESENT_SIMPLE_PLURAL_FIRST = new LinguisticCategory[] { VerbTimeTense.PRESENT_SIMPLE, Number.PLURAL, Person.FIRST };
    LinguisticCategory[] PRESENT_SIMPLE_SINGULAR_SECOND = new LinguisticCategory[] { VerbTimeTense.PRESENT_SIMPLE, Number.SINGULAR, Person.SECOND};
    LinguisticCategory[] PRESENT_SIMPLE_PLURAL_SECOND = new LinguisticCategory[] { VerbTimeTense.PRESENT_SIMPLE, Number.PLURAL, Person.SECOND};
    LinguisticCategory[] PRESENT_SIMPLE_SINGULAR_THIRD = new LinguisticCategory[] { VerbTimeTense.PRESENT_SIMPLE, Number.SINGULAR, Person.THIRD };
    LinguisticCategory[] PRESENT_SIMPLE_PLURAL_THIRD = new LinguisticCategory[] { VerbTimeTense.PRESENT_SIMPLE, Number.PLURAL, Person.THIRD };
}
