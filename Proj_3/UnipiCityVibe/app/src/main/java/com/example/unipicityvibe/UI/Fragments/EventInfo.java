package com.example.unipicityvibe.UI.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.unipicityvibe.Data.Models.EventData;
import com.example.unipicityvibe.R;


public class EventInfo extends Fragment {

    TextView textViewTitle;
    TextView textViewDifferent; // km
    TextView textViewTime; // dd/mm/yyyy h:m


    private void setTextViewTime(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String dateString = sdf.format(new Date(time));
        textViewTime.setText(dateString);
    }
    private void setTextViewDifferent(){
        // first i should understand how to work with gps
    }

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewTitle = getView().findViewById(R.id.textViewTitle);
        textViewDifferent = getView().findViewById(R.id.textViewDifferent);
        textViewTime = getView().findViewById(R.id.textViewTime);
    }

    public void setValues(@NonNull EventData eventData){
        textViewTitle.setText(eventData.title);
        // if time saved wrong
        try {
            long time = Long.parseLong(eventData.time);
            setTextViewTime(time);
        }catch (Exception e){
            textViewTime.setText(eventData.time);
        }

    }
}
