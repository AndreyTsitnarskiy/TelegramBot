package org.example;

import org.example.bot.Bot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    public static void main(String[] args) {

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new Bot());

            Timer timer = new Timer();

            LocalTime currentTime = LocalTime.now();

            long initialDelay = calculateInitialDelay(currentTime);

            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Bot bot = new Bot();
                    bot.parseMessage();
                }
            }, initialDelay, 24 * 60 * 60 * 1000);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }


    private static long calculateInitialDelay(LocalTime currentTime) {
        // Задаем желаемые времена
        LocalTime time1 = LocalTime.of(10, 0);
        LocalTime time2 = LocalTime.of(12, 0);
        LocalTime time3 = LocalTime.of(14, 0);
        LocalTime time4 = LocalTime.of(16, 0);
        LocalTime time5 = LocalTime.of(18, 0);
        LocalTime time6 = LocalTime.of(20, 0);

        if (currentTime.isBefore(time1)) {
            return calculateDelay(currentTime, time1);
        }
        else if (currentTime.isBefore(time2)) {
            return calculateDelay(currentTime, time2);
        }
        else if (currentTime.isBefore(time3)) {
            return calculateDelay(currentTime, time3);
        }
        else if (currentTime.isBefore(time4)) {
            return calculateDelay(currentTime, time3);
        }
        else if (currentTime.isBefore(time5)) {
            return calculateDelay(currentTime, time3);
        }
        else if (currentTime.isBefore(time6)) {
            return calculateDelay(currentTime, time3);
        }
        return 0;
    }

    private static long calculateDelay(LocalTime currentTime, LocalTime targetTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime targetDateTime = LocalDateTime.of(currentDateTime.toLocalDate(), targetTime);
        if (targetTime.isBefore(currentTime)) {
            targetDateTime = targetDateTime.plusDays(1);
        }
        return Duration.between(currentDateTime, targetDateTime).toMillis();
    }
}
