package org.example.service;

import org.quartz.Job;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface SendMessageTask extends Job {
    void executeSendMessage() throws TelegramApiException;
}
