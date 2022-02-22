package ru.job4j.chat.domains;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "tr_chatmessages")
public class ChatMessage {

    public static final class Model {

        private long id;
        private Date time;
        private String text;
        private User.Model author;
        private ChatRoom room;

        public Model(ChatMessage entity) {
            id = entity.id;
            time = entity.time;
            text = entity.text;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public User.Model getAuthor() {
            return author;
        }

        public void setAuthor(User.Model author) {
            this.author = author;
        }

        public ChatRoom getRoom() {
            return room;
        }

        public void setRoom(ChatRoom room) {
            this.room = room;
        }
    }

    @Id
    @SequenceGenerator(
            name = "messagesIdSeq",
            sequenceName = "tr_chatmessages_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messagesIdSeq")
    private long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;
    private String text;
    @Column(name = "id_author")
    private int authorId;
    @Column(name = "id_chat")
    private int roomId;

    public ChatMessage() {
        time = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChatMessage that = (ChatMessage) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}