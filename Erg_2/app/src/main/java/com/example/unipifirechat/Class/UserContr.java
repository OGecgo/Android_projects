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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class UserContr implements  IUserContr{
    private static IUserContr userInst;

    private final FirebaseDatabase mDB = FirebaseDatabase.getInstance();
    private DatabaseReference userDB;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;



    // private
    // --constructor
    private UserContr(){
        this.user = mAuth.getCurrentUser();
        this.userDB = mDB.getReference();
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

    // --methods

    // RTDB keys cannot contain: . # $ [ ] /
    private static String emailToKey(String email) {
        return email.trim().toLowerCase()
                .replace(".", ",")
                .replace("#", ",")
                .replace("$", ",")
                .replace("[", ",")
                .replace("]", ",")
                .replace("/", ",");
    }

    private void addUserToDatabase(String username) {
        // data
        String uid = user.getUid();
        // email username
        String email = user.getEmail();
        if (email == null || username == null) return;
        email = emailToKey(email); // Email@emMil.email -> email@email,email


        Map<String, Object> update = new HashMap<>();
        update.put("users/" + uid + "/username", username);
        update.put("users/" + uid + "/email", email);

        update.put("usernames/" + username, uid);
        update.put("emails/" + email, uid);

        userDB.updateChildren( update, (error, ref) -> { if (error != null) Log.w(TAG, error.getMessage()); });
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
    public void SignUp(String username, String email, String password, AuthCallback authCB){

        // check for errors null
        if (username == null || Objects.equals(username, "") || email == null || Objects.equals(email, "") || password == null || Objects.equals(password, "") ){
            Log.w(TAG, "NULL OBJECT EMAIL PASSWORD OR USERNAME");
            authCB.onCompose(false, "Please Fill In All Fields");
            return;
        }

        // check for properly symbols
        String nUsername = username.trim();
        String nEmail = email.trim();
        String nPassword = password.trim();
        if (!nUsername.matches("^[a-zA-Z0-9_]{3,20}$")){ // check for properly username
            Log.w(TAG, "USERNAME HAS ERROR SYMBOLS");
            authCB.onCompose(false, "Username Have Error Symbols");
            return;
        }


        // first read from database for check user
        userDB.child("usernames").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                // error read
                if (!task.isComplete()) {
                    Log.w(TAG, task.getException());
                    return;
                }
                // username is exist ?
                if (task.getResult().exists()){
                    Log.w(TAG, "USERNAME ALREADY EXIST");
                    authCB.onCompose(false, "That Username Already Exist");
                    // if user exist. don't create user
                    return;
                }

                // authentication
                mAuth.createUserWithEmailAndPassword(nEmail, nPassword).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Log.d(TAG, "createUserWithEmail:success");
                                    user = mAuth.getCurrentUser();
                                    addUserToDatabase(nUsername);
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
        });


    }

    @Override
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

    @Override
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
