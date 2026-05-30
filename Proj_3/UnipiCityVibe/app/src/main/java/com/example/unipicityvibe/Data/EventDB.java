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
                event.title = snapshot.child("title").getValue(String.class);
                event.description = snapshot.child("description").getValue(String.class);
                event.time = snapshot.child("time").getValue(String.class);
                event.price = snapshot.child("price").getValue(String.class);
                event.latitude = snapshot.child("latitude").getValue(String.class);
                event.longitude = snapshot.child("longitude").getValue(String.class);

                l.onCompose(true, "");
            }
            else{
                Log.w(TAG, "[EventDB] No event found");
                l.onCompose(true, EventDBException.NO_EVENT_FOUND);
            }
        }
        else{
            Log.w(TAG, "[EventDB] Failed to retrieve event");
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
                    event.title       = snapshotEvent.child("title")      .getValue(String.class);
                    event.description = snapshotEvent.child("description").getValue(String.class);
                    event.time        = snapshotEvent.child("time")       .getValue(String.class);
                    event.price       = snapshotEvent.child("price")      .getValue(String.class);
                    event.latitude    = snapshotEvent.child("latitude")   .getValue(String.class);
                    event.longitude   = snapshotEvent.child("longitude")  .getValue(String.class);

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
            Log.w(TAG, "[EventDB] Failed to retrieve events");
            l.onCompose(false, EventDBException.ERROR_GET_EVENTS);
        }
    }




    public static EventDB getInstance(){
        if (event == null) event = new EventDB();
        return event;
    }

    public void getEventData(@NonNull EventData eventRef, String event_id, @NonNull OnCompleteListener l){
        eventDB.child(event_id).get().addOnCompleteListener(task -> this.onCompleteListenerGetEventData(task, l, eventRef));
    }
    public void getAllEvents(@NonNull HashMap<String, EventData> eventMapRef, @NonNull OnCompleteListener l){
        eventDB.get().addOnCompleteListener(task -> this.onCompleteListenerGetAllEvents(task, l, eventMapRef));
    }
}
