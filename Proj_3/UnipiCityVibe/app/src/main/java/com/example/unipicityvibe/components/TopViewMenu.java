package com.example.unipicityvibe.components;


import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.unipicityvibe.MainActivity;
import com.example.unipicityvibe.R;
import com.example.unipicityvibe.RegisterActivity;

import org.jetbrains.annotations.NotNull;

public class TopViewMenu extends ConstraintLayout {

    private Button logo;
    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.top_view_menu, this);
        logo = findViewById(R.id.Logo);
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

    @Override
    public void setOnClickListener(View.OnClickListener l){
        logo.setOnClickListener(l);
    }


}
