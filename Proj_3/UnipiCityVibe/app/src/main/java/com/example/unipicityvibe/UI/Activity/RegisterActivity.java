package com.example.unipicityvibe.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.unipicityvibe.R;
import com.example.unipicityvibe.Service.AuthService;
import com.example.unipicityvibe.UI.Exception.ExceptionToMessage;
import com.example.unipicityvibe.Service.Interface.IAuthService;
import com.example.unipicityvibe.Data.Models.UserAuthData;
import com.example.unipicityvibe.UI.CustomView.EditTextView;

public class RegisterActivity extends BaseActivity {

    private EditTextView editTextViewName;
    private EditTextView editTextViewLastName;
    private EditTextView editTextViewEmail;
    private EditTextView editTextViewPassword;
    private EditTextView editTextViewPassword2;
    private TextView errorText;


    private IAuthService authService;

    // ------ Page Change ------
    private void goHomePage(){
        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
    // ------ End Page Change ------

    // ------ Call Back ------
    private void onComposeListenerButton(boolean success, String errorLog){
        if (!success){
            errorText.setText(ExceptionToMessage.AuthExceptionToTextId(errorLog));
        }
        else{
            goHomePage();
        }
    }
    // ------ End Call Back ------

    // returns true if the input is invalid and set errorText text
    private boolean validateRegister(String name, String lastName, String email, String password, String password2){
        if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || password2.isEmpty()){
            errorText.setText(R.string.error_all_fields_required);
            return true;
        }
        if (!email.contains("@") || !email.contains(".")){
            errorText.setText(R.string.error_email_invalid);
            return true;
        }
        if (!password.equals(password2)){
            errorText.setText(R.string.error_passwords_not_match);
            return true;
        }
        if (password.length() <= 6){
            errorText.setText(R.string.error_password_too_short);
            return true;
        }

        return false;
    }

    // ------ Buttons ------
    private void registerButton(View view){
        String password = editTextViewPassword.getEditText().toString();
        String password2 = editTextViewPassword2.getEditText().toString();
        String name = editTextViewName.getEditText().toString();
        String lastName = editTextViewLastName.getEditText().toString();
        String email = editTextViewEmail.getEditText().toString();

        if(validateRegister(name, lastName, email, password, password2)) return;

        authService.userRegister(email, password, name, lastName, this::onComposeListenerButton);

    }
    // ------ End Buttons ------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // initializations
        authService = AuthService.getInstance();

        editTextViewName = findViewById(R.id.editTextViewName);
        editTextViewLastName = findViewById(R.id.editTextViewLastName);
        editTextViewEmail = findViewById(R.id.editTextViewEmail);
        editTextViewPassword = findViewById(R.id.editTextViewPassword);
        editTextViewPassword2 = findViewById(R.id.editTextViewPassword2);
        errorText = findViewById(R.id.textViewError);

        // set to buttons onClickListener
        Button reg = findViewById(R.id.buttonRegister);
        reg.setOnClickListener(this::registerButton);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // if user is authorized. change page to home
        UserAuthData user = authService.getUserAuth();
        if (!user.uID.isEmpty()) goHomePage();
    }
}
