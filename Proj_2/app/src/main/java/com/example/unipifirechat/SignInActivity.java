package com.example.unipifirechat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.unipifirechat.Class.UserContr;
import com.example.unipifirechat.Interfaces.IUserContr;

public class SignInActivity extends AppCompatActivity {

    IUserContr user;

    // methods
    private void initButton(){

        // data
        EditText etEmail = findViewById(R.id.editTextEmail);
        EditText etPassword = findViewById(R.id.editTextPassword);
        // error log
        TextView tvErrorLog = findViewById(R.id.textViewErrorLog);

        Button bIn = findViewById(R.id.buttonSignIn);
        bIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sEmail = String.valueOf(etEmail.getText());
                String sPassword = String.valueOf(etPassword.getText());
                user.SignIn(sEmail, sPassword, (success, errorLog) -> {
                    if(!success){
                        tvErrorLog.setText(errorLog);
                        return;
                    }
                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                });
            }
        });
    }

    private void setUpLogo(){
        TextView logo = findViewById(R.id.textViewLogo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signIn), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // set user
        user = UserContr.getInstance();

        // button
        initButton();
        setUpLogo();
    }

    // they should logout to signin
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed got to homepage.
        if(this.user.getSignIn()){
            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

