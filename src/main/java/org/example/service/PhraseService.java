package org.example.service;

import org.example.model.Phrase;
import org.example.repository.PhraseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class PhraseService {

    private PhraseRepository phraseRepository;

    @Autowired
    public PhraseService(PhraseRepository phraseRepository) {
        this.phraseRepository = phraseRepository;
    }

    public void addPhrase(String phrase, String author) {
        phraseRepository.save(new Phrase(phrase, author));
    }

    public List<Phrase> getAllPhrases() {
        return phraseRepository.findAll();
    }

    public int getPhraseCount() {
        return (int) phraseRepository.count();
    }

    public Phrase getRandomPhrase() {
        Random random = new Random();
        int rand = random.nextInt(getPhraseCount());
        return phraseRepository.findById(rand).orElse(null);
    }
}
