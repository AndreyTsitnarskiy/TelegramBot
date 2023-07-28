package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.config.TelegramConfig;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.time.LocalTime;
import java.util.ArrayList;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {

    private final TelegramConfig telegramConfig;
    private final SendMessageTask sendMessageTask;

    private final LocalTime SPECIFIC_TIME1 = LocalTime.of(23, 24);
    private final LocalTime SPECIFIC_TIME2 = LocalTime.of(23, 25);
    private final LocalTime SPECIFIC_TIME3 = LocalTime.of(23, 26);

    @Autowired
    private UserService userService;

    private Scheduler scheduler;

    @Autowired
    public Bot(TelegramConfig telegramConfig, SendMessageTask sendMessageTask) {
        this.telegramConfig = telegramConfig;
        this.sendMessageTask = sendMessageTask;
    }

    @Override
    public String getBotUsername() {
        return telegramConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return telegramConfig.getBotToken();
    }

    @PostConstruct
    public void init() {
        try {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            scheduler = schedulerFactory.getScheduler();
            scheduler.start();

            scheduleMessageAtSpecificTime(SPECIFIC_TIME1);
            scheduleMessageAtSpecificTime(SPECIFIC_TIME2);
            scheduleMessageAtSpecificTime(SPECIFIC_TIME3);

        } catch (SchedulerException e) {
            log.error("ERROR INIT SCHEDULER: " + e.getMessage());
        }
    }

    private void scheduleMessageAtSpecificTime(LocalTime specificTime) {
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("send-" + specificTime.toString())
                .withSchedule(CronScheduleBuilder.cronSchedule(getCronExpression(specificTime)))
                .build();

        JobDetail job = JobBuilder.newJob(SendMessageTaskImpl.class) // Здесь исправлено на SendMessageTaskImpl
                .withIdentity("sendMessageJob-" + specificTime.toString(), "group1")
                .build();

        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            log.error("ERROR SCHEDULE: " + e.getMessage());
        }

    }

    private String getCronExpression(LocalTime specificTime) {
        int hour = specificTime.getHour();
        int minute = specificTime.getMinute();
        return String.format("0 %d %d ? * *", minute, hour);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message inMess = update.getMessage();
            long chatId = inMess.getChatId();

            if (inMess.getText().equals("/start")) {
                userService.addUserChatId(chatId, new ArrayList<>());
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
            } else {
                sendRandomPhraseToAllUsers();
            }
        }
    }

    private void sendRandomPhraseToAllUsers() {
        try {
            sendMessageTask.executeSendMessage();
        } catch (TelegramApiException e) {
            log.error("ERROR SENDING: " + e.getMessage());
        }
    }
}









