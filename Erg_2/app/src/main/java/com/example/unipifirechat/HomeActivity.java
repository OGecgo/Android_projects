package com.example.unipifirechat;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.unipifirechat.Class.UserContr;
import com.example.unipifirechat.Interfaces.IUserContr;

public class HomeActivity extends AppCompatActivity {

    IUserContr user;

    private void showMessage(String title, String msg){
        new AlertDialog.Builder(HomeActivity.this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void initButtonAccount(){

        Button userB = findViewById(R.id.buttonUser);
        // create dialog
        View viewAccount = getLayoutInflater().inflate(R.layout.dialog_account, null);
        AlertDialog accountDialog = new androidx.appcompat.app.AlertDialog.Builder(HomeActivity.this)
                .setView(viewAccount)
                .setCancelable(true)
                .create();

        // set on click button
        userB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // take object from new dialog
                Button logOut = viewAccount.findViewById(R.id.buttonLogout);
                TextView username = viewAccount.findViewById(R.id.textViewUsername);
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

    public void initButtonAddUser(){
        Button btn = findViewById(R.id.buttonAddUser);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    public void initButtonSearch(){
        Button btn = findViewById(R.id.buttonSearch);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void createButtonChat(String name){
        LinearLayout ll = findViewById(R.id.linearLayoutChats);
        Button btn = new Button(this);
        btn.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.
        ));
        ll.addView(btn);
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


        // buttons
        initButtonAccount();
        initButtonAddUser();
        initButtonSearch();
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
