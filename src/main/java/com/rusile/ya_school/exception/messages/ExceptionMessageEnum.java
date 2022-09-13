package com.rusile.ya_school.exception.messages;

public enum ExceptionMessageEnum {
    BAD_ID_LENGTH("1"),
    CAST_NOT_ALLOWED("2"),
    PARENT_NOT_EXISTS("3"),
    URL_NULL("4"),
    URL_NOT_NULL("5"),
    URL_SIZE("6"),
    FOLDER_SIZE("7"),
    FILE_SIZE("8"),
    ID_NOT_UNIQUE("9"),
    ELEMENT_NOT_FOUND(""),
    PARENT_FILE("");


    ExceptionMessageEnum(String message) {
        this.message = message;
    }

    private final String message;

    public String getMessage() {
        return message;
    }
}
