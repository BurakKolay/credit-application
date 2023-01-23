package com.burakkolay.credit.exception;

import lombok.Data;

@Data
public class  UserNotFoundException extends RuntimeException {
    private String details;
    public UserNotFoundException(String entityName, String cause) {
        super("Related " + entityName + " not found with : [" + cause + "]");
    }
}
