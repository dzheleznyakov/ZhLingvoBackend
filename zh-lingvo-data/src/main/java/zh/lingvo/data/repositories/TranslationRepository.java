package zh.lingvo.data.repositories;

import zh.lingvo.data.fixtures.SubWordRepository;
import zh.lingvo.data.model.Translation;

public interface TranslationRepository extends SubWordRepository<Translation, Long> {
    @Override
    default Class<Translation> getSubWordClass() {
        return Translation.class;
    }
}
