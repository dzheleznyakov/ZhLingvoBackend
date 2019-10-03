package zh.lingvo.caches;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import zh.lingvo.domain.languages.Language;
import zh.lingvo.domain.languages.Spanish;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LanguagesCacheTest {
    @Autowired
    private LanguagesCache languagesCache;

    @Test
    public void returnsAllDefinedLanguages() {
        var actualLanguageCodes = languagesCache.get().stream()
                .map(Language::getCode)
                .collect(ImmutableSet.toImmutableSet());
        var expectedLanguageCodes = ImmutableSet.of("En", "Es", "Ru");
        assertThat(actualLanguageCodes, equalTo(expectedLanguageCodes));
    }

    @Test
    public void returnsLanguageByItsCode() {
        Language actualLanguage = languagesCache.get("Es");
        assertThat(actualLanguage, is(Spanish.getInstance()));
    }

    @Test
    public void checksByCodeWhetherLanguageIsRegistered() {
        boolean esIsRegistered = languagesCache.isRegistered("Es");
        boolean fkIsRegistered = languagesCache.isRegistered("Fk");

        assertTrue(esIsRegistered);
        assertFalse(fkIsRegistered);
    }
}
