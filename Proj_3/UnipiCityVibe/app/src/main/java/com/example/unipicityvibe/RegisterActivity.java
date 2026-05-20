package com.example.unipicityvibe;

import android.annotation.SuppressLint;
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
import com.example.unipicityvibe.Interface.IAuthControl;
import com.example.unipicityvibe.components.EditTextView;
import com.example.unipicityvibe.components.TopViewMenu;

public class RegisterActivity extends AppCompatActivity {

    private IAuthControl authControl;
    private EditTextView editTextViewName;
    private EditTextView editTextViewLastName;
    private EditTextView editTextViewEmail;
    private EditTextView editTextViewPassword;
    private EditTextView editTextViewPassword2;
    private TextView errorText;


    private void goHome(){
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void onComposeListenerButton(boolean success, String errorLog){
        if (!success){
            errorText.setText(errorLog);
        }
        else{
            goHome();
        }
    }

    private boolean validateRegister(String name, String lastName, String email, String password, String password2){
        if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || password2.isEmpty()){
            errorText.setText("Τhere are place who were not completed");
            return true;
        }
        if (!email.contains("@") || !email.contains(".")){
            errorText.setText("Email is not valid");
            return true;
        }
        if (!password.equals(password2)){
            errorText.setText("Password is not similar with repeat password");
            return true;
        }
        if (password.length() <= 6){
            errorText.setText("Password should have more that 6 characterw");
            return true;
        }

        return false;
    }

    private void goHomePageButton(View view){
        goHome();
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

        TopViewMenu tvm = findViewById(R.id.top_view_menu);
        tvm.setOnClickListener(this::goHomePageButton);

        Button reg = findViewById(R.id.buttonRegister);
        reg.setOnClickListener(this::registerButton);


    }
}
