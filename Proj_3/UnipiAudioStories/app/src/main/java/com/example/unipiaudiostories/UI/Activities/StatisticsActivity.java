package com.example.unipiaudiostories.UI.Activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.unipiaudiostories.Data.Local.StatisticsStorage;
import com.example.unipiaudiostories.R;

public class StatisticsActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistics);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.statistics), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tv;
        tv = findViewById(R.id.textViewTotalContext);
        String count = Integer.toString(StatisticsStorage.getCountListens(this));
        tv.setText(count);

        // length == 3
        String[] topStories = StatisticsStorage.getTopThreeStories(this);
        // top can be empty if user do not still listen any story
        if (!topStories[0].isEmpty()){
            tv = findViewById(R.id.textViewTop1);
            tv.setText(topStories[0]);
        }
        if (!topStories[1].isEmpty()){
            tv = findViewById(R.id.textViewTop2);
            tv.setText(topStories[1]);
        }
        if (!topStories[2].isEmpty()){
            tv = findViewById(R.id.textViewTop3);
            tv.setText(topStories[2]);
        }

    }


}
