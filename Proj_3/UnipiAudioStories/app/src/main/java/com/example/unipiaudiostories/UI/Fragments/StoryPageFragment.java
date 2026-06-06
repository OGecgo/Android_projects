package com.example.unipiaudiostories.UI.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.unipiaudiostories.Data.Local.StatisticsStorage;
import com.example.unipiaudiostories.Data.Model.StoryData;
import com.example.unipiaudiostories.Manager.ITTSManager;
import com.example.unipiaudiostories.Manager.TTSManager;
import com.example.unipiaudiostories.R;
import com.example.unipiaudiostories.UI.CustomView.PlayerButton;


public class StoryPageFragment extends Fragment {

    private TextView textViewTitle, textViewWriter, textViewYear, textViewStory;
    private ImageView imageViewStory;
    private PlayerButton playerButton;

    private StoryData storyData;
    private ITTSManager ttsManager;
    private ITTSManager.ListenListener listenListener;

    // ---- buttons click listeners ----
    private void onClickListenerStart(View view){
        // take the text only if user can see it
        ttsManager.start(textViewStory.getText().toString(), null);
    }
    private void onClickListenerStop(View view){
        ttsManager.stop();
    }
    private void onClickListenerReset(View view){
        ttsManager.reset();
    }
    // ---------------------------------

    private void setImageUrl(String url) {
        if (url != null && !url.isEmpty()) {
            Glide.with(requireContext())
                    .load(url)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imageViewStory);
        }
    }

    private void updateUI(){
        if (storyData != null){
            textViewTitle.setText(storyData.title);
            textViewWriter.setText(storyData.writer);
            textViewYear.setText(storyData.year);
            textViewStory.setText(storyData.story);
            setImageUrl(storyData.image_url);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listenListener = new ITTSManager.ListenListener() {
            @Override
            public void onComplete() {
                StatisticsStorage.addProgress(requireContext(), storyData.title, 1);
            }

            @Override
            public void onError(String errorLog) {
                Toast.makeText(requireContext(), errorLog, Toast.LENGTH_SHORT).show();
            }
        };
        ttsManager = new TTSManager(requireContext(), listenListener);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story_page, container, false);
        textViewTitle = view.findViewById(R.id.textViewTitle);
        textViewWriter = view.findViewById(R.id.textViewWriter);
        textViewYear = view.findViewById(R.id.textViewYear);
        textViewStory = view.findViewById(R.id.textViewStory);
        imageViewStory = view.findViewById(R.id.imageViewImageStory);
        playerButton = view.findViewById(R.id.payerButton);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateUI();
        playerButton.setOnClickListenerButtonStart(this::onClickListenerStart);
        playerButton.setOnClickListenerButtonStop(this::onClickListenerStop);
        playerButton.setOnClickListenerButtonReset(this::onClickListenerReset);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ttsManager.shutdown();
    }

    public void setStoryData(StoryData storyData){
        this.storyData = storyData;
    }
}
