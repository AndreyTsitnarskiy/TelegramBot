package org.example;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class Storage {

    private Random random = new Random();
    private static ParseFile parseFile = new ParseFile();
    private static List<String> storage = parseFile.collectionToFinalList(new File("data/say.txt"));

    public Storage() {
    }

    public void addStorage(String message) {
        storage.add(message);
    }

    public void removeStorage(String message) {
        storage.remove(message);
    }

    public String getRandomPhrase() {
        return storage.get(random.nextInt(storage.size()));
    }
}
