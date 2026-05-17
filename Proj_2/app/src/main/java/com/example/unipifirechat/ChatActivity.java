package com.example.unipifirechat;

import static android.view.Gravity.RIGHT;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.unipifirechat.Class.ChatsContr;
import com.example.unipifirechat.Class.UserContr;
import com.example.unipifirechat.Interfaces.IChatsContr;
import com.example.unipifirechat.Interfaces.IUserContr;
import com.example.unipifirechat.Structs.MessageData;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private IChatsContr chat;
    private IUserContr user;
    private String username;
    private String chatId;
    private LinkedList<MessageData> messages;
    // the last time message loaded
    private long lasTime;

    @SuppressLint("RtlHardcoded")
    private void createMessage(String m, boolean myM){
        LinearLayout ll = findViewById(R.id.linearLayoutMessages);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = 15;

        TextView message = new TextView(this);
        message.setText(m);

        if (myM) {
            message.setGravity(RIGHT);
            message.setBackgroundColor(getResources().getColor(R.color.light_primary_color));
            message.setTextColor(getResources().getColor(R.color.primary_text));
        }
        else {
            message.setBackgroundColor(getResources().getColor(R.color.divider_color));
            message.setTextColor(getResources().getColor(R.color.primary_text));
        }
        message.setTextSize(25);

        message.setLayoutParams(lp);
        ll.addView(message);
    }
    @SuppressLint("RestrictedApi")
    private void loadMessage(){
        chat.getMessages(messages, lasTime, chatId, ((success, ErrorLog) -> {
            if(!success){
                Log.w(TAG, ErrorLog);
                return;
            }
            Log.d(TAG, String.valueOf(lasTime));

            for(MessageData item: messages){

                if (item.time > lasTime) lasTime = item.time;
                createMessage(item.message, String.valueOf(item.username).equals(user.getUsername()));
            }
        }));
    }


    private void initSendButton(){
        Button btn = findViewById(R.id.buttonSendMessage);
        btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                TextView tv = findViewById(R.id.editTextChat);
                String text = String.valueOf(tv.getText());
                if (!text.isEmpty()){
                    chat.sentMessage(chatId, text, user.getUsername(), (success, errorLog) -> { if (!success) { Log.d(TAG, errorLog); } });
                    // chat will updated automatically
                }
            }
        });
    }
    private void initLogoReturn(){
        Button btn = findViewById(R.id.buttonLogo);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void setUsername(){
        TextView tx = findViewById(R.id.textViewUsername);
        tx.setText(username);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // padding
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chat), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // var
        user = UserContr.getInstance();
        chat = ChatsContr.getInstance();
        username = getIntent().getStringExtra("username");
        chatId =getIntent().getStringExtra("chatId");
        messages = new LinkedList<>();
        lasTime = 0;

        // load chat from firebase
        setUsername();
        loadMessage();

        // button init
        initLogoReturn();
        initSendButton();
    }

    // if someone into home when has no user
    // then send to main menu
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed got to homepage.
        if(!this.user.getSignIn()){
            Intent intent = new Intent(ChatActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
