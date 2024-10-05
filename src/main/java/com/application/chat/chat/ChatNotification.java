package com.application.chat.chat;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class ChatNotification {
    private Long id; // Change from String to Long
    private String senderId; // ID of the sender
    private String recipientId; // ID of the recipient
    private String content; // Content of the chat message

    // Constructor for just the message ID
    public ChatNotification(Long id) { // Change to Long
        this.id = id;
    }

    // Full constructor for message notification
    public ChatNotification(Long id, String senderId, String recipientId, String content) { // Change to Long
        this.id = id;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.content = content;
    }
}
