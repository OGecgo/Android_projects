package com.example.unipifirechat;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.unipifirechat.Class.ChatsContr;
import com.example.unipifirechat.Class.UserContr;
import com.example.unipifirechat.Interfaces.IChatsContr;
import com.example.unipifirechat.Interfaces.IUserContr;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private IUserContr user;
    private IChatsContr chats;

    // chats
    private Map<String, Object> chatsId_username;

    private void showMessage(String title, String msg){
        new AlertDialog.Builder(HomeActivity.this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void initClickButtonChat(Button btn, String name, String chatId){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
                intent.putExtra("username", name);
                intent.putExtra("chatId", chatId);
                startActivity(intent);
                finish();
            }
        });
    }
    private void createButtonChat(String name, String chatId){
        LinearLayout ll = findViewById(R.id.linearLayoutChats);
        Button btn = new Button(this);

        // set layout parameters for button
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = 10;
        btn.setLayoutParams(lp);

        // button styling
        // for now bad
        btn.setText(name);
        btn.setTextSize(18);
        btn.setAllCaps(false);
        btn.setTextColor(getResources().getColor(R.color.primary_text));
        btn.setBackgroundColor(getResources().getColor(R.color.divider_color));

        initClickButtonChat(btn, name, chatId);
        // add to linear layout
        ll.addView(btn);
    }

    private void initButtonAccount(){

        Button userB = findViewById(R.id.buttonUser);
        // create dialog
        View viewAccount = getLayoutInflater().inflate(R.layout.dialog_account, null);
        AlertDialog accountDialog = new AlertDialog.Builder(HomeActivity.this)
                .setView(viewAccount)
                .setCancelable(true)
                .create();

        // set on click button
        userB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // take object from new dialog
                Button logOut = viewAccount.findViewById(R.id.buttonFindUser);
                TextView username = viewAccount.findViewById(R.id.editTextFindUser);
                username.setText(user.getUsername());


                // buttons dialog alert
                logOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.LogOut(((success, ErrorLog) -> {
                                    if (!success){
                                        showMessage("Error Log Out", ErrorLog);
                                        return;
                                    }
                                    accountDialog.dismiss();
                                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                })
                        );
                    }
                });

                // show acoutn view
                accountDialog.show();
            }
        });
    }

    private void initButtonAddUser(){
        // create dialog
        Button btn = findViewById(R.id.buttonSendMessage);
        // create dialog
        View viewAccount = getLayoutInflater().inflate(R.layout.dialog_add_user, null);
        AlertDialog accountDialog = new AlertDialog.Builder(HomeActivity.this)
                .setView(viewAccount)
                .setCancelable(true)
                .create();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = viewAccount.findViewById(R.id.buttonFindUser);
                EditText et = viewAccount.findViewById(R.id.editTextFindUser);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String username = String.valueOf(et.getText());
                        // send invite and create chat
                        chats.SendInviteTo(username, ((success, ErrorLog) -> {
                            // if error
                            if (!success){
                                showMessage("Find User", ErrorLog);
                                return;
                            }
                            accountDialog.dismiss();
                            // ui update automatically
                        }));
                    }
                });

                accountDialog.show();
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void updateUiChatsButtons(boolean success, String errorLog){
        if (success){
            for (Map.Entry<String, Object> chat: chatsId_username.entrySet()){
                createButtonChat(chat.getValue().toString(), chat.getKey());
            }
        }
        Log.w(TAG, errorLog);
    }


    private void addExistedChats(){
        // String String
        // init maps
        chatsId_username = new HashMap<>();

        // automatically accept all invites and after take chats
        chats.AcceptInvites((success, errorLog) -> { });
        chats.getChatsIdAndUsername(chatsId_username, this::updateUiChatsButtons);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // padding
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // user init
        user = UserContr.getInstance();
        chats = ChatsContr.getInstance();


        // buttons
        initButtonAccount();
        initButtonAddUser();

        // created existed chats
        addExistedChats();
    }

    // if someone into home when has no user
    // then send to main menu
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed got to homepage.
        if(!this.user.getSignIn()){
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


}
