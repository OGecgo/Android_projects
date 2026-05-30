package com.example.unipicityvibe.Service.Interface;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Data.Models.UserAuthData;
import com.example.unipicityvibe.Data.Models.UserData;
import com.example.unipicityvibe.Listeners.OnCompleteListener;

public interface IAuthService {
    UserAuthData getUserAuth();
    // the data will be written to userRef
    void getUserData(@NonNull UserData userData, @NonNull OnCompleteListener l);
    void userLogIn(String email, String password, @NonNull OnCompleteListener l);
    void userLogOut(@NonNull OnCompleteListener l);
    void userRegister(String email, String password, String name, String last_name, @NonNull OnCompleteListener l);
    void userDelete(String password, @NonNull OnCompleteListener l);
}
