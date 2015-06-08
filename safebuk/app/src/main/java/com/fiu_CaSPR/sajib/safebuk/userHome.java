package com.fiu_CaSPR.sajib.safebuk;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.temboo.Library.Twitter.Users.Show;
import com.temboo.core.TembooSession;

import org.json.JSONObject;


public class userHome extends ActionBarActivity {

    public static ErrorHandler errorHandler;
    private TextView userView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        userView = (TextView) findViewById(R.id.userViewLabel);
        // Obtain references to the View objects that we'll use to display results
        // from YouTube and the New York Times.
        //nytResultsView = (TextView) findViewById(R.id.nytResultsView);

        // Specify that links in the NYT results text view should be clickable
        //nytResultsView.setMovementMethod(LinkMovementMethod.getInstance());

        // Obtain a reference to the "movie name" input field
        //movieNameInput = (EditText) findViewById(R.id.movieName);

        // Create a handler, that will be used to pass messages from task
        // threads back to the UI thread.

        try {

            new ShowIDName("2998883597").execute(null, null, null);
            // Obtain a reference to the "go" button, and attach a click listener
            Button pendingButton = (Button)this.findViewById(R.id.followersButton);
            Button tweetButton = (Button)this.findViewById(R.id.tweetButton);
            pendingButton.setOnClickListener(new View.OnClickListener() {

                // Define the action to perform when the button is clicked
                @Override
                public void onClick(View v) {
                    showPending(v);
                }
            });
            tweetButton.setOnClickListener(new View.OnClickListener() {

                // Define the action to perform when the button is clicked
                @Override
                public void onClick(View v) {
                    newTweet(v);
                }
            });
        } catch(Exception e) {
            // if an exception occurred, show an error message
            //Message msg = MainActivity.errorHandler.obtainMessage();
            //msg.obj = e.getMessage();
            //MainActivity.errorHandler.sendMessage(msg);
        }
    }

    /** Called when the user clicks the Send button */
    public void showPending(View view) {
        Intent intent = new Intent(this, pendingFriends.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    /** Called when the user clicks the Send button */
    public void newTweet(View view) {
        Intent intent = new Intent(this, fbHome.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
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
                Show.ShowInputSet showInputs = showChoreo.newInputSet();

// Set inputs
                showInputs.set_UserId(ids);
                showInputs.set_AccessToken("2998883597-AeSHyWwzzPyHewZvkSmU01HI2Qf6HSTVXk7JY0n");
                showInputs.set_AccessTokenSecret("0qUQb7k5O4GNWOBLVECpzL1H7PUKYtmNUEO21BViC7cbR");
                showInputs.set_ConsumerSecret("oJXqvcGjRQtzAxeOFqLGRXKcpIHVuwyaPzApGzXgiTjbAI8NQZ");
                showInputs.set_ConsumerKey("N6TCV53c2qtf3IZkSUlapA6wF");

// Execute Choreo
                Show.ShowResultSet showResults = showChoreo.execute(showInputs);
                return showResults.get_Response();

            } catch(Exception e) {
                // if an exception occurred, show an error message
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
                userView.append("\n\n"+name+", "+location);
                new DownloadImageTask((ImageView) findViewById(R.id.imageView))
                        .execute(image);

            } catch(Exception e) {
                // if an exception occurred, show an error message
                			}
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