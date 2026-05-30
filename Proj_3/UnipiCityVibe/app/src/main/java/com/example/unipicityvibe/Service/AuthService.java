package com.example.unipicityvibe.Service;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Data.UserAuth;
import com.example.unipicityvibe.Data.UserDB;
import com.example.unipicityvibe.Service.Exception.AuthServiceException;
import com.example.unipicityvibe.Service.Interface.IAuthService;
import com.example.unipicityvibe.Listeners.OnCompleteListener;
import com.example.unipicityvibe.Data.Interface.IUserAuth;
import com.example.unipicityvibe.Data.Interface.IUserDB;
import com.example.unipicityvibe.Data.Models.UserAuthData;
import com.example.unipicityvibe.Data.Models.UserData;

// Singleton Class
public class AuthService implements IAuthService {

    private final IUserAuth userAuth;
    private final IUserDB userDB;

    private static AuthService service;


    private AuthService(){
        userAuth = UserAuth.getInstance();
        userDB = UserDB.getInstance();
    }

    // ------ Call Back ------
    private void onCompleteListenerDeleteUser(boolean success, String errorLog, @NonNull OnCompleteListener l, String password){
        if(success){
            userAuth.deleteUser(password, l);
            return;
        }
        l.onCompose(false, errorLog);
    }
    private void onCompleteListenerRegister(boolean success, String errorLog, @NonNull OnCompleteListener l, String name, String last_name){
        if (success){
            userDB.addUser(userAuth.getUserAuth(), name, last_name, l);
            return;
        }
        l.onCompose(false, errorLog);
    }
    // ------ End Call Back ------


    public static AuthService getInstance(){
        if (service == null) service = new AuthService();
        return  service;
    }
    @Override
    public UserAuthData getUserAuth(){
        return  userAuth.getUserAuth();
    }
    @Override
    public void getUserData(@NonNull UserData userData, @NonNull OnCompleteListener l){
        userDB.getUserData(userAuth.getUserAuth(), userData, l);
    }
    @Override
    public void userLogIn(String email, String password, @NonNull OnCompleteListener l){
        if (!userAuth.getUserAuth().uID.isEmpty()){
            l.onCompose(false, AuthServiceException.USER_LOGGED_IN);
            return;
        }
        userAuth.singIn(email, password, l);
    }
    @Override
    public void userLogOut(@NonNull OnCompleteListener l){
        if (userAuth.getUserAuth().uID.isEmpty()){
            l.onCompose(false, AuthServiceException.USER_NOT_LOGGED_IN);
            return;
        }
        userAuth.singOut(l);
    }
    @Override
    public void userRegister(String email, String password, String name, String last_name, @NonNull OnCompleteListener l){
        userAuth.createUser(email, password, (success, errorLog) -> onCompleteListenerRegister(success, errorLog, l, name, last_name));
    }
    @Override
    public void userDelete(String password, @NonNull OnCompleteListener l){
        userDB.deleteUser(userAuth.getFirebaseUser(), password, (success, errorText) -> this.onCompleteListenerDeleteUser(success, errorText, l, password));
    }

}
