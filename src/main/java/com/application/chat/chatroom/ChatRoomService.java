package com.application.chat.chatroom;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private static final Logger logger = LoggerFactory.getLogger(ChatRoomService.class);

    public Optional<String> getChatRoomId(
            String senderId,
            String recipientId,
            boolean createNewRoomIfNotExists
    ) {
        logger.info("Attempting to retrieve chat room ID for sender: {} and recipient: {}", senderId, recipientId);

        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(chatRoom -> {
                    logger.info("Chat room found with ID: {}", chatRoom.getChatId());
                    return chatRoom.getChatId();
                })
                .or(() -> {
                    logger.info("No chat room found for sender: {} and recipient: {}", senderId, recipientId);
                    if(createNewRoomIfNotExists) {
                        var chatId = createChatId(senderId, recipientId);
                        logger.info("New chat room created with ID: {}", chatId);
                        return Optional.of(chatId);
                    }
                    logger.warn("New chat room creation is disabled, returning empty.");
                    return Optional.empty();
                });
    }

    private String createChatId(String senderId, String recipientId) {
        var chatId = String.format("%s_%s", senderId, recipientId);

        ChatRoom senderRecipient = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        ChatRoom recipientSender = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();

        chatRoomRepository.save(senderRecipient);
        logger.info("Saved chat room for sender: {} and recipient: {}", senderId, recipientId);

        chatRoomRepository.save(recipientSender);
        logger.info("Saved chat room for recipient: {} and sender: {}", recipientId, senderId);

        return chatId;
    }
}
