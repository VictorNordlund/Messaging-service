package com.nordlund.app.core.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Message {

    @Nullable
    private final Long id;

    @NotNull
    private final String messageContent;

    @NotNull
    private final String userId;

    public Message(@Nullable final Long id, @NotNull final String messageContent, @NotNull final String userId) {
        this.id = id;
        this.messageContent = messageContent;
        this.userId = userId;
    }

    @Nullable
    public Long getId() {
        return id;
    }

    @NotNull
    public String getMessageContent() {
        return messageContent;
    }

    @NotNull
    public String getUserId() {
        return userId;
    }
}
