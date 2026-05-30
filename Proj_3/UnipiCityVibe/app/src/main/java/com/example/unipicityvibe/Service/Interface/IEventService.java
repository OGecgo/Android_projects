package com.example.unipicityvibe.Service.Interface;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Data.Models.EventData;
import com.example.unipicityvibe.Listeners.OnCompleteListener;

public interface IEventService {

    EventData getEventInfo(String eventId);
    EventData[] getRadiusEvents(String latitude, String longitude);

    void receiveEvents(@NonNull OnCompleteListener l);
}
