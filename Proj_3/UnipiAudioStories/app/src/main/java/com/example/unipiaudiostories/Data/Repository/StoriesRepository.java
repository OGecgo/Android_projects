package com.example.unipiaudiostories.Data.Repository;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.unipiaudiostories.Data.Model.StoryData;
import com.example.unipiaudiostories.Data.Remote.IStoryDB;
import com.example.unipiaudiostories.Data.Remote.StoryDB;

import java.util.Arrays;
import java.util.HashMap;

public class StoriesRepository implements IStoriesRepository{

    private final IStoryDB storyDB;
    private final HashMap<String, StoryData> storiesRef;
    private IStoryDB.DataListener dataListener;


    public StoriesRepository (){
        storyDB = StoryDB.getInstance();
        storiesRef = new HashMap<>();
    }


    public void startReceiveStories(@NonNull StoriesListener listener){
        IStoryDB.DataListener dataListener = new IStoryDB.DataListener() {
            @Override
            public void onDataChanged() {
                listener.onStoriesChanged(getStoriesData());
            }

            @Override
            public void onCancelled(String errorLog) {
                listener.onError(errorLog);
            }
        };
        storyDB.setHashMapListener(storiesRef, dataListener);
        Log.d(TAG, "[StoriesRepository] start receiving stories");
    }
    public void stopReceiveStories(){
        storyDB.removeHashMapListener();
        Log.d(TAG, "[StoriesRepository] stop receiving stories");
    }
    public StoryData[] getStoriesData(){
        return storiesRef.values().toArray(new StoryData[0]);
    }

    @Nullable
    public StoryData getStoryData(@NonNull String story_id){
        if (storiesRef.containsKey(story_id)){
            return storiesRef.get(story_id);
        }
        else return new StoryData();
    }
}
