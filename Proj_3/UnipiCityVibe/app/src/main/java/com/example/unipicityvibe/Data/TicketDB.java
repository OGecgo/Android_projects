package com.example.unipicityvibe.Data;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.unipicityvibe.Data.Exception.TicketDBException;
import com.example.unipicityvibe.Data.Interface.ITicketDB;
import com.example.unipicityvibe.Data.Models.TicketData;
import com.example.unipicityvibe.Listeners.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


// Singleton Class
public class TicketDB implements ITicketDB {

    private final DatabaseReference ticketDB;
    private static TicketDB tickets;
    private ChildEventListener childTicketListener;


    private TicketDB() {
        this.ticketDB = FirebaseDatabase.getInstance().getReference().child("tickets");
    }

    private void addToTicketDataValues(@NonNull TicketData ticketData, @NonNull DataSnapshot snapshotTicket){
        ticketData.ticket_id = snapshotTicket.getKey();
        ticketData.event_id = snapshotTicket.child("event_id")  .getValue() != null ? String.valueOf(snapshotTicket.child("event_id") .getValue()) : "";
        ticketData.time_stamp= snapshotTicket.child("timestamp").getValue() != null ? String.valueOf(snapshotTicket.child("timestamp").getValue()) : "";
    }

    // ------ Call Back ------
    private void onCompleteListenerAddTicket(@NonNull Task<Void> task, @NonNull OnCompleteListener l){
        if (task.isSuccessful()){
            Log.d(TAG, "[TicketDB] Ticket added to user profile successfully");
            l.onCompose(true, "");
        }
        else{
            Log.e(TAG, "[TicketDB] Failed to add ticket to user profile", task.getException());
            l.onCompose(false, TicketDBException.TICKET_NOT_ADDED);
        }
    }

    // ------ End Call Back ------


    public static TicketDB getInstance(){
        if (tickets == null) tickets = new TicketDB();
        return tickets;
    }

    @Override
    public void addTicket(String user_id, String event_id, long timestampMilli, @NonNull OnCompleteListener l){
        if (user_id.isEmpty()){
            Log.e(TAG, "[TicketDB] user is empty");
            return;
        }
        long timestampSec = timestampMilli / 1000;
        DatabaseReference ref = ticketDB.child(user_id).push();
        Map<String, Object> update = new HashMap<>();
        update.put("event_id", event_id);
        update.put("timestamp", timestampSec);

        ref.updateChildren(update).addOnCompleteListener(task -> onCompleteListenerAddTicket(task, l));
    }

    @Override
    public void deleteListenerForTicketMapRef(String user_id){
        DatabaseReference ref = ticketDB.child(user_id);
        ref.removeEventListener(childTicketListener);
        childTicketListener = null;
    }
    @Override
    public void setListenerForTicketMapRef(String user_id, @NonNull HashMap<String, TicketData> ticketsRef, @NonNull OnCompleteListener l){
        if (user_id.isEmpty()){
            Log.e(TAG, "[TicketDB] user is empty");
        return;
        }

        // take referent to user id tickets
        DatabaseReference ref = ticketDB.child(user_id);

        // convert timestampMilli to Sec
        if (childTicketListener != null) {
            Log.w(TAG, "[TicketDB] childTicketListener replaced");
            // delete old listener
            ticketDB.removeEventListener(childTicketListener);
        }

        childTicketListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TicketData ticket = new TicketData();
                addToTicketDataValues(ticket, snapshot);
                ticketsRef.put(ticket.ticket_id, ticket);
                l.onCompose(true, "");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                TicketData ticket = new TicketData();
                addToTicketDataValues(ticket, snapshot);
                ticketsRef.put(ticket.ticket_id, ticket);
                l.onCompose(true, "");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                ticketsRef.remove(snapshot.getKey());
                l.onCompose(true, "");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "[TicketDB] Failed to retrieve events", error.toException());
                l.onCompose(false, TicketDBException.ERROR_GET_TICKETS);
            }
        };

        ref.addChildEventListener(childTicketListener);

    }

}
