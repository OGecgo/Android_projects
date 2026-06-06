package com.example.unipiaudiostories.UI.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.unipiaudiostories.R;

public class StoryRow extends ConstraintLayout {

    private ImageView imageView;
    private TextView textViewTitle;

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.custome_story_row, this, true);
        imageView = findViewById(R.id.imageView);
        textViewTitle = findViewById(R.id.textViewTitle);

        // Ensure the entire view is clickable
        setClickable(true);
        setFocusable(true);
    }



    public StoryRow(@NonNull Context context) {
        super(context);
        init(context);
    }

    public StoryRow(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StoryRow(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }



    public void setTitle(String title) {
        if (textViewTitle != null) {
            textViewTitle.setText(title);
        }
    }


    public void setImageUrl(String url) {
        if (imageView != null && url != null && !url.isEmpty()) {
            Glide.with(getContext())
                    .load(url)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imageView);
        }
    }
}
