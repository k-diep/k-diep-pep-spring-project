package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    // 1: process new User registrations.
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        // Null Username
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        // Null password
        if (account.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        // Length of password too small
        if (account.getPassword().length() < 4) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        Account newAccount = accountService.register(account);
        // If username exists
        if (newAccount == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        return ResponseEntity.ok(newAccount);
    }

    //2: process User logins.
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account loginAccount = accountService.login(account);
        if (loginAccount == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(loginAccount);
    }

    //3: process creation of new messages.
    @PostMapping("/messages")
    public ResponseEntity<Message> postMessage(@RequestBody Message message) {
        // Text of the message is null 
        if (message.getMessageText() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Text of the message is blank 
        if (message.getMessageText().isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Length of Text of the message is greater or equal than 255 
        if (message.getMessageText().length() >= 255){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // PostedBy refers to a real, existing user
        if (!accountService.existsById(message.getPostedBy())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        Message newMessage = messageService.createMessage(message);
        return ResponseEntity.ok(newMessage);
    }

    //4: retrieve all messages
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        if (messages.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.ok(messages);
    }

    //5. retrieve a message by its ID
    @GetMapping("/messages/{messageId}") 
    public ResponseEntity<Message> getMessageById(@PathVariable int messageId) {
        Message message = messageService.getMessageById(messageId);
        if (message == null) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.ok(message);
    }

    //6. delete message by message ID.
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer messageId) {
        int rowsDeleted = messageService.deleteMessagebyId(messageId);
        if (rowsDeleted == 0){
            return ResponseEntity.status(HttpStatus.OK).body(null);
        
        }
        return ResponseEntity.status(HttpStatus.OK).body(rowsDeleted);
        
    }

    //7. update message text by message ID
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessageById(@PathVariable int messageId, @RequestBody Message message) {
        String newText = message.getMessageText();

        // Null message text
        if (newText == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        // Blank message text
        if (newText.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Too large message text
        if (newText.length() >= 255) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        int rowsUpdated = messageService.updateMessageById(messageId, newText);

        if (rowsUpdated == 1){
            return ResponseEntity.status(HttpStatus.OK).body(1);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    //8. retrieve all messages by a particular user
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable int accountId) {
        List<Message> messages = messageService.getAllMessagesById(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }


}
