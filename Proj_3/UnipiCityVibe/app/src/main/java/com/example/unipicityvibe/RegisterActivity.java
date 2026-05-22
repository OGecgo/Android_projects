package com.example.unipicityvibe;

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

import com.example.unipicityvibe.Class.AuthControl;
import com.example.unipicityvibe.Constants.ExceptionToMessage;
import com.example.unipicityvibe.Interface.IAuthControl;
import com.example.unipicityvibe.Struct.UserAuthStruct;
import com.example.unipicityvibe.Components.EditTextView;
import com.example.unipicityvibe.Components.TopViewMenu;

public class RegisterActivity extends AppCompatActivity {

    private EditTextView editTextViewName;
    private EditTextView editTextViewLastName;
    private EditTextView editTextViewEmail;
    private EditTextView editTextViewPassword;
    private EditTextView editTextViewPassword2;
    private TextView errorText;


    private IAuthControl authControl;

    private void goHomePage(){
        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
    private void goLogiPage(){
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void onComposeListenerButton(boolean success, String errorLog){
        if (!success){
            errorText.setText(ExceptionToMessage.AuthExceptionToTextId(errorLog));
        }
        else{
            goHomePage();
        }
    }

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

    private void goHomePageButton(View view){
        goLogiPage();
    }

    private void registerButton(View view){
        String password = editTextViewPassword.getEditText();
        String password2 = editTextViewPassword2.getEditText();
        String name = editTextViewName.getEditText();
        String lastName = editTextViewLastName.getEditText();
        String email = editTextViewEmail.getEditText();

        if(validateRegister(name, lastName, email, password, password2)) return;

        authControl.userRegister(email, password, name, lastName, this::onComposeListenerButton);

    }

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

        authControl = AuthControl.getInstance();

        editTextViewName = findViewById(R.id.editTextViewName);
        editTextViewLastName = findViewById(R.id.editTextViewLastName);
        editTextViewEmail = findViewById(R.id.editTextViewEmail);
        editTextViewPassword = findViewById(R.id.editTextViewPassword);
        editTextViewPassword2 = findViewById(R.id.editTextViewPassword2);
        errorText = findViewById(R.id.textViewError);

        TopViewMenu tvm = findViewById(R.id.topViewMenu );
        tvm.setOnClickListenerButtonLogo(this::goHomePageButton);

        Button reg = findViewById(R.id.buttonRegister);
        reg.setOnClickListener(this::registerButton);
    }

    @Override
    protected void onStart() {
        super.onStart();

        UserAuthStruct user = authControl.getUserAuth();
        if (!user.uID.isEmpty()) goHomePage();
    }
}
