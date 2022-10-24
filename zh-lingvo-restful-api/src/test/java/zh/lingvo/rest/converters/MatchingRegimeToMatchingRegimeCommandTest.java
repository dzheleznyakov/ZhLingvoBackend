package zh.lingvo.rest.converters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zh.lingvo.data.model.enums.MatchingRegime;
import zh.lingvo.rest.commands.MatchingRegimeCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Test MatchingRegime to MatchingRegimeCommand converter")
class MatchingRegimeToMatchingRegimeCommandTest {
    private MatchingRegimeToMatchingRegimeCommand converter;

    @BeforeEach
    void setUp() {
        converter = new MatchingRegimeToMatchingRegimeCommand();
    }

    @Test
    @DisplayName("Should convert a matching regigime to a matching regime command")
    void convertMatchingRegime() {
        MatchingRegime matchingRegime = MatchingRegime.RELAXED;

        MatchingRegimeCommand command = converter.convert(matchingRegime);

        assertThat(command, is(notNullValue()));
        assertThat(command.getValue(), is(equalTo(matchingRegime.toString())));
        assertThat(command.getCode(), is(equalTo(matchingRegime.getCode())));
    }
}