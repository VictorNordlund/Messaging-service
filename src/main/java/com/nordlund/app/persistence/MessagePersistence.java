package com.nordlund.app.persistence;

import com.nordlund.app.core.exception.MessageNotFoundException;
import com.nordlund.app.core.model.ErrorMessages;
import com.nordlund.app.core.model.Message;
import com.nordlund.app.persistence.model.JpaMessage;
import com.nordlund.app.persistence.repository.JpaMessageRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * Handles the persistence logic of the application
 */
@Service
public class MessagePersistence {

    @NotNull
    private final JpaMessageRepository messageRepository;

    @Autowired
    public MessagePersistence(@NotNull final JpaMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Adds a message to the database
     *
     * @param message The message to add
     * @return The added message
     */
    @NotNull
    public Message addMessage(@NotNull final Message message) {
        return messageRepository.save(JpaMessage.fromCore(message)).toCore();
    }

    /**
     * Updates a message in the database
     *
     * @param message The message to update
     * @return The updated message
     * MessageNotFoundException if the message was not found
     */
    @NotNull
    public Message updateMessage(@NotNull final Message message) throws MessageNotFoundException {
        Optional<JpaMessage> existingMessage = messageRepository.findById(Objects.requireNonNull(message.getId()));
        if (existingMessage.isEmpty()) {
            throw new MessageNotFoundException(String.format(ErrorMessages.MESSAGE_NOT_FOUND, message.getId()));
        }
        return messageRepository.save(existingMessage.get().updateMessage(message.getMessageContent())).toCore();
    }

    /**
     * Gets a specific message
     *
     * @param id of the message
     * @return the message
     */
    @NotNull
    public Optional<Message> getMessage(@NotNull final Long id) {
        return messageRepository.findById(id).map(JpaMessage::toCore);
    }

    /**
     * Gets all messages
     *
     * @return a list of messages
     */
    @NotNull
    public List<Message> getMessages() {
        return StreamSupport.stream(messageRepository.findAll().spliterator(), false).map(JpaMessage::toCore)
                .toList();
    }

    /**
     * Deletes a message
     *
     * @param id of the messade to delete
     */
    public void deleteMessage(@NotNull final Long id) {
        messageRepository.deleteById(id);
    }
}
