package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.demo.models.Message;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.MessageService;
import com.example.demo.services.UserService;

@Controller
@RequestMapping("/messages")
public class MessageController {
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    
    // Método para obter a lista de usuários (API)
    @GetMapping("/api/users")
    @ResponseBody
    public List<User> getUsers() {
        // Obter todos os usuários, exceto o usuário atual
        User currentUser = userService.getCurrentUser();
        List<User> allUsers = userService.getAllUsers();
        allUsers.removeIf(user -> user.getId().equals(currentUser.getId()));
        return allUsers;
    }
    
    // Método para carregar a tela de mensagens
    @GetMapping
    public String getMessagesDashboard(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {
        
        User currentUser = userService.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<User> allUsers = userService.getAllUsers();
        allUsers.removeIf(user -> user.getId().equals(currentUser.getId()));
        
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", allUsers);
        
        return "messages";
    }
    
    // Método para carregar a conversa
    @GetMapping("/conversation/{userId}")
    public String getConversation(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long userId,
            Model model) throws Exception {
        
        User currentUser = userService.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        User otherUser = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Message> messages = messageService.getConversation(currentUser, otherUser);
        
        List<Map<String, Object>> displayMessages = new ArrayList<>();
        for (Message message : messages) {
            String decryptedContent = messageService.decryptMessage(message, currentUser);
            
            displayMessages.add(Map.of(
                "id", message.getId(),
                "content", decryptedContent,
                "timestamp", message.getTimestamp(),
                "isFromCurrentUser", message.getSender().getId().equals(currentUser.getId())
            ));
        }
        
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("otherUser", otherUser);
        model.addAttribute("messages", displayMessages);
        
        return "conversation";
    }
    
    // Método para enviar mensagem
    @PostMapping("/send")
    public String sendMessage(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long recipientId,
            @RequestParam String content) throws Exception {
        
        User sender = userService.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        User recipient = userRepository.findById(recipientId)
            .orElseThrow(() -> new RuntimeException("Recipient not found"));
        
        messageService.saveMessage(sender, recipient, content);
        
        return "redirect:/messages/conversation/" + recipientId;
    }
}
