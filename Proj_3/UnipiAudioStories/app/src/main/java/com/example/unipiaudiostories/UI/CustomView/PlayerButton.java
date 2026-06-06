package com.example.unipiaudiostories.UI.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.unipiaudiostories.R;

public class PlayerButton extends ConstraintLayout{


    private ImageButton imageButtonStart, imageButtonStop, imageButtonReset;

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.custome_player_buttons, this, true);
        imageButtonStart = findViewById(R.id.imageButtonStart);
        imageButtonStop = findViewById(R.id.imageButtonStop);
        imageButtonReset = findViewById(R.id.imageButtonReset);

        // Ensure the entire view is clickable
        setClickable(true);
        setFocusable(true);
    }

    public PlayerButton(@NonNull Context context) {
        super(context);
        init(context);
    }

    public PlayerButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayerButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public void setOnClickListenerButtonStart(@Nullable OnClickListener l) {
        imageButtonStart.setOnClickListener(l);
    }
    public void setOnClickListenerButtonStop(@Nullable OnClickListener l) {
        imageButtonStop.setOnClickListener(l);
    }
    public void setOnClickListenerButtonReset(@Nullable OnClickListener l) {
        imageButtonReset.setOnClickListener(l);
    }
}
