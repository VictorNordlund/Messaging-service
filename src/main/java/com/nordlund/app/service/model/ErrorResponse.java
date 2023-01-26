package com.nordlund.app.service.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;

/**
 * Standard error response
 */
public class ErrorResponse {

    @JsonProperty
    private final String message;

    @JsonCreator
    public ErrorResponse(@Nullable final String message) {
        this.message = message == null ? "No message available" : message;
    }

    public @NotNull String getMessage() {
        return message;
    }
}
