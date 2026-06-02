package com.example.unipicityvibe.Service.Data;

// create NotificationData only from NotificationService for proper work NotificationService
public class NotificationData {
    // read only
    public final int id;
    public final String title;
    public final String description;
    public final String code;


    public NotificationData(int id, String title, String description, String code){
        this.id = id;
        this.title = title;
        this.description = description;
        this.code = code;
    }

}
