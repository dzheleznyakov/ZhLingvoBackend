package zh.lingvo.data.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zh.lingvo.data.exceptions.FailedToPersist;
import zh.lingvo.data.model.User;
import zh.lingvo.data.repositories.UserRepository;

import javax.persistence.PersistenceException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static zh.hamcrest.ZhMatchers.empty;
import static zh.hamcrest.ZhMatchers.hasPropertySatisfying;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test UserServiceImpl")
class UserServiceImplTest {
    private static final String NAME = "username";
    private static final String TOKEN = "token";

    @Mock
    private UserRepository userRepository;

    private UserServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new UserServiceImpl(userRepository);
    }

    @Nested
    @DisplayName("Test UserService.findByName(name)")
    class FindByName {
        @Test
        @DisplayName("Should return empty optional when user is not found")
        void testNotFound() {
            when(userRepository.findByName(NAME)).thenReturn(Optional.empty());

            Optional<User> userOptional = service.findByName(NAME);

            assertThat(userOptional.isEmpty(), is(true));
            verify(userRepository, only()).findByName(NAME);
        }

        @Test
        @DisplayName("Should return user when it is found")
        void testFound() {
            User persistedUser = User.builder().name(NAME).build();
            when(userRepository.findByName(NAME)).thenReturn(Optional.of(persistedUser));

            Optional<User> userOptional = service.findByName(NAME);

            assertThat(userOptional.isPresent(), is(true));
            verify(userRepository, only()).findByName(NAME);
        }
    }

    @Nested
    @DisplayName("Test UserService.existsByName(name)")
    class ExistsByName {
        @Test
        @DisplayName("Should return false if the user does not exist")
        void doesNotExist() {
            when(userRepository.existsByName(NAME)).thenReturn(false);

            boolean userExists = service.existsByName(NAME);

            assertThat(userExists, is(false));
            verify(userRepository, only()).existsByName(NAME);
        }

        @Test
        @DisplayName("Should return true if the user exists")
        void userExists() {
            when(userRepository.existsByName(NAME)).thenReturn(true);

            boolean userExists = service.existsByName(NAME);

            assertThat(userExists, is(true));
            verify(userRepository, only()).existsByName(NAME);
        }
    }

    @Nested
    @DisplayName("Test UserService.findByAuthToken(token)")
    class ExistsByAuthToken {
        @Test
        @DisplayName("Should return nothing if there is no user for the token")
        void doesNotExist() {
            when(userRepository.findByToken(TOKEN)).thenReturn(Optional.empty());

            Optional<User> userOptional = service.findByAuthToken(TOKEN);

            assertThat(userOptional, is(empty()));
            verify(userRepository, only()).findByToken(TOKEN);
        }

        @Test
        @DisplayName("Should return the user if the user for the token exists")
        void userExists() {
            User existingUser = User.builder().id(1L).name(NAME).build();
            when(userRepository.findByToken(TOKEN)).thenReturn(Optional.of(existingUser));

            Optional<User> userOptional = service.findByAuthToken(TOKEN);

            assertThat(userOptional, is(not(empty())));
            assertThat(userOptional, hasPropertySatisfying(User::getName, NAME::equals));
            verify(userRepository, only()).findByToken(TOKEN);
        }
    }

    @Nested
    @DisplayName("Test UserService.save(user)")
    class Save {
        @Test
        @DisplayName("Should throw if the downstream fails to persist")
        void failedToSave() {
            when(userRepository.save(any(User.class))).thenThrow(new PersistenceException("Something went terribly wrong"));

            assertThrows(FailedToPersist.class, () -> service.save(User.builder().name(NAME).build()));
        }

        @Test
        void successfullySaved() {
            User savedUser = User.builder().id(0L).name(NAME).build();
            when(userRepository.save(any(User.class))).thenReturn(savedUser);

            User returnedUser = service.save(User.builder().name(NAME).build());

            assertThat(returnedUser, is(savedUser));
        }
    }
}