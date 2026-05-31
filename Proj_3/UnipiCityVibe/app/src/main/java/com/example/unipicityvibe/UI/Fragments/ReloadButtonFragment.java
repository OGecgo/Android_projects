package com.example.unipicityvibe.UI.Fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.unipicityvibe.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.widget.Toast;

public class ReloadButtonFragment extends Fragment {

    // ----- Button -----
    private void reloadParentFragmentButton(View view){
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            parentFragment.getParentFragmentManager().beginTransaction()
                    .detach(parentFragment)
                    .commit();
            parentFragment.getParentFragmentManager().beginTransaction()
                    .attach(parentFragment)
                    .commit();
        } else {
            Log.e(TAG, "[ReloadButtonFragment] Parent fragment not found");
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
