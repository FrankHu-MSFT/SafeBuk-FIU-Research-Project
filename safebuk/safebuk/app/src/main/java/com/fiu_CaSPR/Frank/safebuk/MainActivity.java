package com.fiu_CaSPR.Frank.safebuk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.fiu_CaSPR.Frank.Constants.FacebookRegexPatternPool;
import com.fiu_CaSPR.Frank.DataStructures.FriendRequest;
import com.fiu_CaSPR.Frank.Utility.MyTimerTask;
import com.fiu_CaSPR.Frank.Utility.RetrieveFriendRequestInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Timer;
import java.util.regex.Matcher;


public class MainActivity extends Activity {

    public static boolean finishedLoggingIn = false;


    private FacebookOAuthHelper oAuthHelper;
    public static String htmlSource = "";
    public static String urlLoad = "";
    public static WebView webView;
    private ProgressDialog dialog = null;
    // We won't navigate to this URL, we simply use it as an indicator of
    // when in the OAuth flow we should go through the finalize routines
    private final static String FORWARDING_URL = "http://temboo.placeholder.url";
    public static String friendRequestDataString = "";
    public static boolean alreadyLoadedFriendsURL = false;
    final MyTimerTask myTask = new MyTimerTask(this);
    final Timer myTimer = new Timer();
    public static WebSettings webSettings =null;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (isNetworkAvailable()) {
            Toast.makeText(this, "Loading Please Wait", Toast.LENGTH_LONG);
        } else {
            Toast.makeText(this, "No Network Connection", Toast.LENGTH_LONG);
            this.finish();
            System.exit(0);
        }


        // Initialize the WebView
        webView = (WebView) findViewById(R.id.webView);
        webView.addJavascriptInterface(new LoadListener(), "HTMLOUT");
        webSettings =  webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(false);

        // Set up WebView for OAuth2 login - intercept redirect when the redirect
        // URL matches our FORWARDING_URL, in which case we will complete the OAuth
        // flow using Temboo
        webView.setWebViewClient(new WebViewClient() {
            boolean timer = false;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith(FORWARDING_URL)) {
                    // spawn worker thread to do api calls to get list of contacts to display
                    oAuthHelper.getUserInfo();
                    // true = do not navigate to URL in web view
                    return true;
                }

                // Default behavior - redirect to specified URL
                return super.shouldOverrideUrlLoading(view, url);
            }

            public void onPageFinished(WebView view, String url) {
                if (webView != null) {
                    String s = webView.getUrl();
                    if (s.contains("m.facebook.com")) {
                        Log.i("PageLoadURL:", webView.getUrl());
                        String[] urlCut = webView.getUrl().split("/");
                        StringBuilder builder = new StringBuilder();
                        for (int i = 0; i < urlCut[urlCut.length - 1].length(); ++i) {
                            if (urlCut[urlCut.length - 1].charAt(i) != '?') {
                                builder.append(urlCut[urlCut.length - 1].charAt(i));
                            } else break;
                        }
                        FacebookRegexPatternPool.userName = builder.toString();
                    }
                    view.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                    view.loadUrl("javascript:window.onhashchange = function() { HTMLOUT.processHTML(); };");
                    CookieSyncManager.getInstance().sync();
                    CookieManager cm = CookieManager.getInstance();
                    String cookie = cm.getCookie("https://www.facebook.com/friends/requests/?split=1&fcref=ft");
                    FacebookRegexPatternPool.cookie = cookie;
                    if (!timer)
                        myTimer.schedule(myTask, 3000, 1500);
                    timer = true;
                    // if(!FacebookRegexPatternPool.userName.equals(""))
                    //     dialog.dismiss();
                }
            }
        });

        // Initialize OAuth helper
        oAuthHelper = new FacebookOAuthHelper(webView, FORWARDING_URL, this);

        // Get the Facebook auth URL
        oAuthHelper.initOauth();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myTimer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    for(FriendRequest fr: FacebookRegexPatternPool.friendRequestList){
                        fr.setInjected(false);
                    }
                    webSettings.setUserAgentString("");
                    Log.d("URLINFO:", webView.getUrl() + "");
                    Log.d("URLINFO:","\"https://m.facebook.com/\" + FacebookRegexPatternPool.userName +\"?_rdr\"");
                    Log.d("URLINFO:", webView.getUrl().equalsIgnoreCase("https://m.facebook.com/" + FacebookRegexPatternPool.userName +"?_rdr") + "");
                    if(webView.getUrl().equalsIgnoreCase("https://m.facebook.com/" + FacebookRegexPatternPool.userName +"?_rdr")) {

                        Log.d("URL:", "Ran within finish");
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        this.getApplicationContext().startActivity(startMain);
                    }else if (webView.canGoBack()) {
                        if(webView.getUrl().equalsIgnoreCase("https://m.facebook.com/" + FacebookRegexPatternPool.userName +"?_rdr")) {
                            Log.d("URL:", "Ran within finish");
                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            this.getApplicationContext().startActivity(startMain);
                        }else {
                            MainActivity.webView.post(new Runnable() {
                                @Override
                                public void run() {
                                    // MainActivity.webView.loadData("<html><body>Please Click one of the buttons at the top right to see Friend Requests and to post to your wall.</body></html>", "text/html; charset=UTF-8", null);
                                    MainActivity.webView.loadUrl("https://www.facebook.com/" + FacebookRegexPatternPool.userName);
                                }
                            });
                        }
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.friends: {
                if (finishedLoggingIn) {
                    CookieSyncManager.getInstance().sync();
                    CookieManager cm = CookieManager.getInstance();
                    String cookie = cm.getCookie("https://www.facebook.com/friends/requests/?split=1&fcref=ft");
                    new RetrieveFriendRequestInfo(this).execute(cookie, "https://www.facebook.com/friends/requests/?split=1&fcref=ft");
                }
                return true;
            }
            case R.id.post: {
                if (finishedLoggingIn) {
                    dialog = ProgressDialog.show(this, "Please Wait", "Loading");
                    // Log.i("Url: ", "https://m.facebook.com/" + FacebookRegexPatternPool.userName + "?ref=bookmark&soft=composer");
                    //webView.loadUrl("https://m.facebook.com/" + FacebookRegexPatternPool.userName + "?ref=bookmark&soft=composer");
//                    CookieSyncManager.getInstance().sync();
//                    CookieManager cm = CookieManager.getIns/about";
//                    String cookie = cm.getCookie(url);
//                    new RetrieveFriendRequestInfo(this).execute(cookie, url);
//                    webView.setVisibility(View.INVISIBLE);
                }
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void loadUrl() {
        webView.loadData(urlLoad, "text/html; charset=UTF-8", null);
    }

    class LoadListener {
        @JavascriptInterface
        public void processHTML(String html) {
            Document doc = Jsoup.parse(FacebookRegexPatternPool.mobileHTML);
            Elements elements = doc.body().select("div[class=_5s61 _54k6]");
            FacebookRegexPatternPool.elements = elements;
        }
    }
}


