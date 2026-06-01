package com.example.unipicityvibe.Data.Interface;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Data.Models.TicketData;
import com.example.unipicityvibe.Data.Models.UserAuthData;
import com.example.unipicityvibe.Listeners.OnCompleteListener;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public interface ITicketDB {

    void addTicket(String user_id, String event_id, long timestampMilli, @NonNull OnCompleteListener l);
    void setListenerForTicketMapRef(String user_id, @NonNull HashMap<String, TicketData> ticketsRef, @NonNull OnCompleteListener l);
    void deleteListenerForTicketMapRef(String user_id);
}
