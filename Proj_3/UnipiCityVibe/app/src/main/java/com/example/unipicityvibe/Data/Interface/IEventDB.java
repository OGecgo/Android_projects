package com.example.unipicityvibe.Data.Interface;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Listeners.OnCompleteListener;
import com.example.unipicityvibe.Data.Models.EventData;

import java.util.HashMap;

public interface IEventDB {
    void getEventData(@NonNull EventData eventRef, String event_id, @NonNull OnCompleteListener l);
    void deleteListenerForEventMapRef();
    void setListenerForEventMapRef(long timestampMilli, @NonNull HashMap<String, EventData> eventMapRef, @NonNull OnCompleteListener l);
}
