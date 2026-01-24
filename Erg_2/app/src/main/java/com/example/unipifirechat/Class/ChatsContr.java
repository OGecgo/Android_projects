package com.example.unipifirechat.Class;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.unipifirechat.Interfaces.DatabaseCallback;
import com.example.unipifirechat.Interfaces.IChatsContr;
import com.example.unipifirechat.Interfaces.IUserContr;
import com.example.unipifirechat.Structs.MessageData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ChatsContr implements IChatsContr{
    private static IChatsContr chats;

    private final FirebaseDatabase mDB = FirebaseDatabase.getInstance();
    private final IUserContr user = UserContr.getInstance();

    // tha will updated with listener
    private LinkedList<String> invites;
    private LinkedList<String> chatsId;
    private LinkedList<MessageData> messages;



    private ChatsContr() {
        this.invites = null;
        this.chatsId = null;
        this.messages = null;
    }

    @SuppressLint("RestrictedApi")
    private void CreateChat(String withUserUid, DatabaseCallback chatDB){
        if (withUserUid == null || withUserUid.isEmpty()){
            chatDB.onCompose(false, "user uid don't exist");
            return;
        }
        String uid = user.getUid();
        // set id
        String chatId = mDB.getReference().child("chats").push().getKey();

        Map<String, Object> update = new HashMap<>();
        // create chat and add themself to chat
        update.put("chats/" + chatId + "/members/" + uid, true);
        // send invite to withUserUid
        update.put("users/" + withUserUid + "/inviteChat/" + uid, chatId);

        // only if chat exist user can add chat to users/chats
        mDB.getReference().updateChildren(update, ((error, ref) -> {
            if (error == null) {
                // add chat to themself chats
                update.put("users/" + uid + "/chatsId/" + chatId, true);
                chatDB.onCompose(true, "");
            }
            else {
                Log.w(TAG, error.toString());
                chatDB.onCompose(false, "error create chat");
            }
        }));

    }

    public static IChatsContr  getInstance(){
        if (chats == null){
            chats = new ChatsContr();
        }
        return chats;
    }

    @Override
    public void getInvites(LinkedList<String> invites){

    }
    @Override
    public void getChatsId(LinkedList<String> chatsId){

    }


    @Override
    public void getMessages(String chatId, LinkedList<MessageData> messages) {

    }
    @SuppressLint("RestrictedApi")
    @Override
    public void SendInviteTo(String username, DatabaseCallback chatDB){
        if (username == null || username.isEmpty()){
            chatDB.onCompose(false, "User don't exist");
            return;
        }
        if (!user.getSignIn()){
            Log.w(TAG, "user not sign in");
            return;
        }
        String myUid = user.getUid();

        // when read the uid user they search
        mDB.getReference().child("usernames").child(username.trim()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isComplete()){
                    if (task.getResult().exists()){
                        // create chat and add themself to chat and send invite
                        CreateChat(task.getResult().getValue(String.class), chatDB);
                    }
                    else {
                        chatDB.onCompose(false, "username not found");
                    }
                }
                else{
                    chatDB.onCompose(false, "error database");
                }
            }
        });

    }
    @Override
    public void AcceptInvite(String chatId, DatabaseCallback chatDB){

    }
    @Override
    public void DennyInvite(String chatId, DatabaseCallback chatDB){

    }
    @Override
    public void sentMessage(String chatId, String message, DatabaseCallback chatDB){

    }



}
