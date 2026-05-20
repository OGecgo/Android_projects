package com.example.unipicityvibe.Class.firebase;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Interface.OnCompleteListener;
import com.example.unipicityvibe.Interface.firebase.IUserAuth;
import com.example.unipicityvibe.Struct.UserAuthStruct;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class UserAuth implements IUserAuth {

    private final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private static UserAuth user;

    private UserAuth(){}

    // return true if not valid input
    private boolean emailPasswordTest(String email, String password, @NonNull OnCompleteListener l){
        // empty values
        if (Objects.equals(email, "") || email == null){
            l.onCompose(false, "Email can not be empty");
            return true;
        }
        if (Objects.equals(password, "") || password == null){
            l.onCompose(false, "Password can not be empty");
            return true;
        }
        // simple email validation
        if (!email.contains("@") || !email.contains(".")){
            l.onCompose(false, "Email validation error");
            return true;
        }
        return false;
    }

    private void onCompleteListenerCreatedUser(@NonNull Task<AuthResult> task, @NonNull OnCompleteListener l){
        if (task.isSuccessful()){
            Log.d(TAG, "Create User With Email:: Success");
            l.onCompose(true, "");
        }
        else{
            Log.w(TAG, "Create User With Email:: Failure\n", task.getException());
            l.onCompose(false, "Error user create");
        }
    }
    private void onCompleteListenerSingIn(@NonNull Task<AuthResult> task, @NonNull OnCompleteListener l){
        if (task.isSuccessful()){
            Log.d(TAG, "SingIn User With Email:: Success");
            l.onCompose(true, "");
        }
        else{
            Log.w(TAG, "SingIn User With Email:: Failure\n", task.getException());
            l.onCompose(false, "Error user singIn");
        }
    }
    private void onCompleteListenerDelete(@NonNull Task<Void> task, @NonNull OnCompleteListener l){
        if (task.isSuccessful()){
            Log.d(TAG, "Delete User:: Success");
            l.onCompose(true, "");
        }
        else{
            Log.w(TAG, "Delete User:: Failure\n", task.getException());
            l.onCompose(false, "Error user delete");
        }
    }
    private void onCompleteListenerReauthenticate(@NonNull Task<Void> task, @NonNull OnCompleteListener l){
        if (task.isSuccessful()){
            Log.d(TAG, "Authentication:: Success");
            // onCompleteListenerReauthenticate called only if current User is not null
            fAuth.getCurrentUser().delete().addOnCompleteListener((task2) -> this.onCompleteListenerDelete(task2, l));
        }
        else{
            Log.w(TAG, "Authentication:: Failure\n", task.getException());
            l.onCompose(false, "Error user authentication");
        }
    }


    public static UserAuth getInstance(){
        if (user == null) user = new UserAuth();
        return user;
    }
    @Override
    @NonNull
    public UserAuthStruct getUser(){
        UserAuthStruct sUserAuth = new UserAuthStruct();

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
            Log.d(TAG, "LogOut User:: Success");
            l.onCompose(true, "");
        } catch (Exception e) { // error logout
            Log.d(TAG, "LogOut User:: Failure");
            l.onCompose(false, "SingOut fail");
        }
    }
    @Override
    public void deleteUser(String password, @NonNull OnCompleteListener l){
        FirebaseUser userAuth = fAuth.getCurrentUser();
        if (userAuth == null){
            l.onCompose(false, "User not exist");
            return;
        }
        String email = userAuth.getEmail();
        if (emailPasswordTest(email, password, l)) return;
        // email tested into emailPasswordTest for null
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        userAuth.reauthenticate(credential).addOnCompleteListener((task) -> this.onCompleteListenerReauthenticate(task, l));
    }
}
