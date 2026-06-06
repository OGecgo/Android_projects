package com.example.unipiaudiostories.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unipiaudiostories.Data.Model.StoryData;
import com.example.unipiaudiostories.Data.Repository.IStoriesRepository;
import com.example.unipiaudiostories.Data.Repository.StoriesRepository;

import java.util.Arrays;

public class StoriesViewModel extends ViewModel {

    private final MutableLiveData<StoryData[]> stories;
    private final MutableLiveData<String> errorMessage;
    private final IStoriesRepository storiesRepository;

    public StoriesViewModel() {
        stories = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        storiesRepository = new StoriesRepository();

        IStoriesRepository.StoriesListener storiesListener = new IStoriesRepository.StoriesListener() {
            @Override
            public void onStoriesChanged(StoryData[] storiesArray) {
                stories.postValue(storiesArray);
            }

            @Override
            public void onError(String error) {
                errorMessage.postValue(error);
            }
        };
        storiesRepository.startReceiveStories(storiesListener);
    }

    public MutableLiveData<StoryData[]> getStories() {
        return stories;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        storiesRepository.stopReceiveStories();
    }
}
