package zh.lingvo.data.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import zh.lingvo.data.domain.User;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static zh.hamcrest.ZhMatchers.empty;
import static zh.hamcrest.ZhMatchers.hasPropertySatisfying;

@ContextConfiguration(classes = UserRepository.class)
class UserRepositoryTest extends BaseRepositoryTest<UserRepository> {
    private static final String NAME_1 = "Name 1";
    private static final String NAME_2 = "Name 2";
    private static final String NAME_3 = "Name 3";

    @BeforeEach
    void setUpDb() {
        entityManager.persist(getUser(NAME_1));
        entityManager.persist(getUser(NAME_2));
        entityManager.persist(getUser(NAME_3));
        entityManager.flush();
    }

    private User getUser(String name) {
        return User.builder()
                .name(name)
                .build();
    }

    @Test
    @DisplayName("Should return nothing when there is no user with the given name")
    void findByName_NoUser() {
        Optional<User> userOptional = repository.findByName("No name");

        assertThat(userOptional, is(empty()));
    }

    @Test
    @DisplayName("Should return the user by name when it exists in the DB")
    void findByName_UserExists() {
        Optional<User> userOptional = repository.findByName(NAME_2);

        assertThat(userOptional, is(not(empty())));
        assertThat(userOptional, hasPropertySatisfying(User::getName, NAME_2::equals));
    }
}