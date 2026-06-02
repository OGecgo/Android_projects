package com.example.unipicityvibe.Service;

import static android.content.ContentValues.TAG;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.unipicityvibe.Data.Models.EventData;
import com.example.unipicityvibe.R;
import com.example.unipicityvibe.Service.Data.NotificationData;
import com.example.unipicityvibe.Service.Interface.IEventService;
import com.example.unipicityvibe.Service.Interface.ILocationService;
import com.example.unipicityvibe.Service.Interface.INotificationService;
import com.example.unipicityvibe.UI.Activity.UserActivity;

import java.util.HashSet;


public class NotificationService implements INotificationService {

    public static final String HANDLE_CODE_KEY = "CODE_KEY";
    // it tested for application context on constructor
    private static NotificationService service;
    private final Context context;
    // HashSet for fast add and find values
    private final HashSet<String> codeList;
    private int last_id;
    private String channel_id;

    private NotificationService(@NonNull Context context){
        if (!context.equals(context.getApplicationContext())){
            throw new RuntimeException("[NotificationService] CRITICAL ERROR: context should be application context to prevent memory leaks. Use getApplicationContext");
        }
        last_id = -1;
        codeList = new HashSet<>();
        this.context = context;
    }

    private void ShowNotification(@NonNull NotificationData notificationData){
        Intent intent = new Intent(context, UserActivity.class);
        // add code to intent
        intent.putExtra(HANDLE_CODE_KEY, notificationData.code);
        
        // Use FLAG_UPDATE_CURRENT to ensure extras are updated if the notification is replaced
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 
                notificationData.id, 
                intent, 
                PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentTitle(notificationData.title)
                .setContentText(notificationData.description)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManager manager = context.getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.notify(notificationData.id, builder.build());
        }
        else{
            Log.e(TAG, "[NotificationService] Error show notification");
        }
    }

    public static NotificationService getInstance(Context context){
        if (service == null) service = new NotificationService(context);
        return service;
    }

    public void createNotificationChannel(String channel_id, int id_string_name, int id_string_description){
        // sdk is always >= 33
        this.channel_id = channel_id;

        String name = context.getString(id_string_name);
        String descriptionText = context.getString(id_string_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
        channel.setDescription(descriptionText);

        NotificationManager manager = context.getSystemService(NotificationManager.class);
        if (manager != null){
            manager.createNotificationChannel(channel);
        }
        else {
            Log.e(TAG, "[NotificationService] Error get notification manager");
        }
    }

    public NotificationData createNotification(String title, String description, String code){
        last_id += 1;
        return new NotificationData(last_id, title, description, code);
    }
    public void showNotifications(@NonNull NotificationData[] notificationDataArray){
        for (NotificationData notificationData : notificationDataArray){
            if (!codeList.contains(notificationData.code)){
                ShowNotification(notificationData);
                codeList.add(notificationData.code);
            }
        }
    }


}
