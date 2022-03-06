package ru.academits.phonebook;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.academits.model.ErrorInfo;

@ControllerAdvice
public class ErrorController {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorInfo processException(Exception e) {
        return new ErrorInfo(e.getMessage());
    }
}
