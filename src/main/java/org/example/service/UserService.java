package org.example.service;

import org.example.model.UserChatID;
import org.example.repository.UserChatIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserChatIdRepository userChatIdRepository;

    private UserService(UserChatIdRepository userChatIdRepository) {
        this.userChatIdRepository = userChatIdRepository;
    }

    public void addUserChatId(long chatId, List<Integer> phraseIdList) {
        userChatIdRepository.save(new UserChatID(chatId, phraseIdList));
    }

    public List<UserChatID> getAllUserChatId() {
        return userChatIdRepository.findAll();
    }

    public boolean checkPhraseUserList(UserChatID userChatId, int phraseId){
        userChatIdRepository.findById(userChatId.getId());
        List<Integer> phraseIdList = userChatId.getPhraseIdList();
        for (int i = 0; i < phraseIdList.size(); i++) {
            if (phraseIdList.get(i).equals(phraseId)) {
                return true;
            }
        }
        return false;
    }

    public void sortListPhraseUser() {
        List<UserChatID> userChatIdList = userChatIdRepository.findAll();

    }
}
