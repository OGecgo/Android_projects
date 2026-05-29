package com.example.unipicityvibe.Data.Interface;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Service.Interface.OnCompleteListener;
import com.example.unipicityvibe.Data.Models.UserAuthData;
import com.example.unipicityvibe.Data.Models.UserData;
import com.google.firebase.auth.FirebaseUser;

public interface IUserDB {
    // the data will be written to userRef
    void getUserData(@NonNull UserAuthData user, @NonNull UserData userRef, @NonNull OnCompleteListener l);
    void addUser(@NonNull UserAuthData user, String name, String last_name, @NonNull OnCompleteListener l);
    void deleteUser(@NonNull FirebaseUser userF, String password, @NonNull OnCompleteListener l);
}
