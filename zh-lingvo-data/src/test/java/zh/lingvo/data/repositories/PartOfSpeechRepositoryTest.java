package zh.lingvo.data.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import zh.lingvo.data.domain.PartOfSpeech;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static zh.hamcrest.ZhMatchers.empty;
import static zh.hamcrest.ZhMatchers.hasPropertySatisfying;

@ContextConfiguration(classes = PartOfSpeechRepository.class)
class PartOfSpeechRepositoryTest extends BaseRepositoryTest<PartOfSpeechRepository> {
    private static final String NOUN = "NOUN";
    private static final String VERB = "VERB";
    private static final String ADVERB = "ADVERB";

    @BeforeEach
    void setUpDb() {
        entityManager.persist(getPos(NOUN));
        entityManager.persist(getPos(VERB));
        entityManager.persist(getPos(ADVERB));
        entityManager.flush();
    }

    @Test
    @DisplayName("Should return nothing when there is no such part of speech")
    void findByName_NoPos() {
        Optional<PartOfSpeech> posOptional = repository.findByName("ADJECTIVE");

        assertThat(posOptional, is(empty()));
    }

    @Test
    @DisplayName("Should return POS by its name")
    void findByName_PosExists() {
        Optional<PartOfSpeech> posOptional = repository.findByName(NOUN);

        assertThat(posOptional, is(not(empty())));
        assertThat(posOptional, hasPropertySatisfying(PartOfSpeech::getName, NOUN::equals));
    }

    private PartOfSpeech getPos(String name) {
        return PartOfSpeech.builder()
                .name(name)
                .build();
    }

}