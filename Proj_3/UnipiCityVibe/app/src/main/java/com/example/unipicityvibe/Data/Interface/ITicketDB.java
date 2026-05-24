package com.example.unipicityvibe.Data.Interface;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Data.Models.TicketData;
import com.example.unipicityvibe.Data.Models.UserAuthData;
import com.example.unipicityvibe.Service.Interface.OnCompleteListener;

import java.util.concurrent.atomic.AtomicReference;

public interface ITicketDB {

    void addTicket(@NonNull UserAuthData user, int eventID, int time, @NonNull OnCompleteListener l);
    // the data will be written to ticketRef
    void getTicketData(@NonNull UserAuthData user, int ticketID, @NonNull TicketData ticketRef, @NonNull OnCompleteListener l);
    // the data will be written to ticketsRef (AtomicReference used for a pointer to an arrayPointer)
    void getUserTickets(@NonNull UserAuthData user, @NonNull AtomicReference<TicketData[]> ticketsRef, @NonNull OnCompleteListener l);
}
