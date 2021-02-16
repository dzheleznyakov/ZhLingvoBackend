package zh.lingvo.rest.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GeneralExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(Throwable.class)
    public <EX extends Throwable> String handleConflict(EX exception) {
        log.error("Error", exception);
        return "Service is currently unavailable. Please try again later";
    }
}
