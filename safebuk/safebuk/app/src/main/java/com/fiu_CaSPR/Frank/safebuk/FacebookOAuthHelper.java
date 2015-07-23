package com.fiu_CaSPR.Frank.safebuk;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;

import com.fiu_CaSPR.Frank.Constants.Dictionary;
import com.fiu_CaSPR.Frank.Constants.FacebookRegexPatternPool;
import com.temboo.Library.Facebook.OAuth.FinalizeOAuth;
import com.temboo.Library.Facebook.OAuth.FinalizeOAuth.FinalizeOAuthInputSet;
import com.temboo.Library.Facebook.OAuth.FinalizeOAuth.FinalizeOAuthResultSet;
import com.temboo.Library.Facebook.OAuth.InitializeOAuth;
import com.temboo.Library.Facebook.OAuth.InitializeOAuth.InitializeOAuthInputSet;
import com.temboo.Library.Facebook.OAuth.InitializeOAuth.InitializeOAuthResultSet;
import com.temboo.Library.Facebook.Reading.User;
import com.temboo.Library.Facebook.Reading.User.UserInputSet;
import com.temboo.Library.Facebook.Reading.User.UserResultSet;
import com.temboo.core.TembooException;
import com.temboo.core.TembooSession;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Iterator;

/**
 * An AsyncTask that will be used to retrieve and display video query
 * results from Youtube.
 */
class FacebookOAuthHelper {


    private String accessToken;
    private String forwardingURL;
    private String stateToken;
    private TembooSession session;
    private WebView webView;

    private final static String FACEBOOK_APP_ID = "1429957367301208";
    private final static String FACEBOOK_APP_SECRET = "a8f104eb7601f6ad197b22f474b049f9";
    private Context mContext;
    // Replace with your Temboo credentials.
    private static final String TEMBOO_ACCOUNT_NAME = "fiuseclab";
    private static final String TEMBOO_APP_KEY_NAME = "safebuk";
    private static final String TEMBOO_APP_KEY_VALUE = "Xa31WNulJ4EzrlkT08rkyNkiiUvzWNvT";

    public FacebookOAuthHelper(WebView webView, String forwardingURL, Context ctx) {
        this.forwardingURL = forwardingURL;
        this.webView = webView;
        mContext = ctx;
        // Initialize Temboo session
        try {
            session = new TembooSession(TEMBOO_ACCOUNT_NAME, TEMBOO_APP_KEY_NAME, TEMBOO_APP_KEY_VALUE);
        } catch (TembooException te) {
            Log.d("", "Error1");
            Log.e("FacebookOAuthHelpder", te.getMessage());
        }

        // Generates a secure custom callback ID
        SecureRandom random = new SecureRandom();
        stateToken = "facebook-" + random.nextInt();
    }

    public void initOauth() {
        new FacebookInitOAuthTask().execute(null, null, null);
    }

    public void getUserInfo() {
        // Finalize OAuth, which in turn retrieves/displays the user's info via FacebookGetUserInfoTask
        new FacebookFinalizeOAuthTask().execute(null, null, null);
    }

    private class FacebookInitOAuthTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                // Instantiate the InitializeOAuth choreo, using a session object.
                InitializeOAuth initializeOauthChoreo = new InitializeOAuth(session);

                // Get an input set for InitializeOAuth.
                InitializeOAuthInputSet initializeOauthInputs = initializeOauthChoreo.newInputSet();

                // Set inputs for InitializeOAuth, use a state token as the custom callback id
                initializeOauthInputs.set_AppID(FACEBOOK_APP_ID);
                initializeOauthInputs.set_CustomCallbackID(stateToken);
                initializeOauthInputs.set_ForwardingURL(forwardingURL);
                initializeOauthInputs.set_Scope("publish_actions");
                // Execute InitializeOAuth choreo.
                InitializeOAuthResultSet initializeOauthResults = initializeOauthChoreo.execute(initializeOauthInputs);
                Log.d("", initializeOauthResults.get_AuthorizationURL());
                // This is the URL that the user will be directed to in order to login to FB and allow access.
                return initializeOauthResults.get_AuthorizationURL();
            } catch (Exception e) {
                // if an exception occurred, log it
                Log.d("", "Error2");
                Log.e(this.getClass().toString(), e.getMessage());
            }
            return null;
        }

        protected void onPostExecute(String authURL) {
            try {
                // Redirect the user to the authorization (Facebook) URL
                webView.loadUrl(authURL);
            } catch (Exception e) {
                // if an exception occurred, show an error message
                Log.e(this.getClass().toString(), e.getMessage());
            }
        }
    }

    private class FacebookFinalizeOAuthTask extends AsyncTask<Void, Void, String> {
        // fiuseclab:Sajib45*45
        @Override
        protected String doInBackground(Void... params) {
            try {
                // Instantiate the FinalizeOAuth choreo, using a session object.
                FinalizeOAuth finalizeOauthChoreo = new FinalizeOAuth(session);
                FinalizeOAuthInputSet finalizeOauthInputs = finalizeOauthChoreo.newInputSet();

                // Set input for FinalizeOAuth choreo.
                finalizeOauthInputs.set_AppID(FACEBOOK_APP_ID);
                finalizeOauthInputs.set_AppSecret(FACEBOOK_APP_SECRET);
                finalizeOauthInputs.set_LongLivedToken("1");

                final String customCallbackID = TEMBOO_ACCOUNT_NAME + "/" + stateToken;
                finalizeOauthInputs.set_CallbackID(customCallbackID);

                // Execute FinalizeOAuth choreo and retrieve the access token
                FinalizeOAuthResultSet finalizeOauthResults = finalizeOauthChoreo.execute(finalizeOauthInputs);

                accessToken = finalizeOauthResults.get_AccessToken();
                FacebookRegexPatternPool.accessToken = accessToken;
                return "Retrieved access token: " + accessToken;
            } catch (Exception e) {
                // if an exception occurred, log it
                Log.e(this.getClass().toString(), e.getMessage());
            }
            return null;
        }

        protected void onPostExecute(String accessToken) {
            try {
                new FacebookGetUserInfoTask().execute();
            } catch (Exception e) {
                // if an exception occurred, show an error message
                Log.e(this.getClass().toString(), e.getMessage());
            }
        }
    }


    private class FacebookGetUserInfoTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                // Get user info. Instantiate the choreo, using a session object.
                User userChoreo = new User(session);

                // Get an InputSet object for the Facebook.Reading.User choreo.
                UserInputSet userInputs = userChoreo.newInputSet();

                // Pass access token to the Facebook.Reading.User choreo.
                Log.d("", "Access Token: " + accessToken);
                MainActivity.finishedLoggingIn = true;
                userInputs.set_AccessToken(accessToken);

                // Execute Facebook.Reading.User choreo.
                UserResultSet userResults = userChoreo.execute(userInputs);

                return userResults.get_Response();
            } catch (Exception e) {
                // if an exception occurred, log it
                Log.e(this.getClass().toString(), e.getMessage());
            }
            return null;
        }

        protected void onPostExecute(String userInfo) {
            try {

                JSONObject nytJSON = new JSONObject(userInfo);
                final String id = nytJSON.get("id").toString();
                Log.i("ID: ", id);
                Iterator<String> iter = nytJSON.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    try {
                        if(!nytJSON.get(key).toString().contains("{"))
                            Dictionary.privateInformation.add(nytJSON.get(key).toString());
                        else{
                            String[] s = nytJSON.get(key).toString().split(",");
                            Dictionary.privateInformation.add(s[0].substring(1,s[0].length()-1));
                            for(int i =1;i <s.length-1; ++i){
                                Dictionary.privateInformation.add(s[i]);
                            }
                            Dictionary.privateInformation.add(s[s.length-1].substring(1,s[s.length-1].length()-1));
                        }

                        Log.i("Private Info: ", nytJSON.get(key) + "");
                    } catch (JSONException e) {
                        // Something went wrong!
                    }
                }


                MainActivity.webView.post(new Runnable() {
                    @Override
                    public void run() {
                        // MainActivity.webView.loadData("<html><body>Please Click one of the buttons at the top right to see Friend Requests and to post to your wall.</body></html>", "text/html; charset=UTF-8", null);
                        MainActivity.webView.loadUrl("https://www.facebook.com/" + id);
                    }
                });

            /*
                // Display user's account info
                //webView.loadData(userInfo, "text/json", "utf-8");
                // Add YouTube parsing/display code here!
                JSONObject nytJSON = new JSONObject(userInfo);
                String Name = nytJSON.get("name").toString();
                String Gender = nytJSON.get("gender").toString();
                final String Url = nytJSON.get("link").toString();
                String summary = "<html><body>User Name: "+Name+" </br>Gender: "+Gender+"</body></html>";
                Log.d("", "Calling fetchContent");
               // final String html = fetchContent(webView,Url);

                //webView.loadData(summary, "text/html", "utf-8");
             /*   Thread downloadThread = new Thread() {
                    public void run() {
                        Document doc;
                        try {

                            //String html = Jsoup.connect(Url).get().toString();

                            doc = Jsoup.parse(html);
                            Element link = doc.select("a").first();

                            String text = doc.body().text(); // "An example link"
                            //Log.d("","Printing HTML: "+link + text);
                        } catch (Exception e) {
                            Log.d("","Error Here");
                            e.printStackTrace();
                        }
                    }


                };
                downloadThread.start();
                Log.d("","Loading Webview");
                */
                //webView.loadDataWithBaseURL(Url, html, "text/html", "utf-8", Url); // todo: get mime, charset from entity
                //webView.loadUrl(Url);
                //String name = nytJSON.get("name").toString();
                //String location = nytJSON.get("location").toString();
                //String image = nytJSON.get("profile_image_url").toString();
                //Log.d("",ids);


                // JSONArray colArray = response.getJSONArray(COLUMNS);
                // for(int i=0; i<firstResult.length(); i++){
                //Log.d("","Column "+firstResult.getString(i));
                // }

// Display movie title, summary, and full-review link in UI
                // userView.append("\n\n"+name+", "+location);

                //String html = "<p>An <a href='http://example.com/'><b>example</b></a> link.</p>";


            } catch (Exception e) {
                // if an exception occurred, show an error message
                Log.e("error:", e.toString());
            }
        }


        private String fetchContent(WebView webView, String url) throws IOException {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet get = new HttpGet(url);
            HttpResponse response = httpClient.execute(get);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            HttpEntity entity = response.getEntity();
            String html = EntityUtils.toString(entity); // assume html for simplicity

            if (statusCode != 200) {
                // handle fail
            }
            return html;
        }
    }


}
