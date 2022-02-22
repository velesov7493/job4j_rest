package ru.job4j.chat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.chat.domains.ChatMessage;

import java.util.List;

@Repository
public interface MessagesRepository extends JpaRepository<ChatMessage, Long> {

    @Query(value =
        "FROM ChatMessage m "
        + "WHERE m.roomId = ?1"
    )
    List<ChatMessage> findAllByRoomId(int chatRoomId);
}
