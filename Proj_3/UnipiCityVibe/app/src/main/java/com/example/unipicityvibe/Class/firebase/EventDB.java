package com.example.unipicityvibe.Class.firebase;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Interface.OnCompleteListener;
import com.example.unipicityvibe.Interface.firebase.IEventDB;
import com.example.unipicityvibe.Struct.EventStruct;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.atomic.AtomicReference;

public class EventDB implements IEventDB {
    private final DatabaseReference eventDB;
    private static EventDB event;

    private EventDB() {
        this.eventDB = FirebaseDatabase.getInstance().getReference().child("events");
    }
    private void onCompleteListenerGetEventData(Task<DataSnapshot> task, @NonNull OnCompleteListener l, @NonNull EventStruct event){
        if (task.isComplete()){
            if(task.getResult().exists()){
                Log.d(TAG, "[EventDB] Event retrieved successfully");

                DataSnapshot snapshot = task.getResult();
                event.event_id = snapshot.getKey();
                event.title = snapshot.child("title").getValue(String.class);
                event.description = snapshot.child("description").getValue(String.class);
                event.time = snapshot.child("time").getValue(String.class);
                event.price = snapshot.child("price").getValue(String.class);
                event.location = snapshot.child("location").getValue(String.class);

                l.onCompose(true, "");
            }
            else{
                Log.w(TAG, "[EventDB] No event found");
                l.onCompose(true, "");
            }
        }
        else{
            Log.w(TAG, "[EventDB] Failed to retrieve event");
            l.onCompose(false, "");
        }
    }

    private  void onCompleteListenerGetEventsRadius(Task<DataSnapshot> task, @NonNull OnCompleteListener l, @NonNull AtomicReference<EventStruct[]> events){
        if (task.isComplete()){
            if(task.getResult().exists()){
                Log.d(TAG, "[EventDB] Events retrieved successfully");

                DataSnapshot snapshot = task.getResult();
                int size = Math.toIntExact(snapshot.getChildrenCount());
                int pos = 0;
                for (DataSnapshot snapshotTicket: snapshot.getChildren()) {
                    events.get()[pos].event_id = snapshot.getKey();
                    events.get()[pos].title = snapshot.child("title").getValue(String.class);
                    events.get()[pos].description = snapshot.child("description").getValue(String.class);
                    events.get()[pos].time = snapshot.child("time").getValue(String.class);
                    events.get()[pos].price = snapshot.child("price").getValue(String.class);
                    events.get()[pos].location = snapshot.child("location").getValue(String.class);
                    pos ++;
                }
                l.onCompose(true, "");
            }
            else{
                Log.w(TAG, "[EventDB] No events found");
                l.onCompose(true, "");
            }
        }
        else{
            Log.w(TAG, "[EventDB] Failed to retrieve events");
            l.onCompose(false, "");
        }
    }




    public EventDB getInstance(){
        if (event == null) event = new EventDB();
        return event;
    }

    public void getEventData(@NonNull EventStruct event, int event_id, @NonNull OnCompleteListener l){
        eventDB.child(Integer.toString(event_id)).get().addOnCompleteListener(task -> this.onCompleteListenerGetEventData(task, l, event));
    }
    public void getEventRadius(@NonNull AtomicReference<EventStruct[]> events, int min, int max, @NonNull OnCompleteListener l){
        eventDB
                .orderByChild("time")
                .startAt(min)
                .endAt(max)
                .get().addOnCompleteListener(task -> this.onCompleteListenerGetEventsRadius(task, l, events));
    }
}
