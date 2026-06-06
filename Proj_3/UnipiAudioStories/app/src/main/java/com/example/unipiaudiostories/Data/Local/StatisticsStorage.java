package com.example.unipiaudiostories.Data.Local;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONObject;
import java.util.Iterator;

public final class StatisticsStorage {

    private static final String PREFS_NAME_APP_STATISTICS = "PREFS_NAME_APP_STATISTICS";
    private static final String KEY_STORY_PROGRESS = "KEY_STORY_PROGRESS";

    // used story_title instead of story_id for reduce call data from database
    public static void addProgress(@NonNull Context context, String story_title, int count){
        try {
            SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREFS_NAME_APP_STATISTICS, Context.MODE_PRIVATE);

            // bring old JSON
            String jsonString = pref.getString(KEY_STORY_PROGRESS, "{}");
            JSONObject json = new JSONObject(jsonString);
            // add
            int newCount = 0;
            if (json.has(story_title)) {
                newCount += json.getInt(story_title);
            }
            newCount += count;
            json.put(story_title, newCount);
            // save
            pref.edit().putString(KEY_STORY_PROGRESS, json.toString()).apply();
        } catch (Exception e){
            Log.e(TAG, "[StatisticsStorage] add progress error \n" + e);
        }
    }
    public static int getCountListens(@NonNull Context context){
        try{
            SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREFS_NAME_APP_STATISTICS, Context.MODE_PRIVATE);
            // bring JSON
            String jsonString = pref.getString(KEY_STORY_PROGRESS, "{}");
            JSONObject json = new JSONObject(jsonString);
            // do sum
            int sum = 0;
            for (Iterator<String> it = json.keys(); it.hasNext(); ) {
                String story_title = it.next();
                sum += json.getInt(story_title);
            }
            // return value
            return sum;

        }catch (Exception e){
            Log.e(TAG, "[StatisticsStorage] get counts error \n" + e);
            return -1;
        }
    }

    // will return string with 3 stories_id
    public static String[] getTopThreeStories(@NonNull Context context){
        try{
            SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PREFS_NAME_APP_STATISTICS, Context.MODE_PRIVATE);
            // bring JSON
            String jsonString = pref.getString(KEY_STORY_PROGRESS, "{}");
            JSONObject json = new JSONObject(jsonString);
            // get the most listened stories
            int top1 = -1;
            String story_title1 = "";
            int top2 = -1;
            String story_title2 = "";
            int top3 = -1;
            String story_title3 = "";
            for (Iterator<String> it = json.keys(); it.hasNext(); ) {
                String key = it.next();
                int t = json.getInt(key);
                if (t > top1){
                    top3 = top2;
                    story_title3 = story_title2;
                    top2 = top1;
                    story_title2 = story_title1;
                    top1 = t;
                    story_title1 = key;
                }
                else if (t > top2){
                    top3 = top2;
                    story_title3 = story_title2;
                    top2 = t;
                    story_title2 = key;
                }
                else if (t > top3){
                    top3 = t;
                    story_title3 = key;
                }
            }
            Log.d(TAG, json.toString());
            // return that stories
            return new String[]{story_title1, story_title2, story_title3};
        }catch (Exception e){
            Log.e(TAG, "[StatisticsStorage] top three stories error \n" + e);
            return new String[]{"", "", ""};
        }
    }


}
