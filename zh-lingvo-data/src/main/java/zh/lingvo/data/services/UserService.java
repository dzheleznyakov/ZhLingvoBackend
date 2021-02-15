package zh.lingvo.data.services;

import zh.lingvo.data.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByName(String name);

    boolean existsByName(String name);

    User save(User user);

    class FailedToPersist extends RuntimeException {
        public FailedToPersist(Throwable cause, String pattern, Object... params) {
            super(String.format(pattern, params), cause);
        }
    }
}
