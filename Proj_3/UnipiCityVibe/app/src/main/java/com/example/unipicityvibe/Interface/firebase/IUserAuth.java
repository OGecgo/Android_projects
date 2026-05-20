package com.example.unipicityvibe.Interface.firebase;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Interface.OnCompleteListener;
import com.example.unipicityvibe.Struct.UserAuthStruct;

public interface IUserAuth {
    @NonNull
    UserAuthStruct getUser();
    void createUser(String email, String password, @NonNull OnCompleteListener l);
    void singIn(String email, String password, @NonNull OnCompleteListener l);
    void singOut(OnCompleteListener l);
    void deleteUser(String password, @NonNull OnCompleteListener l);
}
