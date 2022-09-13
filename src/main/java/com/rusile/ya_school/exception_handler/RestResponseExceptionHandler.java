package com.rusile.ya_school.exception_handler;

import com.rusile.ya_school.exception.BadSizeIdException;
import com.rusile.ya_school.exception.IllegalDateIntervalException;
import com.rusile.ya_school.exception.NotFoundElementException;
import com.rusile.ya_school.exception.ValidationException;
import com.rusile.ya_school.exception.messages.ExceptionMessageEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.rusile.ya_school.http_classes.Error;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


@ControllerAdvice
public class RestResponseExceptionHandler {

    @ExceptionHandler(value = IllegalDateIntervalException.class)
    protected ResponseEntity<Error> handleImportValidationExc(IllegalDateIntervalException ex) {
        Error error = new Error(HttpStatus.BAD_REQUEST.value(), ExceptionMessageEnum.BAD_DATE_INTERVAL.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    protected ResponseEntity<Error> handleMissingArgExc(MissingServletRequestParameterException ex) {
        Error error = new Error(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Error> handleArgTypeExc(MethodArgumentTypeMismatchException ex) {
        Error error = new Error(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ValidationException.class)
    protected ResponseEntity<Error> handleImportValidationExc(ValidationException ex) {
        StringBuilder stringBuilder = new StringBuilder();
        Map<Integer, List<String>> errorsMap = ex.getErrorsMap();
        stringBuilder.append("{");
        Set<Integer> positions = errorsMap.keySet();


        if (errorsMap.get(0) != null) {
            stringBuilder.append(errorsMap.get(0).get(0)).append("; ");
            errorsMap.remove(0);
        }
        Iterator<Integer> iterator = positions.iterator();
        for (int i = 0; i < positions.size(); i++) {
            Integer position = iterator.next();
            List<String> messages = errorsMap.get(position);
            stringBuilder.append("items[").append(position).append("]: ");

            for (int j = 0; j < messages.size(); j++) {
                if (j != messages.size() - 1) {
                    stringBuilder.append(messages.get(j)).append(", ");
                } else {
                    stringBuilder.append(messages.get(j));
                }
            }
            if (i != positions.size() - 1) {
                stringBuilder.append("; ");
            }
        }
        stringBuilder.append("}");

        Error error = new Error(HttpStatus.BAD_REQUEST.value(), stringBuilder.toString());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(value = NotFoundElementException.class)
    protected ResponseEntity<Error> handleElementNotFoundExc() {
        Error error = new Error(HttpStatus.NOT_FOUND.value(), ExceptionMessageEnum.ELEMENT_NOT_FOUND.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = BadSizeIdException.class)
    protected ResponseEntity<Error> handleBadIdExc() {
        Error error = new Error(HttpStatus.BAD_REQUEST.value(), ExceptionMessageEnum.BAD_ID_LENGTH.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
