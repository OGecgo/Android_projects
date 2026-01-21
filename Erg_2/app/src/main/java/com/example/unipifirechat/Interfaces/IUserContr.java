package com.example.unipifirechat.Interfaces;

public interface IUserContr {

    public boolean getSignIn();

    public boolean SignUp(String email, String password);
    public boolean SignIn(String email, String password);
    public boolean LogOut();
    public boolean DeleteUser(String password);
}
