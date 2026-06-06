package com.example.unipiaudiostories.Utils;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.unipiaudiostories.UI.Activities.HomeActivity;
import com.example.unipiaudiostories.UI.Activities.StatisticsActivity;
import com.example.unipiaudiostories.UI.Activities.StoriesActivity;

public final class PageMovementHelper {

    public static void moveToHomeActivity(@NonNull Activity activity){
        Intent intent = new Intent(activity, HomeActivity.class);
        activity.finish();
        activity.startActivity(intent);
    }

    public static void moveToStatisticsActivity(@NonNull Activity activity){
        Intent intent = new Intent(activity, StatisticsActivity.class);
        activity.finish();
        activity.startActivity(intent);
    }

    public static void moveToStoriesActivity(@NonNull Activity activity){
        Intent intent = new Intent(activity, StoriesActivity.class);
        activity.finish();
        activity.startActivity(intent);
    }

    public static void moveBack(@NonNull FragmentManager fm, @NonNull Activity activity){
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        }
        else{
            moveToHomeActivity(activity);
        }
    }

    public static void reloadActivity(@NonNull Activity activity){
        activity.recreate();
    }
    public static void reloadFragment(@NonNull Fragment fragment){
        fragment.getParentFragmentManager().beginTransaction()
                .detach(fragment)
                .commit();
        fragment.getParentFragmentManager().beginTransaction()
                .attach(fragment)
                .commit();
    }

}

