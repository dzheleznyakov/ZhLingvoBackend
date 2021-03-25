package zh.lingvo.data.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import zh.lingvo.data.model.Word;

import java.util.List;
import java.util.Optional;

public interface WordRepository extends CrudRepository<Word, Long> {
    List<Word> findAllByMainForm(String mainForm);

    @Query("SELECT w from word w JOIN FETCH w.semanticBlocks WHERE w.id = (:id)")
    Optional<Word> findByIdWithSubWordParts(@Param("id") Long id);
}
