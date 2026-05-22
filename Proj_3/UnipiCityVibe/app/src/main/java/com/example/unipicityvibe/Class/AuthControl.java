package com.example.unipicityvibe.Class;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Class.firebase.UserAuth;
import com.example.unipicityvibe.Class.firebase.UserDB;
import com.example.unipicityvibe.Constants.Exception.AuthControlException;
import com.example.unipicityvibe.Interface.IAuthControl;
import com.example.unipicityvibe.Interface.OnCompleteListener;
import com.example.unipicityvibe.Interface.firebase.IUserAuth;
import com.example.unipicityvibe.Interface.firebase.IUserDB;
import com.example.unipicityvibe.Struct.UserAuthStruct;
import com.example.unipicityvibe.Struct.UserDataStruct;

public class AuthControl implements IAuthControl {

    private final IUserAuth userAuth;
    private final IUserDB userDB;

    private static AuthControl user;


    private AuthControl(){
        userAuth = UserAuth.getInstance();
        userDB = UserDB.getInstance();
    }

    private void onCompleteListenerDeleteUser(boolean success, String errorLog, @NonNull OnCompleteListener l, String password){
        if(success){
            userAuth.deleteUser(password, l);
            return;
        }
        l.onCompose(false, errorLog);
    }

    private void onCompleteListenerRegister(boolean success, String errorLog, @NonNull OnCompleteListener l, String name, String lastName){
        if (success){
            userDB.addUser(userAuth.getUser(), name, lastName, l);
            return;
        }
        l.onCompose(false, errorLog);
    }

    public static AuthControl getInstance(){
        if (user == null) user = new AuthControl();
        return  user;
    }

    public UserAuthStruct getUserAuth(){ return  userAuth.getUser(); }
    public void getUserDataStruct(@NonNull UserDataStruct userData, @NonNull OnCompleteListener l){
        userDB.getUserData(userAuth.getUser(), userData, l);
    }
    public void userLogIn(String email, String password, @NonNull OnCompleteListener l){
        if (!userAuth.getUser().uID.isEmpty()){
            l.onCompose(false, AuthControlException.USER_LOGGED_IN);
            return;
        }
        userAuth.singIn(email, password, l);
    }
    public void userLogOut(@NonNull OnCompleteListener l){
        if (userAuth.getUser().uID.isEmpty()){
            l.onCompose(false, AuthControlException.USER_NOT_LOGGED_IN);
            return;
        }
        userAuth.singOut(l);
    }
    public void userRegister(String email, String password, String name, String lastName, @NonNull OnCompleteListener l){
        userAuth.createUser(email, password, (success, errorLog) -> onCompleteListenerRegister(success, errorLog, l, name, lastName));
    }
    public void userDelete(String password, @NonNull OnCompleteListener l){
        userDB.deleteUser(userAuth.getUser(), (success, errorText) -> this.onCompleteListenerDeleteUser(success, errorText, l, password));
    }

}
