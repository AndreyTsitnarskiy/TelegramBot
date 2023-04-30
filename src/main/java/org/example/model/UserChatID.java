package org.example.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "user_chat_id")
public class UserChatID {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "chat_id")
    private long chatId;

    public UserChatID(long chatId) {
        this.chatId = chatId;
    }

    public UserChatID() {
    }
}
