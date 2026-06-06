package com.example.unipiaudiostories.UI.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.unipiaudiostories.Data.Model.StoryData;
import com.example.unipiaudiostories.R;
import com.example.unipiaudiostories.UI.Activities.StoriesActivity;
import com.example.unipiaudiostories.UI.CustomView.StoryRow;
import com.example.unipiaudiostories.ViewModel.StoriesViewModel;
import com.google.firebase.database.IgnoreExtraProperties;

public class StoryListPageFragment extends Fragment {

    private LinearLayout linearLayoutStories;

    private void onClickListenerStory(View view, StoryData storyData){
        try{
            ((StoriesActivity) requireActivity()).showStoryPage(storyData);
        } catch (Exception ignore) {/* ignore */}

    }

    private void onCancelled(String errorLog){
        Toast.makeText(requireContext(), errorLog, Toast.LENGTH_SHORT).show();
    }

    private void updateUI(StoryData[] stories){
        if (linearLayoutStories == null) return;
        // first clean list
        linearLayoutStories.removeAllViews();
        for (StoryData story : stories) {
            StoryRow storyRow = new StoryRow(requireContext());
            storyRow.setTitle(story.title);
            storyRow.setImageUrl(story.image_url);
            storyRow.setOnClickListener((view) -> onClickListenerStory(view, story));
            linearLayoutStories.addView(storyRow);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story_list_page, container, false);
        linearLayoutStories = view.findViewById(R.id.linear_layout_stories);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StoriesViewModel storiesViewModel = new ViewModelProvider(this).get(StoriesViewModel.class);
        storiesViewModel.getStories().observe(getViewLifecycleOwner(), this::updateUI);
        storiesViewModel.getErrorMessage().observe(getViewLifecycleOwner(), this::onCancelled);
    }
}
