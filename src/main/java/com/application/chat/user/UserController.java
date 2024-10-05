package com.application.chat.user;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public User addUser(@Payload User user) {
        logger.info("Adding user: {}", user);
        userService.saveUser(user);
        logger.info("User added successfully: {}", user);
        return user;
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public User disconnectUser(@Payload User user) {
        logger.info("Disconnecting user: {}", user);
        var storedUser = userService.findByNickName(user.getNickName());
        if (storedUser != null) {
            userService.disconnect(storedUser);
            logger.info("User disconnected successfully: {}", storedUser);
            return storedUser;
        }
        logger.warn("User not found for disconnection: {}", user.getNickName());
        return user; // Return the original payload if no user is found
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> findConnectedUsers() {
        logger.info("Fetching connected users");
        List<User> connectedUsers = userService.findConnectedUsers();
        logger.info("Connected users fetched successfully, count: {}", connectedUsers.size());
        return ResponseEntity.ok(connectedUsers);
    }
}
