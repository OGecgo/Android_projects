package com.example.unipicityvibe.Service.Interface;

import android.content.Context;
import androidx.annotation.NonNull;

import com.example.unipicityvibe.Service.Data.NotificationData;

public interface INotificationService {
    // first create notification channel before show notifications
    void createNotificationChannel(String channel_id, int id_string_name, int id_string_description);
    // code used for handle it
    NotificationData createNotification(String title, String description, String code);
    // use notification only what created from createNotification(); else showNotifications() have unpredictable behavior
    // will show only notification what not still showed. list showed notification reset after close application
    void showNotifications(@NonNull NotificationData[] notificationDataArray);
}
