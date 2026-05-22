package com.example.unipicityvibe;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.unipicityvibe.Class.AuthControl;
import com.example.unipicityvibe.Constants.ExceptionToMessage;
import com.example.unipicityvibe.Interface.IAuthControl;
import com.example.unipicityvibe.Struct.UserAuthStruct;
import com.example.unipicityvibe.Components.EditTextView;

public class MainActivity extends AppCompatActivity {

    private EditTextView editTextViewEmail;
    private EditTextView editTextViewPassword;
    private TextView errorText;

    private IAuthControl authControl;


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

    private void onCompleteListenerLogIn(boolean success, String errorLog){
        if (!success){
            errorText.setText(ExceptionToMessage.AuthExceptionToTextId(errorLog));
            return;
        }
        goHomePage();

    }

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

    private void loginButton(View l){
        String email = editTextViewEmail.getEditText();
        String password = editTextViewPassword.getEditText();
        if (validateLogIn(email, password)) return;
        authControl.userLogIn(email, password, this::onCompleteListenerLogIn);
    }

    private void registerButton(View view){
        goRegisterPage();
    }

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

        authControl = AuthControl.getInstance();

        editTextViewEmail = findViewById(R.id.editTextViewEmail);
        editTextViewPassword = findViewById(R.id.editTextViewPassword);
        errorText = findViewById(R.id.textViewError);

        Button b;
        b = findViewById(R.id.buttonLogin);
        b.setOnClickListener(this::loginButton);
        b = findViewById(R.id.buttonRegister);
        b.setOnClickListener(this::registerButton);
    }

    @Override
    protected void onStart() {
        super.onStart();

        UserAuthStruct user = authControl.getUserAuth();
        if (!user.uID.isEmpty()) goHomePage();
    }
}