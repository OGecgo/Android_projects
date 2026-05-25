package com.example.unipicityvibe.UI.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.unipicityvibe.Data.Models.UserAuthData;
import com.example.unipicityvibe.Data.Models.UserData;
import com.example.unipicityvibe.R;
import com.example.unipicityvibe.Service.AuthService;
import com.example.unipicityvibe.Service.Interface.IAuthService;
import com.example.unipicityvibe.UI.DialogFragment.DialogDeleteUser;
import com.example.unipicityvibe.UI.Exception.ExceptionToMessage;
import com.example.unipicityvibe.Data.Local.AppSettings;

public class SettingsActivity extends BaseActivity {

    // ------ Page Change ------
    private void goLogInPage(){
        Intent intent=new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    // ------ End Page Change ------

    // ------ Call Back ------
    private void onCompleteListenerGetData(boolean success, String errorLog, UserData userData){
        if (success){
            TextView tv;
            tv = findViewById(R.id.textViewEmail);
            tv.setText(userData.email);
            tv = findViewById(R.id.textViewName);
            tv.setText(userData.name);
            tv = findViewById(R.id.textViewLastName);
            tv.setText(userData.last_name);
            return;
        }
        TextView errorText = findViewById(R.id.textViewError);
        errorText.setText(ExceptionToMessage.AuthExceptionToTextId(errorLog));
    }
    // ------ End Call Back ------

    // ------ Radio Group ------
    private void setSmallText(){
        AppSettings.setFontScale(getBaseContext(), 0.7f);
        finish();
        startActivity(getIntent());
    }

    private void setNormalText(){
        AppSettings.setFontScale(getBaseContext(), 1f);
        finish();
        startActivity(getIntent());
    }

    private void setLargeText(){
        AppSettings.setFontScale(getBaseContext(), 1.7f);
        finish();
        startActivity(getIntent());
    }

    private void sizeTextRadioGroupTextSize(RadioGroup radioGroup, int checkedId) {
        if (checkedId == R.id.radioButtonSmall) setSmallText();
        else if (checkedId == R.id.radioButtonNormal) setNormalText();
        else if (checkedId == R.id.radioButtonLarge) setLargeText();
    }
    // ------ End Radio Group ------

    // ------ Switch ------
    private void darkModeSwitch(CompoundButton compoundButton, boolean b){
        AppSettings.setDarkMode(getBaseContext(), b);
        finish();
        startActivity(getIntent());
    }
    // ------ End Switch ------

    // ------ Buttons ------
    private void deleteButton(View view){
        DialogDeleteUser ddu = new DialogDeleteUser();
        ddu.show(getSupportFragmentManager(), "DIALOG_DELETE_USER");
    }
    // ------ End Buttons ------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // test if user exist. if not. send to Log In page
        IAuthService service = AuthService.getInstance();
        UserAuthData userAuthData = service.getUserAuth();
        if (userAuthData.uID.isEmpty()){
            goLogInPage();
            return;
        }

        // get user data and set it
        UserData userData = new UserData();
        service.getUserData(userData, (success, errorLog) -> this.onCompleteListenerGetData(success, errorLog, userData));

        // get last settings and set to radio group and switch
        float scale = AppSettings.getFontScale(getBaseContext());
        RadioGroup rg = findViewById(R.id.radioGroupTextSize);
        if (scale < 1) rg.check(R.id.radioButtonSmall);
        else if (scale == 1) rg.check(R.id.radioButtonNormal);
        else rg.check(R.id.radioButtonLarge);

        rg.setOnCheckedChangeListener(this::sizeTextRadioGroupTextSize);

        boolean darkMode = AppSettings.isDarkMode(getBaseContext());
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch s = findViewById(R.id.switchDarkMode);
        s.setChecked(darkMode);
        s.setOnCheckedChangeListener(this::darkModeSwitch);

        // set delete button
        Button b = findViewById(R.id.buttonDelete);
        b.setOnClickListener(this::deleteButton);
    }

}
