package com.example.unipicityvibe.UI.DialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.unipicityvibe.Data.Local.AppSettings;
import com.example.unipicityvibe.R;

public class LanguageDialogFragment extends DialogFragment {

    private void changeLanguage(String lang) {
        AppSettings.setLanguage(requireContext(), lang);
        // Recreate activity to apply changes
        if (getActivity() != null) {
            getActivity().recreate();
        }
        dismiss();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_language, container, false);

        Button btnEnglish = view.findViewById(R.id.buttonEnglish);
        Button btnGreek = view.findViewById(R.id.buttonGreek);
        Button btnSpanish = view.findViewById(R.id.buttonRussian);

        btnEnglish.setOnClickListener(v -> changeLanguage("en"));
        btnGreek.setOnClickListener(v -> changeLanguage("el"));
        btnSpanish.setOnClickListener(v -> changeLanguage("ru"));

        return view;
    }
}
