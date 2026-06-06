package com.example.unipiaudiostories.Data.Remote;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.unipiaudiostories.Data.Model.StoryData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.HashMap;

public interface IStoryDB {

    void setHashMapListener(@NonNull HashMap<String, StoryData> storiesRef, @NonNull DataListener listener);
    void removeHashMapListener();

    // ----------- INTERFACES -----------
    interface DataListener {
        void onDataChanged();
        void onCancelled(String errorLog);
    }
}
