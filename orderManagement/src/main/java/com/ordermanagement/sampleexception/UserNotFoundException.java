package com.ordermanagement.sampleexception;


public class UserNotFoundException  extends Exception{
    public UserNotFoundException(String message) {
        super(message);
    }
}