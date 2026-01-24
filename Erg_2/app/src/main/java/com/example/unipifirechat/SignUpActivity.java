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

public class SignUpActivity extends AppCompatActivity {

    private IUserContr user;

    private void initButton(){


        // data form
        EditText username = findViewById(R.id.editTextUsername);
        EditText email = findViewById(R.id.editTextEmail);
        EditText password = findViewById(R.id.editTextPassword);

        // log
        TextView errLog = findViewById(R.id.textViewErrorLog);

        Button bIn = findViewById(R.id.buttonSignUp);
        bIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // take data
                String sUsername = String.valueOf(username.getText());
                String sEmail = String.valueOf(email.getText());
                String sPassword = String.valueOf(password.getText());
                // signup user
                user.SignUp(sUsername, sEmail, sPassword, ((success, ErrorLog) -> {
                    if (!success){
                        errLog.setText(ErrorLog);
                        return;
                    }
                    Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }));


            }
        });
    }

    private void setUpLogo(){
        TextView logo = findViewById(R.id.textViewLogo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signUp), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // set user
        this.user = UserContr.getInstance();


        // button signup
        initButton();
        setUpLogo();
    }

    // they should logout to signup
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed got to homepage.
        if(this.user.getSignIn()){
            Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
