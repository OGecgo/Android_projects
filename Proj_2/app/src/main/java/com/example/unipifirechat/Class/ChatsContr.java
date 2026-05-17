package com.example.unipifirechat.Class;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Entity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.unipifirechat.Interfaces.DatabaseCallback;
import com.example.unipifirechat.Interfaces.IChatsContr;
import com.example.unipifirechat.Interfaces.IUserContr;
import com.example.unipifirechat.Structs.MessageData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.Reference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ChatsContr implements IChatsContr{
// ---------------------- private ----------------------

    private static IChatsContr chats;

    private final FirebaseDatabase mDB = FirebaseDatabase.getInstance();
    private final IUserContr user = UserContr.getInstance();


    private ChatsContr() {}

    private void AcceptInvite(String chatId, String fromId, String username, DatabaseCallback chatDB){
        // add themself to chat
        mDB.getReference().child("chats").child(chatId).child("members").child(user.getUid()).setValue(true)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete()){
                            // add chat to chats and delete invite
                            mDB.getReference().child("users").child(user.getUid()).child("chatsId").child(chatId).setValue(username);
                            mDB.getReference().child("users").child(user.getUid()).child("inviteChat").child(chatId).setValue(null);
                            chatDB.onCompose(true, "");
                        }else{
                            chatDB.onCompose(false, "error add themselves to chat");
                        }
                    }
                });

    }
    @SuppressLint("RestrictedApi")
    private void CreateChat(String username, String withUserUid, DatabaseCallback chatDB){
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
        update.put("users/" + withUserUid + "/inviteChat/" + chatId, uid);

        // only if chat has user
        // user can add chat to users/chats
        mDB.getReference().updateChildren(update, ((error, ref) -> {
            if (error == null) {
                // add chat to themself chats
                assert chatId != null;
                mDB.getReference().child("users").child(uid).child("chatsId").child(chatId).setValue(username);
                chatDB.onCompose(true, "");
            }
            else {
                Log.w(TAG, error.toString());
                chatDB.onCompose(false, "error create chat");
            }
        }));

    }



// ---------------------- public ----------------------

    public static IChatsContr  getInstance(){
        if (chats == null){
            chats = new ChatsContr();
        }
        return chats;
    }

    @Override
    public void getChatsIdAndUsername(Map<String, Object> chatId_username, DatabaseCallback chatDB){
        mDB.getReference().child("users").child(user.getUid()).child("chatsId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object raw = snapshot.getValue();
                if (raw instanceof Map){
                    Map<String, Object> temp = new HashMap<>();
                    temp.putAll(chatId_username);

                    chatId_username.clear();

                    Map<String, Object> newMap = (Map<String, Object>) raw;

                    for(Map.Entry<String, Object> item: newMap.entrySet()){
                        if(!temp.containsKey(item.getKey())){
                            chatId_username.put(item.getKey(), item.getValue());
                        }
                    }
                    chatDB.onCompose(true, "");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                chatDB.onCompose(false, error.getMessage());
            }
        });
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
                        CreateChat(username, task.getResult().getValue(String.class), chatDB);
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
    public void AcceptInvites(DatabaseCallback chatDB){
        if (!user.getSignIn()){
            chatDB.onCompose(false, "error sign in");
            return;
        }

        mDB.getReference().child("users").child(user.getUid()).child("inviteChat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item: snapshot.getChildren()){
                    String chatId = item.getKey();
                    String fromId = item.getValue(String.class);

                    // find username
                    mDB.getReference().child("users").child(fromId).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isComplete()){
                                String username = task.getResult().getValue(String.class);
                                AcceptInvite(chatId, fromId, username, chatDB);
                            }
                            else {
                                chatDB.onCompose(false, "error find user");
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                chatDB.onCompose(false, error.getMessage());
            }
        });


    }



    @Override
    public void getMessages(LinkedList<MessageData> messages, long fromNTimeRead, String chatId, DatabaseCallback chatDB) {
        mDB.getReference().child("chats").child(chatId).child("messages")
                .orderByChild("time")
                .startAt(fromNTimeRead + 1)
                .addChildEventListener(new ChildEventListener() {

                    @Override
                    public void onChildAdded(@NonNull DataSnapshot item, String previousChildName) {
                        MessageData m = new MessageData();
                        m.message = item.child("text").getValue(String.class);
                        m.username = item.child("username").getValue(String.class);
                        m.time = item.child("time").getValue(Long.class);
                        messages.clear();
                        messages.add(m);
                        chatDB.onCompose(true, "");
                    }

                    @Override public void onChildChanged(@NonNull DataSnapshot snapshot, String s) {}
                    @Override public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
                    @Override public void onChildMoved(@NonNull DataSnapshot snapshot, String s) {}
                    @Override public void onCancelled(@NonNull DatabaseError error) {
                        chatDB.onCompose(false, error.getMessage());
                    }
                });
    }

    @Override
    public void sentMessage(String chatId, String text, String username, DatabaseCallback chatDB){
        if (text == null || username == null || text.isEmpty() || username.isEmpty()){
            chatDB.onCompose(false, "empty val");
            return;
        }

        String key = mDB.getReference().child("chats").child(chatId).child("messages").push().getKey();
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("text", text);
        map.put("time", ServerValue.TIMESTAMP);


        assert key != null;
        DatabaseReference ref = mDB.getReference().child("chats").child(chatId).child("messages").child(key);
        ref.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    ref.orderByChild("time");
                    chatDB.onCompose(true, "");
                }
                else chatDB.onCompose(false, "message error send");
            }
        });
    }



}
