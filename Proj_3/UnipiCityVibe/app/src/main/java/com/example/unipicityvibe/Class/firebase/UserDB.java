package com.example.unipicityvibe.Class.firebase;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Constants.UserDBException;
    import com.example.unipicityvibe.Interface.OnCompleteListener;
import com.example.unipicityvibe.Interface.firebase.IUserDB;
import com.example.unipicityvibe.Struct.TicketStruct;
import com.example.unipicityvibe.Struct.UserAuthStruct;
import com.example.unipicityvibe.Struct.UserDataStruct;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class UserDB implements IUserDB {

    private final DatabaseReference userDB;
    private static UserDB user;

    private UserDB(){
        this.userDB = FirebaseDatabase.getInstance().getReference();
    }

    private boolean userTestValues(@NonNull UserAuthStruct user, @NonNull OnCompleteListener l){
        // empty value
        if (user.email.isEmpty()){
            l.onCompose(false, UserDBException.EMPTY_EMAIL);
            return true;
        }
        if (user.uID.isEmpty()){
            l.onCompose(false, UserDBException.EMPTY_UID);
            return true;
        }
        // simple email validation
        if (!user.email.contains("@") && !user.email.contains(".")){
            l.onCompose(false, UserDBException.EMAIL_VALIDATION_ERROR);
            return true;
        }
        return false;
    }

    private void onCompleteListenerGetUserData(@NotNull Task<DataSnapshot> task, @NonNull OnCompleteListener l, UserDataStruct userData){
        if (task.isComplete()){
            if (task.getResult().exists()){
                Log.d(TAG, "[UserDB] User data retrieved successfully");

                DataSnapshot snapshot = task.getResult();
                userData.email = snapshot.child("email").getValue(String.class);
                userData.name = snapshot.child("name").getValue(String.class);
                userData.lastName = snapshot.child("last_name").getValue(String.class);

                l.onCompose(true, "");
            }
            else{
                Log.w(TAG, "[UserDB] User data not found in database");
                l.onCompose(false, UserDBException.EMPTY_USER);
            }
        }
        else{
            Log.e(TAG, "[UserDB] Failed to retrieve user data", task.getException());
            l.onCompose(false, UserDBException.ERROR_GET_USER);
        }
    }

    private void onCompleteListenerAddUser(@NonNull Task<Void> task, @NonNull OnCompleteListener l){
        if (task.isSuccessful()){
            Log.d(TAG, "[UserDB] User profile saved to database successfully");
            l.onCompose(true, "");
        }
        else{
            Log.e(TAG, "[UserDB] Failed to save user profile to database", task.getException());
            l.onCompose(false, UserDBException.ERROR_USER_CREATE);
        }
    }

    private void onCompleteListenerDeleteUser(@NonNull Task<Void> task, @NonNull OnCompleteListener l){
        if (task.isSuccessful()){
            Log.d(TAG, "[UserDB] User profile deleted from database successfully");
            l.onCompose(true, "");
        }
        else{
            Log.e(TAG, "[UserDB] Failed to delete user profile from database", task.getException());
            l.onCompose(false, UserDBException.ERROR_USER_DELETE);
        }
    }

    private void onCompleteListenerAddTicket(@NonNull Task<Void> task, @NonNull OnCompleteListener l){
        if (task.isComplete()){
            Log.d(TAG, "[UserDB] Ticket added to user profile successfully");
            l.onCompose(true, "");
        }
        else{
            Log.e(TAG, "[UserDB] Failed to add ticket to user profile", task.getException());
            l.onCompose(false, UserDBException.TICKET_NOT_ADDED);
        }
    }

    private void onCompleteListenerGetTicketData(@NonNull Task<DataSnapshot> task, @NonNull OnCompleteListener l, TicketStruct ticket){
        if (task.isComplete()){
            if (task.getResult().exists()){
                Log.d(TAG, "[UserDB] Ticket data retrieved successfully");

                DataSnapshot snapshot = task.getResult();
                ticket.event_id = snapshot.child("event_id").getValue(String.class);
                ticket.time_stamp= snapshot.child("time_stamp").getValue(String.class);

                l.onCompose(true, "");
            }
            else{
                Log.w(TAG, "[UserDB] Ticket data not found in database");
                l.onCompose(false, UserDBException.EMPTY_TICKET);
            }
        }
        else{
            Log.e(TAG, "[UserDB] Failed to retrieve ticket data", task.getException());
            l.onCompose(false, UserDBException.ERROR_GET_TICKET);
        }
    }

    private void onCompleteListenerGetUserTickets(@NonNull Task<DataSnapshot> task, @NonNull OnCompleteListener l, @NonNull AtomicReference<TicketStruct[]> ticketsRef){
        if (task.isComplete()){
            if (task.getResult().exists()){
                Log.d(TAG, "[UserDB] User tickets retrieved successfully");

                DataSnapshot snapshot = task.getResult();
                int size = Math.toIntExact(snapshot.getChildrenCount());
                ticketsRef.set(new TicketStruct[size]);
                int pos = 0;
                for (DataSnapshot snapshotTicket: snapshot.getChildren()){
                    ticketsRef.get()[pos].ticket_id = snapshotTicket.getKey();
                    ticketsRef.get()[pos].event_id = snapshotTicket.child("event_id").getValue(String.class);
                    ticketsRef.get()[pos].time_stamp= snapshotTicket.child("time_stamp").getValue(String.class);
                    pos ++;
                }

                l.onCompose(true, "");
            }
            else{
                Log.w(TAG, "[UserDB] No tickets found for user");
                l.onCompose(false, UserDBException.NO_TICKETS_FOUND);
            }
        }
        else{
            Log.e(TAG, "[UserDB] Failed to retrieve user tickets", task.getException());
            l.onCompose(false, UserDBException.ERROR_GET_TICKETS);
        }
    }


    public static UserDB getInstance(){
        if (user == null) user = new UserDB();
        return user;
    }
    @Override
    public void getUserData(@NonNull UserAuthStruct user, @NonNull UserDataStruct userRef, @NonNull OnCompleteListener l){
        if (user.uID.isEmpty()) return;

        userDB.child("users").child(user.uID).get().addOnCompleteListener(task -> onCompleteListenerGetUserData(task, l, userRef));
    }
    @Override
    public void addUser(@NonNull UserAuthStruct user, String name, String lastName, @NonNull OnCompleteListener l){
        // test
        if (userTestValues(user, l)) return;
        if (name.isEmpty()){
            l.onCompose(false, UserDBException.NAME_EMPTY);
            return;
        }
        if (lastName.isEmpty()){
            l.onCompose(false, UserDBException.LASTNAME_EMPTY);
            return;
        }

        String tName = name.trim();
        String tLastName = lastName.trim();

        Map<String, Object> update = new HashMap<>();
        update.put("users/" + user.uID + "/email", user.email);
        update.put("users/" + user.uID + "/name", tName);
        update.put("users/" + user.uID + "/last_name", tLastName);


        userDB.updateChildren(update).addOnCompleteListener(task ->  this.onCompleteListenerAddUser(task, l));
    }
    @Override
    public void deleteUser(@NonNull UserAuthStruct user, @NonNull OnCompleteListener l){
        if (user.uID.isEmpty()){
            l.onCompose(false, UserDBException.USER_NOT_EXIST);
            return;
        }
        userDB.child("users").child(user.uID).removeValue().addOnCompleteListener(task ->  this.onCompleteListenerDeleteUser(task, l));
    }
    @Override
    public void addTicket(@NonNull UserAuthStruct user, int eventID, int time, @NonNull OnCompleteListener l){
        if (userTestValues(user, l)) return;

        DatabaseReference ref = userDB.child("tickets").child(user.uID).push();
        Map<String, Object> update = new HashMap<>();
        update.put("event_id", Integer.toString(eventID));
        update.put("time_stamp", Integer.toString(time));

        ref.updateChildren(update).addOnCompleteListener(task -> onCompleteListenerAddTicket(task, l));
    }
    @Override
    public void getTicketData(@NonNull UserAuthStruct user, int ticketID, @NonNull TicketStruct ticketRef, @NonNull OnCompleteListener l){
        if (userTestValues(user, l)) return;

        ticketRef.ticket_id = Integer.toString(ticketID);
        DatabaseReference ref = userDB.child("tickets").child(user.uID).child(ticketRef.ticket_id);
        ref.get().addOnCompleteListener(task -> onCompleteListenerGetTicketData(task, l, ticketRef));
    }
    @Override
    public void getUserTickets(@NonNull UserAuthStruct user, @NonNull AtomicReference<TicketStruct[]> ticketsRef, @NonNull OnCompleteListener l){
        if (userTestValues(user, l)){
            ticketsRef.set(new TicketStruct[1]); ;
            ticketsRef.get()[0].ticket_id = "";
            ticketsRef.get()[0].event_id = "";
            ticketsRef.get()[0].time_stamp = "";
        }

        DatabaseReference ref = userDB.child("tickets").child(user.uID);
        ref.get().addOnCompleteListener(task -> onCompleteListenerGetUserTickets(task, l, ticketsRef));
    }
}
