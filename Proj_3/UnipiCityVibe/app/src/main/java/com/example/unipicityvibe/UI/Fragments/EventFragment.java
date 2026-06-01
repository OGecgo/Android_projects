package com.example.unipicityvibe.UI.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.unipicityvibe.Data.Models.EventData;
import com.example.unipicityvibe.R;
import com.example.unipicityvibe.Service.GeocoderService;
import com.example.unipicityvibe.Service.Interface.IGeocoderService;
import com.example.unipicityvibe.UI.Activity.UserActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventFragment extends Fragment {

    private TextView textViewPlace;
    private EventData eventData;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    // ----- Call Back -----

    private final Geocoder.GeocodeListener geocodeListener = new Geocoder.GeocodeListener() {
        @Override
        public void onGeocode(@NonNull List<Address> addresses) {
            if (getActivity() != null) {
                // run on main thread to avoid called from wrong thread
                getActivity().runOnUiThread(() -> {
                    if (!addresses.isEmpty()) {
                        textViewPlace.setText(addresses.get(0).getAddressLine(0));
                    } else {
                        setLatLonValue();
                    }
                });
            }
        }

        @Override
        public void onError(@Nullable String errorMessage) {
            Geocoder.GeocodeListener.super.onError(errorMessage);
            if (getActivity() != null) {
                // run on main thread to avoid called from wrong thread
                getActivity().runOnUiThread(() -> {
                    setLatLonValue();
                });
            }
        }
    };
    private void onClickBuy(DialogInterface dialog, int which){
        // TODO add evet to user ticket
        //  if complete. move user else show error Toast
        ((UserActivity) requireActivity()).showHomeFragment();
    }
    // ----- End Call Back -----


    // called only if event != null
    private void setLatLonValue(){
        String text = "Lat: " + eventData.latitude + " Lon: " + eventData.longitude;
        textViewPlace.setText(text);
    }


    // ----- Buttons -----
    private void showBuyDialogButton(View view) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.buy)
                .setMessage(getString(R.string.question_user_buy_ticket))
                .setPositiveButton(R.string.buy, (this::onClickBuy))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
    private void backButton(View view){
        if (getParentFragmentManager().getBackStackEntryCount() > 0) {
            getParentFragmentManager().popBackStack();
        }
    }
    // ----- End Buttons -----

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        IGeocoderService geocoderService = GeocoderService.getInstance(requireContext().getApplicationContext());
        Button b;
        b = view.findViewById(R.id.buttonBack);
        b.setOnClickListener(this::backButton);
        b = view.findViewById(R.id.buttonBuy);
        b.setOnClickListener(this::showBuyDialogButton);

        if (eventData != null) {
            // Title
            TextView tv;
            tv = view.findViewById(R.id.textViewTitle);
            tv.setText(eventData.title);
            // Description
            tv = view.findViewById(R.id.textViewDescriptionContext);
            tv.setText(eventData.description);
            // Time
            tv = view.findViewById(R.id.textViewTimeContext);
            try {
                long time = Long.parseLong(eventData.time);
                tv.setText(sdf.format(new Date(time)));
            } catch (Exception e) {
                tv.setText(eventData.time);
            }
            // Place

            textViewPlace = view.findViewById(R.id.textViewPlaceContext);
            if (geocoderService != null) {
                double eventLat = Double.parseDouble(eventData.latitude);
                double eventLon = Double.parseDouble(eventData.longitude);
                geocoderService.getAddress(eventLat, eventLon, geocodeListener);
            }
            else{
                setLatLonValue();
            }
            // Price
            tv = view.findViewById(R.id.textViewPriceContext);
            String text = eventData.price + " $";
            tv.setText(eventData.price);
        }


    }


    public void setEventData(EventData eventData) {
        this.eventData = eventData;
    }

}
