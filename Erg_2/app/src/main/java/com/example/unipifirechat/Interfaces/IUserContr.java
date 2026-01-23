package com.example.unipifirechat.Interfaces;


// ErrorCallback use for logs and errors
public interface IUserContr  {
    String getUsername();
    String getEmail();
    boolean getSignIn();

    void SignUp(String username, String email, String password, ErrorCallback authCB);
    void SignIn(String email, String password, ErrorCallback authCB);
    void LogOut(ErrorCallback authCB);
}
