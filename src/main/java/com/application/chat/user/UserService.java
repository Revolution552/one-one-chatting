package com.application.chat.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("ALL")
@Service
@RequiredArgsConstructor
@Slf4j // Add this annotation for logging
public class UserService {

    private final UserRepository repository;

    public void saveUser(User user) {
        user.setStatus(Status.ONLINE);
        repository.save(user);
        log.info("User saved: {}", user); // Log user save action
    }

    public void disconnect(User user) {
        var storedUser = repository.findById(user.getId()).orElse(null);
        if (storedUser != null) {
            storedUser.setStatus(Status.OFFLINE);
            repository.save(storedUser);
            log.info("User disconnected: {}", storedUser); // Log user disconnect action
        } else {
            log.warn("User not found for disconnect: {}", user.getId()); // Log warning if user not found
        }
    }

    public List<User> findConnectedUsers() {
        List<User> connectedUsers = repository.findAllByStatus(Status.ONLINE);
        log.info("Connected users retrieved: {}", connectedUsers.size()); // Log the number of connected users
        return connectedUsers;
    }

    public User findByNickName(String nickName) {
        User foundUser = repository.findByNickName(nickName).orElse(null);
        if (foundUser != null) {
            log.info("User found by nickname '{}': {}", nickName, foundUser); // Log user found by nickname
        } else {
            log.warn("User not found by nickname '{}'", nickName); // Log warning if user not found
        }
        return foundUser;
    }
}
