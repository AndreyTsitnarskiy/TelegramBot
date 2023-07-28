package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;

@Slf4j
@Component
public class SendMessageTaskImpl implements SendMessageTask {

    @Autowired
    private WorkerService workerService;

    @Autowired
    private TelegramLongPollingBot bot;

    @Override
    public void executeSendMessage() throws TelegramApiException {
        log.info("Executing SendMessageTask...");

        // В этом методе выполняется логика отправки сообщений
        // Получите список сообщений для отправки
        HashMap<Long, String> listMessage = workerService.getListMessage();

        // Отправьте каждое сообщение
        for (HashMap.Entry<Long, String> entry : listMessage.entrySet()) {
            long chatId = entry.getKey();
            String messageText = entry.getValue();

            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(messageText);

            try {
                bot.execute(message);
            } catch (TelegramApiException e) {
                log.error("Error sending message to chatId " + chatId + ": " + e.getMessage());
            }
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            executeSendMessage();
        } catch (TelegramApiException e) {
            log.error("Error executing SendMessageTask: " + e.getMessage());
        }
    }
}