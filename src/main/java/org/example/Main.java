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
            Bot bot = new Bot();
            telegramBotsApi.registerBot(bot);

            Timer timer = new Timer();

            LocalTime currentTime = LocalTime.now();
            long initialDelay = calculateInitialDelay(currentTime);

            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    bot.parseMessage();
                }
            }, initialDelay, 24 * 60 * 60 * 1000);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }


    private static long calculateInitialDelay(LocalTime currentTime) {
        // Задаем желаемые времена
        LocalTime time1 = LocalTime.of(21, 44);
        LocalTime time2 = LocalTime.of(21, 45);
        LocalTime time3 = LocalTime.of(21, 47);
        LocalTime time4 = LocalTime.of(21, 49);
        LocalTime time5 = LocalTime.of(21, 51);
        LocalTime time6 = LocalTime.of(21, 53);

        long minDelay = Long.MAX_VALUE;
        for (LocalTime targetTime : new LocalTime[]{time1, time2, time3, time4, time5, time6}) {
            long delay = calculateDelay(currentTime, targetTime);
            if (delay < minDelay) {
                minDelay = delay;
            }
        }
        return minDelay;
    }

    private static long calculateDelay(LocalTime currentTime, LocalTime targetTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime targetDateTime = LocalDateTime.of(currentDateTime.toLocalDate(), targetTime);
        if (targetTime.isBefore(currentTime) || targetTime.equals(currentTime)) {
            targetDateTime = targetDateTime.plusDays(1);
        }
        return Duration.between(currentDateTime, targetDateTime).toMillis();
    }
}
