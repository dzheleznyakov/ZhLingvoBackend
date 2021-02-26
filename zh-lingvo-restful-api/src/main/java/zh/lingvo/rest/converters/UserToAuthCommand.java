package zh.lingvo.rest.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import zh.lingvo.data.model.User;
import zh.lingvo.rest.commands.AuthCommand;

import javax.annotation.Nullable;

@Component
public class UserToAuthCommand implements Converter<User, AuthCommand> {
    @Override
    public AuthCommand convert(@Nullable User source) {
        if (source == null)
            return null;

        return AuthCommand.builder()
                .username(source.getName())
                .token(getAuthToken(source))
                .build();
    }

    private String getAuthToken(User source) {
        return source.getName();
    }
}
