package zh.lingvo.data.repositories;

import org.springframework.data.repository.CrudRepository;
import zh.lingvo.data.domain.Language;

import java.util.Optional;

public interface LanguageRepository extends CrudRepository<Language, Integer> {
    Optional<Language> findByTwoLetterCode(String code);
}
