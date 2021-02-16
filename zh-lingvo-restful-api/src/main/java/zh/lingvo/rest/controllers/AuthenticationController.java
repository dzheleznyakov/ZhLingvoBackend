package zh.lingvo.rest.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import zh.lingvo.rest.annotations.ApiController;
import zh.lingvo.data.model.User;
import zh.lingvo.data.services.UserService;

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
            throw new UserAlreadyExists(username);

        User userToSave = User.builder().name(username).build();
        User savedUser = userService.save(userToSave);
        return savedUser.getId() + "-" + savedUser.getName();
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExists.class)
    public String handleExistingUserException(UserAlreadyExists ex) {
        log.warn("Error while creating a user", ex);
        return ex.getMessage();
    }

    public static class UserAlreadyExists extends RuntimeException {
        public UserAlreadyExists(String username) {
            super(String.format("User [%s] already exists", username));
        }
    }
}
