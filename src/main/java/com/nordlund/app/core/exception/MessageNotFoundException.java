package com.nordlund.app.core.exception;

import org.springframework.lang.Nullable;

public class MessageNotFoundException extends Exception {
    public MessageNotFoundException(@Nullable final String message) {
        super(message);
    }
}
