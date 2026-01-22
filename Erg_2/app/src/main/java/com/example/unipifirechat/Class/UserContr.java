package com.example.unipifirechat.Class;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.unipifirechat.Interfaces.AuthCallback;
import com.example.unipifirechat.Interfaces.IUserContr;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class UserContr implements  IUserContr{
    private static IUserContr userInst;

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;



    // private
    // --constructor
    private UserContr(){
        this.user = mAuth.getCurrentUser();
    }

    // --var
    private String ErrorLog(Exception e) {
        if (e instanceof FirebaseAuthException){
            String error = ((FirebaseAuthException)e).getErrorCode();
            switch (error){
                case "ERROR_INVALID_EMAIL":
                    return "Invalid Email";
                case "ERROR_WEAK_PASSWORD":
                    return "Weak Password";
                case "ERROR_EMAIL_ALREADY_IN_USE":
                    return "That Email Already In Use";

                case "ERROR_USER_NOT_FOUND":
                    return "That User Not Found";
                case "ERROR_WRONG_PASSWORD":
                    return "Wrong Password";

                case "ERROR_USER_DISABLED":
                    return "User Disabled";
                case "ERROR_NETWORK_REQUEST_FAILED":
                    return "Network Request Failed";
                case "ERROR_TOO_MANY_REQUESTS":
                    return "Too Many Requests";

                default:
                    return "Unknow Error";
            }
        }
        return "";
    }



    // public

    // --var
    public boolean getSignIn(){ return user != null; }

    // singleton
    public static IUserContr getInstance(){
        if (userInst == null){
            userInst = new UserContr();
        }
        return userInst;
    }

    // --methods



    @Override
    public void SignUp(String email, String password, AuthCallback authCB){
        if (email == null || Objects.equals(email, "") || password == null || Objects.equals(password, "") ){
            Log.w(TAG, "NULL OBJECT EMAIL-PASSWORD");
            authCB.onCompose(false, "Please Fill In All Fields");
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmail:success");
                            user = mAuth.getCurrentUser();
                            authCB.onCompose(true, "");
                        }
                        else { // Error authentication
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            authCB.onCompose(false, ErrorLog(task.getException()));
                        }
                    }
                }
        );

    }
    public void SignIn(String email, String password, AuthCallback authCB){
        if (email == null || Objects.equals(email, "") || password == null || Objects.equals(password, "") ){
            Log.w(TAG, "NULL OBJECT EMAIL-PASSWORD");
            authCB.onCompose(false, "Please Fill In All Fields");
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            user = mAuth.getCurrentUser();
                            authCB.onCompose(true, "");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            authCB.onCompose(false, ErrorLog(task.getException()));
                        }
                    }
                });
    }
    public void LogOut(AuthCallback authCB){
        try {
            FirebaseAuth.getInstance().signOut();
            user = mAuth.getCurrentUser();
            Log.d(TAG, "logoutUser:success");
            authCB.onCompose(true, "");
        } catch (Exception e) { // error logout
            Log.d(TAG, "logoutUser:failure");
            authCB.onCompose(false, ErrorLog(e));
        }

    }
}
