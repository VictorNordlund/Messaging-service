package com.nordlund.app.core.exception;

import org.springframework.lang.Nullable;

public class UserNotAuthorizedException extends Exception {
    public UserNotAuthorizedException(@Nullable final String message) {
        super(message);
    }
}
