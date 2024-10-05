package com.application.chat.chat;

import com.application.chat.chatroom.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j // Add this annotation to enable logging
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;

    public ChatMessage save(ChatMessage chatMessage) {
        log.info("Saving chat message from senderId: {} to recipientId: {}", chatMessage.getSenderId(), chatMessage.getRecipientId());

        var chatId = chatRoomService
                .getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
                .orElseThrow(() -> {
                    log.error("Chat room not found for senderId: {} and recipientId: {}", chatMessage.getSenderId(), chatMessage.getRecipientId());
                    return new RuntimeException("Chat room not found"); // You can create your own dedicated exception
                });

        chatMessage.setChatId(chatId);
        repository.save(chatMessage);
        log.info("Chat message saved successfully with chatId: {}", chatId);
        return chatMessage;
    }

    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        log.info("Finding chat messages for senderId: {} and recipientId: {}", senderId, recipientId);

        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false);
        List<ChatMessage> messages = chatId.map(repository::findByChatId).orElse(new ArrayList<>());

        if (messages.isEmpty()) {
            log.warn("No chat messages found for senderId: {} and recipientId: {}", senderId, recipientId);
        } else {
            log.info("Found {} chat messages for senderId: {} and recipientId: {}", messages.size(), senderId, recipientId);
        }

        return messages;
    }
}
