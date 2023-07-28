package org.example.service;

import org.example.model.Phrase;
import org.example.model.UserChatID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class WorkerService {

    @Autowired
    private PhraseService phraseService;

    @Autowired
    private UserService userService;

    @Autowired
    public WorkerService() {
    }

    public HashMap<Long, String> getListMessage() {
        HashMap<Long, String> listMessage = new HashMap<>();
        List<UserChatID> userChatIdList = userService.getAllUserChatId();
        for (UserChatID userChatId : userChatIdList) {
            String randomPhrase = getUserChatId(userChatId);
            listMessage.put(userChatId.getChatId(), randomPhrase);
        }
        return listMessage;
    }

    public String getUserChatId(UserChatID userChatId) {
        StringBuilder stringBuilder = new StringBuilder();
        Phrase phrase = phraseService.getRandomPhrase();
        if (!userChatId.getPhraseIdList().contains(phrase.getId())) {
            userChatId.getPhraseIdList().add(phrase.getId());
            stringBuilder.append(phrase.getPhrase() + "\n" + phrase.getAuthor());
        } else {
            getUserChatId(userChatId);
        }
        return stringBuilder.toString();
    }
}
