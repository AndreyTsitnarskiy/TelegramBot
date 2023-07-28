package org.example.config;

import org.example.service.Bot;
import org.example.service.SendMessageTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotInitializer {

    private SendMessageTask sendMessageTask;
    private TelegramConfig telegramConfig;

    public BotInitializer(SendMessageTask sendMessageTask, TelegramConfig telegramConfig) {
        this.sendMessageTask = sendMessageTask;
        this.telegramConfig = telegramConfig;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        try {
            api.registerBot(new Bot(sendMessageTask));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
