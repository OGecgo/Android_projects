package com.example.unipifirechat.Interfaces;


// AuthCallback use for logs and errors
public interface IUserContr  {
    public boolean getSignIn();

    public void SignUp(String username, String email, String password, AuthCallback authCB);
    public void SignIn(String email, String password, AuthCallback authCB);
    public void LogOut(AuthCallback authCB);
}
