package com.example.unipicityvibe.Service;

import android.location.Location;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Data.EventDB;
import com.example.unipicityvibe.Data.Interface.IEventDB;
import com.example.unipicityvibe.Data.Models.EventData;
import com.example.unipicityvibe.Service.Interface.IEventService;
import com.example.unipicityvibe.Listeners.OnCompleteListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventService implements IEventService {
    private final static int MILLISECOND_TO_SECOND = 1000;
    private final static int RADIUS_M = 200;
    private static EventService service;
    private final IEventDB eventDB;
    private final HashMap<String, EventData> events;

    // constructor
    private EventService(){
        eventDB = EventDB.getInstance();
        events = new HashMap<>();
    }



    public static EventService getInstance(){
        if (service == null) service = new EventService();
        return service;
    }

    @Override
    public EventData getEventInfo(String eventId){
        if (events.containsKey(eventId)) {
            return events.get(eventId);
        }
        else {
            return new EventData();
        }
    }
    @Override
    public EventData[] getRadiusEvents(double userLatitude, double userLongitude){
        List<EventData> list = new ArrayList<>();
        for (EventData data : events.values()){
            if (data.latitude.isEmpty() || data.longitude.isEmpty()) continue;

            double dataLat = Double.parseDouble(data.latitude);
            double dataLon = Double.parseDouble(data.longitude);

            float[] result = new float[1]; // 1 only for distance
            Location.distanceBetween(userLatitude, userLongitude, dataLat, dataLon, result);
            if (result[0] <= RADIUS_M){
                list.add(data);
            }
        }

        return list.toArray( new EventData[0]);
    }
    @Override
    public void StartReceiveEvents(@NonNull OnCompleteListener l){
        events.clear();
        long currentTime = System.currentTimeMillis();
        // protect from very frequent receives
        eventDB.setListenerForEventMapRef(currentTime / MILLISECOND_TO_SECOND, events, l);

    }
    @Override
    public void StopReceiveEvents(){
        events.clear();
        eventDB.deleteListenerForEventMapRef();
    }
}
