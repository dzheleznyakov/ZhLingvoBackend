package zh.lingvo.rest.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import zh.lingvo.rest.commands.ErrorCommand;
import zh.lingvo.rest.exceptions.RequestMalformed;
import zh.lingvo.rest.exceptions.RequestNotAuthorised;
import zh.lingvo.rest.exceptions.ResourceAlreadyExists;
import zh.lingvo.rest.exceptions.ResourceNotFound;

@Slf4j
@ControllerAdvice
public class ExceptionsAdvice {
    @ExceptionHandler(Throwable.class)
    public <EX extends Throwable> ResponseEntity<ErrorCommand> handleConflict(EX exception) {
        return handleException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Service is currently unavailable. Please try again later",
                "Error: [{}]",
                exception);
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ErrorCommand> handleNotFound(ResourceNotFound exception) {
        return handleException(HttpStatus.NOT_FOUND, "Error while fetching resource: [{}]", exception);
    }

    @ExceptionHandler(ResourceAlreadyExists.class)
    public ResponseEntity<ErrorCommand> handleExistingUserException(ResourceAlreadyExists exception) {
        return handleException(HttpStatus.CONFLICT, "Error while creating a resource: [{}]", exception);
    }

    @ExceptionHandler(RequestNotAuthorised.class)
    public ResponseEntity<ErrorCommand> handleRequestNotAuthorised(RequestNotAuthorised exception) {
        return handleException(HttpStatus.UNAUTHORIZED, "Not authorised request: [{}]", exception);
    }

    @ExceptionHandler(RequestMalformed.class)
    public ResponseEntity<ErrorCommand> handleRequestMalformed(RequestMalformed exception) {
        return handleException(HttpStatus.BAD_REQUEST, "Malformed request: [{}]", exception);
    }

    private <E extends Throwable> ResponseEntity<ErrorCommand> handleException(HttpStatus httpStatus, String messageTemplate, E exception) {
        return handleException(httpStatus, exception.getMessage(), messageTemplate, exception);
    }

    private <E extends Throwable> ResponseEntity<ErrorCommand> handleException(
            HttpStatus httpStatus,
            String errorCommandMessage,
            String messageTemplate,
            E exception
    ) {
        log.warn(messageTemplate, exception.getMessage(), exception);
        ErrorCommand error = ErrorCommand.builder()
                .message(errorCommandMessage)
                .build();
        return ResponseEntity
                .status(httpStatus)
                .body(error);
    }
}
