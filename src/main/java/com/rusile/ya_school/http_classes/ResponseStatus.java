package com.rusile.ya_school.http_classes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ResponseStatus {
    @Getter
    private final int code;
    @Getter
    private final String message;
}
