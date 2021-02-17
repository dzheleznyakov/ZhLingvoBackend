package zh.lingvo.rest.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import zh.lingvo.rest.commands.ErrorCommand;
import zh.lingvo.rest.exceptions.ResourceAlreadyExists;
import zh.lingvo.rest.exceptions.ResourceNotFound;

@Slf4j
@ControllerAdvice
public class ExceptionsAdvice {
    @ExceptionHandler(Throwable.class)
    public <EX extends Throwable> ResponseEntity<ErrorCommand> handleConflict(EX exception) {
        log.error("Error", exception);
        ErrorCommand error = ErrorCommand.builder()
                .message("Service is currently unavailable. Please try again later")
                .build();
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(error);
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ErrorCommand> handleNotFound(ResourceNotFound exception) {
        log.warn("Error while fetching resource", exception);
        ErrorCommand error = ErrorCommand.builder()
                .message(exception.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(ResourceAlreadyExists.class)
    public ResponseEntity<ErrorCommand> handleExistingUserException(ResourceAlreadyExists exception) {
        log.warn("Error while creating a resource", exception);
        ErrorCommand error = ErrorCommand.builder()
                .message(exception.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }
}
