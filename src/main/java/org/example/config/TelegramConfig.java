package org.example.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class TelegramConfig {
    private String botName;
    private String botToken;

    public TelegramConfig() {
        Yaml yaml = new Yaml();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.yaml");
        Map<String, Object> obj = yaml.load(inputStream);
        Map<String, Object> telegramObj = (Map<String, Object>) obj.get("telegram");
        botToken = (String) telegramObj.get("bot-token");
        botName = (String) telegramObj.get("bot-name");
    }

    public String getBotToken() {
        return botToken;
    }

    public String getBotName() {
        return botName;
    }
}
