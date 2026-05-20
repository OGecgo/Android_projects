package com.example.unipicityvibe.Interface.firebase;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Interface.OnCompleteListener;
import com.example.unipicityvibe.Struct.TicketStruct;
import com.example.unipicityvibe.Struct.UserAuthStruct;
import com.example.unipicityvibe.Struct.UserDataStruct;

import java.util.concurrent.atomic.AtomicReference;

public interface IUserDB {
    public void getUserData(@NonNull UserAuthStruct user, @NonNull UserDataStruct userRef, @NonNull OnCompleteListener l);
    public void addUser(@NonNull UserAuthStruct user, String name, String lastName, @NonNull OnCompleteListener l);
    public void deleteUser(@NonNull UserAuthStruct user, @NonNull OnCompleteListener l);
    public void addTicket(@NonNull UserAuthStruct user, int eventID, int time, @NonNull OnCompleteListener l);
    public void getTicketData(@NonNull UserAuthStruct user, int ticketID, @NonNull TicketStruct ticketRef, @NonNull OnCompleteListener l);
    public void getUserTickets(@NonNull UserAuthStruct user, @NonNull AtomicReference<TicketStruct[]> ticketsRef, @NonNull OnCompleteListener l);
}
