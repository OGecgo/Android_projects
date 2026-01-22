package com.example.unipifirechat.Class;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

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
    private String ErrorLog;

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;

    private boolean signIn;
    private boolean successExe;


    // private
    // --constructor
    private UserContr(){
        this.user = mAuth.getCurrentUser();
        if (this.user != null){
            this.signIn = true;
            return;
        }
        this.signIn = false;
        this.ErrorLog = "";
        this.successExe = true;
    }

    // --var
    private void setError(Exception e) {
        if (e instanceof FirebaseAuthException){
            String error = ((FirebaseAuthException)e).getErrorCode();
            switch (error){
                case "ERROR_INVALID_EMAIL":
                    this.ErrorLog = "Invalid Email";
                    break;
                case "ERROR_WEAK_PASSWORD":  // not end for password
                    this.ErrorLog = "Weak Password";
                    break;
                case "ERROR_EMAIL_ALREADY_IN_USE":
                    this.ErrorLog = "That Email Already In Use";
                    break;

                case "ERROR_USER_NOT_FOUND":
                    this.ErrorLog = "That User Not Found";
                    break;
                case "ERROR_WRONG_PASSWORD":
                    this.ErrorLog = "Wrong Password";
                    break;

                case "ERROR_USER_DISABLED":
                    this.ErrorLog = "User Disabled";
                    break;
                case "ERROR_NETWORK_REQUEST_FAILED":
                    this.ErrorLog = "Network Request Failed";
                    break;
                case "ERROR_TOO_MANY_REQUESTS":
                    this.ErrorLog = "Too Many Requests";
                    break;

                default:
                    this.ErrorLog = "Unknow";
                    break;
            }
        }
    }

    // --methods

    private void updateSingUp(Exception e){
        if (e == null){
            this.successExe = true;
            return;
        }
        setError(e);
    }

    // public



    // --var
    public boolean getSignIn(){ return this.signIn;}

    public String getErrorLog(){ return this.ErrorLog; }
    public static IUserContr getInstance(){
        if (userInst == null){
            userInst = new UserContr();
        }
        return userInst;
    }

    // --methods

    @Override
    public void SignUp(String email, String password, AuthCallback authCB){
        this.successExe = false;
        if (email == null || Objects.equals(email, "") || password == null || Objects.equals(password, "") ){
            this.ErrorLog = "Please Fill In All Fields";
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
                        else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            updateSingUp(task.getException());
                            authCB.onCompose(false, "error");
                        }
                    }
                }
        );

        if (this.successExe){
            this.signIn = true;
        }
        return;
    }
    public boolean SignIn(String email, String password){
        if (this.successExe){
            this.signIn = true;
        }
        return this.successExe;
    }
    public boolean LogOut(){
        if (this.successExe){
            this.signIn = false;
        }
        return this.successExe;

    }
    public boolean DeleteUser(String password){
        if (this.successExe){
            this.signIn = false;
        }
        return this.successExe;
    }
}
