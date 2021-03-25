package zh.lingvo.data.integrationtests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.data.exceptions.FailedToPersist;
import zh.lingvo.data.model.User;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static zh.hamcrest.ZhMatchers.empty;
import static zh.hamcrest.ZhMatchers.hasPropertySatisfying;

@DisplayName("Test UserService related workflows")
public class UserServiceIT extends BaseDataIntegrationTest {
    @Test
    @DisplayName("Should persist a new user")
    void testSavingUser() {
        String username = "user" + System.currentTimeMillis();
        User toSave = User.builder().name(username).build();

        User saved = userService.save(toSave);

        User persisted = findEntity(User.class, saved.getId());
        assertThat(persisted, is(notNullValue()));
        assertThat(persisted.getId(), is(notNullValue()));
        assertThat(persisted.getName(), is(username));
    }

    @Test
    @DisplayName("Should update the username")
    void testUpdatingUsername() {
        String username = "user" + System.currentTimeMillis();
        User user = setupPersistedUser(username);

        String updatedUsername = "user-updated" + System.currentTimeMillis();
        user.setName(updatedUsername);

        User updatedUser = userService.save(user);

        assertThat(updatedUser.getId(), is(user.getId()));
        assertThat(updatedUser.getName(), is(updatedUsername));
    }

    @Test
    @DisplayName("Should find user by its username")
    void testFindingUserByName() {
        String username = "user" + System.currentTimeMillis();
        User user = setupPersistedUser(username);

        Optional<User> userOptional = userService.findByName(username);

        assertThat(userOptional, is(not(empty())));
        assertThat(userOptional, hasPropertySatisfying(User::getId, user.getId()::equals));
    }

    @Test
    @DisplayName("Should not allow two users with the same name")
    void testUsernameUniqueness() {
        String username = "user" + System.currentTimeMillis();
        setupPersistedUser(username);
        User newUser = User.builder().name(username).build();

        Assertions.assertThrows(FailedToPersist.class, () -> userService.save(newUser));
    }

    private User setupPersistedUser(String username) {
        return userService.save(User.builder().name(username).build());
    }
}
