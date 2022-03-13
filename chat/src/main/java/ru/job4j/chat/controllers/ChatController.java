package ru.job4j.chat.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.domains.Operations;
import ru.job4j.chat.exceptions.AccessDeniedException;
import ru.job4j.chat.exceptions.ObjectNotFoundException;
import ru.job4j.chat.exceptions.OperationNotAcceptableException;
import ru.job4j.chat.domains.Account;
import ru.job4j.chat.domains.ChatMessage;
import ru.job4j.chat.domains.ChatRoom;
import ru.job4j.chat.services.AccountsApi;
import ru.job4j.chat.services.ChatService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ChatController extends JwtAuthorizationController {

    private final ChatService chats;

    public ChatController(ChatService chats, AccountsApi accounts) {
        super(accounts);
        this.chats = chats;
    }

    @PostMapping("/chatrooms")
    @Validated(Operations.OnCreate.class)
    public ResponseEntity<ChatRoom> createChat(
            @RequestHeader(name = AccountsApi.AUTH_HEADER_NAME, required = false) String token,
            @Valid @RequestBody ChatRoom room
    ) throws AccessDeniedException, OperationNotAcceptableException {

        authorize(token);
        ChatRoom result = chats.saveChat(room);
        if (result == null) {
            throw new OperationNotAcceptableException();
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/chatrooms")
    @Validated(Operations.OnUpdate.class)
    public ResponseEntity<Void> updateChat(
            @RequestHeader(name = AccountsApi.AUTH_HEADER_NAME, required = false) String token,
            @Valid @RequestBody ChatRoom room
    ) throws AccessDeniedException, OperationNotAcceptableException {

        authorizeIf(token, (a) ->
            a.getAuthorityNames().contains("ROLE_ADMIN")
            || a.getAuthorityNames().contains("ROLE_STAFF")
        );
        ChatRoom result = chats.saveChat(room);
        if (result == null) {
            throw new OperationNotAcceptableException();
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/chatrooms")
    @Validated(Operations.OnUpdate.class)
    public ResponseEntity<Void> patchChat(
            @RequestHeader(name = AccountsApi.AUTH_HEADER_NAME, required = false) String token,
            @Valid @RequestBody ChatRoom room
    ) throws AccessDeniedException, OperationNotAcceptableException, ObjectNotFoundException {

        authorizeIf(token, (a) ->
            a.getAuthorityNames().contains("ROLE_ADMIN")
            || a.getAuthorityNames().contains("ROLE_STAFF")
        );
        ChatRoom result = chats.patchChat(room);
        if (result == null) {
            throw new OperationNotAcceptableException();
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
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
    public ResponseEntity<ChatRoom> getChatById(@PathVariable("id") int chatId)
        throws ObjectNotFoundException {

        ChatRoom result = chats.findChatById(chatId);
        if (result == null) {
            throw new ObjectNotFoundException();
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/chatroom/{id}")
    public ResponseEntity<Void> deleteChat(
            @RequestHeader(name = AccountsApi.AUTH_HEADER_NAME, required = false) String token,
            @PathVariable(name = "id") int chatId
    ) throws AccessDeniedException, ObjectNotFoundException {

        authorizeIf(token, (a) -> a.getAuthorityNames().contains("ROLE_ADMIN"));
        if (!chats.deleteChatById(chatId)) {
            throw new ObjectNotFoundException();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/chatroom/{id}/messages")
    public ResponseEntity<List<ChatMessage.Model>> getChatMessages(
            @RequestHeader(name = AccountsApi.AUTH_HEADER_NAME, required = false) String token,
            @PathVariable("id") int chatId
    ) throws AccessDeniedException {

        authorize(token);
        List<ChatMessage.Model> list = chats.findAllMessagesByRoomId(chatId);
        return
                list == null || list.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/messages")
    @Validated(Operations.OnCreate.class)
    public ResponseEntity<ChatMessage.Model> createMessage(
            @RequestHeader(name = AccountsApi.AUTH_HEADER_NAME, required = false) String token,
            @Valid @RequestBody ChatMessage message
    ) throws AccessDeniedException, OperationNotAcceptableException {

        Account acc = authorize(token);
        message.setAuthorId(acc.getId());
        ChatMessage.Model result = chats.saveMessage(message);
        if (result == null) {
            throw new OperationNotAcceptableException();
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/messages")
    @Validated(Operations.OnUpdate.class)
    public ResponseEntity<Void> updateMessage(
            @RequestHeader(name = AccountsApi.AUTH_HEADER_NAME, required = false) String token,
            @Valid @RequestBody ChatMessage message
    ) throws AccessDeniedException, OperationNotAcceptableException {

        ChatMessage.Model oldMsg = chats.findMessageById(message.getId());
        Account acc = authorizeIf(token, (a) -> oldMsg.getAuthor().getId() == a.getId());
        message.setAuthorId(acc.getId());
        ChatMessage.Model result = chats.saveMessage(message);
        if (result == null) {
            throw new OperationNotAcceptableException();
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/messages")
    @Validated(Operations.OnUpdate.class)
    public ResponseEntity<ChatMessage> patchMessage(
            @RequestHeader(name = AccountsApi.AUTH_HEADER_NAME, required = false) String token,
            @Valid @RequestBody ChatMessage message
    ) throws AccessDeniedException, OperationNotAcceptableException, ObjectNotFoundException {

        ChatMessage.Model oldMsg = chats.findMessageById(message.getId());
        Account acc = authorizeIf(token, (a) -> oldMsg.getAuthor().getId() == a.getId());
        message.setAuthorId(acc.getId());
        ChatMessage.Model result = chats.patchMessage(message);
        if (result == null) {
            throw new OperationNotAcceptableException();
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/message/{id}")
    public ResponseEntity<Void> deleteMessage(
            @RequestHeader(name = AccountsApi.AUTH_HEADER_NAME, required = false) String token,
            @PathVariable("id") long messageId
    ) throws AccessDeniedException, ObjectNotFoundException {

        authorize(token);
        if (!chats.deleteMessageById(messageId)) {
            throw new ObjectNotFoundException();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}