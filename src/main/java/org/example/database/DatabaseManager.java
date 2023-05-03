package org.example.database;

import org.example.model.OldPhrase;
import org.example.model.Phrase;
import org.example.model.UserChatID;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class DatabaseManager {
    private SessionFactory sessionFactory;

    public DatabaseManager() {
        Configuration configuration = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Phrase.class)
                .addAnnotatedClass(UserChatID.class)
                .addAnnotatedClass(OldPhrase.class);

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());

        sessionFactory = configuration.buildSessionFactory(builder.build());
    }

    public void close() {
        sessionFactory.close();
    }

    public Phrase getRandomPhrases(int count) {
        Phrase phrase = new Phrase();
        try (Session session = sessionFactory.openSession()) {
            phrase = session.createQuery("FROM Phrase WHERE id = " + count, Phrase.class).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return phrase;
    }

    public void saveOldPhrase(Phrase phrase) {
        try (Session session = sessionFactory.openSession()) {
            OldPhrase oldPhrase = new OldPhrase(phrase.getPhrase(), phrase.getAuthor());
            Transaction transaction = session.beginTransaction();
            session.save(oldPhrase);
            transaction.commit();
        }
    }

    public void deletePhrase(Phrase phrase) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(phrase);
            transaction.commit();
        }
    }

    public List<Long> getUserChatIds() {
        try (Session session = sessionFactory.openSession()) {
            List<Long> chatIds = session.createQuery("SELECT chatId FROM UserChatID", Long.class).getResultList();
            return chatIds;
        }
    }

    public void addUserChatId(UserChatID userChatID) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(userChatID);
            transaction.commit();
        }
    }

    public long getRowCount() {
        return sessionFactory.openSession().createQuery("SELECT COUNT(*) FROM Phrase", Integer.class).getSingleResult();
    }

    public List<Integer> getPhraseIds() {
        return sessionFactory.openSession().createQuery("SELECT id FROM Phrase", Integer.class).getResultList();
    }
}
