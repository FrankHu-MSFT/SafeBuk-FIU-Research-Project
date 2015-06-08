package com.fiu_CaSPR.sajib.safebuk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends Activity {

    private FacebookOAuthHelper oAuthHelper;
    public static String htmlSource = "";
    public static String urlLoad = "";
    private static  WebView webView;
    private ProgressDialog dialog = null;
    // We won't navigate to this URL, we simply use it as an indicator of
    // when in the OAuth flow we should go through the finalize routines
    private final static String FORWARDING_URL = "http://temboo.placeholder.url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = ProgressDialog.show(this, "Please Wait", "Loading");
        // Initialize the WebView
        webView = (WebView)findViewById(R.id.webView);
        webView.addJavascriptInterface(new LoadListener(), "HTMLOUT");
        WebSettings webSettings = webView.getSettings();
        String newUA= "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
        webView.getSettings().setUserAgentString(newUA);
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(false);
        // Set up WebView for OAuth2 login - intercept redirect when the redirect
        // URL matches our FORWARDING_URL, in which case we will complete the OAuth
        // flow using Temboo
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.startsWith(FORWARDING_URL)) {
                    // spawn worker thread to do api calls to get list of contacts to display
                    oAuthHelper.getUserInfo();
                    // true = do not navigate to URL in web view
                    return true;
                }

                // Default behavior - redirect to specified URL
                return super.shouldOverrideUrlLoading(view, url);
            }

            public void onPageFinished(WebView view, String url){

                dialog.dismiss();
                view.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
           }
        });

        // Initialize OAuth helper
        oAuthHelper = new FacebookOAuthHelper(webView, FORWARDING_URL);

        // Get the Facebook auth URL
        oAuthHelper.initOauth();
        webView.loadUrl("https://www.facebook.com/friends/requests/?split=1&fcref=ft");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void loadUrl(){
        webView.loadData(urlLoad, "text/html; charset=UTF-8", null);
    }

    class LoadListener{

        @JavascriptInterface
        public void processHTML(String html){
            Log.e("html", html);
            htmlSource = html;
            Log.e("Total?",htmlSource.contains("</body>")+"");



            String stylePattern = "<html[^*]*<body[^>]*[>]";
            Pattern style = Pattern.compile(stylePattern);
            final Matcher styleMatcher = style.matcher(htmlSource);


            if (styleMatcher.find())
                Log.d("style :", styleMatcher.group());

            String pattern = "<h2 class=\"_34e\">Respond to Your *[>]*[^>]*[>]*[^>]*[>][^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]*[^>]*[>]";
            Pattern r = Pattern.compile(pattern);
            final Matcher m = r.matcher(htmlSource);


            if (m.find()) {

                Log.d("data" , m.toString());
                Log.d("group : ", m.group());
                Log.d("data", urlLoad);

                String friendRequestDivSource =  m.group();


                /*String friendRequestPatternString = "<div class=\"clearfix ruUserBox _3-z\"*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*[>]*[^>]*[>]*";
                Pattern friendRequestPattern= Pattern.compile(friendRequestPatternString);
                final Matcher friendRequestMatcher = friendRequestPattern.matcher(friendRequestDivSource);
                */


                String friendDivSource = "";
                ArrayList<String> friendRequestSourceList = new ArrayList<>();
                int i =0;
               /* while(friendRequestMatcher.find()) {
                    Log.d("friend While:" , ++i +"");
                    Log.d("groupFRM: ", friendRequestMatcher.group() + "");
                    // friendRequestSourceList.add("<div style=\"background-color: #FF0000\">" + friendRequestMatcher.group() + "</div>");
                    friendDivSource += "<div style=\"background-color: #FF0000\">" + friendRequestMatcher.group()+ "</div>";
                }*/

                Log.d("afterWhil;",friendDivSource);
              /* for(int i = 0; i< friendRequestMatcher.groupCount(); ++i){



                    // TODO: create algorithm to determine safety of Facebook Friend
                    // String levelOfSafety = "red";

                    friendDivSource += "<div style=\"background-color: #FF0000\">" + friendRequestMatcher.group + "</div>";


                    // code below must be revised later because it will not work with algorithm
                  /*  switch(levelOfSafety) {
                        case ("red"):
                            friendDivSource += "<div style=\"background-color: #FF0000\">" + friendRequestMatcher.group(i) + "</div>";
                        case ("yellow"):
                            friendDivSource += "<div style=\"background-color: #FFFF00\">" + friendRequestMatcher.group(i) + "</div>";
                        case ("green"):
                            friendDivSource += "<div style=\"background-color: #33CC33\">" + friendRequestMatcher.group(i) + "</div>";

                    }



              }*/


                final String divSource = friendDivSource;
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadData(styleMatcher.group() + divSource +"</body></html>","text/html; charset=UTF-8", null);
                    }
                });

            } else {
               Log.d("d","NO MATCH");
            }

        }


    }
}


