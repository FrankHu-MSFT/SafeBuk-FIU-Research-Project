package com.fiu_CaSPR.Frank.Utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.util.Log;

import com.fiu_CaSPR.Frank.Constants.FacebookRegexPatternPool;
import com.fiu_CaSPR.Frank.DataStructures.FriendRequest;
import com.fiu_CaSPR.Frank.safebuk.MainActivity;

import org.apache.http.cookie.Cookie;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.regex.Matcher;

/**
 * Created by fhu004 on 6/12/2015.
 */
public class RetrieveFriendRequestInfo extends AsyncTask<String, Void, String> {

    private Exception exception;
    private String htmlSource = "";
    private String url = "";
    private String cookie;
    private Dialog dialog;
    private Context mContext;

    public RetrieveFriendRequestInfo(Context context) {
        mContext = context;
    }

    //other methods like onPreExecute etc.
    @Override
    protected void onPreExecute() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Begun Processing Friend Requests")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        //Create progress dialog here and show it
        //dialog = ProgressDialog.show(mContext, "Please Wait", "Loading");
        super.onPreExecute();
    }

    protected String doInBackground(String... urls) {
        try {
            int numberItem = 0;
            if (FacebookRegexPatternPool.processed) {
                MainActivity.webView.post(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.webView.loadData(FacebookRegexPatternPool.div, "text/html; charset=UTF-8", null);
                    }
                });
                return "Worked";
            }
            this.url = urls[1];
            this.cookie = urls[0];
            Connection.Response response = Jsoup.connect(urls[1]).cookie(urls[1], urls[0]).ignoreContentType(true).userAgent("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0")
                    .timeout(3000)
                    .execute();
            Log.i("Info Element: ", response.body());
            Log.i("Info Element: ", response.url().toString());
            htmlSource = response.body();
            final Matcher styleMatcher = FacebookRegexPatternPool.style.matcher(htmlSource);
            if (styleMatcher.find())
                Log.d("style :", styleMatcher.group());
            final Matcher m = FacebookRegexPatternPool.r.matcher(htmlSource);
            FacebookRegexPatternPool.styleDiv = styleMatcher.group();
            FacebookRegexPatternPool.div = styleMatcher.group();
            if (m.find()) {
                String friendRequestDivSource = m.group();
                final Matcher friendRequestMatcher = FacebookRegexPatternPool.friendRequestPattern.matcher(friendRequestDivSource);
                String friendDivSource = "";
                while (friendRequestMatcher.find()) {
                    FacebookRegexPatternPool.currentFriendRequest = new FriendRequest();
                    // TODO: create algorithm to determine safety of Facebook Friend
                    // friendRequestSourceList.add("<div style=\"background-color: #FF0000\">" + friendRequestMatcher.group() + "</div>");
                    // TODO: code below must be revised later because it will not work with algorithm
                    // friendDivSource += "<div style=\"background-color: #FF0000\">" + friendRequestMatcher.group() + "</div>";
                    String levelOfSafety;
                    FacebookRegexPatternPool.findNames(friendRequestMatcher.group());
                    //Log.i("ID of :", FacebookRegexPatternPool.currentFriendRequest.getName() + " " + FacebookRegexPatternPool.currentFriendRequest.getId());
                    FacebookRegexPatternPool.processCurrentFriendRequestOverview(cookie);
                    FacebookRegexPatternPool.processCurrentFriendRequestPhotos(cookie);
                    FacebookRegexPatternPool.processCurrentFriendRequestLifeEvents(cookie);
                    levelOfSafety = FacebookRegexPatternPool.currentFriendRequest.getDangerScore();
                    /*switch (levelOfSafety) {
                        case ("red"):
                            friendDivSource = "<div style=\"background-color: #FF0000\">" + friendRequestMatcher.group() + "<div>Trust Level:" + FacebookRegexPatternPool.currentFriendRequest.getPerecentScore() + "%</div></div>";
                            break;
                        case ("yellow"):
                            friendDivSource = "<div style=\"background-color: #FFFF00\">" + friendRequestMatcher.group() + "<div>Trust Level:" + FacebookRegexPatternPool.currentFriendRequest.getPerecentScore() + "%</div></div>";
                            break;
                        case ("green"):
                            friendDivSource = "<div style=\"background-color: #33CC33\">" + friendRequestMatcher.group() + "<div>Trust Level:" + FacebookRegexPatternPool.currentFriendRequest.getPerecentScore() + "%</div></div>";
                            break;
                    }
                    */
                    FacebookRegexPatternPool.currentFriendRequest.setNumberInPage(numberItem++);
                    FacebookRegexPatternPool.currentFriendRequest.setDiv(friendDivSource);
                    FacebookRegexPatternPool.friendRequestList.add(FacebookRegexPatternPool.currentFriendRequest);
                    FacebookRegexPatternPool.div += friendDivSource;
                }
                //final String divSource = friendDivSource;


         /*       MainActivity.webView.post(new Runnable() {
                    @Override
                    public void run() {
                        FacebookRegexPatternPool.div = styleMatcher.group() + divSource + "</body></html>";
                        MainActivity.webView.loadData(styleMatcher.group() + divSource + "</body></html>", "text/html; charset=UTF-8", null);
                        FacebookRegexPatternPool.processed = true;
                    }
                });
                */

            } else {
                Log.d("d", "NO MATCH");
            }

        } catch (Exception e) {
            Log.i("Error: ", e.toString());
        }
        return "didn't work";
    }

    protected void onPostExecute(String result) {
        FacebookRegexPatternPool.processed = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Finished processing all Friend Requests")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        super.onPostExecute(result);
    }
}

