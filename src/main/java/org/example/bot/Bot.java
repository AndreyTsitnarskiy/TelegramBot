package org.example.bot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.TelegramConfig;
import org.example.database.DataBaseOperations;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Bot extends TelegramLongPollingBot {

    private TelegramConfig telegramConfig = new TelegramConfig();

    private final String BOT_TOKEN = telegramConfig.getBotToken();
    private final String BOT_NAME = telegramConfig.getBotName();

    private DataBaseOperations dataBaseOperations = new DataBaseOperations();

    private final static Logger LOGGER = LogManager.getLogger("send");

    private Keyboard keyboard = new Keyboard();

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
                dataBaseOperations.addUserChatIdTimeZone(chatId);
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

    public void parseMessage() {
        String message = dataBaseOperations.getPhraseAndAuthor();
        List<Long> userChatIds = dataBaseOperations.getUserChatIds();
        for (Long chatId : userChatIds) {
            SendMessage messageToUser = new SendMessage();
            messageToUser.setChatId(String.valueOf(chatId));
            messageToUser.setText(message);
            try {
                execute(messageToUser);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
