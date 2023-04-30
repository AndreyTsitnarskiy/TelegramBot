package org.example.database;

import org.example.model.OldPhrase;
import org.example.model.Phrase;
import org.example.model.UserChatID;
import org.hibernate.Session;
import org.hibernate.Transaction;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class DataBaseOperations {
    private final Session session;
    private long rowCount;

    public DataBaseOperations() {
        session = new Connection().getSessionFactory().openSession();
        rowCount = countRows();
    }

    public String getPhraseAndAuthor(){
        String finalPhrase;
        Transaction transaction = session.beginTransaction();
        List<Integer> allIDs = session.createQuery("select id from Phrase", Integer.class).getResultList();
        int randomIndex = new Random().nextInt(allIDs.size());
        int randomID = allIDs.get(randomIndex);
        Phrase phrase = session.get(Phrase.class, randomID);
        OldPhrase oldPhrase = new OldPhrase(phrase.getPhrase(), phrase.getAuthor());
        session.save(oldPhrase);
        finalPhrase = phrase.getAuthor() + "\n\n" + phrase.getPhrase();
        session.delete(phrase);
        rowCount--;
        if (rowCount == 0) {
            rowCount = countRows();
        }
        session.flush();
        transaction.commit();
        return finalPhrase;
    }

    private long countRows() {
        return session.createQuery("select count(*) from Phrase", Long.class).getSingleResult();
    }

    public void addUserChatIdTimeZone(long chatId) {
        session.save(new UserChatID(chatId));
    }

    public List<Long> getUserChatIds() {
        return session.createQuery("select chatId from UserChatID", Long.class).getResultList();
    }

    public void close() {
        session.close();
    }
}
