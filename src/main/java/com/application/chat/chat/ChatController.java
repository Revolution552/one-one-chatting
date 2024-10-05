package com.application.chat.chat;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    // Logger instance
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @MessageMapping("/chat")
    public void processMessage(@Payload com.application.chat.chat.ChatMessage chatMessage) {
        logger.info("Processing message from sender: {} to recipient: {}", chatMessage.getSenderId(), chatMessage.getRecipientId());

        // Save the chat message using the service
        com.application.chat.chat.ChatMessage savedMsg = chatMessageService.save(chatMessage);
        logger.info("Saved message with ID: {}", savedMsg.getId());

        // Notify the recipient of the new message
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), "/queue/messages",
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getSenderId(),
                        savedMsg.getRecipientId(),
                        savedMsg.getContent()
                )
        );
        logger.info("Notified recipient: {} with message ID: {}", chatMessage.getRecipientId(), savedMsg.getId());
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId,
                                                              @PathVariable String recipientId) {
        logger.info("Fetching chat messages between sender: {} and recipient: {}", senderId, recipientId);

        // Retrieve chat messages between sender and recipient
        List<ChatMessage> messages = chatMessageService.findChatMessages(senderId, recipientId);
        logger.info("Retrieved {} messages between {} and {}", messages.size(), senderId, recipientId);

        return ResponseEntity.ok(messages);
    }
}
