package zh.lingvo.rest.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import zh.lingvo.data.model.User;
import zh.lingvo.data.services.UserService;
import zh.lingvo.rest.annotations.ApiController;
import zh.lingvo.rest.commands.AuthCommand;
import zh.lingvo.rest.converters.UserToAuthCommand;
import zh.lingvo.rest.exceptions.ResourceAlreadyExists;
import zh.lingvo.rest.exceptions.ResourceNotFound;

import java.util.Map;

@Slf4j
@ApiController
@RequestMapping(produces = ControllersConstants.CONTENT_TYPE)
public class AuthenticationController {
    private static final String PAYLOAD_FIELD_USERNAME = "username";
    private static final String PAYLOAD_FIELD_TOKEN = "token";

    private final UserService userService;
    private final UserToAuthCommand userToAuthConverter;

    public AuthenticationController(UserService userService, UserToAuthCommand userToAuthConverter) {
        this.userService = userService;
        this.userToAuthConverter = userToAuthConverter;
    }

    @PostMapping("/signup")
    public AuthCommand signUp(@RequestBody Map<String, String> payload) {
        String username = payload.get(PAYLOAD_FIELD_USERNAME);
        boolean userExists = userService.existsByName(username);
        if (userExists)
            throw new ResourceAlreadyExists(String.format("User [%s] already exists", username));

        User userToSave = User.builder().name(username).build();
        User savedUser = userService.save(userToSave);
        return userToAuthConverter.convert(savedUser);
    }

    @PostMapping("/signin")
    public AuthCommand signIn(@RequestBody Map<String, String> payload) {
        String username = payload.get(PAYLOAD_FIELD_USERNAME);
        return userService.findByName(username)
                .map(userToAuthConverter::convert)
                .orElseThrow(() -> new ResourceNotFound(String.format("User [%s] is not found", username)));
    }

    @PostMapping("/resign")
    public AuthCommand reSign(@RequestBody Map<String, String> payload) {
        String token = payload.get(PAYLOAD_FIELD_TOKEN);
        return userService.findByAuthToken(token)
                .map(userToAuthConverter::convert)
                .orElseThrow(() -> new ResourceNotFound("User is not found"));
    }
}
