package com.fiu_CaSPR.Frank.DataStructures;

import android.util.Log;

import com.fiu_CaSPR.Frank.Constants.Dictionary;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by frankhu on 6/17/15.
 */
public class Post {
    private int score = 0;
    private int mildlyBadWordCount = 0;
    private int badWordCount = 0;
    String text = "";
    String mildlyBadRegexToFilter = "";
    String badRegexToFilter = "";
    Pattern mildlyBadPattern;
    Pattern badPattern;
    String privateInfoFilter = "";
    Pattern privateInfoPattern;


    public Post(){
        StringBuilder build = new StringBuilder();
        for(int i =0 ;i< Dictionary.badDctionary.length-1; ++i)
            build.append(Dictionary.badDctionary[i] + "|");
        build.append(Dictionary.badDctionary[Dictionary.badDctionary.length-1]);
        badRegexToFilter = build.toString();
        build = new StringBuilder();
        for(int i =0; i< Dictionary.mildlyBadDictionary.length; ++i){
            build.append(Dictionary.mildlyBadDictionary[i] + "|");
        }
        build.append(Dictionary.mildlyBadDictionary[Dictionary.mildlyBadDictionary.length-1]);
        mildlyBadRegexToFilter = build.toString();
        build = new StringBuilder();
        for(int i =0; i< Dictionary.privateInformation.size(); ++i){
            build.append(Dictionary.privateInformation.get(i) + "|");
        }
        build.append(Dictionary.privateInformation.get(Dictionary.privateInformation.size()-1));
        privateInfoFilter = build.toString();

        mildlyBadPattern = Pattern.compile(mildlyBadRegexToFilter);
        badPattern = Pattern.compile(badRegexToFilter);
        privateInfoPattern = Pattern.compile(privateInfoFilter);

    }

    public int getScore(){
        return score;
    }


    public int getScoreColor(String text) {
        this.text = text;
        Matcher matcher = mildlyBadPattern.matcher(text);
        int count = 0;
        while (matcher.find()) count++;
        mildlyBadWordCount = count;
        count = 0;
        Matcher matcher2 = badPattern.matcher(text);
        while(matcher2.find()) count++;
        badWordCount = count;
        count = 0;
        Matcher matcher3 = privateInfoPattern.matcher(text);
        while(matcher3.find()) count++;
        badWordCount += count;
        return Dictionary.computeScore(mildlyBadWordCount, badWordCount);
    }

    public void setMildlyBadWordCount(int count) {
        mildlyBadWordCount = count;
    }

    public void setBadWordCount(int count) {
        badWordCount = count;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void filterMildlyBadWords() {
        text.replaceAll(text, mildlyBadRegexToFilter);
    }
    public void filterBadWords(){
        text.replaceAll(text,badRegexToFilter);
    }
}
