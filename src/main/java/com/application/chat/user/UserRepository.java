package com.application.chat.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find all users by their status (ONLINE/OFFLINE)
    List<User> findAllByStatus(Status status);

    // Find a user by their nickName
    Optional<User> findByNickName(String nickName);
}
