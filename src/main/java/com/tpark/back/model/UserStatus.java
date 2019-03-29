package com.tpark.back.model;

public enum UserStatus {
    ACCESS_ERROR,
    NOT_FOUND,
    ALREADY_AUTHENTICATED,
    SUCCESSFULLY_CREATED,
    NOT_UNIQUE_FIELDS_IN_REQUEST,
    EMPTY_FIELDS_IN_REQUEST,
    WRONG_CREDENTIALS,
    SUCCESSFULLY_AUTHED,
    SUCCESSFULLY_CHANGED,
    SUCCESSFULLY_LOGGED_OUT,
    ALREADY_EXISTS
}
