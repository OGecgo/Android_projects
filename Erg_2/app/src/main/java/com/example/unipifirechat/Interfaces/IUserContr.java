package com.example.unipifirechat.Interfaces;


// DatabaseCallback use for synchronize, logs and errors
public interface IUserContr  {
    String getUsername();
    String getEmail();
    boolean getSignIn();

    void SignUp(String username, String email, String password, DatabaseCallback authCB);
    void SignIn(String email, String password, DatabaseCallback authCB);
    void LogOut(DatabaseCallback authCB);
}
