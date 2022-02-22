package ru.job4j.chat.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.job4j.chat.domains.ChatMessage;
import ru.job4j.chat.domains.ChatRoom;
import ru.job4j.chat.repositories.MessagesRepository;
import ru.job4j.chat.repositories.RoomsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    private static final Logger LOG = LoggerFactory.getLogger(ChatService.class);

    private final RoomsRepository rooms;
    private final MessagesRepository messages;
    private final UsersService users;

    public ChatService(RoomsRepository repo1, MessagesRepository repo2, UsersService service1) {
        rooms = repo1;
        messages = repo2;
        users = service1;
    }

    private ChatMessage.Model fillMessageModel(ChatMessage value) {
        ChatMessage.Model m = new ChatMessage.Model(value);
        m.setAuthor(users.findUserById(value.getAuthorId()));
        m.setRoom(findChatById(value.getRoomId()));
        return m;
    }

    public List<ChatRoom> findAllChats() {
        return rooms.findAll();
    }

    public ChatRoom findChatById(Integer chatId) {
        return rooms.findById(chatId).orElse(null);
    }

    public ChatRoom saveChat(ChatRoom entity) {
        ChatRoom result = null;
        try {
            result = rooms.save(entity);
        } catch (Throwable ex) {
            LOG.error("Ошибка записи чата: ", ex);
        }
        return result;
    }

    public boolean deleteChatById(Integer chatId) {
        boolean result = false;
        try {
            rooms.deleteById(chatId);
            result = true;
        } catch (Throwable ex) {
            LOG.error("Ошибка удаления чата: ", ex);
        }
        return result;
    }

    public List<ChatMessage.Model> findAllMessagesByRoomId(int chatRoomId) {
        List<ChatMessage> list = messages.findAllByRoomId(chatRoomId);
        List<ChatMessage.Model> result = new ArrayList<>();
        list.forEach((m) -> result.add(fillMessageModel(m)));
        return result;
    }

    public ChatMessage.Model findMessageById(long messageId) {
        Optional<ChatMessage> message = messages.findById(messageId);
        return message.isPresent() ? fillMessageModel(message.get()) : null;
    }

    public ChatMessage.Model saveMessage(ChatMessage entity) {
        ChatMessage.Model result = null;
        try {
            result = fillMessageModel(messages.save(entity));
        } catch (Throwable ex) {
            LOG.error("Ошибка записи сообщения: ", ex);
        }
        return result;
    }

    public boolean deleteMessageById(long messageId) {
        boolean result = false;
        try {
            messages.deleteById(messageId);
            result = true;
        } catch (Throwable ex) {
            LOG.error("Ошибка удаления сообщения: ", ex);
        }
        return result;
    }
}