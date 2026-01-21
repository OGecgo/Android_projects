package com.example.unipifirechat.Class;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.unipifirechat.Interfaces.IUserContr;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UserContr implements  IUserContr{

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;
    private boolean signIn;
    private boolean successExe;


    // private

    private void update(FirebaseUser user){
        this.successExe = (user != null);
    }



    // public

    // --constructor
    public UserContr(){
        this.user = mAuth.getCurrentUser();
        if (user != null){
            this.signIn = true;
            return;
        }
        this.signIn = false;
    }

    // --var
    public boolean getSignIn(){ return this.signIn;}

    // --methods
    public boolean SignUp(String email, String password){
        this.successExe = false;
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmail:success");
                            user = mAuth.getCurrentUser();
                            update(user);
                        }
                        else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            update(null);
                        }
                    }
                }
        );

        if (this.successExe){
            this.signIn = true;
        }
        return this.successExe;
    }
    public boolean SignIn(String email, String password){
        if (this.successExe){
            this.signIn = true;
        }
        return this.successExe;
    }
    public boolean LogOut(){
        if (this.successExe){
            this.signIn = false;
        }
        return this.successExe;

    }
    public boolean DeleteUser(String password){
        if (this.successExe){
            this.signIn = false;
        }
        return this.successExe;
    }
}
