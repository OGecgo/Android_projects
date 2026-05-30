package com.example.unipicityvibe.Service;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Data.EventDB;
import com.example.unipicityvibe.Data.Interface.IEventDB;
import com.example.unipicityvibe.Data.Models.EventData;
import com.example.unipicityvibe.Service.Interface.IEventService;
import com.example.unipicityvibe.Listeners.OnCompleteListener;

import java.util.HashMap;

public class EventService implements IEventService {
    private final static int RADIUS_M = 200;
    private static EventService service;
    private final IEventDB eventDB;
    private final HashMap<String, EventData> events;



    private EventService(){
        eventDB = EventDB.getInstance();
        events = new HashMap<>();
    }



    public static EventService getInstance(){
        if (service == null) service = new EventService();
        return service;
    }

    public EventData getEventInfo(String eventId){
        if (events.containsKey(eventId)) {
            return events.get(eventId);
        }
        else {
            return new EventData();
        }
    }
    public EventData[] getRadiusEvents(String latitude, String longitude){
        // TODO return data only in radius off RADIUS_M
        return null;
    }

    public void receiveEvents(@NonNull OnCompleteListener l){
        eventDB.getAllEvents(events, l);
    }
}
