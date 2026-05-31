package com.example.unipicityvibe.Data;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.unipicityvibe.Data.Interface.IEventDB;
import com.example.unipicityvibe.Data.Exception.EventDBException;
import com.example.unipicityvibe.Listeners.OnCompleteListener;
import com.example.unipicityvibe.Data.Models.EventData;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EventDB implements IEventDB {
    private final DatabaseReference eventDB;
    private static EventDB event;
    private ChildEventListener childEventListener;

    private EventDB() {
        this.eventDB = FirebaseDatabase.getInstance().getReference().child("events");
    }

    private void addToEventDataValues(@NonNull EventData event, @NonNull DataSnapshot snapshotEvent){
        event.event_id    = snapshotEvent.getKey();
        event.title       = snapshotEvent.child("title")      .getValue() != null ? String.valueOf(snapshotEvent.child("title").getValue()) : "";
        event.description = snapshotEvent.child("description").getValue() != null ? String.valueOf(snapshotEvent.child("description").getValue()) : "";
        event.time        = snapshotEvent.child("time")       .getValue() != null ? String.valueOf(snapshotEvent.child("time").getValue()) : "";
        // convert sec to milli
        event.time += "000";
        event.price       = snapshotEvent.child("price")      .getValue() != null ? String.valueOf(snapshotEvent.child("price").getValue()) : "";
        event.latitude    = snapshotEvent.child("latitude")   .getValue() != null ? String.valueOf(snapshotEvent.child("latitude").getValue()) : "";
        event.longitude   = snapshotEvent.child("longitude")  .getValue() != null ? String.valueOf(snapshotEvent.child("longitude").getValue()) : "";
    }

    // ----- Call Back -----
    private void onCompleteListenerGetEventData(@NonNull Task<DataSnapshot> task, @NonNull OnCompleteListener l, @NonNull EventData event){
        if (task.isSuccessful()){
            if(task.getResult().exists()){
                Log.d(TAG, "[EventDB] Event retrieved successfully");

                DataSnapshot snapshot = task.getResult();
                addToEventDataValues(event, snapshot);
                l.onCompose(true, "");
            }
            else{
                Log.w(TAG, "[EventDB] No event found");
                l.onCompose(true, EventDBException.NO_EVENT_FOUND);
            }
        }
        else{
            Log.e(TAG, "[EventDB] Failed to retrieve event", task.getException());
            l.onCompose(false, EventDBException.ERROR_GET_EVENT);
        }
    }
    // ----- End Call Back -----


    public static EventDB getInstance(){
        if (event == null) event = new EventDB();
        return event;
    }

    @Override
    public void getEventData(@NonNull EventData eventRef, String event_id, @NonNull OnCompleteListener l){
        eventDB.child(event_id).get().addOnCompleteListener(task -> this.onCompleteListenerGetEventData(task, l, eventRef));
    }

    @Override
    public void deleteListenerForEventMapRef(){
        eventDB.removeEventListener(childEventListener);
        childEventListener = null;
    }
    @Override
    public void setListenerForEventMapRef(long timestampMilli, @NonNull HashMap<String, EventData> eventMapRef, @NonNull OnCompleteListener l) {
        // convert timestampMilli to Sec
        long timestampSec = timestampMilli / 1000;
        if (childEventListener != null) {
            Log.w(TAG, "[EventDB] childEventListener replaced");
            // delete old listener
            eventDB.removeEventListener(childEventListener);
        }

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                EventData event = new EventData();
                addToEventDataValues(event, snapshot);
                eventMapRef.put(event.event_id, event);
                l.onCompose(true, "");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                EventData event = new EventData();
                addToEventDataValues(event, snapshot);
                eventMapRef.put(event.event_id, event);
                l.onCompose(true, "");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                eventMapRef.remove(snapshot.getKey());
                l.onCompose(true, "");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "[EventDB] Failed to retrieve events", error.toException());
                l.onCompose(false, EventDBException.ERROR_GET_EVENTS);
            }
        };

        eventDB.orderByChild("time")
                .startAt(timestampSec)
                .addChildEventListener(childEventListener);
    }
}
