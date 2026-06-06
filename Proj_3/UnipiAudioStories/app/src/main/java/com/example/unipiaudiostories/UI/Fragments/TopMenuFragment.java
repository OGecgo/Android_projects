package com.example.unipiaudiostories.UI.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.unipiaudiostories.R;
import com.example.unipiaudiostories.Utils.PageMovementHelper;

public class TopMenuFragment extends Fragment {


    private void onClickListenerLogo(View view){
        PageMovementHelper.moveToHomeActivity(requireActivity());
    }
    private void onClickListenerBack(View view){
        PageMovementHelper.moveBack(getParentFragmentManager(), requireActivity());
    }


    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button logo = view.findViewById(R.id.buttonLogo);
        logo.setOnClickListener(this::onClickListenerLogo);
        ImageButton back = view.findViewById(R.id.imageButtonBack);
        back.setOnClickListener(this::onClickListenerBack);
    }


}
