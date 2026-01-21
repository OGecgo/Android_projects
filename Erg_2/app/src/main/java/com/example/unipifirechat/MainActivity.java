package com.example.unipifirechat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.unipifirechat.Class.UserContr;
import com.example.unipifirechat.Enums.AuthenticationRequest;
import com.example.unipifirechat.Interfaces.IUserContr;

import org.jspecify.annotations.NonNull;

public class MainActivity extends AppCompatActivity {

    private IUserContr user;
    private void loadHome(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Set User
//        user = new UserContr();
//        // if user singIn
//        if (user.getSignIn()){
//            TextView textView = findViewById(R.id.UserName);
//            textView.setText("username"); // debug
//        }

        // Intent request
//        Intent intent = getIntent();
//        if (intent != null){
//
//        }

//        Button bReg = findViewById(R.id.buttonRegistration);
//        bReg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
//                startActivity(intent);
//            }
//        });
//    }


//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        if(user.getSignIn()){
//            loadHome();
//        }
    }
}