package com.example.unipiaudiostories.Data.Repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.unipiaudiostories.Data.Model.StoryData;
import com.example.unipiaudiostories.Data.Remote.IStoryDB;

public interface IStoriesRepository {

    void startReceiveStories(@NonNull StoriesListener listener);
    void stopReceiveStories();
    StoryData[] getStoriesData();



    // ----------- INTERFACES -----------
    // callback for synchronize data
    interface StoriesListener {
        void onStoriesChanged(StoryData[] stories);
        void onError(String errorLog);
    }
}
