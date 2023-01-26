package com.nordlund.app.service;

import com.nordlund.app.core.MessageHandler;
import com.nordlund.app.core.exception.MessageNotFoundException;
import com.nordlund.app.core.exception.UserNotAuthorizedException;
import com.nordlund.app.service.model.JsonMessage;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest controller responsible for message handling
 */
@RestController
@RequestMapping("/messaging-service/messages")
public class MessagingController {

    @NotNull
    private final MessageHandler messageHandler;

    @Autowired
    public MessagingController(@NotNull final MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    /**
     * To add a new message
     *
     * @param message The request body of the message
     * @return The created message
     */
    @PostMapping()
    public JsonMessage addMessage(@NotNull @RequestBody final JsonMessage message) {
        return JsonMessage.fromCore(messageHandler.addMessage(message.toCore()));
    }

    /**
     * Updates an existing message
     *
     * @param messageId The id of the message to update
     * @param message   The request body containing the new message
     * @return The updated message
     * @throws MessageNotFoundException   If message to update is not found
     * @throws UserNotAuthorizedException If user is not authorized to update the message
     */
    @PutMapping("{messageId}")
    public JsonMessage updateMessage(@NotNull @PathVariable final Long messageId,
                                     @NotNull @RequestBody final JsonMessage message) throws MessageNotFoundException, UserNotAuthorizedException {
        return JsonMessage.fromCore(messageHandler.updateMessage(message.setId(messageId).toCore()));
    }

    /**
     * Deletes a message
     *
     * @param messageId The id of the message to delete
     * @param userId    The userId
     * @throws UserNotAuthorizedException
     */
    @DeleteMapping("{messageId}/{userId}")
    public void deleteMessage(@NotNull @PathVariable final Long messageId,
                              @NotNull @PathVariable final String userId) throws UserNotAuthorizedException {
        messageHandler.deleteMessage(messageId, userId);
    }

    /**
     * Gets all messages
     *
     * @return All messages
     */
    @GetMapping()
    public List<JsonMessage> getMessages() {
        return messageHandler.getMessages().stream().map(JsonMessage::fromCore).toList();
    }
}
