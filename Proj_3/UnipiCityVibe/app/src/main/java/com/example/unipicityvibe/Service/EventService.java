package com.example.unipicityvibe.Service;

import com.example.unipicityvibe.Data.Models.EventData;
import com.example.unipicityvibe.Service.Interface.IEventService;

public class EventService implements IEventService {
    private int minRadious;
    private int maxRadious;
    private int lastEventsNotReturnedYet;
    private EventData[] events;


    public EventData getEventInfo(String eventId){return null;}
    public EventData[] getBunchOfEvents(String pos){return null;}
}
