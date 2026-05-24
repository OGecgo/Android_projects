package com.example.unipicityvibe.Data.Interface;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Service.Interface.OnCompleteListener;
import com.example.unipicityvibe.Data.Models.EventData;

import java.util.concurrent.atomic.AtomicReference;

public interface IEventDB {
    void getEventData(@NonNull EventData event, int event_id, @NonNull OnCompleteListener l);
    void getEventRadius(@NonNull AtomicReference<EventData[]> events, int min, int max, @NonNull OnCompleteListener l);
}
