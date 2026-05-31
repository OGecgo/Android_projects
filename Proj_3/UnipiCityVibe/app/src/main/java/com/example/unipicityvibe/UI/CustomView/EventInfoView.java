package com.example.unipicityvibe.UI.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.unipicityvibe.Data.Models.EventData;
import com.example.unipicityvibe.R;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventInfoView extends ConstraintLayout {

    private SimpleDateFormat sdf;
    private TextView textViewTitle;
    private TextView textViewTime;

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.custom_event_info_view, this, true);

        sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewTime = findViewById(R.id.textViewTime);

        // Make the entire view clickable with a ripple effect
        setClickable(true);
        setFocusable(true);
    }

    // ----- Constructors -----
    public EventInfoView(@NonNull Context context){
        super(context);
        init(context);
    }

    public EventInfoView(@NonNull Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    public EventInfoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public EventInfoView(@NotNull Context context, @NotNull AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }
    // ----- End Constructors -----

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }


    public void setValues(@NonNull EventData eventData){
        // set title
        textViewTitle.setText(eventData.title);
        // set time
        long time = Long.parseLong(eventData.time);
        Date date = new Date(time);
        String text = sdf.format(date);
        textViewTime.setText(text);
    }



}
