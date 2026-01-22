package com.example.unipifirechat;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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


    private boolean checkUsernameValidation(String username){
        return true;
    }
    private boolean checkEmailValidation(String email){

        return true;
    }
    private boolean checkPasswordValidation(String password){
        return true;
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

        // data form
        EditText username = findViewById(R.id.editTextUsername);
        EditText email = findViewById(R.id.editTextEmail);
        EditText password = findViewById(R.id.editTextPassword);

        // log
        TextView errLog = findViewById(R.id.textViewErrorLog);

        // button register
        final Button bReg = findViewById(R.id.buttonSingUp);
        bReg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // read data
                    String sUsername = String.valueOf(username.getText());
                    String sEmail = String.valueOf(email.getText());
                    String sPassword = String.valueOf(password.getText());
                    // signin with data

                    user.SignUp(sEmail, sPassword, ((success, ErrorLog) -> {
                        if (!success){
                            errLog.setText(ErrorLog);
                            return;
                        }
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent);
                    }));


                }
            }
        );

    }
}
