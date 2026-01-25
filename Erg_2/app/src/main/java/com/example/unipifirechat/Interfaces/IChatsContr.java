package com.example.unipifirechat.Interfaces;

import com.example.unipifirechat.Structs.MessageData;

import java.util.LinkedList;
import java.util.Map;

public interface IChatsContr {


    void getChatsIdAndUsername(Map<String, Object> chatsId_username, DatabaseCallback chatDB);

    void SendInviteTo(String username, DatabaseCallback chatDB);
    void AcceptInvites(DatabaseCallback chatDB);
    void getMessages(LinkedList<MessageData> messages, long fromNTimeRead, String chatId, DatabaseCallback chatDB);
    void sentMessage(String chatId, String text, String username, DatabaseCallback chatDB);
}
