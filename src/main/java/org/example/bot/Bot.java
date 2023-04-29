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
            String chatId = inMess.getChatId().toString();
            String response = parseMessage(inMess.getText());
            SendMessage outMess = new SendMessage();

            LOGGER.info("Отправлено сообщение: " + update.getMessage().getText());
            LOGGER.info("Получено сообщение: " + response);

            outMess.setReplyMarkup(keyboard.initKeyboard());
            outMess.setChatId(chatId);
            outMess.setText(response);

            try {
                execute(outMess);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String parseMessage(String textMsg) {
        String response;

        if (textMsg.equals("/start"))
            response = "Приветствую, бот знает много цитат. Жми /get, чтобы получить случайную из них";
        else if (textMsg.equals("/get") || textMsg.equals("Просвяти"))
            response = dataBaseOperations.getPhraseAndAuthor();
        else
            response = "Сообщение не распознано";
        return response;
    }
}
