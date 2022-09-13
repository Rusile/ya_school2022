package com.rusile.ya_school.exception.messages;

public enum ExceptionMessageEnum {
    BAD_ID_LENGTH("ID must be shorter than 256 symbols"),
    CAST_NOT_ALLOWED("Cast from file to folder or vice versa isn't allowed"),
    PARENT_NOT_EXISTS("Parent doesn't exists"),
    URL_NULL("File's url must be filled"),
    URL_NOT_NULL("Folder's url must be null"),
    URL_SIZE("Url must be shorter than 256 symbols"),
    FOLDER_SIZE("Folder size must be null"),
    FILE_SIZE("File size can't be null or 0"),
    ID_NOT_UNIQUE("IDs of entities in import must be unique"),
    ELEMENT_NOT_FOUND("Element isn't found"),
    PARENT_IS_FILE("File can't be parent"),
    BAD_DATE_INTERVAL("dateStart can't be after dateEnd");

    ExceptionMessageEnum(String message) {
        this.message = message;
    }

    private final String message;

    public String getMessage() {
        return message;
    }
}
