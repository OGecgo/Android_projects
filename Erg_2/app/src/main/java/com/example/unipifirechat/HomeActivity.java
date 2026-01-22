package com.example.unipifirechat;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

        // Dialogs
        View viewAccount = getLayoutInflater().inflate(R.layout.dialog_account, null);
        AlertDialog accountDialog = new androidx.appcompat.app.AlertDialog.Builder(HomeActivity.this)
                .setView(viewAccount)
                .setCancelable(true)
                .create();


        // buttons
        Button userB = findViewById(R.id.buttonUser);

        userB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button logOut = viewAccount.findViewById(R.id.buttonLogout);
                TextView username = viewAccount.findViewById(R.id.textViewUsername); // for now nothing

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
