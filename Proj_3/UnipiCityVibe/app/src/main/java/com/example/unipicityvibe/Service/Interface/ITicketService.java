package com.example.unipicityvibe.Service.Interface;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Data.Exception.TicketDBException;
import com.example.unipicityvibe.Data.Models.EventData;
import com.example.unipicityvibe.Data.Models.TicketData;
import com.example.unipicityvibe.Listeners.OnCompleteListener;

public interface ITicketService {
    // before do anything. set first user_id
    void setUserID(String user_id);
    void addTicket(String event_id, @NonNull OnCompleteListener l);
    TicketData[] getTickets();
    void StartReceiveTickets(@NonNull OnCompleteListener l);
    void StopReceiveEvents();
}
