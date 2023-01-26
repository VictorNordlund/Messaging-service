package com.nordlund.app.core;

import com.nordlund.app.core.exception.MessageNotFoundException;
import com.nordlund.app.core.exception.UserNotAuthorizedException;
import com.nordlund.app.core.model.ErrorMessages;
import com.nordlund.app.core.model.Message;
import com.nordlund.app.persistence.MessagePersistence;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MessageHandler {

    @NotNull
    private final MessagePersistence messagePersistence;

    @Autowired
    public MessageHandler(@NotNull final MessagePersistence messagePersistence) {
        this.messagePersistence = messagePersistence;
    }

    /**
     * Adds a new message
     *
     * @param message to add
     * @return the added message
     */
    @NotNull
    public Message addMessage(@NotNull final Message message) {
        return messagePersistence.addMessage(message);
    }

    /**
     * Updates a message, within a transaction
     *
     * @param message the message to update
     * @return the updated message
     * @throws MessageNotFoundException   If the message to update was not found
     * @throws UserNotAuthorizedException If the user is not authorized to update the message
     */
    @NotNull
    @Transactional
    public Message updateMessage(@NotNull final Message message) throws MessageNotFoundException, UserNotAuthorizedException {
        Optional<Message> existingMessageOpt = messagePersistence.getMessage(Objects.requireNonNull(message.getId()));
        if (existingMessageOpt.isEmpty()) {
            throw new MessageNotFoundException(String.format(ErrorMessages.MESSAGE_NOT_FOUND, message.getId()));
        }
        Message existingMessage = existingMessageOpt.get();
        if (!existingMessage.getUserId().equals(message.getUserId())) {
            throw new UserNotAuthorizedException(ErrorMessages.USER_NOT_AUTHORIZED);
        }
        return messagePersistence.updateMessage(message);
    }

    /**
     * Deletes a message, within a transaction
     *
     * @param messageId the id of the message to delete
     * @param userId    the id of the user
     * @throws UserNotAuthorizedException if the user not authorized to update the message
     */
    @Transactional
    public void deleteMessage(@NotNull final Long messageId, @NotNull final String userId) throws UserNotAuthorizedException {
        Optional<Message> existingMessageOpt = messagePersistence.getMessage(messageId);
        if (existingMessageOpt.isEmpty()) {
            return;
        }
        if (!existingMessageOpt.get().getUserId().equals(userId)) {
            throw new UserNotAuthorizedException(ErrorMessages.USER_NOT_AUTHORIZED);
        }
        messagePersistence.deleteMessage(messageId);
    }

    /**
     * Gets all messages
     *
     * @return the messages
     */
    @NotNull
    public List<Message> getMessages() {
        return messagePersistence.getMessages();
    }
}
