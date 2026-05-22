package com.example.unipicityvibe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.unipicityvibe.Class.AuthControl;
import com.example.unipicityvibe.Components.TopViewMenu;
import com.example.unipicityvibe.Struct.UserAuthStruct;

public class HomeActivity extends AppCompatActivity {

    private void goLoginPage(){
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void logOutButton(boolean success, String errorText){
        if (success){
            goLoginPage();
        }
        // i can show dialog with error
    }

    private void deleteButton(View view){
        goLoginPage();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        UserAuthStruct user = AuthControl.getInstance().getUserAuth();

        TopViewMenu topViewMenu = findViewById(R.id.topViewMenu);
        topViewMenu.setFragmentManager(getSupportFragmentManager());
        topViewMenu.setOnClickListenerButtonLogOut(this::logOutButton);
        topViewMenu.setOnClickListenerDeleteAccount(this::deleteButton);
    }
}
