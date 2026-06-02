package com.example.unipicityvibe.Managers.Interfaces;



// object should exist time exist the activity else will be memory leak
public interface IEventNotificationManager {
    void startEventNotifications();

    void stopEventNotifications();
}
