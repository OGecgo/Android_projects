package com.example.unipiaudiostories.Data.Model;

import androidx.annotation.NonNull;

public class StoryData{
    @NonNull
    public String story_id;
    @NonNull
    public String title;
    @NonNull
    public String image_url;
    @NonNull
    public String story;
    @NonNull
    public String writer;
    @NonNull
    public String year;

    public StoryData(){
        story_id = "";
        title = "";
        image_url = "";
        story = "";
        writer = "";
        year = "";
    }
}
