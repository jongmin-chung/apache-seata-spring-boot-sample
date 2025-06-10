package app.tozzi.controller.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleBadRequest(MethodArgumentNotValidException me) {

        return buildProblemDetail(HttpStatus.BAD_REQUEST, me.getBindingResult().getAllErrors().stream().map(error -> {
            if (error instanceof FieldError fe) {
                return fe.getField() + ": " + error.getDefaultMessage();
            }
            return error.getObjectName() + ": " + error.getDefaultMessage();
        }).collect(Collectors.joining("; ")));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleBadRequest(ConstraintViolationException me) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, me.getConstraintViolations().stream().map(c -> c.getPropertyPath() + ": " + c.getMessage()).collect(Collectors.joining("; ")));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleNotFound(NoResourceFoundException ex) {
        return buildProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ProblemDetail handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        return buildProblemDetail(HttpStatus.UNSUPPORTED_MEDIA_TYPE, null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ProblemDetail handleUnprocessableEntity(HttpMessageNotReadableException ex) {
        var message = HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase();
        if (ex.getCause() instanceof InvalidFormatException ife) {
            message = message + ". Invalid value: " + ife.getValue();

        } else if (ex.getCause() instanceof ValueInstantiationException vie && vie.getCause() instanceof IllegalArgumentException iae) {
            var path = vie.getPath().stream().map(JsonMappingException.Reference::getFieldName).collect(Collectors.joining("."));
            message = message + ". " + path + ":" + iae.getMessage();
        }
        return buildProblemDetail(HttpStatus.UNPROCESSABLE_ENTITY, message);
    }

    private static ProblemDetail buildProblemDetail(HttpStatus httpStatus, String message) {
        var problemDetail = ProblemDetail.forStatus(httpStatus);
        problemDetail.setTitle(httpStatus.getReasonPhrase());
        problemDetail.setDetail(message != null ? message : httpStatus.getReasonPhrase());
        return problemDetail;
    }
}
