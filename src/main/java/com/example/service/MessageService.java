package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.entity.Message;
import com.example.repository.MessageRepository;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;

    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(int messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }

    public int deleteMessagebyId(int messageId){
        // Message exists
        if (messageRepository.existsById(messageId)){
            messageRepository.deleteById(messageId);
            return 1;
        }

        else{
            return 0;
        }
        
    }

    public int updateMessageById(int messageId, String newMessageText){
        // Checks if message exists
        if (messageRepository.existsById(messageId)) {
            Optional<Message> optionalMessage = messageRepository.findById(messageId);
            if (optionalMessage.isPresent()) {
                Message newMessage = optionalMessage.get();
                newMessage.setMessageText(newMessageText);
                messageRepository.save(newMessage);
                return 1;
            }
        }
        return 0;
    }

    public List<Message> getAllMessagesById(int accountId){
        return messageRepository.findByPostedBy(accountId);
    }



}
