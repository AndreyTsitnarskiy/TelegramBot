package org.example.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@Table(name = "phrase")
public class Phrase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "phrase")
    private String phrase;

    @Column(name = "author")
    private String author;

    public Phrase(String phrase, String author) {
        this.phrase = phrase;
        this.author = author;
    }
}
