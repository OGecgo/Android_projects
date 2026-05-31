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
import com.example.unipicityvibe.Data.Models.UserAuthData;
import com.example.unipicityvibe.Enums.LocationTypeEnum;
import com.example.unipicityvibe.R;
import com.example.unipicityvibe.Service.AuthService;
import com.example.unipicityvibe.Service.EventService;
import com.example.unipicityvibe.Service.Interface.IAuthService;
import com.example.unipicityvibe.Service.Interface.IEventService;
import com.example.unipicityvibe.Service.Interface.ILocationService;
import com.example.unipicityvibe.Service.LocationService;
import com.example.unipicityvibe.Utils.ExceptionToMessageHelper;
import com.example.unipicityvibe.Utils.PermissionHelper;
import com.example.unipicityvibe.UI.Fragments.ErrorFragment;
import com.example.unipicityvibe.UI.Fragments.EventListFragment;
import com.example.unipicityvibe.UI.Fragments.HomeFragment;
import com.example.unipicityvibe.UI.Fragments.MapsFragment;
import com.example.unipicityvibe.UI.Fragments.SettingsFragment;
import com.example.unipicityvibe.UI.Fragments.TopViewMenu;

public class UserActivity extends BaseActivity {

    // Tags for Fragments
    private static final String TAG_HOME = "TAG_HOME";
    private static final String TAG_SETTINGS = "TAG_SETTINGS";
    private static final String TAG_EVENT_LIST = "TAG_EVENT_LIST";
    private static final String TAG_ERROR = "TAG_ERROR";
    private static final String TAG_MAPS = "TAG_MAPS";

    // Location
    private ILocationService locationService;
    private IEventService eventService;

    // ----- Call Back -----
    private void onCompleteListenerStartReceive(boolean success, String errorLog){
        // show errors
        if (!success){
            int messageResId = ExceptionToMessageHelper.AuthExceptionToTextId(errorLog);
            Toast.makeText(this, getString(messageResId), Toast.LENGTH_SHORT).show();
        }
    }

    // TODO another call back for notifications. they will called somewhere every time user go out of radius
    // ----- End Call Back -----


    // ------ Change Page ------
    private void goLogInPage() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
    // ------ End Change Page ------


    // ----- Fragments -----
    private void showHomeFragment() {
        HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(TAG_HOME);
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        
        if (AppSettings.getLocationAccuracy(this) == LocationTypeEnum.OFF_LOCATION){
            homeFragment.setEventListButton(this::showErrorFragment);
            homeFragment.setEventMapButton(this::showErrorFragment);
        }
        else{
            homeFragment.setEventListButton(this::showEventListFragment);
            homeFragment.setEventMapButton(this::showMapsFragment);
        }
        // homeFragment.setMyTicketButton(this::showMyTicketsFragment);
        
        replaceFragment(homeFragment, TAG_HOME, false);
    }

    private void showSettingsFragment() {
        SettingsFragment settingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentByTag(TAG_SETTINGS);
        if (settingsFragment == null) settingsFragment = new SettingsFragment();
        replaceFragment(settingsFragment, TAG_SETTINGS, false);
    }

    private void showEventListFragment() {
        EventListFragment eventListFragment = (EventListFragment) getSupportFragmentManager().findFragmentByTag(TAG_EVENT_LIST);
        if (eventListFragment == null) eventListFragment = new EventListFragment();
        replaceFragment(eventListFragment, TAG_EVENT_LIST, true);
    }

    private void showErrorFragment() {
        ErrorFragment errorFragment = (ErrorFragment) getSupportFragmentManager().findFragmentByTag(TAG_ERROR);
        if (errorFragment == null) errorFragment = new ErrorFragment();
        replaceFragment(errorFragment, TAG_ERROR, false);
    }

    private void showMapsFragment() {
        MapsFragment mapsFragment = (MapsFragment) getSupportFragmentManager().findFragmentByTag(TAG_MAPS);
        if (mapsFragment == null) mapsFragment = new MapsFragment();
        replaceFragment(mapsFragment, TAG_MAPS, true);
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
        TopViewMenu tvm = (TopViewMenu) getSupportFragmentManager().findFragmentById(R.id.topViewMenuContainer);
        if (tvm != null) {
            // set button home and settings for movement
            tvm.setHomeButton(this::showHomeFragment);
            tvm.setSettingsButton(this::showSettingsFragment);
        }

        if (savedInstanceState == null) {
            showHomeFragment();
        }

        // start receive data from database
        eventService = EventService.getInstance();
        eventService.StartReceiveEvents(this::onCompleteListenerStartReceive);


        // require permissions from user if not required
        // location permission
        if (!PermissionHelper.isGrantedLocationPermission(this)) {
            AppSettings.setLocationAccuracy(getBaseContext(), LocationTypeEnum.OFF_LOCATION);
            PermissionHelper.requestLocationPermission(this);
        }
        // notification permission

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionHelper.PERMISSION_REQUEST_CODE_GPS){
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

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // test if user exist. if not. send to Log In page
        IAuthService service = AuthService.getInstance();
        UserAuthData userAuthData = service.getUserAuth();
        if (userAuthData.uID.isEmpty()) goLogInPage();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // is permissions changed nothing is available.s
        if (PermissionHelper.isGrantedLocationPermission(this)){
            locationService = LocationService.getInstance(getApplicationContext());
            // if permission denied. locationService will be null
            if (locationService == null) {
                Log.w(TAG, "[UserActivity] locationService is null");
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
        }
        else{
            AppSettings.setLocationAccuracy(getBaseContext(), LocationTypeEnum.OFF_LOCATION);
            if (locationService != null) locationService.stopLocationUpdate();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationService != null)
            locationService.stopLocationUpdate();
        if (eventService != null) 
            eventService.StopReceiveEvents();
    }
}


