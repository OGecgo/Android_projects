package com.example.unipicityvibe.Service;

import static android.content.ContentValues.TAG;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.unipicityvibe.Data.EventDB;
import com.example.unipicityvibe.Data.Interface.IEventDB;
import com.example.unipicityvibe.Data.Models.EventData;
import com.example.unipicityvibe.Data.Models.TicketData;
import com.example.unipicityvibe.Service.Interface.IEventService;
import com.example.unipicityvibe.Listeners.OnCompleteListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventService implements IEventService {
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
    public void getEventInfo(String eventId, @NonNull EventData eventRef,@NonNull OnCompleteListener l){
        // first search into received events
        if (events.containsKey(eventId)) {
            EventData eventData = events.get(eventId);
            eventRef.event_id = eventData.event_id;
            eventRef.title = eventData.title;
            eventRef.description = eventData.description;
            eventRef.latitude = eventData.latitude;
            eventRef.longitude = eventData.longitude;
            eventRef.time = eventData.time;
            eventRef.price = eventData.price;
            l.onCompose(true, "");
        }
        // after only search into database
        else {
            eventDB.getEventData(eventRef, eventId, l);
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
    public EventData[] getEventsFromTickets(TicketData[] tickets){
        ArrayList<EventData> eventList = new ArrayList<>();
        for (TicketData ticketData: tickets){
            if (events.containsKey(ticketData.event_id)){
                eventList.add(events.get(ticketData.event_id));
            }
            else{
                Log.e(TAG, "[EventService] Event not found with id::" + ticketData.event_id);
            }
        }
        return eventList.toArray(new EventData[0]);
    }
    @Override
    public void StartReceiveEvents(@NonNull OnCompleteListener l){
        events.clear();
        long currentTime = System.currentTimeMillis();
        eventDB.setListenerForEventMapRef(currentTime, events, l);

    }
    @Override
    public void StopReceiveEvents(){
        events.clear();
        eventDB.deleteListenerForEventMapRef();
    }
}
