package com.example.unipicityvibe.UI.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.unipicityvibe.R;
import com.example.unipicityvibe.Listeners.RefFunctionListener;

public class HomeFragment extends Fragment {

    private RefFunctionListener eventListListener;
    private RefFunctionListener eventMapListener;
    private RefFunctionListener myTicketListener;


    // ----- Buttons -----
    private void eventListButton(View view){
        if (eventListListener != null) eventListListener.execute();
    }
    private void eventMapButton(View view){
        if (eventMapListener != null) eventMapListener.execute();
    }
    private void myTicketButton(View view){
        if (myTicketListener != null) myTicketListener.execute();
    }
    // ----- End Buttons -----

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button b;
        b = view.findViewById(R.id.buttonEventList);
        b.setOnClickListener(this::eventListButton);
        b = view.findViewById(R.id.buttonEventMap);
        b.setOnClickListener(this::eventMapButton);
        b = view.findViewById(R.id.buttonMyTickets);
        b.setOnClickListener(this::myTicketButton);

    }

    public void setEventListButton(@NonNull RefFunctionListener l){
        eventListListener = l;
    }
    public void setEventMapButton(@NonNull RefFunctionListener l){
        eventMapListener = l;
    }
    public void setMyTicketButton(@NonNull RefFunctionListener l){
        myTicketListener = l;
    }

}
