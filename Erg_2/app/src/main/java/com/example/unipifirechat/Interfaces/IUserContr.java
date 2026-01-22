package com.example.unipifirechat.Interfaces;

public interface IUserContr {

    // cheks and logs from IUserContr
    interface AuthCallback {
        void onCompose (boolean success, String ErrorLog);
    }
    public String getErrorLog();
    public boolean getSignIn();


    public void SignUp(String email, String password, AuthCallback authCB);
    public void SignIn(String email, String password, AuthCallback authCB);
    public void LogOut(AuthCallback authCB);
    public void DeleteUser(String password, AuthCallback authCsV);
}
