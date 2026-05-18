package com.example.unipicityvibe.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.unipicityvibe.R;

import org.jetbrains.annotations.NotNull;

public class TopViewMenu extends ConstraintLayout {

    private void goHome(View l){
        // here write code what send me home
        // of if not logined. send me to login
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.top_view_menu, this);

        Button logo = findViewById(R.id.Logo);
        logo.setOnClickListener(this::goHome);
    }

    public TopViewMenu(@NotNull Context context){
        super(context);
        init(context);
    }
    public TopViewMenu(@NotNull Context context, @NotNull AttributeSet attrs){
        super(context, attrs);
        init(context);
    }
    public TopViewMenu(@NotNull Context context, @NotNull AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public TopViewMenu(@NotNull Context context, @NotNull AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }


}
