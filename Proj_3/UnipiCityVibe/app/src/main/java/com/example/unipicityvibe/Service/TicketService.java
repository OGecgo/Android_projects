package com.example.unipicityvibe.Service;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.unipicityvibe.Data.EventDB;
import com.example.unipicityvibe.Data.Exception.TicketDBException;
import com.example.unipicityvibe.Data.Interface.IEventDB;
import com.example.unipicityvibe.Data.Interface.ITicketDB;
import com.example.unipicityvibe.Data.Models.EventData;
import com.example.unipicityvibe.Data.Models.TicketData;
import com.example.unipicityvibe.Data.TicketDB;
import com.example.unipicityvibe.Listeners.OnCompleteListener;
import com.example.unipicityvibe.Service.Interface.ITicketService;

import java.util.HashMap;

public class TicketService implements ITicketService {
    private static TicketService service;
    private final ITicketDB ticketDB;
    private final IEventDB eventDB;
    private final HashMap<String, TicketData> tickets;
    private String user_id;


    private TicketService(){
        ticketDB = TicketDB.getInstance();
        eventDB = EventDB.getInstance();
        tickets = new HashMap<>();
    }

    public static TicketService getInstance(){
        if (service == null) service = new TicketService();
        return service;
    }

    @Override
    public void setUserID(String user_id){
        this.user_id = user_id;
    }

    @Override
    public void addTicket(String event_id, @NonNull OnCompleteListener l){
        if (user_id.isEmpty()){
            Log.w(TAG, "[TicketService] Cannot add ticket. User ID does not exist");
            l.onCompose(false, TicketDBException.EMPTY_UID);
            return;
        }
        long currentTime = System.currentTimeMillis();
        ticketDB.addTicket(user_id, event_id, currentTime, l);
    }

    @Override
    public TicketData[] getTickets(){
        return tickets.values().toArray(new TicketData[0]);
    }

    @Override
    public void StartReceiveTickets(@NonNull OnCompleteListener l){
        if (user_id.isEmpty()){
            Log.w(TAG, "[TicketService] Cannot start receiving events. User ID is null");
            l.onCompose(false, TicketDBException.EMPTY_UID);
            return;
        }
        tickets.clear();
        ticketDB.setListenerForTicketMapRef(user_id, tickets, l);
    }
    @Override
    public void StopReceiveEvents(){
        if (user_id.isEmpty()){
            Log.w(TAG, "[TicketService] Cannot stop receiving events. User ID is null");
            return;
        }
        tickets.clear();
        ticketDB.deleteListenerForTicketMapRef(user_id);
    }

}
