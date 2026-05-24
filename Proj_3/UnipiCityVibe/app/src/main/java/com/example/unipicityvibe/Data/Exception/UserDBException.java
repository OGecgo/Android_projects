package com.example.unipicityvibe.Data.Exception;

public final class UserDBException {
    // Validation errors
    public static final String EMPTY_EMAIL = "EMPTY_EMAIL";
    public static final String EMPTY_UID = "EMPTY_UID";
    public static final String EMAIL_VALIDATION_ERROR = "EMAIL_VALIDATION_ERROR";
    public static final String NAME_EMPTY = "NAME_EMPTY";
    public static final String LASTNAME_EMPTY = "LASTNAME_EMPTY";

    // User operations
    public static final String EMPTY_USER = "EMPTY_USER";
    public static final String ERROR_GET_USER = "ERROR_GET_USER";
    public static final String ERROR_USER_CREATE = "ERROR_USER_CREATE";
    public static final String ERROR_USER_DELETE = "ERROR_USER_DELETE";
    public static final String USER_NOT_EXIST = "USER_NOT_EXIST";


    private UserDBException() {}
}
