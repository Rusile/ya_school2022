package com.rusile.ya_school.http_classes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class Error {

    @Getter
    @Setter
    private int code;

    @Getter
    @Setter
    private String message;
}
