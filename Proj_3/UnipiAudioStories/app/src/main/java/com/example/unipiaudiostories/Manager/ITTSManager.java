package com.example.unipiaudiostories.Manager;

import android.os.Bundle;

public interface ITTSManager {

    void start(String txt, Bundle params);
    void stop();

    void reset();

    void shutdown();

    interface ListenListener{
        void onComplete();
        void onError(String errorLog);
    }
}
