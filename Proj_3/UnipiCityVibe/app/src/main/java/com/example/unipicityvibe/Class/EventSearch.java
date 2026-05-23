package com.example.unipicityvibe.Class;

import com.example.unipicityvibe.Struct.EventStruct;

public class EventSearch {
    private int minRadious;
    private int maxRadious;
    private int lastEventsNotReturnedYet;
    private EventStruct[] events;


    public EventStruct getEventInfo(String eventId);
    pubic EventStruct[] getBunchOfEvents(Position pos);
}
