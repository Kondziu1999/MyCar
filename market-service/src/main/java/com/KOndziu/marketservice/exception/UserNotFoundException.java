package com.KOndziu.marketservice.exception;

public class UserNotFoundException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE_ID= "User with given id cannot be found";
    private static final String EXCEPTION_MESSAGE_NAME_SURNAME= "User with given identities cannot be found";
    public UserNotFoundException(Integer id) {

        super(EXCEPTION_MESSAGE_ID+id);
    }

}
