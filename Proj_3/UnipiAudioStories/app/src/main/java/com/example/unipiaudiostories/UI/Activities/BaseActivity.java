package com.example.unipiaudiostories.UI.Activities;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.unipiaudiostories.Data.Local.SettingsStorage;
import com.example.unipiaudiostories.R;

public class BaseActivity extends AppCompatActivity {

    // fragment manager transaction pages
    protected void replaceFragment(int id_fragmentContainer,Fragment fragment, String tag, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(id_fragmentContainer, fragment, tag);
        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(SettingsStorage.wrapContext(newBase));
    }

}
