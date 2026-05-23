package com.example.unipicityvibe.Interface.firebase;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Interface.OnCompleteListener;
import com.example.unipicityvibe.Struct.EventStruct;

import java.util.concurrent.atomic.AtomicReference;

public interface IEventDB {
    public void getEventData(@NonNull EventStruct event, int event_id, @NonNull OnCompleteListener l);
    public void getEventRadius(@NonNull AtomicReference<EventStruct[]> events, int min, int max, @NonNull OnCompleteListener l);
}
