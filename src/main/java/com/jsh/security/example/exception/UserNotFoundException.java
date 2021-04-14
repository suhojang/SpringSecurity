package com.jsh.security.example.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String userEmail){
        super(userEmail + " NotFoundException");
    }
}
