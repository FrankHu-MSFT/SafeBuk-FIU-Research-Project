package com.fiu_CaSPR.Frank.safebuk;

import android.os.Bundle;

//new imports
import com.temboo.Library.Twitter.Users.Show;
import com.temboo.Library.Twitter.Users.Show.*;
import com.temboo.Library.Twitter.FriendsAndFollowers.IncomingFriendships;
import com.temboo.Library.Twitter.FriendsAndFollowers.IncomingFriendships.*;
import com.temboo.core.TembooSession;
import org.json.*;

import android.util.Log;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.method.LinkMovementMethod;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class pendingFriends extends Activity {

    private TextView followerResultsView;
    private EditText movieNameInput;
    private LinearLayout followerResultsList;

    public static ErrorHandler errorHandler;

    /**
     * The onCreate method is automatically called when this activity is first created.
     * Obtain references to relevant UI objects, and attach a click-handler to the UI button.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_friends);

        // Obtain references to the View objects that we'll use to display results
        // from YouTube and the New York Times.
        followerResultsView = (TextView) findViewById(R.id.followerResultsView);
        followerResultsList = (LinearLayout) findViewById(R.id.followerResultsList);

        // Specify that links in the NYT results text view should be clickable
        followerResultsView.setMovementMethod(LinkMovementMethod.getInstance());

        // Obtain a reference to the "movie name" input field
        //movieNameInput = (EditText) findViewById(R.id.movieName);

        // Create a handler, that will be used to pass messages from task
        // threads back to the UI thread.
        errorHandler = new ErrorHandler();

        try {

            new FindRequestID().execute(null, null, null);

        } catch(Exception e) {
            // if an exception occurred, show an error message
            Message msg = pendingFriends.errorHandler.obtainMessage();
            msg.obj = e.getMessage();
            pendingFriends.errorHandler.sendMessage(msg);
        }
    }


    /**
     * An AsyncTask that will be used to retrieve and display movie review data
     * from the New York Times.
     */
    private class FindRequestID extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                // Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
                TembooSession session = new TembooSession("fiuseclab", "myFirstApp", "fea7fa83813941fc8b445a5b4ed22bb0");


                IncomingFriendships incomingFriendshipsChoreo = new IncomingFriendships(session);

// Get an InputSet object for the choreo
                IncomingFriendshipsInputSet incomingFriendshipsInputs = incomingFriendshipsChoreo.newInputSet();

// Set inputs
                incomingFriendshipsInputs.set_AccessToken("2998883597-AeSHyWwzzPyHewZvkSmU01HI2Qf6HSTVXk7JY0n");
                incomingFriendshipsInputs.set_AccessTokenSecret("0qUQb7k5O4GNWOBLVECpzL1H7PUKYtmNUEO21BViC7cbR");
                incomingFriendshipsInputs.set_ConsumerKey("N6TCV53c2qtf3IZkSUlapA6wF");
                incomingFriendshipsInputs.set_ConsumerSecret("oJXqvcGjRQtzAxeOFqLGRXKcpIHVuwyaPzApGzXgiTjbAI8NQZ");

// Execute Choreo
                IncomingFriendshipsResultSet incomingFriendshipsResults = incomingFriendshipsChoreo.execute(incomingFriendshipsInputs);
                return incomingFriendshipsResults.get_Response();
            } catch(Exception e) {
                // if an exception occurred, show an error message
                Message msg = pendingFriends.errorHandler.obtainMessage();
                msg.obj = e.getMessage();
                pendingFriends.errorHandler.sendMessage(msg);
            }
            return null;
        }

        protected void onPostExecute(String result) {
            try {

                // Add New York Times parsing/display code here!
                // parse JSON result
                JSONObject nytJSON = new JSONObject(result);
                //JSONObject firstResult = nytJSON.getJSONArray("ids").getJSONObject(0);

                String ids = nytJSON.getJSONArray("ids").get(0).toString();
                Log.d("",nytJSON.getJSONArray("ids").get(0).toString());
                Log.d("",nytJSON.getJSONArray("ids").get(1).toString());

                // JSONArray colArray = response.getJSONArray(COLUMNS);
                // for(int i=0; i<firstResult.length(); i++){
                //Log.d("","Column "+firstResult.getString(i));
                // }

// Display movie title, summary, and full-review link in UI
                followerResultsView.setText("");
                //nytResultsView.setText(ids);
                //nytResultsView.append(Html.fromHtml(review));
                //nytResultsView.append("\n");
                //nytResultsView.append(Html.fromHtml("<a href='" + link + "'>" + linkText + "</a>"));

                for(int i=0;i<nytJSON.getJSONArray("ids").length();i++) {
                    new ShowIDName(nytJSON.getJSONArray("ids").get(i).toString()).execute(null, null, null);
                }

            } catch(Exception e) {
                // if an exception occurred, show an error message
                Message msg = pendingFriends.errorHandler.obtainMessage();
                msg.obj = e.getMessage();
                pendingFriends.errorHandler.sendMessage(msg);			}
        }
    }


    /**
     * An AsyncTask that will be used to retrieve and display video query
     * results from Youtube.
     */
    private class ShowIDName extends AsyncTask<Void, Void, String> {

        String ids=new String();
        public ShowIDName(String ids) {
            this.ids=ids;

        }

        @Override
        protected String doInBackground(Void... arg0) {
            try {

                // Add YouTube Choreo code here!


                // Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
                TembooSession session = new TembooSession("fiuseclab", "myFirstApp", "fea7fa83813941fc8b445a5b4ed22bb0");
                Show showChoreo = new Show(session);

// Get an InputSet object for the choreo
                ShowInputSet showInputs = showChoreo.newInputSet();

// Set inputs
                showInputs.set_UserId(ids);
                showInputs.set_AccessToken("2998883597-AeSHyWwzzPyHewZvkSmU01HI2Qf6HSTVXk7JY0n");
                showInputs.set_AccessTokenSecret("0qUQb7k5O4GNWOBLVECpzL1H7PUKYtmNUEO21BViC7cbR");
                showInputs.set_ConsumerSecret("oJXqvcGjRQtzAxeOFqLGRXKcpIHVuwyaPzApGzXgiTjbAI8NQZ");
                showInputs.set_ConsumerKey("N6TCV53c2qtf3IZkSUlapA6wF");

// Execute Choreo
                ShowResultSet showResults = showChoreo.execute(showInputs);
                return showResults.get_Response();

            } catch(Exception e) {
                // if an exception occurred, show an error message
                Message msg = pendingFriends.errorHandler.obtainMessage();
                msg.obj = e.getMessage();
                pendingFriends.errorHandler.sendMessage(msg);
            }
            return null;
        }

        protected void onPostExecute(String result) {

            try {

                // Add YouTube parsing/display code here!
                JSONObject nytJSON = new JSONObject(result);
                //JSONObject firstResult = nytJSON.getJSONArray("ids").getJSONObject(0);

                String name = nytJSON.get("name").toString();
                String location = nytJSON.get("location").toString();
                String image = nytJSON.get("profile_image_url").toString();
                //Log.d("",ids);


                // JSONArray colArray = response.getJSONArray(COLUMNS);
                // for(int i=0; i<firstResult.length(); i++){
                //Log.d("","Column "+firstResult.getString(i));
                // }

// Display movie title, summary, and full-review link in UI
                //followerResultsView.append("\n\n" + name + ", " + location);


                //nytResultsView.append(Html.fromHtml(review));
                //nytResultsView.append("\n");


                LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.container);

                LinearLayout.LayoutParams paramsLeft = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0);
                paramsLeft.weight=1f;

                LinearLayout.LayoutParams paramsRight = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0);
                //paramsRight.weight=2f;

                    ImageView pendingFriendImage = new ImageView(pendingFriends.this);
                    new DownloadImageTask(pendingFriendImage)
                            .execute(image);
                    linearLayout1.addView(pendingFriendImage,paramsLeft);
                    TextView pendingFriendName = new TextView(pendingFriends.this);
                    pendingFriendName.append("\n\n" + name + ", " + location);
                linearLayout1.addView(pendingFriendName);

                /*
                RelativeLayout Layout1 = (RelativeLayout) findViewById(R.id.container);

                RelativeLayout.LayoutParams paramsLeft = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramsLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);

                RelativeLayout.LayoutParams paramsRight = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

                    ImageView pendingFriendImage = new ImageView(pendingFriends.this);
                    new DownloadImageTask(pendingFriendImage)
                            .execute(image);
                    Layout1.addView(pendingFriendImage,paramsLeft);
                    TextView pendingFriendName = new TextView(pendingFriends.this);
                    pendingFriendName.append("\n\n" + name + ", " + location);
                Layout1.addView(pendingFriendName,paramsRight);
                 */

            } catch(Exception e) {
                // if an exception occurred, show an error message
                Message msg = pendingFriends.errorHandler.obtainMessage();
                msg.obj = e.getMessage();
                pendingFriends.errorHandler.sendMessage(msg);			}
        }
    }


    /**
     * A simple utility Handler to display an error message as a Toast popup
     * @param
     */

    private class ErrorHandler extends Handler {
        @Override
        public void handleMessage(Message msg)
        {
            Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
        }
    }
}

