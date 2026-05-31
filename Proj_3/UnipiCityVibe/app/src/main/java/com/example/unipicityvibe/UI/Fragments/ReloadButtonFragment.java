package com.example.unipicityvibe.UI.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.unipicityvibe.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ReloadButtonFragment extends Fragment {

    // ----- Button -----
    private void reloadParentFragmentButton(View view){
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            parentFragment.getParentFragmentManager().beginTransaction()
                    .detach(parentFragment)
                    .commitNow();
            parentFragment.getParentFragmentManager().beginTransaction()
                    .attach(parentFragment)
                    .commitNow();
        }
    }
    // ----- End Button -----

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reload_button, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton reloadBtn = view.findViewById(R.id.floatingActionButton);
        reloadBtn.setOnClickListener(this::reloadParentFragmentButton);
    }


}
