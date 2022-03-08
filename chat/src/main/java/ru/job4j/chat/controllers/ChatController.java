package ru.job4j.chat.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.domains.Account;
import ru.job4j.chat.domains.ChatMessage;
import ru.job4j.chat.domains.ChatRoom;
import ru.job4j.chat.services.AccountsApi;
import ru.job4j.chat.services.ChatService;

import java.util.List;

@RestController
public class ChatController {

    private final ChatService chats;
    private final AccountsApi accounts;

    public ChatController(ChatService chats, AccountsApi accounts) {
        this.chats = chats;
        this.accounts = accounts;
    }

    @PostMapping("/chatrooms")
    public ResponseEntity<ChatRoom> createChat(
            @RequestHeader(name = AccountsApi.AUTH_HEADER_NAME, required = false) String token,
            @RequestBody ChatRoom room
    ) {
        Account acc = accounts.getAccountByToken(token);
        if (acc == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ChatRoom result = chats.saveChat(room);
        return
                result == null
                ? new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE)
                : new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/chatrooms")
    public ResponseEntity<Void> updateChat(
            @RequestHeader(name = AccountsApi.AUTH_HEADER_NAME, required = false) String token,
            @RequestBody ChatRoom room
    ) {
        Account acc = accounts.getAccountByToken(token);
        if (
                acc == null || room.getId() != 0
                && !acc.getAuthorityNames().contains("ROLE_ADMIN")
                && !acc.getAuthorityNames().contains("ROLE_STAFF")
        ) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        ChatRoom result = chats.saveChat(room);
        return
                result == null
                ? new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE)
                : new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/chatrooms")
    public ResponseEntity<List<ChatRoom>> getAllChats() {
        List<ChatRoom> list = chats.findAllChats();
        return
                list == null || list.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/chatroom/{id}")
    public ResponseEntity<ChatRoom> getChatById(@PathVariable("id") int chatId) {
        ChatRoom result = chats.findChatById(chatId);
        return
                result == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/chatroom/{id}")
    public ResponseEntity<Void> deleteChat(
            @RequestHeader(name = AccountsApi.AUTH_HEADER_NAME, required = false) String token,
            @PathVariable(name = "id") int chatId
    ) {
        Account acc = accounts.getAccountByToken(token);
        if (acc == null || !acc.getAuthorityNames().contains("ROLE_ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return
                chats.deleteChatById(chatId)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/chatroom/{id}/messages")
    public ResponseEntity<List<ChatMessage.Model>> getChatMessages(
            @RequestHeader(name = AccountsApi.AUTH_HEADER_NAME, required = false) String token,
            @PathVariable("id") int chatId
    ) {
        Account acc = accounts.getAccountByToken(token);
        if (acc == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<ChatMessage.Model> list = chats.findAllMessagesByRoomId(chatId);
        return
                list == null || list.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/messages")
    public ResponseEntity<ChatMessage.Model> createMessage(
            @RequestHeader(name = AccountsApi.AUTH_HEADER_NAME, required = false) String token,
            @RequestBody ChatMessage message
    ) {
        Account acc = accounts.getAccountByToken(token);
        if (acc == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        message.setAuthorId(acc.getId());
        ChatMessage.Model result = chats.saveMessage(message);
        return
                result == null
                ? new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE)
                : new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/messages")
    public ResponseEntity<Void> updateMessage(
            @RequestHeader(name = AccountsApi.AUTH_HEADER_NAME, required = false) String token,
            @RequestBody ChatMessage message
    ) {
        Account acc = accounts.getAccountByToken(token);
        ChatMessage.Model oldMsg = chats.findMessageById(message.getId());
        if (acc == null || oldMsg.getAuthor().getId() != acc.getId()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        message.setAuthorId(acc.getId());
        ChatMessage.Model result = chats.saveMessage(message);
        return
                result == null
                ? new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE)
                : new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/message/{id}")
    public ResponseEntity<Void> deleteMessage(
            @RequestHeader(name = AccountsApi.AUTH_HEADER_NAME, required = false) String token,
            @PathVariable("id") long messageId
    ) {
        Account acc = accounts.getAccountByToken(token);
        if (acc == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return
                chats.deleteMessageById(messageId)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}