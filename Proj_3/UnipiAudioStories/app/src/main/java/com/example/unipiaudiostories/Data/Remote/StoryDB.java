package com.example.unipiaudiostories.Data.Remote;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.unipiaudiostories.Data.Model.StoryData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class StoryDB implements IStoryDB{
    private static StoryDB storyDB;
    private final DatabaseReference storiesRefDB;
    private ChildEventListener childEventListener;

    private StoryDB(){
        storiesRefDB = FirebaseDatabase.getInstance().getReference().child("stories");
    }

    private void setValues(@NonNull DataSnapshot dataSnapshot, @NonNull StoryData storyData){
        String story_id  = dataSnapshot.getKey();
        String title     = dataSnapshot.child("title")    .getValue(String.class);
        String image_url = dataSnapshot.child("image_url").getValue(String.class);
        String story     = dataSnapshot.child("story")    .getValue(String.class);
        String writer    = dataSnapshot.child("writer")   .getValue(String.class);
        String year      = dataSnapshot.child("year")     .getValue(String.class);

        storyData.story_id   = story_id  != null ? story_id  : "";
        storyData.title      = title     != null ? title     : "";
        storyData.image_url  = image_url != null ? image_url : "";
        storyData.story      = story     != null ? story     : "";
        storyData.writer     = writer    != null ? writer    : "";
        storyData.year       = year      != null ? year      : "";
    }

    @NonNull
    public static StoryDB getInstance(){
        if (storyDB == null) storyDB = new StoryDB();
        return storyDB;
    }


    public void setHashMapListener(@NonNull HashMap<String, StoryData> storiesRef, @NonNull DataListener listener){
        if (childEventListener != null){
            removeHashMapListener();
        }
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                StoryData storyData = new StoryData();
                setValues(snapshot, storyData);
                storiesRef.put(snapshot.getKey(), storyData);
                Log.d(TAG, "[StoryDB] Child added: " + snapshot.getKey());
                listener.onDataChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                StoryData storyData = new StoryData();
                setValues(snapshot, storyData);
                storiesRef.put(snapshot.getKey(), storyData);
                Log.d(TAG, "[StoryDB] Child changed: " + snapshot.getKey());
                listener.onDataChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                storiesRef.remove(snapshot.getKey());
                Log.d(TAG, "[StoryDB] Child removed: " + snapshot.getKey());
                listener.onDataChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onCancelled(error.toString());
                Log.e(TAG, "[StoryDB] failed setHashMapListener\n" + error.getMessage());
            }
        };

       storiesRefDB.addChildEventListener(childEventListener);
    }

    public void removeHashMapListener(){
        storiesRefDB.removeEventListener(childEventListener);
        childEventListener = null;
    }
}
