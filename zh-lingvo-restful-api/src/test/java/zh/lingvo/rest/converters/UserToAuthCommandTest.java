package zh.lingvo.rest.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.data.model.User;
import zh.lingvo.rest.commands.AuthCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@DisplayName("Test User to AuthCommand conversion")
class UserToAuthCommandTest {
    private static final String USERNAME = "username";

    private UserToAuthCommand converter;

    @BeforeEach
    void setUp() {
        converter = new UserToAuthCommand();
    }

    @Test
    @DisplayName("Should convert null to null")
    void convertNull() {
        AuthCommand command = converter.convert(null);

        assertThat(command, is(nullValue()));
    }

    @Test
    @DisplayName("Should convert a valid user into an auth command")
    void convertUser() {
        User user = User.builder()
                .id(1L)
                .name(USERNAME)
                .build();

        AuthCommand command = converter.convert(user);

        assertThat(command, is(notNullValue(AuthCommand.class)));
        assertThat(command.getUsername(), is(USERNAME));
        assertThat(command.getToken(), is(USERNAME));
    }
}