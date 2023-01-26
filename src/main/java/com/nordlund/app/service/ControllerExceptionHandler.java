package com.nordlund.app.service;

import com.nordlund.app.core.exception.MessageNotFoundException;
import com.nordlund.app.core.exception.UserNotAuthorizedException;
import com.nordlund.app.service.model.ErrorResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    /**
     * Handles the case of an exception
     *
     * @param e The exception
     * @return A standard error response with appropriate HTTP code
     */
    @ExceptionHandler(value = MessageNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMessageAlreadyExists(@NotNull final MessageNotFoundException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles the case of an exception
     *
     * @param e The exception
     * @return A standard error response with appropriate HTTP code
     */
    @ExceptionHandler(value = UserNotAuthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUserNotAuthorized(@NotNull final UserNotAuthorizedException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}
