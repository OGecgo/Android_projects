package com.example.unipicityvibe.Data;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Data.Interface.IEventDB;
import com.example.unipicityvibe.Data.Exception.EventDBException;
import com.example.unipicityvibe.Listeners.OnCompleteListener;
import com.example.unipicityvibe.Data.Models.EventData;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EventDB implements IEventDB {
    private final DatabaseReference eventDB;
    private static EventDB event;

    private EventDB() {
        this.eventDB = FirebaseDatabase.getInstance().getReference().child("events");
    }
    private void onCompleteListenerGetEventData(@NonNull Task<DataSnapshot> task, @NonNull OnCompleteListener l, @NonNull EventData event){
        if (task.isSuccessful()){
            if(task.getResult().exists()){
                Log.d(TAG, "[EventDB] Event retrieved successfully");

                DataSnapshot snapshot = task.getResult();
                event.event_id = snapshot.getKey();
                event.title = snapshot.child("title")            .getValue() != null ? String.valueOf(snapshot.child("title").getValue()) : "";
                event.description = snapshot.child("description").getValue() != null ? String.valueOf(snapshot.child("description").getValue()) : "";
                event.time = snapshot.child("time")              .getValue() != null ? String.valueOf(snapshot.child("time").getValue()) : "";
                event.price = snapshot.child("price")            .getValue() != null ? String.valueOf(snapshot.child("price").getValue()) : "";
                event.latitude = snapshot.child("latitude")      .getValue() != null ? String.valueOf(snapshot.child("latitude").getValue()) : "";
                event.longitude = snapshot.child("longitude")    .getValue() != null ? String.valueOf(snapshot.child("longitude").getValue()) : "";

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

    private  void onCompleteListenerGetAllEvents(@NonNull Task<DataSnapshot> task, @NonNull OnCompleteListener l, @NonNull HashMap<String, EventData> eventMapRef){
        if (task.isSuccessful()){
            if(task.getResult().exists()){
                Log.d(TAG, "[EventDB] Events retrieved successfully");
                DataSnapshot snapshot = task.getResult();

                for (DataSnapshot snapshotEvent: snapshot.getChildren()) {
                    EventData event = new EventData();
                    event.event_id    = snapshotEvent.getKey();
                    event.title       = snapshotEvent.child("title")      .getValue() != null ? String.valueOf(snapshotEvent.child("title").getValue()) : "";
                    event.description = snapshotEvent.child("description").getValue() != null ? String.valueOf(snapshotEvent.child("description").getValue()) : "";
                    event.time        = snapshotEvent.child("time")       .getValue() != null ? String.valueOf(snapshotEvent.child("time").getValue()) : "";
                    event.price       = snapshotEvent.child("price")      .getValue() != null ? String.valueOf(snapshotEvent.child("price").getValue()) : "";
                    event.latitude    = snapshotEvent.child("latitude")   .getValue() != null ? String.valueOf(snapshotEvent.child("latitude").getValue()) : "";
                    event.longitude   = snapshotEvent.child("longitude")  .getValue() != null ? String.valueOf(snapshotEvent.child("longitude").getValue()) : "";

                    eventMapRef.put(event.event_id, event);
                }
                l.onCompose(true, "");
            }
            else{
                Log.w(TAG, "[EventDB] No events found");
                l.onCompose(true, EventDBException.NO_EVENTS_FOUND);
            }
        }
        else{
            Log.e(TAG, "[EventDB] Failed to retrieve events", task.getException());
            l.onCompose(false, EventDBException.ERROR_GET_EVENTS);
        }
    }




    public static EventDB getInstance(){
        if (event == null) event = new EventDB();
        return event;
    }

    @Override
    public void getEventData(@NonNull EventData eventRef, String event_id, @NonNull OnCompleteListener l){
        eventDB.child(event_id).get().addOnCompleteListener(task -> this.onCompleteListenerGetEventData(task, l, eventRef));
    }


    @Override
    public void getAllEventsSince(long timestampSec, @NonNull HashMap<String, EventData> eventMapRef, @NonNull OnCompleteListener l) {
        eventDB.orderByChild("time")
                .startAt(timestampSec)
                .get()
                .addOnCompleteListener(task -> this.onCompleteListenerGetAllEvents(task, l, eventMapRef));
    }
}
