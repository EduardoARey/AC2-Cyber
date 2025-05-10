package com.example.demo.services;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Message;
import com.example.demo.models.User;
import com.example.demo.repositories.MessageRepository;


@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private CryptoService cryptoService;
    
    public Message saveMessage(User sender, User recipient, String plainContent) throws Exception {
        String iv = cryptoService.generateIV();
        String encryptedContent = cryptoService.encrypt(plainContent, sender.getEncryptionKey(), iv);
        
        Message message = new Message(sender, recipient, encryptedContent, iv);
        return messageRepository.save(message);
    }
    
    public List<Message> getConversation(User user1, User user2) {
        List<Message> messagesSent = messageRepository.findBySenderAndRecipientOrderByTimestampDesc(user1, user2);
        List<Message> messagesReceived = messageRepository.findBySenderAndRecipientOrderByTimestampDesc(user2, user1);
        
        List<Message> allMessages = messagesSent;
        allMessages.addAll(messagesReceived);
        return allMessages.stream()
                .sorted(Comparator.comparing(Message::getTimestamp))
                .collect(Collectors.toList());
    }
    
    public String decryptMessage(Message message, User currentUser) throws Exception {
        // Escolher a chave de criptografia corretamente
        String keyToUse = message.getSender().getId().equals(currentUser.getId()) 
                          ? currentUser.getEncryptionKey() 
                          : message.getSender().getEncryptionKey();

        return cryptoService.decrypt(message.getEncryptedContent(), keyToUse, message.getIv());
    }
    
    public List<User> getRecentContacts(User user) {
        List<Message> sentMessages = messageRepository.findBySenderOrderByTimestampDesc(user);
        List<Message> receivedMessages = messageRepository.findByRecipientOrderByTimestampDesc(user);
        
        return Stream.concat(sentMessages.stream().map(Message::getRecipient), receivedMessages.stream().map(Message::getSender))
                     .distinct()
                     .limit(10)
                     .collect(Collectors.toList());
    }
}
