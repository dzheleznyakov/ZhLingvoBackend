package zh.lingvo.data.services;

import zh.lingvo.data.fixtures.SubWordPart;

import java.util.Optional;

public interface SubWordService {
    <E extends SubWordPart> Optional<E> save(E entity);
}
