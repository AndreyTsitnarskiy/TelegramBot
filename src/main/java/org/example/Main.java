package org.example;

import org.example.bot.Bot;
import org.example.bot.SendMessageJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import java.util.UUID;

public class Main {

    public static void main(String[] args) throws SchedulerException {

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            Bot bot = new Bot();
            telegramBotsApi.registerBot(bot);

            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();

            scheduler.start();

            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("send_08_00")
                    .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(21, 47)).build();

            Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("send_10_00")
                    .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(10, 00)).build();

            Trigger trigger2 = TriggerBuilder.newTrigger().withIdentity("send_14_00")
                    .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(14, 00)).build();

            Trigger trigger3 = TriggerBuilder.newTrigger().withIdentity("send_16_00")
                    .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(16, 00)).build();

            Trigger trigger4 = TriggerBuilder.newTrigger().withIdentity("send_18_00")
                    .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(18, 00)).build();

            JobDetail job = JobBuilder.newJob(SendMessageJob.class)
                    .withIdentity("sendMessageJob_08_00" + UUID.randomUUID(), "group1")
                    .build();
            JobDetail job1 = JobBuilder.newJob(SendMessageJob.class)
                    .withIdentity("sendMessageJob_10_00" + UUID.randomUUID(), "group1")
                    .build();
            JobDetail job2 = JobBuilder.newJob(SendMessageJob.class)
                    .withIdentity("sendMessageJob_14_00" + UUID.randomUUID(), "group1")
                    .build();
            JobDetail job3 = JobBuilder.newJob(SendMessageJob.class)
                    .withIdentity("sendMessageJob_16_00" + UUID.randomUUID(), "group1")
                    .build();
            JobDetail job4 = JobBuilder.newJob(SendMessageJob.class)
                    .withIdentity("sendMessageJob_18_00" + UUID.randomUUID(), "group1")
                    .build();


            scheduler.scheduleJob(job, trigger);
            scheduler.scheduleJob(job1, trigger1);
            scheduler.scheduleJob(job2, trigger2);
            scheduler.scheduleJob(job3, trigger3);
            scheduler.scheduleJob(job4, trigger4);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
