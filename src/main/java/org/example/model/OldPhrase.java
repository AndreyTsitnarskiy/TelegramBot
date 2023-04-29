package org.example.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "old_phrase")
public class OldPhrase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "phrase", nullable = false, length = 2000)
    private String phrase;

    @Column(name = "author" , nullable = false, length = 2000)
    private String author;

    public OldPhrase(String phrase, String author) {
        this.phrase = phrase;
        this.author = author;
    }

    public OldPhrase() {
    }
}