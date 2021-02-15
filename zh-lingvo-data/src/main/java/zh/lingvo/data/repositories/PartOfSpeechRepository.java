package zh.lingvo.data.repositories;

import org.springframework.data.repository.CrudRepository;
import zh.lingvo.data.model.PartOfSpeech;

import java.util.Optional;

public interface PartOfSpeechRepository extends CrudRepository<PartOfSpeech, Integer> {
    Optional<PartOfSpeech> findByName(String name);
}
