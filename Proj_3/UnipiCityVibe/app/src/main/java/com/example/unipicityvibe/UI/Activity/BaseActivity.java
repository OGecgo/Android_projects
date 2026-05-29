package com.example.unipicityvibe.UI.Activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.unipicityvibe.Data.Local.AppSettings;
import com.example.unipicityvibe.R;

public class BaseActivity extends AppCompatActivity {


    // fragment manager transaction pages
    protected void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(AppSettings.wrapContext(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppSettings.applyDarkMode(AppSettings.isDarkMode(this));
        super.onCreate(savedInstanceState);
    }




}
