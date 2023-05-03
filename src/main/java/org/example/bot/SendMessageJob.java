package org.example.bot;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class SendMessageJob implements Job {

    private final Bot bot = new Bot();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        bot.sendRandomPhraseToAllUsers();
    }
}
