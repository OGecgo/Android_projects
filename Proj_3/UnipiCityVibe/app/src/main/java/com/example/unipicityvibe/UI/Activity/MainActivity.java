package com.example.unipicityvibe.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.unipicityvibe.R;
import com.example.unipicityvibe.Service.AuthService;
import com.example.unipicityvibe.UI.Exception.ExceptionToMessage;
import com.example.unipicityvibe.Service.Interface.IAuthService;
import com.example.unipicityvibe.Data.Models.UserAuthData;
import com.example.unipicityvibe.UI.CustomView.EditTextView;

public class MainActivity extends AppCompatActivity {

    private EditTextView editTextViewEmail;
    private EditTextView editTextViewPassword;
    private TextView errorText;

    private IAuthService authService;


    // ------ Page Change ------
    private void goHomePage(){
        Intent intent=new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void goRegisterPage(){
        Intent intent=new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
    // ------ End Page Change ------


    // ----- Call Back ------
    private void onCompleteListenerLogIn(boolean success, String errorLog){
        if (!success){
            errorText.setText(ExceptionToMessage.AuthExceptionToTextId(errorLog));
            return;
        }
        goHomePage();

    }
    // ------ End Call Back ------


    // returns true if the input is invalid and set errorText text
    private boolean validateLogIn(String email, String password){
        if (email.isEmpty() || password.isEmpty()){
            errorText.setText(R.string.error_all_fields_required);
            return true;
        }
        if (!email.contains("@") || !email.contains(".")){
            errorText.setText(R.string.error_email_invalid);
            return true;
        }

        return false;
    }

    // ------ Buttons ------
    private void loginButton(View view){
        String email = editTextViewEmail.getEditText().toString();
        String password = editTextViewPassword.getEditText().toString();
        if (validateLogIn(email, password)) return;
        authService.userLogIn(email, password, this::onCompleteListenerLogIn);
    }

    private void registerButton(View view){
        goRegisterPage();
    }
    // ------ End Buttons ------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // initializations
        authService = AuthService.getInstance();

        editTextViewEmail = findViewById(R.id.editTextViewEmail);
        editTextViewPassword = findViewById(R.id.editTextViewPassword);
        errorText = findViewById(R.id.textViewError);

        // set to buttons onClickListener
        Button b;
        b = findViewById(R.id.buttonLogin);
        b.setOnClickListener(this::loginButton);
        b = findViewById(R.id.buttonRegister);
        b.setOnClickListener(this::registerButton);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // if user is authorized. change page to home
        UserAuthData user = authService.getUserAuth();
        if (!user.uID.isEmpty()) goHomePage();
    }
}