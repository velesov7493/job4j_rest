package ru.job4j.chat.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.domains.ChatMessage;
import ru.job4j.chat.domains.ChatRoom;
import ru.job4j.chat.services.ChatService;

import java.util.List;

@RestController
public class ChatController {

    private final ChatService chats;

    public ChatController(ChatService service) {
        chats = service;
    }

    @PostMapping("/chatrooms")
    public ResponseEntity<ChatRoom> createChat(@RequestBody ChatRoom room) {
        ChatRoom result = chats.saveChat(room);
        return
                result == null
                ? new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE)
                : new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/chatrooms")
    public ResponseEntity<Void> updateChat(@RequestBody ChatRoom room) {
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
    public ResponseEntity<Void> deleteChat(@PathVariable(name = "id") int chatId) {
        return
                chats.deleteChatById(chatId)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/chatroom/{id}/messages")
    public ResponseEntity<List<ChatMessage.Model>> getChatMessages(
            @PathVariable("id") int chatId
    ) {
        List<ChatMessage.Model> list = chats.findAllMessagesByRoomId(chatId);
        return
                list == null || list.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/messages")
    public ResponseEntity<ChatMessage.Model> createMessage(@RequestBody ChatMessage message) {
        ChatMessage.Model result = chats.saveMessage(message);
        return
                result == null
                ? new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE)
                : new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/messages")
    public ResponseEntity<Void> updateMessage(@RequestBody ChatMessage message) {
        ChatMessage.Model result = chats.saveMessage(message);
        return
                result == null
                ? new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE)
                : new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/message/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable("id") long messageId) {
        return
                chats.deleteMessageById(messageId)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}