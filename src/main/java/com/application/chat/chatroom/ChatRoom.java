package com.application.chat.chatroom;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "chat_rooms") // Optional: specify table name if not using the default naming convention
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Automatically generate IDs
    private Long id; // Changed to Long for MySQL compatibility

    private String chatId;
    private String senderId;
    private String recipientId;
}
