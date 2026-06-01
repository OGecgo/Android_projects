package com.example.unipicityvibe.UI.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.unipicityvibe.Data.Models.EventData;
import com.example.unipicityvibe.Data.Models.TicketData;
import com.example.unipicityvibe.R;
import com.example.unipicityvibe.Service.EventService;
import com.example.unipicityvibe.Service.Interface.IEventService;
import com.example.unipicityvibe.Service.Interface.ITicketService;
import com.example.unipicityvibe.Service.TicketService;
import com.example.unipicityvibe.UI.Activity.UserActivity;
import com.example.unipicityvibe.UI.CustomView.EventInfoView;
import com.example.unipicityvibe.Utils.PermissionHelper;

public class MyTicketsFragment extends Fragment {
    private ITicketService ticketService;
    private IEventService eventService;

    // ----- Change Page -----
    private void goHomePage() {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), UserActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }
    // ----- Change Page -----

    // ----- Button -----
    private void ticketButton(View view, EventData event, String timestampTicket){
        ((UserActivity) requireActivity()).showTicketFragment(event, timestampTicket);
    }
    // ----- End Button -----

    private void showEvents(EventData[] eventsData, TicketData[] ticketsData) {
        View view = getView();
        if (view == null) return;
        LinearLayout container = view.findViewById(R.id.event_list_container);
        if (container == null) return;

        // clean page from old view
        container.removeAllViews();

        // show a message if no events are found
        if (eventsData.length == 0) {
            Toast.makeText(requireContext(), R.string.error_no_tickets, Toast.LENGTH_SHORT).show();
            return;
        }

        // eventsData and ticketsData is 1-1
        for (int i = 0; i < eventsData.length; i++){
            EventData event = eventsData[i];
            TicketData ticket = ticketsData[i];

            EventInfoView eventView = new EventInfoView(requireContext());
            eventView.setValues(event);
            eventView.setOnClickListener(v -> ticketButton(v, event, ticket.time_stamp));
            container.addView(eventView);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventService = EventService.getInstance();
        // UserActivity set the user_id for ticketService
        ticketService = TicketService.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_list_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!PermissionHelper.isGrantedLocationPermission(requireContext())) {
            goHomePage();
            return;
        }

        // ticketData and EventData is 1-1
        TicketData[] ticketsData = ticketService.getTickets();
        EventData[] eventsData = eventService.getEventsFromTickets(ticketsData);
        showEvents(eventsData, ticketsData);
    }
}
