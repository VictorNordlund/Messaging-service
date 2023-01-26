package com.nordlund.app.persistence.model;

import com.nordlund.app.core.model.Message;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

/**
 * The entity representing the message
 */
@Entity
@Table(name = "MESSAGE")
public class JpaMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "MESSAGE_CONTENT", length = 250, nullable = false)
    private String messageContent;

    @Column(name = "USER_ID", length = 15, nullable = false)
    private String userId;

    public JpaMessage() {
        //For hibernate
    }

    @NotNull
    public static JpaMessage fromCore(@NotNull final Message message) {
        JpaMessage result = new JpaMessage();
        result.messageContent = message.getMessageContent();
        result.userId = message.getUserId();
        return result;
    }

    @NotNull
    public Message toCore() {
        return new Message(id, messageContent, userId);
    }

    @NotNull
    public JpaMessage updateMessage(@NotNull final String newMessageContent) {
        this.messageContent = newMessageContent;
        return this;
    }

}
