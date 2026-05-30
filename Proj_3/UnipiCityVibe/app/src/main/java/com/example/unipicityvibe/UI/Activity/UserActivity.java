package com.example.unipicityvibe.UI.Activity;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
import com.example.unipicityvibe.Service.Interface.IAuthService;
import com.example.unipicityvibe.Service.Interface.ILocationService;
import com.example.unipicityvibe.Service.LocationService;
import com.example.unipicityvibe.Utils.PermissionHelper;
import com.example.unipicityvibe.UI.Fragments.ErrorFragment;
import com.example.unipicityvibe.UI.Fragments.EventListFragment;
import com.example.unipicityvibe.UI.Fragments.HomeFragment;
import com.example.unipicityvibe.UI.Fragments.MapsFragment;
import com.example.unipicityvibe.UI.Fragments.SettingsFragment;
import com.example.unipicityvibe.UI.Fragments.TopViewMenu;

public class UserActivity extends BaseActivity {

    // Fragments
    private HomeFragment homeFragment;
    private SettingsFragment settingsFragment;
    private EventListFragment eventListFragment;
    private ErrorFragment errorFragment;
    private MapsFragment mapsFragment;

    // Location
    private ILocationService locationService;


    // ------ Change Page ------
    private void goLogInPage() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
    // ------ End Change Page ------


    // ----- Fragments -----
    private void showHomeFragment() {
        if (homeFragment == null) homeFragment = new HomeFragment();
        if (AppSettings.getLocationAccuracy(this) == LocationTypeEnum.OFF_LOCATION){
            homeFragment.setEventListButton(this::showErrorFragment);
            homeFragment.setEventMapButton(this::showErrorFragment);
        }
        else{
            homeFragment.setEventListButton(this::showEventListFragment);
            homeFragment.setEventMapButton(this::showMapsFragment);
        }
        // homeFragment.set(this::showMyTicketsFragment);
        replaceFragment(homeFragment, false);

    }

    private void showSettingsFragment() {
        if (settingsFragment == null) settingsFragment = new SettingsFragment();
        replaceFragment(settingsFragment, false);
    }

    private void showEventListFragment() {
        if (eventListFragment == null) eventListFragment = new EventListFragment();
        replaceFragment(eventListFragment, true);
    }

    private void showErrorFragment() {
        if (errorFragment == null) errorFragment = new ErrorFragment();
        replaceFragment(errorFragment, false);
    }

    private void showMapsFragment() {
        if (mapsFragment == null) mapsFragment = new MapsFragment();
        replaceFragment(mapsFragment, true);
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

        if (savedInstanceState == null) {
            showHomeFragment();
        }

        // take fragment
        TopViewMenu tvm = (TopViewMenu) getSupportFragmentManager().findFragmentById(R.id.topViewMenuContainer);
        // set button home and settings for movement
        tvm.setHomeButton(this::showHomeFragment);
        tvm.setSettingsButton(this::showSettingsFragment);

        // request permissions from user if not required
        if (!PermissionHelper.isGrantedLocationPermission(this)) {
            AppSettings.setLocationAccuracy(getBaseContext(), LocationTypeEnum.OFF_LOCATION);
            PermissionHelper.requestLocationPermission(this);
        }


        // permission notification

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
                // recreate activity for synchronize data
                this.recreate();
            }
            else{
                AppSettings.setLocationAccuracy(this, LocationTypeEnum.OFF_LOCATION);
            }
            this.recreate();
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
    protected void onStop() {
        super.onStop();
        if (locationService != null)
            locationService.stopLocationUpdate();
    }


}
