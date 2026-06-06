package com.example.unipiaudiostories.UI.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.unipiaudiostories.Data.Local.SettingsStorage;
import com.example.unipiaudiostories.R;
import com.example.unipiaudiostories.Utils.PageMovementHelper;

public class LanguageDialogFragment extends DialogFragment {

    private void changeLanguage(String lang) {
        SettingsStorage.setLanguage(requireContext(), lang);
        // Recreate activity to apply changes
        PageMovementHelper.reloadActivity(requireActivity());
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
