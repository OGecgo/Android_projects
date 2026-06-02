package com.example.unipicityvibe.UI.Activity;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.unipicityvibe.Data.Local.AppSettings;
import com.example.unipicityvibe.Data.Models.EventData;
import com.example.unipicityvibe.Data.Models.UserAuthData;
import com.example.unipicityvibe.Enums.LocationTypeEnum;
import com.example.unipicityvibe.Managers.EventNotificationManager;
import com.example.unipicityvibe.Managers.Interfaces.IEventNotificationManager;
import com.example.unipicityvibe.R;
import com.example.unipicityvibe.Service.AuthService;
import com.example.unipicityvibe.Service.EventService;
import com.example.unipicityvibe.Service.Interface.IAuthService;
import com.example.unipicityvibe.Service.Interface.IEventService;
import com.example.unipicityvibe.Service.Interface.ILocationService;
import com.example.unipicityvibe.Service.Interface.ITicketService;
import com.example.unipicityvibe.Service.LocationService;
import com.example.unipicityvibe.Service.NotificationService;
import com.example.unipicityvibe.Service.TicketService;
import com.example.unipicityvibe.UI.Fragments.EventFragment;
import com.example.unipicityvibe.UI.Fragments.MyTicketsFragment;
import com.example.unipicityvibe.UI.Fragments.TicketFragment;
import com.example.unipicityvibe.Utils.ExceptionToMessageHelper;
import com.example.unipicityvibe.Utils.PermissionHelper;
import com.example.unipicityvibe.UI.Fragments.ErrorFragment;
import com.example.unipicityvibe.UI.Fragments.EventListAroundFragment;
import com.example.unipicityvibe.UI.Fragments.HomeFragment;
import com.example.unipicityvibe.UI.Fragments.MapsFragment;
import com.example.unipicityvibe.UI.Fragments.SettingsFragment;
import com.example.unipicityvibe.UI.Fragments.TopMenuFragment;

public class UserActivity extends BaseActivity {

    // Tags for Fragments
    private static final String TAG_HOME = "TAG_HOME";
    private static final String TAG_SETTINGS = "TAG_SETTINGS";
    private static final String TAG_EVENT_LIST_AROUND = "TAG_EVENT_LIST_AROUND";
    private static final String TAG_MAPS = "TAG_MAPS";
    private static final String TAG_EVENT = "TAG_EVENT";
    private static final String TAG_MY_TICKETS = "TAG_MY_TICKETS";
    private static final String TAG_TICKET = "TAG_TICKET";
    private static final String TAG_ERROR = "TAG_ERROR";

    private IAuthService authService;
    private ILocationService locationService;
    private IEventService eventService;
    private ITicketService ticketService;
    private IEventNotificationManager notificationManager;

    // ----- Call Back -----
    private void onCompleteListenerStartReceive(boolean success, String errorLog){
        // show errors
        if (!success){
            int messageResId = ExceptionToMessageHelper.AuthExceptionToTextId(errorLog);
            Toast.makeText(this, getString(messageResId), Toast.LENGTH_SHORT).show();
        }
    }

    // ----- End Call Back -----


    // ------ Change Page ------
    private void goLogInPage() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
    // ------ End Change Page ------


    // ----- Fragments -----
    public void showHomeFragment() {
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(TAG_HOME);
        if (homeFragment == null) homeFragment = new HomeFragment();
        replaceFragment(homeFragment, TAG_HOME, false);
    }

    public void showSettingsFragment() {
        SettingsFragment settingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentByTag(TAG_SETTINGS);
        if (settingsFragment == null) settingsFragment = new SettingsFragment();
        replaceFragment(settingsFragment, TAG_SETTINGS, false);
    }

    public void showEventListAroundFragment() {
        EventListAroundFragment eventListAroundFragment = (EventListAroundFragment) getSupportFragmentManager().findFragmentByTag(TAG_EVENT_LIST_AROUND);
        if (eventListAroundFragment == null) eventListAroundFragment = new EventListAroundFragment();
        replaceFragment(eventListAroundFragment, TAG_EVENT_LIST_AROUND, true);
    }

    public void showMapsFragment() {
        MapsFragment mapsFragment = (MapsFragment) getSupportFragmentManager().findFragmentByTag(TAG_MAPS);
        if (mapsFragment == null) mapsFragment = new MapsFragment();
        replaceFragment(mapsFragment, TAG_MAPS, true);
    }

    public void showEventFragment(EventData eventData) {
        EventFragment eventFragment = (EventFragment) getSupportFragmentManager().findFragmentByTag(TAG_EVENT);
        if (eventFragment == null) eventFragment = new EventFragment();
        eventFragment.setEventData(eventData);
        replaceFragment(eventFragment, TAG_EVENT, true);
    }

    public void showMyTicketsFragment() {
        MyTicketsFragment myTicketsFragment = (MyTicketsFragment) getSupportFragmentManager().findFragmentByTag(TAG_MY_TICKETS);
        if (myTicketsFragment == null) myTicketsFragment = new MyTicketsFragment();
        replaceFragment(myTicketsFragment, TAG_MY_TICKETS, true);
    }

    public void showTicketFragment(EventData eventData, String timestampTicket) {
        TicketFragment ticketFragment = (TicketFragment) getSupportFragmentManager().findFragmentByTag(TAG_TICKET);
        if (ticketFragment == null) ticketFragment = new TicketFragment();
        ticketFragment.setData(eventData, timestampTicket);
        replaceFragment(ticketFragment, TAG_TICKET, true);
    }

    public void showErrorFragment() {
        ErrorFragment errorFragment = (ErrorFragment) getSupportFragmentManager().findFragmentByTag(TAG_ERROR);
        if (errorFragment == null) errorFragment = new ErrorFragment();
        replaceFragment(errorFragment, TAG_ERROR, false);
    }
    // ----- End Fragments -----



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // take fragment
        TopMenuFragment tvm = (TopMenuFragment) getSupportFragmentManager().findFragmentById(R.id.topViewMenuContainer);
        if (tvm != null) {
            // set button home and settings for movement
            tvm.setHomeButton(this::showHomeFragment);
            tvm.setSettingsButton(this::showSettingsFragment);
        }

        if (savedInstanceState == null) {
            showHomeFragment();
        }

        // test if user is existed
        authService = AuthService.getInstance();
        UserAuthData userAuthData = authService.getUserAuth();
        if (userAuthData.uID.isEmpty()) {
            goLogInPage();
            return;
        }

        // start receive data from database
        // events
        eventService = EventService.getInstance();
        eventService.StartReceiveEvents(this::onCompleteListenerStartReceive);
        // tickets
        ticketService = TicketService.getInstance();
        ticketService.setUserID(userAuthData.uID);
        ticketService.StartReceiveTickets((success, errorLog) -> {});

        // if user come to UserActivity from notification
        String eventID = getIntent().getStringExtra(NotificationService.HANDLE_CODE_KEY);
        if (eventID != null){
            EventData event = eventService.getEventInfo(eventID);
            if (!event.event_id.isEmpty()){
                showEventFragment(event);
            }
            else{
                Toast.makeText(this, getString(R.string.error_no_event), Toast.LENGTH_SHORT).show();
                Log.w(TAG, "[UserActivity] Event not found from notification intent");
            }
        }

        // require permissions from user if not required
        // location permission
        if (!PermissionHelper.isGrantedLocationPermission(this)) {
            AppSettings.setLocationAccuracy(getBaseContext(), LocationTypeEnum.OFF_LOCATION);
            PermissionHelper.requestLocationPermission(this);
        }
        // notification permission
        if (!PermissionHelper.isGrantedNotification(this)){
            AppSettings.setNotificationPermission(getBaseContext(), false);
            PermissionHelper.requestNotificationPermission(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PermissionHelper.PERMISSION_REQUEST_CODE_LOCATION:
                if (PermissionHelper.isGrantedLocationPermission(this)) {
                    if (PermissionHelper.isGrantedFine(this)){
                        AppSettings.setLocationAccuracy(this, LocationTypeEnum.FINE);
                    }
                    else if (PermissionHelper.isGrantedCoarse(this)){
                        AppSettings.setLocationAccuracy(this, LocationTypeEnum.COARSE);
                    }
                    // recreate for synchronize data
                    this.recreate();
                }
                else{
                    AppSettings.setLocationAccuracy(this, LocationTypeEnum.OFF_LOCATION);
                    if (locationService != null) locationService.stopLocationUpdate();
                }
                break;
            case PermissionHelper.PERMISSION_REQUEST_CODE_NOTIFICATION:
                if (PermissionHelper.isGrantedNotification(this)){
                    AppSettings.setNotificationPermission(this, true);
                    // synchronization
                    this.recreate();
                }
                else{
                    AppSettings.setNotificationPermission(this, false);
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // test if user exist. if not. send to Log In page
        authService = AuthService.getInstance();
        UserAuthData userAuthData = authService.getUserAuth();
        if (userAuthData.uID.isEmpty()) goLogInPage();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // is permissions changed
        if (PermissionHelper.isGrantedLocationPermission(this)){

            // LOCATIONS
            locationService = LocationService.getInstance(getApplicationContext());
            // if permission denied. locationService will be null
            if (locationService == null) {
                Log.w(TAG, "[UserActivity] Location service is null");
                return;
            }

            // if permission changed and fine is not available
            if (!PermissionHelper.isGrantedFine(this) && AppSettings.getLocationAccuracy(this) == LocationTypeEnum.FINE){
                AppSettings.setLocationAccuracy(this, LocationTypeEnum.COARSE);
            }

            // if appSettings changed permissions
            LocationTypeEnum accuracy = AppSettings.getLocationAccuracy(this);
            if (accuracy == LocationTypeEnum.FINE){
                locationService.stopLocationUpdate();
                locationService.startLocationUpdate(LocationTypeEnum.FINE, this);
            }
            else if (accuracy == LocationTypeEnum.COARSE){
                locationService.stopLocationUpdate();
                locationService.startLocationUpdate(LocationTypeEnum.COARSE, this);
            }
            else {
                locationService.stopLocationUpdate();
            }

            // NOTIFICATIONS
            if (PermissionHelper.isGrantedNotification(this)){
                // set the notification service
                notificationManager = new EventNotificationManager(this,
                        locationService,
                        eventService,
                        NotificationService.getInstance(this.getApplicationContext())
                );

                if (AppSettings.getNotificationPermission(this))
                    notificationManager.startEventNotifications();
            }
            else{
                AppSettings.setNotificationPermission(this, false);
                if (notificationManager != null) notificationManager.stopEventNotifications();
            }
        }
        else{
            AppSettings.setLocationAccuracy(getBaseContext(), LocationTypeEnum.OFF_LOCATION);
            if (locationService != null) locationService.stopLocationUpdate();
            if (notificationManager != null) notificationManager.stopEventNotifications();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationService != null)
            locationService.stopLocationUpdate();
        if (notificationManager != null)
            notificationManager.stopEventNotifications();
        if (eventService != null) 
            eventService.StopReceiveEvents();
        if (ticketService != null)
            ticketService.StopReceiveEvents();
    }
}


