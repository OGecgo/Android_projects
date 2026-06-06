package com.example.unipiaudiostories.Manager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;


import androidx.annotation.NonNull;

import java.util.Locale;

public class TTSManager implements ITTSManager{

    private final TextToSpeech tts;
    private UtteranceProgressListener utteranceProgressListener;
    private String lastText = "";
    private int lastPosition = 0;
    private int currentOffset = 0;

    private void onInitTTS(int i) {
        if (i == TextToSpeech.SUCCESS) {
            // Text have no specifics language + Locale have no Russian and Greek options
            tts.setLanguage(Locale.ENGLISH);
            tts.setOnUtteranceProgressListener(utteranceProgressListener);
        }
    }

    public TTSManager(Context context, @NonNull ListenListener listenListener){
        tts = new TextToSpeech(context, this::onInitTTS);
        // the utteranceProgressListener run on background thread
        // handler need to run listenListener on main thread
        Handler mainHandler = new Handler(Looper.getMainLooper());

        utteranceProgressListener = new UtteranceProgressListener() {
            @Override
            public void onDone(String s) {
                lastPosition = 0;
                currentOffset = 0;
                mainHandler.post(listenListener::onComplete);
            }

            @Override
            public void onError(String s) {
                mainHandler.post(() -> listenListener.onError(s));
            }

            @Override
            public void onStart(String s) {

            }

            @Override
            public void onRangeStart(String utteranceId, int start, int end, int frame) {
                currentOffset = start;
            }
        };
    }

    public void start(@NonNull String txt, Bundle params) {
        if (!txt.equals(lastText)) {
            lastText = txt;
            lastPosition = 0;
        } else {
            // move last position to current
            lastPosition += currentOffset;
        }
        // reset offset
        currentOffset = 0;
        // take text from lastPosition
        String textToSpeak = lastText.substring(lastPosition);
        // speak
        tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, params, "TTS_ID");
    }

    public void stop(){
        tts.stop();
    }

    public void reset() {
        stop();
        lastPosition = 0;
        currentOffset = 0;
        if (!lastText.isEmpty()) {
            start(lastText, null);
        }
    }

    public void shutdown() {
        tts.stop();
        tts.shutdown();
    }
}
