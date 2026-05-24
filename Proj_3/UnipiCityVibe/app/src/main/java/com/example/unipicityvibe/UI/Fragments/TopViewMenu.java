package com.example.unipicityvibe.UI.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.unipicityvibe.Service.AuthService;
import com.example.unipicityvibe.UI.PopupMenu.PopUpMenuAccount;
import com.example.unipicityvibe.UI.Activity.HomeActivity;
import com.example.unipicityvibe.Service.Interface.IAuthService;
import com.example.unipicityvibe.UI.Activity.MainActivity;
import com.example.unipicityvibe.R;
import com.example.unipicityvibe.Data.Models.UserAuthData;


public class TopViewMenu extends Fragment {


    private PopUpMenuAccount popUpMenuAccount;
    private Button buttonName;

    // ------ Buttons ------
    private void logoButtonLogIn(View view){
        if (requireContext().getClass().equals(MainActivity.class)) return;
        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
    }
    private void logoButtonHome(View view){
        if (requireContext().getClass().equals(HomeActivity.class)) return;
        Intent intent = new Intent(requireContext(), HomeActivity.class);
        startActivity(intent);
    }
    private void nameButton(View view){
        popUpMenuAccount.showAsDropDown(buttonName);
    }
    // ------ End Buttons ------

    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_view_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialize popupmenu for button buttonName
        popUpMenuAccount = new PopUpMenuAccount(getContext());

        // initialization for testing.
        // If user exist. buttonLogo will do transfer to Home page and buttonName will work
        // If not. buttonLogo will do transfer to LogIn page
        IAuthService authService = AuthService.getInstance();
        UserAuthData user = authService.getUserAuth();

        Button buttonLogo = getView().findViewById(R.id.buttonLogo);
        buttonName  = getView().findViewById(R.id.buttonName);

        if (!user.uID.isEmpty()) {
            buttonName.setText(user.email);
            buttonName.setOnClickListener(this::nameButton);
            buttonLogo.setOnClickListener(this::logoButtonHome);
        }
        else{
            buttonLogo.setOnClickListener(this::logoButtonLogIn);
        }
    }




}







