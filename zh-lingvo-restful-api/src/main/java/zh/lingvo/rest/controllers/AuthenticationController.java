package zh.lingvo.rest.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import zh.lingvo.data.model.User;
import zh.lingvo.data.services.UserService;
import zh.lingvo.rest.annotations.ApiController;
import zh.lingvo.rest.commands.AuthCommand;
import zh.lingvo.rest.exceptions.ResourceAlreadyExists;
import zh.lingvo.rest.exceptions.ResourceNotFound;

import java.util.Map;


@Slf4j
@ApiController
@RequestMapping(produces = ControllersConstants.CONTENT_TYPE)
public class AuthenticationController {
    private static final String PAYLOAD_FIELD_USERNAME = "username";

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public AuthCommand signUp(@RequestBody Map<String, String> payload) {
        String username = payload.get(PAYLOAD_FIELD_USERNAME);
        boolean userExists = userService.existsByName(username);
        if (userExists)
            throw new ResourceAlreadyExists(String.format("User [%s] already exists", username));

        User userToSave = User.builder().name(username).build();
        User savedUser = userService.save(userToSave);
        return AuthCommand.builder()
                .username(savedUser.getName())
                .token(savedUser.getName())
                .build();
    }

    @PostMapping("/signin")
    public AuthCommand signIn(@RequestBody Map<String, String> payload) {
        String username = payload.get(PAYLOAD_FIELD_USERNAME);
        boolean userExists = userService.existsByName(username);
        if (!userExists)
            throw new ResourceNotFound(String.format("User [%s] is not found", username));
        return AuthCommand.builder()
                .username(username)
                .token(username)
                .build();
    }
}
