package zh.lingvo.data.services.impl;

import org.springframework.stereotype.Service;
import zh.lingvo.data.model.User;
import zh.lingvo.data.repositories.UserRepository;
import zh.lingvo.data.services.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public boolean existsByName(String name) {
        return userRepository.existsByName(name);
    }

    @Override
    public User save(User user) {
        try {
            return userRepository.save(user);
        } catch (Throwable t) {
            throw new FailedToPersist(t, "Failed to save user [%s]", user);
        }
    }
}
