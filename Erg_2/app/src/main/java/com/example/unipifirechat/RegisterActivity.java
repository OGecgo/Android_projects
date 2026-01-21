package com.example.unipifirechat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.unipifirechat.Class.UserContr;
import com.example.unipifirechat.Interfaces.IUserContr;

public class RegisterActivity extends AppCompatActivity {

    private IUserContr user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registration), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        // set user
//        user = new UserContr();
//
//        // data form
//        EditText email = findViewById(R.id.editTextEmail);
//        EditText password = findViewById(R.id.editTextPassword);
//
//        // button register
//        final Button bReg = findViewById(R.id.buttonRegister);
//        bReg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // read data
//                    String sEmail = String.valueOf(email.getText());
//                    String sPassword = String.valueOf(password.getText());
//                    // signin with data
//                    user.SignUp(sEmail, sPassword);
//                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                    startActivity(intent);
//                }
//            }
//        );

    }
}
