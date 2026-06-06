package com.example.unipiaudiostories.UI.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.unipiaudiostories.R;
import com.example.unipiaudiostories.Utils.PageMovementHelper;

public class HomeActivity extends BaseActivity {

    private void onClickListenerStories(View view){
        PageMovementHelper.moveToStoriesActivity(this);
    }
    private void onClickListenerStatistics(View view){
        PageMovementHelper.moveToStatisticsActivity(this);
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

        Button b;
        b = findViewById(R.id.buttonStories);
        b.setOnClickListener(this::onClickListenerStories);
        b = findViewById(R.id.buttonStatistics);
        b.setOnClickListener(this::onClickListenerStatistics);
    }
}