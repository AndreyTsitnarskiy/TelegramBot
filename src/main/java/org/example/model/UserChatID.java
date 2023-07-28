package org.example.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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

    @ElementCollection
    @CollectionTable(name = "user_chat_id_phrase_ids", joinColumns = @JoinColumn(name = "user_chat_id_id"))
    @Column(name = "phrase_id")
    private List<Integer> phraseIdList;

    public UserChatID(long chatId, List<Integer> phraseIdList) {
        this.chatId = chatId;
        this.phraseIdList = phraseIdList;
    }
}
