package ru.job4j.chat.domains;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "tz_chatrooms")
public class ChatRoom {

    @Id
    @SequenceGenerator(
            name = "roomsIdSeq",
            sequenceName = "tz_chatrooms_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roomsIdSeq")
    @Min(value = 1, message = "Id должен быть больше нуля", groups = {Operations.OnUpdate.class})
    private int id;
    @NotNull @NotBlank
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChatRoom patch(ChatRoom other) {
        if (id == 0) {
            throw new IllegalStateException("Эта операция неприменима к новым объектам!");
        }
        if (other.name != null) {
            name = other.name;
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChatRoom chatRoom = (ChatRoom) o;
        return id == chatRoom.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}