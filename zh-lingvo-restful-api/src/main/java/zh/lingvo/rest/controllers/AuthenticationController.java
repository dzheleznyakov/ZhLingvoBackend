package zh.lingvo.rest.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import zh.lingvo.rest.annotations.ApiController;
import zh.lingvo.data.model.User;
import zh.lingvo.data.services.UserService;
import zh.lingvo.rest.exceptions.ResourceAlreadyExists;
import zh.lingvo.rest.exceptions.ResourceNotFound;

@ApiController
@ControllerAdvice
@Slf4j
public class AuthenticationController {
    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public String signUp(@RequestBody String username) {
        boolean userExists = userService.existsByName(username);
        if (userExists)
            throw new ResourceAlreadyExists(String.format("User [%s] already exists", username));

        User userToSave = User.builder().name(username).build();
        User savedUser = userService.save(userToSave);
        return savedUser.getName();
    }

    @PostMapping("/signin")
    public String signIn(@RequestBody String username) {
        boolean userExists = userService.existsByName(username);
        if (!userExists)
            throw new ResourceNotFound(String.format("User [%s] is not found", username));
        return username;
    }
}
