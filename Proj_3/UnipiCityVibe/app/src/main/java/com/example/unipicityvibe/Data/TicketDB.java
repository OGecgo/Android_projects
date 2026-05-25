package com.example.unipicityvibe.Data;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Data.Exception.TicketDBException;
import com.example.unipicityvibe.Data.Interface.ITicketDB;
import com.example.unipicityvibe.Data.Models.TicketData;
import com.example.unipicityvibe.Data.Models.UserAuthData;
import com.example.unipicityvibe.Service.Interface.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

// Singleton Class
public class TicketDB implements ITicketDB {

    private final DatabaseReference ticketDB;
    private static TicketDB tickets;

    private TicketDB() {
        this.ticketDB = FirebaseDatabase.getInstance().getReference().child("tickets");
    }

    // returns true if the input is invalid
    private boolean userTestValues(@NonNull UserAuthData user, @NonNull OnCompleteListener l){
        // empty value
        if (user.email.isEmpty()){
            l.onCompose(false, TicketDBException.EMPTY_EMAIL);
            return true;
        }
        if (user.uID.isEmpty()){
            l.onCompose(false, TicketDBException.EMPTY_UID);
            return true;
        }
        // simple email validation
        if (!user.email.contains("@") && !user.email.contains(".")){
            l.onCompose(false, TicketDBException.EMAIL_VALIDATION_ERROR);
            return true;
        }
        return false;
    }

    // ------ Call Back ------
    private void onCompleteListenerAddTicket(@NonNull Task<Void> task, @NonNull OnCompleteListener l){
        if (task.isSuccessful()){
            Log.d(TAG, "[UserDB] Ticket added to user profile successfully");
            l.onCompose(true, "");
        }
        else{
            Log.e(TAG, "[UserDB] Failed to add ticket to user profile", task.getException());
            l.onCompose(false, TicketDBException.TICKET_NOT_ADDED);
        }
    }

    private void onCompleteListenerGetTicketData(@NonNull Task<DataSnapshot> task, @NonNull OnCompleteListener l, TicketData ticket){
        if (task.isSuccessful()){
            if (task.getResult().exists()){
                Log.d(TAG, "[UserDB] Ticket data retrieved successfully");

                DataSnapshot snapshot = task.getResult();
                ticket.event_id = snapshot.child("event_id").getValue(String.class);
                ticket.time_stamp= snapshot.child("time_stamp").getValue(String.class);

                l.onCompose(true, "");
            }
            else{
                Log.w(TAG, "[UserDB] Ticket data not found in database");
                l.onCompose(false, TicketDBException.EMPTY_TICKET);
            }
        }
        else{
            Log.e(TAG, "[UserDB] Failed to retrieve ticket data", task.getException());
            l.onCompose(false, TicketDBException.ERROR_GET_TICKET);
        }
    }

    private void onCompleteListenerGetUserTickets(@NonNull Task<DataSnapshot> task, @NonNull OnCompleteListener l, @NonNull AtomicReference<TicketData[]> ticketsRef){
        if (task.isSuccessful()){
            if (task.getResult().exists()){
                Log.d(TAG, "[UserDB] User tickets retrieved successfully");

                DataSnapshot snapshot = task.getResult();
                int size = Math.toIntExact(snapshot.getChildrenCount());
                ticketsRef.set(new TicketData[size]);
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
                l.onCompose(false, TicketDBException.NO_TICKETS_FOUND);
            }
        }
        else{
            Log.e(TAG, "[UserDB] Failed to retrieve user tickets", task.getException());
            l.onCompose(false, TicketDBException.ERROR_GET_TICKETS);
        }
    }
    // ------ End Call Back ------


    public static TicketDB getInstance(){
        if (tickets == null) tickets = new TicketDB();
        return tickets;
    }

    @Override
    public void addTicket(@NonNull UserAuthData user, int eventID, int time, @NonNull OnCompleteListener l){
        if (userTestValues(user, l)) return;

        DatabaseReference ref = ticketDB.child(user.uID).push();
        Map<String, Object> update = new HashMap<>();
        update.put("event_id", Integer.toString(eventID));
        update.put("time_stamp", Integer.toString(time));

        ref.updateChildren(update).addOnCompleteListener(task -> onCompleteListenerAddTicket(task, l));
    }
    @Override
    public void getTicketData(@NonNull UserAuthData user, int ticketID, @NonNull TicketData ticketRef, @NonNull OnCompleteListener l){
        if (userTestValues(user, l)) return;

        ticketRef.ticket_id = Integer.toString(ticketID);
        DatabaseReference ref = ticketDB.child(user.uID).child(ticketRef.ticket_id);
        ref.get().addOnCompleteListener(task -> onCompleteListenerGetTicketData(task, l, ticketRef));
    }
    @Override
    public void getUserTickets(@NonNull UserAuthData user, @NonNull AtomicReference<TicketData[]> ticketsRef, @NonNull OnCompleteListener l){
        if (userTestValues(user, l)){
            ticketsRef.set(new TicketData[1]); ;
            ticketsRef.get()[0].ticket_id = "";
            ticketsRef.get()[0].event_id = "";
            ticketsRef.get()[0].time_stamp = "";
        }

        DatabaseReference ref = ticketDB.child(user.uID);
        ref.get().addOnCompleteListener(task -> onCompleteListenerGetUserTickets(task, l, ticketsRef));
    }
}
