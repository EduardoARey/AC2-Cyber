package com.example.demo.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Message;
import com.example.demo.models.User;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByRecipientOrderByTimestampDesc(User recipient);
    List<Message> findBySenderOrderByTimestampDesc(User sender);
    List<Message> findBySenderAndRecipientOrderByTimestampDesc(User sender, User recipient);
}
