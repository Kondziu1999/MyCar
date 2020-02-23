package com.KOndziu.usercarservice.exceptions;


import com.KOndziu.usercarservice.modules.User;
import com.KOndziu.usercarservice.payload.UserDTO;

public class UserAlreadyExists extends RuntimeException {
    private static final String ERROR_MESSAGE="User already exists ";
    public UserAlreadyExists(User user) {
        super(ERROR_MESSAGE+":name "+user.getName()+"surname "+user.getSecondName());
    }
    public UserAlreadyExists(UserDTO user) {
        super(ERROR_MESSAGE+":name "+user.getName()+", surname "+user.getSecondName());
    }
}
