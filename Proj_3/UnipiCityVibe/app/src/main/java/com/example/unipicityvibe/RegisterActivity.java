package com.example.unipicityvibe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.unipicityvibe.Class.AuthControl;
import com.example.unipicityvibe.Constants.AuthControlException;
import com.example.unipicityvibe.Constants.UserAuthException;
import com.example.unipicityvibe.Constants.UserDBException;
import com.example.unipicityvibe.Interface.IAuthControl;
import com.example.unipicityvibe.components.EditTextView;
import com.example.unipicityvibe.components.TopViewMenu;

public class RegisterActivity extends AppCompatActivity {

    private IAuthControl authControl;
    private EditTextView editTextViewName;
    private EditTextView editTextViewLastName;
    private EditTextView editTextViewEmail;
    private EditTextView editTextViewPassword;
    private EditTextView editTextViewPassword2;
    private TextView errorText;



    private void goHome(){
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void onComposeListenerButton(boolean success, String errorLog){
        if (!success){
            // handle exception and convert it to text for user
            int textResId;
            switch (errorLog){
                // AuthControl exceptions
                case AuthControlException.USER_LOGGED_IN:
                    textResId = R.string.error_user_already_logged_in;
                    break;
                case AuthControlException.USER_NOT_LOGGED_IN:
                    textResId = R.string.error_user_not_logged_in;
                    break;

                // UserAuth & UserDB Validation
                case UserAuthException.EMPTY_EMAIL: // UserDBException.EMPTY_EMAIL
                    textResId = R.string.error_email_empty;
                    break;
                case UserAuthException.EMPTY_PASSWORD:
                    textResId = R.string.error_password_empty;
                    break;
                case UserAuthException.EMAIL_VALIDATION_ERROR: // UserDBException.EMAIL_VALIDATION_ERROR
                    textResId = R.string.error_email_invalid;
                    break;
                case UserDBException.EMPTY_UID:
                    textResId = R.string.error_uid_empty;
                    break;
                // UserAuth & UserDB Authentication
                case UserAuthException.ERROR_USER_CREATE: // UserDBException.ERROR_USER_CREATE
                    textResId = R.string.error_user_create_failed;
                    break;
                case UserAuthException.ERROR_USER_DELETE: // UserDBException.ERROR_USER_DELETE
                    textResId = R.string.error_user_delete_failed;
                    break;
                case UserAuthException.USER_NOT_EXIST: // UserDBException.USER_NOT_EXIST
                    textResId = R.string.error_user_not_exist;
                    break;

                // UserAuth exceptions
                case UserAuthException.ERROR_USER_SIGNIN:
                    textResId = R.string.error_signin_failed;
                    break;

                case UserAuthException.ERROR_USER_AUTHENTICATION:
                    textResId = R.string.error_auth_failed;
                    break;
                case UserAuthException.SIGNOUT_FAIL:
                    textResId = R.string.error_signout_failed;
                    break;

                
                // UserDB exceptions
                case UserDBException.NAME_EMPTY:
                    textResId = R.string.error_name_empty;
                    break;
                case UserDBException.LASTNAME_EMPTY:
                    textResId = R.string.error_lastname_empty;
                    break;
                case UserDBException.EMPTY_USER:
                    textResId = R.string.error_user_data_not_found;
                    break;
                case UserDBException.ERROR_GET_USER:
                    textResId = R.string.error_get_user_failed;
                    break;
                case UserDBException.TICKET_NOT_ADDED:
                    textResId = R.string.error_ticket_add_failed;
                    break;
                case UserDBException.EMPTY_TICKET:
                    textResId = R.string.error_ticket_not_found;
                    break;
                case UserDBException.ERROR_GET_TICKET:
                    textResId = R.string.error_get_ticket_failed;
                    break;
                case UserDBException.NO_TICKETS_FOUND:
                    textResId = R.string.error_no_tickets;
                    break;
                case UserDBException.ERROR_GET_TICKETS:
                    textResId = R.string.error_get_tickets_failed;
                    break;
                
                default:
                    textResId = R.string.error_unknown;
                    break;
            }

            errorText.setText(textResId);
        }
        else{
            goHome();
        }
    }

    private boolean validateRegister(String name, String lastName, String email, String password, String password2){
        if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || password2.isEmpty()){
            errorText.setText(R.string.error_all_fields_required);
            return true;
        }
        if (!email.contains("@") || !email.contains(".")){
            errorText.setText(R.string.error_email_invalid);
            return true;
        }
        if (!password.equals(password2)){
            errorText.setText(R.string.error_passwords_not_match);
            return true;
        }
        if (password.length() <= 6){
            errorText.setText(R.string.error_password_too_short);
            return true;
        }

        return false;
    }

    private void goHomePageButton(View view){
        goHome();
    }

    private void registerButton(View view){
        String password = editTextViewPassword.getEditText();
        String password2 = editTextViewPassword2.getEditText();
        String name = editTextViewName.getEditText();
        String lastName = editTextViewLastName.getEditText();
        String email = editTextViewEmail.getEditText();

        if(validateRegister(name, lastName, email, password, password2)) return;

        authControl.userRegister(email, password, name, lastName, this::onComposeListenerButton);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authControl = AuthControl.getInstance();

        editTextViewName = findViewById(R.id.editTextViewName);
        editTextViewLastName = findViewById(R.id.editTextViewLastName);
        editTextViewEmail = findViewById(R.id.editTextViewEmail);
        editTextViewPassword = findViewById(R.id.editTextViewPassword);
        editTextViewPassword2 = findViewById(R.id.editTextViewPassword2);
        errorText = findViewById(R.id.textViewError);

        TopViewMenu tvm = findViewById(R.id.top_view_menu);
        tvm.setOnClickListener(this::goHomePageButton);

        Button reg = findViewById(R.id.buttonRegister);
        reg.setOnClickListener(this::registerButton);


    }
}
