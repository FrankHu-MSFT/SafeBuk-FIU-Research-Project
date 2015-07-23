package com.fiu_CaSPR.Frank.Constants;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by frankhu on 6/17/15.
 */
public class Dictionary {
    public static String[] mildlyBadDictionary = {"court", "poop", "poor","bad", "ugly", "retarded","stupid", "evil"};
    public static String[] badDctionary = {"fuck you", "shit", "bitch", "cunt", "nigger"};
    public static ArrayList<String> privateInformation = new ArrayList<>();

    // > 5
    // > 3 yellow
    // > 0 Green
    public static int computeScore(int mildlyBadWords, int badWords){
        int score = mildlyBadWords*2 + badWords *4;
        if(score > 25){
            return Color.RED;
        }else if(score > 5){
            return Color.YELLOW;
        }else{
            return Color.GREEN;
        }
    }
}
