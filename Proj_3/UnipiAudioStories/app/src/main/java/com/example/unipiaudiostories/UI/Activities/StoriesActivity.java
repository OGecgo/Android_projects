package com.example.unipiaudiostories.UI.Activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.unipiaudiostories.Data.Model.StoryData;
import com.example.unipiaudiostories.R;
import com.example.unipiaudiostories.UI.Fragments.StoryListPageFragment;
import com.example.unipiaudiostories.UI.Fragments.StoryPageFragment;

public class StoriesActivity extends BaseActivity {

    private final static String TAG_STORY_LIST_PAGE = "TAG_STORY_LIST_PAGE";
    private final static String TAG_STORY_PAGE = "TAG_STORY_PAGE";


    public void showStoryListPage() {
        StoryListPageFragment storyListPageFragment = (StoryListPageFragment) getSupportFragmentManager().findFragmentByTag(TAG_STORY_LIST_PAGE);
        if (storyListPageFragment == null) storyListPageFragment = new StoryListPageFragment();
        replaceFragment(R.id.fragment_container, storyListPageFragment, TAG_STORY_LIST_PAGE, false);
    }

    public void showStoryPage(StoryData storyData) {
        StoryPageFragment storyPageFragment = (StoryPageFragment) getSupportFragmentManager().findFragmentByTag(TAG_STORY_PAGE);
        if (storyPageFragment == null) storyPageFragment = new StoryPageFragment();
        storyPageFragment.setStoryData(storyData);
        replaceFragment(R.id.fragment_container, storyPageFragment, TAG_STORY_PAGE, true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_default);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_default_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState == null){
            showStoryListPage();
        }

    }



}

