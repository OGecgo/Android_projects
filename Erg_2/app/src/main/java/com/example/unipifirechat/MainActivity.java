package com.example.unipifirechat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.unipifirechat.Class.UserContr;
import com.example.unipifirechat.Interfaces.IUserContr;

public class MainActivity extends AppCompatActivity {

    IUserContr user;


    private void initButtons(){
        Button bUp = findViewById(R.id.buttonSignUp);
        bUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button bIn = findViewById(R.id.buttonSignIn);
        bIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);//
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Set User
        this.user = UserContr.getInstance();

        initButtons();
    }


     @Override
     public void onStart() {
         super.onStart();
         // Check if user is signed got to homepage.
         if(this.user.getSignIn()){
             Intent intent = new Intent(MainActivity.this, HomeActivity.class);
             startActivity(intent);
             finish();
         }
     }
}