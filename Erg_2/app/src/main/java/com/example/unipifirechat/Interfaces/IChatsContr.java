package com.example.unipifirechat.Interfaces;

import com.example.unipifirechat.Structs.MessageData;

import java.util.LinkedList;

public interface IChatsContr {
    void getInvites(LinkedList<String> invites);
    void getChatsId(LinkedList<String> chatsId);
    void getMessages(String chatId, LinkedList<MessageData> messages);

    void SendInviteTo(String username);
    void AcceptInvite(String chatId);
    void DennyInvite(String chatId);

    void sentMessage(String chatId, String message);
}
