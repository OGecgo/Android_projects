package com.example.unipifirechat.Interfaces;

import com.example.unipifirechat.Structs.MessageData;

import java.util.LinkedList;

public interface IChatsContr {
    // LinkedList will updated
    void getInvites(LinkedList<String> invites);
    void getChatsId(LinkedList<String> chatsId);
    void getMessages(String chatId, LinkedList<MessageData> messages);


    void SendInviteTo(String username, DatabaseCallback chatDB);
    void AcceptInvite(String chatId, DatabaseCallback chatDB);
    void DennyInvite(String chatId, DatabaseCallback chatDB);

    void sentMessage(String chatId, String message, DatabaseCallback chatDB);
}
