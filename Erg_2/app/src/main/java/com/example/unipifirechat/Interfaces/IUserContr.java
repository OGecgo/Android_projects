package com.example.unipifirechat.Interfaces;

public interface IUserContr {

    interface AuthCallback {
        void onCompose (boolean success, String ErrorLog);
    }
    public String getErrorLog();
    public boolean getSignIn();


    public void SignUp(String email, String password, AuthCallback authCB);
    public boolean SignIn(String email, String password);
    public boolean LogOut();
    public boolean DeleteUser(String password);
}
