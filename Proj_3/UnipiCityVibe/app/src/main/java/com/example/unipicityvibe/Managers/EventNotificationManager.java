package com.example.unipicityvibe.Managers;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.unipicityvibe.Data.Local.AppSettings;
import com.example.unipicityvibe.Data.Models.EventData;
import com.example.unipicityvibe.Managers.Interfaces.IEventNotificationManager;
import com.example.unipicityvibe.R;
import com.example.unipicityvibe.Service.Data.NotificationData;
import com.example.unipicityvibe.Service.Interface.IEventService;
import com.example.unipicityvibe.Service.Interface.ILocationService;
import com.example.unipicityvibe.Service.Interface.INotificationService;

public class EventNotificationManager implements IEventNotificationManager {

    private static final String CHANNEL_ID = "Events Notification";
    private static final int CHECK_INTERVAL_MS = 1000 * 3;

    private final Context context;
    private final ILocationService locationService;
    private final IEventService eventService;
    private final INotificationService notificationService;

    // handler using for execute action after n interval on main thread
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isRunning = false;

    private final Runnable checkNearbyEventsTask = new Runnable() {
        @Override
        public void run() {
            checkAndNotify();
            handler.postDelayed(this, CHECK_INTERVAL_MS);
        }
    };


    private void checkAndNotify() {
        double lat = locationService.getLatitude();
        double lon = locationService.getLongitude();

        // Avoid checking if location is not yet available (0,0)
        if (lat == 0.0 && lon == 0.0) return;

        EventData[] nearbyEvents = eventService.getRadiusEvents(lat, lon);
        if (nearbyEvents.length > 0) {
            // create notifications
            NotificationData[] notifications = new NotificationData[nearbyEvents.length];
            for (int i = 0; i < nearbyEvents.length; i++) {
                notifications[i] = notificationService.createNotification(
                        nearbyEvents[i].title,
                        nearbyEvents[i].description,
                        nearbyEvents[i].event_id
                );
            }
            // show notifications
            notificationService.showNotifications(notifications);
        }
    }

    // constructor
    public EventNotificationManager(@NonNull Context context, @NonNull ILocationService locationService, @NonNull IEventService eventService, @NonNull INotificationService notificationService) {
        this.context = context;
        this.locationService = locationService;
        this.eventService = eventService;
        this.notificationService = notificationService;

        // Initialize notification channel
        notificationService.createNotificationChannel(
                CHANNEL_ID,
                R.string.notification_channel_event_name,
                R.string.notification_channel_event_description
        );
    }

    public void startEventNotifications() {
        // protect from multiple starts
        if (isRunning) return;
        
        if (!AppSettings.getNotificationPermission(context)) {
            Log.e(TAG, "[EventNotificationManager] Error start. Permissions denied");
            return;
        }
        isRunning = true;
        handler.post(checkNearbyEventsTask);
        Log.d(TAG, "[EventNotificationManger] Start event notification manager");
    }

    public void stopEventNotifications() {
        isRunning = false;
        handler.removeCallbacks(checkNearbyEventsTask);
        Log.d(TAG, "[EventNotificationManger] Stop event notification manager");
    }


}
