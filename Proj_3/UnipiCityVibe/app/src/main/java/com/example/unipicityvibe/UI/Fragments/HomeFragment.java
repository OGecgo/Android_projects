package com.example.unipicityvibe.UI.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.unipicityvibe.R;
import com.example.unipicityvibe.Service.Interface.RefFunction;

public class HomeFragment extends Fragment {

    private TextView errorText;
    private RefFunction eventListListener;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        errorText = view.findViewById(R.id.textViewError);

        Button btnEventList = view.findViewById(R.id.buttonEventList);
        btnEventList.setOnClickListener(v -> {
            if (eventListListener != null) eventListListener.execute();
        });
    }

    public void setEventListButton(@NonNull RefFunction l){
        eventListListener = l;
    }
}
