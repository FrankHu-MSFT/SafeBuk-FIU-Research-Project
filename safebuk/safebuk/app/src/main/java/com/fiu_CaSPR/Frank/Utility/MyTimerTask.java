package com.fiu_CaSPR.Frank.Utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Debug;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.fiu_CaSPR.Frank.Constants.Dictionary;
import com.fiu_CaSPR.Frank.Constants.FacebookRegexPatternPool;
import com.fiu_CaSPR.Frank.DataStructures.FriendRequest;
import com.fiu_CaSPR.Frank.FrankIntents.PostActivity;
import com.fiu_CaSPR.Frank.safebuk.MainActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Collection;
import java.util.Collections;
import java.util.TimerTask;

/**
 * Created by frankhu on 6/16/15.
 */
public class MyTimerTask extends TimerTask {

    private Context mContext = null;
    private Dialog dialog;
    private boolean alreadyLoading = false;
    private boolean foundUserAlready = false;
    private boolean alreadyBegunThread = false;

    public MyTimerTask(Context context) {
        mContext = context;
    }

    public void run() {
        // ERROR
        // how update TextView in link below
        // http://android.okhelp.cz/timer-task-timertask-run-cancel-android-example/

        if (MainActivity.webView != null)

            MainActivity.webView.post(new Runnable() {
                                          @Override
                                          public void run() {
                                              // MainActivity.webView.loadData("<html><body>Please Click one of the buttons at the top right to see Friend Requests and to post to your wall.</body></html>", "text/html; charset=UTF-8", null);
                                              if (FacebookRegexPatternPool.userName.equals("") && !foundUserAlready && !FacebookRegexPatternPool.accessToken.equals("")) {
                                                  dialog = ProgressDialog.show(mContext, "Please Wait", "Loading");
                                                  foundUserAlready = true;
                                              }
                                              if (!FacebookRegexPatternPool.userName.equals("") && foundUserAlready && !alreadyBegunThread) {
                                                  alreadyBegunThread = true;
                                                  dialog.dismiss();
                                                  new RetrieveFriendRequestInfo(mContext).execute(FacebookRegexPatternPool.cookie, "https://www.facebook.com/friends/requests/?split=1&fcref=ft");
                                              }
                                              Log.i("Timer : ", "Url : " + MainActivity.webView.getUrl());
                                              Log.i("URL : ", "Equals?: " + "https://m.facebook.com/" + FacebookRegexPatternPool.userName + "?soft=requests");

                                              // Post to
                                              if (MainActivity.webView != null)
                                                  if (MainActivity.webView.getUrl().contains("soft=composer") && !alreadyLoading) {
                                                      Log.i("Posting ", "Posting Page");
                                                      MainActivity.webView.loadUrl("https://www.facebook.com/" + FacebookRegexPatternPool.userName);
                                                      Intent intent = new Intent(mContext, PostActivity.class);
                                                      mContext.startActivity(intent);
                                                  } else if (MainActivity.webView.getUrl().contains("soft=requests") && !alreadyLoading) {
                                                      // This is the friend requests page, we want to do a javascript injection

                                                      alreadyLoading = true;
                                                      Collections.sort(FacebookRegexPatternPool.friendRequestList);
                                                      if (!FacebookRegexPatternPool.processed) {
                                                          AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                                          builder.setMessage("Not finished processing all friend requests, some may not be shown. Please come back when it is done. A dialog will pop up when all friend requests have been processed.")
                                                                  .setCancelable(false)
                                                                  .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                      public void onClick(DialogInterface dialog, int id) {
                                                                          //do things
                                                                      }
                                                                  });
                                                          AlertDialog alert = builder.create();
                                                          alert.show();
                                                      }

                                                      MainActivity.webView.post(new Runnable() {
                                                                                    @Override
                                                                                    public void run() {

                                                                                        for (FriendRequest fr : FacebookRegexPatternPool.friendRequestList) {

                                                                                            MainActivity.webView.loadUrl("javascript:void(document.getElementsByClassName(\"_5s61 _54k6\")[" + fr.getNumberInPage() + "].style.width=\"43px\")");
                                                                                            switch (fr.getDangerScore()) {
                                                                                                case "red":
                                                                                                    if (fr.getInjected()) {
                                                                                                        break;
                                                                                                    } else
                                                                                                        fr.setInjected(true);
                                                                                                    MainActivity.webView.loadUrl("javascript:void(document.getElementsByClassName(\"img profpic\")[" + fr.getNumberInPage() + "].style.border=\"red solid 3px\")");
                                                                                                    MainActivity.webView.loadUrl("javascript:var img = document.createElement(\"img\"); img.style.width= \"38px\"; img.style.marginLeft=\"5px\";img.style.height=\"38px\";img.src = \"http://www.readableblog.com/wp-content/uploads/warning2.gif\";void(document.getElementsByClassName(\"_5s61 _54k6\")[" + fr.getNumberInPage() + "].appendChild(img))");
                                                                                                    break;
                                                                                                case "yellow":
                                                                                                    if (fr.getInjected()) {
                                                                                                        break;
                                                                                                    } else
                                                                                                        fr.setInjected(true);
                                                                                                    MainActivity.webView.loadUrl("javascript:void(document.getElementsByClassName(\"img profpic\")[" + fr.getNumberInPage() + "].style.border=\"yellow solid 3px\")");
                                                                                                    MainActivity.webView.loadUrl("javascript:var img = document.createElement(\"img\"); img.style.width= \"38px\"; img.style.marginLeft=\"5px\";img.style.height=\"38px\";img.src = \"http://thumb101.shutterstock.com/display_pic_with_logo/93851/268648007/stock-photo-yellow-traffic-label-with-question-mark-pictogram-268648007.jpg\";void(document.getElementsByClassName(\"_5s61 _54k6\")[" + fr.getNumberInPage() + "].appendChild(img))");
                                                                                                    break;
                                                                                                case "green":
                                                                                                    if (fr.getInjected()) {
                                                                                                        break;
                                                                                                    } else
                                                                                                        fr.setInjected(true);
                                                                                                    MainActivity.webView.loadUrl("javascript:void(document.getElementsByClassName(\"img profpic\")[" + fr.getNumberInPage() + "].style.border=\"green solid 3px\")");
                                                                                                    MainActivity.webView.loadUrl("javascript:var img = document.createElement(\"img\"); img.style.width= \"38px\"; img.style.marginLeft=\"5px\";img.style.height=\"38px\";img.src = \"http://images.all-free-download.com/images/graphiclarge/check_mark_clip_art_9677.jpg\";void(document.getElementsByClassName(\"_5s61 _54k6\")[" + fr.getNumberInPage() + "].appendChild(img))");
                                                                                                    break;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                      );

                                                      //  new RetrieveFriendRequestInfo(mContext).execute(FacebookRegexPatternPool.cookie, "https://www.facebook.com/friends/requests/?split=1&fcref=ft");
                                                  } else if (alreadyLoading)

                                                  {
                                                      if (!MainActivity.webView.getUrl().contains("soft=requests"))
                                                          alreadyLoading = false;
                                                  }
                                          }
                                      }

            );
    }
}
