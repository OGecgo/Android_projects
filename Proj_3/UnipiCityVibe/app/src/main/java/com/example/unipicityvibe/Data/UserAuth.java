package com.example.unipicityvibe.Data;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Data.Exception.UserAuthException;
import com.example.unipicityvibe.Data.Interface.IUserAuth;
import com.example.unipicityvibe.Data.Models.UserAuthData;
import com.example.unipicityvibe.Listeners.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

// Singleton Class
public class UserAuth implements IUserAuth {

    private final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private static UserAuth user;

    private UserAuth(){}

    // returns true if the input is invalid
    private boolean emailPasswordTest(String email, String password, @NonNull OnCompleteListener l){
        // empty values
        if (Objects.equals(email, "") || email == null){
            Log.w(TAG, "[UserAuth] User email is empty");
            l.onCompose(false, UserAuthException.EMPTY_EMAIL);
            return true;
        }
        if (Objects.equals(password, "") || password == null){
            Log.w(TAG, "[UserDB] User password is empty");
            l.onCompose(false, UserAuthException.EMPTY_PASSWORD);
            return true;
        }
        // simple email validation
        if (!email.contains("@") || !email.contains(".")){
            Log.w(TAG, "[UserAuth] User email validation error");
            l.onCompose(false, UserAuthException.EMAIL_VALIDATION_ERROR);
            return true;
        }
        return false;
    }

    // ----- Call Backs ------
    private void onCompleteListenerCreatedUser(@NonNull Task<AuthResult> task, @NonNull OnCompleteListener l){
        if (task.isSuccessful()){
            Log.d(TAG, "[UserAuth] User account created successfully");
            l.onCompose(true, "");
        }
        else{
            Log.e(TAG, "[UserAuth] Failed to create user account", task.getException());
            l.onCompose(false, UserAuthException.ERROR_USER_CREATE);
        }
    }
    private void onCompleteListenerSingIn(@NonNull Task<AuthResult> task, @NonNull OnCompleteListener l){
        if (task.isSuccessful()){
            Log.d(TAG, "[UserAuth] User signed in successfully");
            l.onCompose(true, "");
        }
        else{
            Log.e(TAG, "[UserAuth] Failed to sign in user", task.getException());
            l.onCompose(false, UserAuthException.ERROR_USER_SIGNIN);
        }
    }
    private void onCompleteListenerDelete(@NonNull Task<Void> task, @NonNull OnCompleteListener l){
        if (task.isSuccessful()){
            Log.d(TAG, "[UserAuth] User account deleted successfully");
            l.onCompose(true, "");
        }
        else{
            Log.e(TAG, "[UserAuth] Failed to delete user account", task.getException());
            l.onCompose(false, UserAuthException.ERROR_USER_DELETE);
        }
    }
    private void onCompleteListenerReauthenticate(@NonNull Task<Void> task, @NonNull OnCompleteListener l){
        if (task.isSuccessful()){
            Log.d(TAG, "[UserAuth] User re-authentication successful, proceeding with deletion");
            // onCompleteListenerReauthenticate called only if current User is not null
            fAuth.getCurrentUser().delete().addOnCompleteListener((task2) -> this.onCompleteListenerDelete(task2, l));
        }
        else{
            Log.e(TAG, "[UserAuth] User re-authentication failed", task.getException());
            l.onCompose(false, UserAuthException.ERROR_USER_AUTHENTICATION);
        }
    }
    // ------ End Call Backs ------

    public static UserAuth getInstance(){
        if (user == null) user = new UserAuth();
        return user;
    }
    @Override
    @NonNull
    public UserAuthData getUserAuth(){
        UserAuthData sUserAuth = new UserAuthData();

        FirebaseUser userAuth = fAuth.getCurrentUser();
        if (userAuth == null){
            sUserAuth.uID = "";
            sUserAuth.email = "";
        }
        else {
            sUserAuth.uID = userAuth.getUid();
            sUserAuth.email = userAuth.getEmail();
        }
        return sUserAuth;
    }
    @Override
    public FirebaseUser getFirebaseUser(){
        return fAuth.getCurrentUser();
    }
    @Override
    public void createUser(String email, String password, @NonNull OnCompleteListener l){
        if (emailPasswordTest(email, password, l)) return;

        String tEmail = email.trim();
        String tPassword = password.trim();
        fAuth.createUserWithEmailAndPassword(tEmail, tPassword).addOnCompleteListener( (task -> this.onCompleteListenerCreatedUser(task, l)) );
    }
    @Override
    public void singIn(String email, String password, @NonNull OnCompleteListener l){
        if (emailPasswordTest(email, password, l)) return;

        String tEmail = email.trim();
        String tPassword = password.trim();
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener( (task) -> this.onCompleteListenerSingIn(task, l) );
    }
    @Override
    public void singOut(OnCompleteListener l){
        try {
            FirebaseAuth.getInstance().signOut();
            Log.d(TAG, "[UserAuth] User signed out successfully");
            l.onCompose(true, "");
        } catch (Exception e) { // error logout
            Log.e(TAG, "[UserAuth] Failed to sign out user", e);
            l.onCompose(false, UserAuthException.SIGNOUT_FAIL);
        }
    }
    @Override
    public void deleteUser(String password, @NonNull OnCompleteListener l){
        FirebaseUser userAuth = fAuth.getCurrentUser();
        if (userAuth == null){
            Log.w(TAG, "[UserAuth] User not exist");
            l.onCompose(false, UserAuthException.USER_NOT_EXIST);
            return;
        }
        String email = userAuth.getEmail();
        if (emailPasswordTest(email, password, l)) return;
        // email tested into emailPasswordTest for null
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        userAuth.reauthenticate(credential).addOnCompleteListener((task) -> this.onCompleteListenerReauthenticate(task, l));
    }
}
