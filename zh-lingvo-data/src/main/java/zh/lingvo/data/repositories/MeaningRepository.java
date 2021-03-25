package zh.lingvo.data.repositories;

import zh.lingvo.data.fixtures.SubWordRepository;
import zh.lingvo.data.model.Meaning;

public interface MeaningRepository extends SubWordRepository<Meaning, Long> {
    @Override
    default Class<Meaning> getSubWordClass() {
        return Meaning.class;
    }
}
