package org.example;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ParseFile {

    private static final Logger logger = LogManager.getLogger("phrase");

    public List<String> parseFile(File file) {
        List<String> resultParseFile = new ArrayList<>();
        try {
            if (!file.exists()) {
                logger.info("Файл не существует");
                return resultParseFile;
            }
            BufferedReader br = new BufferedReader(new java.io.FileReader(file));
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                resultParseFile.add(line);
                logger.info(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Ошибка при открытии файла");
            logger.error(e.getMessage());
        }
        return resultParseFile;
    }

    public List<String> collectionToFinalList(File file) {
        List<String> list = parseFile(file);
        List<String> phrasesAuthor = returnAuthor(list);
        List<String> phrases = returnPhrases(list);
        List<String> phrasesAuthorAndPhrase = new ArrayList<>();
        for (int i = 0; i < phrasesAuthor.size(); i++) {
            phrasesAuthorAndPhrase.add(phrasesAuthor.get(i) + "\n\n" + phrases.get(i));
        }
        return phrasesAuthorAndPhrase;
    }

    public List<String> returnPhrases(List<String> list) {
        List<String> phrase = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i % 2 != 0) {
                phrase.add(list.get(i));
            }
        }
        return phrase;
    }

    public List<String> returnAuthor(List<String> list) {
        List<String> author = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i % 2 == 0) {
                author.add(list.get(i));
            }
        }
        return author;
    }
}
