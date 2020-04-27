package eu.kalodiodev.controlmycar.web.controllers;

import eu.kalodiodev.controlmycar.web.model.ValidationErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class MvcExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationErrorDto>> validationErrorHandler(MethodArgumentNotValidException e) {
        List<ValidationErrorDto> errorsList = new ArrayList<>(e.getBindingResult().getAllErrors().size());

        e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> ((FieldError) error))
                .forEach(error -> {
                    ValidationErrorDto errorDto = ValidationErrorDto.builder()
                            .field(error.getField())
                            .message(error.getDefaultMessage())
                            .build();

                    errorsList.add(errorDto);
                });

        return new ResponseEntity<>(errorsList, HttpStatus.BAD_REQUEST);
    }
}
