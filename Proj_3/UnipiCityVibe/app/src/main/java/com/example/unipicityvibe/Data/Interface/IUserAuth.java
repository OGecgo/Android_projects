package com.example.unipicityvibe.Data.Interface;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Service.Interface.OnCompleteListener;
import com.example.unipicityvibe.Data.Models.UserAuthData;
import com.google.firebase.auth.FirebaseUser;

public interface IUserAuth {
    @NonNull
    UserAuthData getUserAuth();
    FirebaseUser getFirebaseUser();
    void createUser(String email, String password, @NonNull OnCompleteListener l);
    void singIn(String email, String password, @NonNull OnCompleteListener l);
    void singOut(OnCompleteListener l);
    void deleteUser(String password, @NonNull OnCompleteListener l);
}
