package com.nordlund.app.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nordlund.app.core.model.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Java POJO of expected JSON
 */
public class JsonMessage {

    @JsonProperty
    @Nullable
    private Long id;

    @JsonProperty
    @NotNull
    private String messageContent;

    @JsonProperty
    @NotNull
    private String userId;

    public JsonMessage(@Nullable final Long id, @NotNull final String messageContent, @NotNull final String userId) {
        this.id = id;
        this.messageContent = messageContent;
        this.userId = userId;
    }

    @NotNull
    public static JsonMessage fromCore(@NotNull final Message message) {
        return new JsonMessage(message.getId(), message.getMessageContent(), message.getUserId());
    }

    @NotNull
    public String getMessageContent() {
        return messageContent;
    }

    @Nullable
    public Long getId() {
        return id;
    }

    @NotNull
    public JsonMessage setId(@Nullable final Long id) {
        this.id = id;
        return this;
    }

    @NotNull
    public String getUserId() {
        return userId;
    }

    @NotNull
    public Message toCore() {
        return new Message(id, messageContent, userId);
    }
}
