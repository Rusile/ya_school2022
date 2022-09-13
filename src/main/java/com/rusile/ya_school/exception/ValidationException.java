package com.rusile.ya_school.exception;

import java.util.List;
import java.util.Map;

public class ValidationException extends RuntimeException {
    private final Map<Integer, List<String>> errorsMap;

    public ValidationException(Map<Integer, List<String>> errorsMap) {
        this.errorsMap = errorsMap;
    }

    public Map<Integer, List<String>> getErrorsMap() {
        return errorsMap;
    }
}
