package zh.lingvo.data.services;

import zh.lingvo.data.model.Meaning;
import zh.lingvo.data.model.User;

import java.util.Optional;

public interface MeaningService {
    Optional<Meaning> findById(Long id, User user);
}
