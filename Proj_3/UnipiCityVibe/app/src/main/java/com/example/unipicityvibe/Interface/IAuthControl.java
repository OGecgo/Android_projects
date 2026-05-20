package com.example.unipicityvibe.Interface;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Struct.UserAuthStruct;
import com.example.unipicityvibe.Struct.UserDataStruct;

public interface IAuthControl {
    public UserAuthStruct getUserAuth();
    public void getUserDataStruct(@NonNull UserDataStruct userData, @NonNull OnCompleteListener l);
    public void userLogIn(String email, String password, @NonNull OnCompleteListener l);
    public void userLogOut(@NonNull OnCompleteListener l);
    public void userRegister(String email, String password, String name, String lastName, @NonNull OnCompleteListener l);
    public void userDelete(String password, @NonNull OnCompleteListener l);
}
