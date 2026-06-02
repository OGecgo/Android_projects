package com.example.unipicityvibe.Data;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Data.Exception.UserDBException;
import com.example.unipicityvibe.Data.Interface.IUserDB;
import com.example.unipicityvibe.Listeners.OnCompleteListener;
import com.example.unipicityvibe.Data.Models.UserAuthData;
import com.example.unipicityvibe.Data.Models.UserData;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

// Singleton Class
public class UserDB implements IUserDB {

    private final DatabaseReference userDB;
    private static UserDB user;

    private UserDB(){
        this.userDB = FirebaseDatabase.getInstance().getReference();
    }

    // returns true if the input is invalid
    private boolean userTestValues(@NonNull UserAuthData user, @NonNull OnCompleteListener l){
        // empty value
        if (user.email.isEmpty()){
            Log.w(TAG, "[UserDB] User email is empty");
            l.onCompose(false, UserDBException.EMPTY_EMAIL);
            return true;
        }
        if (user.uID.isEmpty()){
            Log.w(TAG, "[UserDB] User does not exist");
            l.onCompose(false, UserDBException.EMPTY_UID);
            return true;
        }
        // simple email validation
        if (!user.email.contains("@") && !user.email.contains(".")){
            Log.w(TAG, "[UserDB] User email validation error");
            l.onCompose(false, UserDBException.EMAIL_VALIDATION_ERROR);
            return true;
        }
        return false;
    }

    // ------ Call Back ------
    private void onCompleteListenerGetUserData(@NotNull Task<DataSnapshot> task, @NonNull OnCompleteListener l, UserData userData){
        if (task.isSuccessful()){
            if (task.getResult().exists()){
               Log.d(TAG, "[UserDB] User data retrieved successfully");

               DataSnapshot snapshot = task.getResult();
               userData.email = snapshot.child("email")        .getValue() != null ? String.valueOf(snapshot.child("email")    .getValue()) : "";
               userData.name = snapshot.child("name")          .getValue() != null ? String.valueOf(snapshot.child("name")     .getValue()) : "";
               userData.last_name = snapshot.child("last_name").getValue() != null ? String.valueOf(snapshot.child("last_name").getValue()) : "";

               l.onCompose(true, "");
            }
            else{
                Log.w(TAG, "[UserDB] User data not found in database");
                l.onCompose(false, UserDBException.EMPTY_USER);
            }
        }
        else{
            Log.e(TAG, "[UserDB] Failed to retrieve user data", task.getException());
            l.onCompose(false, UserDBException.ERROR_GET_USER);
        }
    }

    private void onCompleteListenerAddUser(@NonNull Task<Void> task, @NonNull OnCompleteListener l){
        if (task.isSuccessful()){
            Log.d(TAG, "[UserDB] User profile saved to database successfully");
            l.onCompose(true, "");
        }
        else{
            Log.e(TAG, "[UserDB] Failed to save user profile to database", task.getException());
            l.onCompose(false, UserDBException.ERROR_USER_CREATE);
        }
    }

    private void onCompleteListenerReauthenticate(Task<Void> task, String uID, @NonNull OnCompleteListener l){
        if (task.isSuccessful()) {
            userDB.child("users").child(uID).removeValue().addOnCompleteListener((task_t) -> this.onCompleteListenerDeleteUser(task_t, l));
        }
        else{
            Log.w(TAG, "[UserDB] User authentication error", task.getException());
            l.onCompose(false, UserDBException.ERROR_USER_AUTHENTICATION);
        }
    }
    private void onCompleteListenerDeleteUser(@NonNull Task<Void> task, @NonNull OnCompleteListener l){
        if (task.isSuccessful()){
            Log.d(TAG, "[UserDB] User profile deleted from database successfully");
            l.onCompose(true, "");
        }
        else{
            Log.e(TAG, "[UserDB] Failed to delete user profile from database", task.getException());
            l.onCompose(false, UserDBException.ERROR_USER_DELETE);
        }
    }

    // ------ End Call Back ------

    public static UserDB getInstance(){
        if (user == null) user = new UserDB();
        return user;
    }
    @Override
    public void getUserData(@NonNull UserAuthData user, @NonNull UserData userRef, @NonNull OnCompleteListener l){
        if (user.uID.isEmpty()){
            Log.w(TAG, "[UserDB] User properties are empty");
            l.onCompose(false, UserDBException.EMPTY_USER);
            return;
        }

        userDB.child("users").child(user.uID).get().addOnCompleteListener(task -> onCompleteListenerGetUserData(task, l, userRef));
    }
    @Override
    public void addUser(@NonNull UserAuthData user, String name, String last_name, @NonNull OnCompleteListener l){
        // test
        if (userTestValues(user, l)) return;
        if (name.isEmpty()){
            Log.w(TAG, "[UserDB] User name is empty");
            l.onCompose(false, UserDBException.NAME_EMPTY);
            return;
        }
        if (last_name.isEmpty()){
            Log.w(TAG, "[UserDB] User last name is empty");
            l.onCompose(false, UserDBException.LASTNAME_EMPTY);
            return;
        }

        String tName = name.trim();
        String tLastName = last_name.trim();

        Map<String, Object> update = new HashMap<>();
        update.put("users/" + user.uID + "/email", user.email);
        update.put("users/" + user.uID + "/name", tName);
        update.put("users/" + user.uID + "/last_name", tLastName);


        userDB.updateChildren(update).addOnCompleteListener(task ->  this.onCompleteListenerAddUser(task, l));
    }
    @Override
    public void deleteUser(@NonNull FirebaseUser userF, String password, @NonNull OnCompleteListener l){
        // test user and password
        if (userF.getUid().isEmpty()){
            Log.w(TAG, "[UserDB] User properties are empty");
            l.onCompose(false, UserDBException.USER_NOT_EXIST);
            return;
        }
        if (password.isEmpty()){
            Log.w(TAG, "[UserDB] User authentication error");
            l.onCompose(false, UserDBException.ERROR_USER_AUTHENTICATION);
            return;
        }
        AuthCredential credential = EmailAuthProvider.getCredential(userF.getEmail(), password);
        userF.reauthenticate(credential).addOnCompleteListener((task) -> this.onCompleteListenerReauthenticate(task, userF.getUid(), l));
    }


}
