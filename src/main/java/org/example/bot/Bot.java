package org.example.bot;

import org.example.config.TelegramConfig;
import org.example.database.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.model.Phrase;
import org.example.model.UserChatID;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Random;

public class Bot extends TelegramLongPollingBot {

    private static final Logger SEND_LOGGER = LoggerFactory.getLogger("SEND");

    private TelegramConfig telegramConfig = new TelegramConfig();

    private final String BOT_TOKEN = telegramConfig.getBotToken();
    private final String BOT_NAME = telegramConfig.getBotName();

    private final DatabaseManager databaseManager = new DatabaseManager();

    //private Keyboard keyboard = new Keyboard();

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message inMess = update.getMessage();
            long chatId = inMess.getChatId();

            if (inMess.getText().equals("/start")) {
                UserChatID userChatId = new UserChatID(chatId);
                databaseManager.addUserChatId(userChatId);
                String response = "Вы успешно подписались на нашего бота, бот умеет отправлять сообщения" +
                        "с цитатами известных личностей в течении дня!";
                SendMessage messageToUser = new SendMessage();
                messageToUser.setChatId(String.valueOf(chatId));
                messageToUser.setText(response);
                try {
                    execute(messageToUser);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    public void sendRandomPhraseToAllUsers() {
        int randomNumber = RandomNumber();
        Phrase randomPhrase = databaseManager.getRandomPhrases(randomNumber);
        SEND_LOGGER.info("Random id phrase: " + randomNumber);
        SEND_LOGGER.info("Random phrase: " + randomPhrase.getPhrase());
        databaseManager.saveOldPhrase(randomPhrase);
        SEND_LOGGER.info("Old phrase " + randomPhrase.getPhrase() + " saved");
        List<Long> userChatIds = databaseManager.getUserChatIds();
        for (Long chatId : userChatIds) {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(adapterPhrase(randomPhrase));
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        databaseManager.deletePhrase(randomPhrase);
        SEND_LOGGER.info("Phrase " + randomPhrase.getPhrase() + " deleted");
        databaseManager.close();
    }

    private int RandomNumber() {
        List<Integer> phraseIds = databaseManager.getPhraseIds();
        return phraseIds.get(new Random().nextInt(phraseIds.size()));
    }

    private String adapterPhrase(Phrase phrase) {
        return phrase.getAuthor() + "\n\n" + phrase.getPhrase();
    }
}
