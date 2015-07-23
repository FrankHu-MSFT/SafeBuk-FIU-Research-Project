package com.fiu_CaSPR.Frank.safebuk;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.temboo.Library.Twitter.Tweets.StatusesUpdate;
import com.temboo.core.TembooSession;

import java.util.StringTokenizer;


public class NewTweet extends ActionBarActivity {

    public static ErrorHandler errorHandler;
    private TextView userView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tweet);
        try {
            // Obtain a reference to the "go" button, and attach a click listener
            Button tweetPostButton = (Button)this.findViewById(R.id.tweetPostButton);
            Button tweetCancelButton = (Button)this.findViewById(R.id.tweetCancelButton);
            tweetPostButton.setOnClickListener(new View.OnClickListener() {

                // Define the action to perform when the button is clicked
                @Override
                public void onClick(View v) {
                    postTweet(v);
                }
            });
            tweetCancelButton.setOnClickListener(new View.OnClickListener() {

                // Define the action to perform when the button is clicked
                @Override
                public void onClick(View v) {
                    cancelTweet(v);
                }
            });
        } catch(Exception e) {
            // if an exception occurred, show an error message
            //Message msg = MainActivity.errorHandler.obtainMessage();
            //msg.obj = e.getMessage();
            //MainActivity.errorHandler.sendMessage(msg);
        }
    }

    /** Called when the user clicks the Post button */
    public void postTweet(View view) {

        int sensitive_status=0;
        EditText TweetInput = (EditText) findViewById(R.id.newTweetInput);
        String tweetText=TweetInput.getText().toString();
        Log.d("",tweetText);
        StringTokenizer tokenizer = new StringTokenizer(tweetText, " \t\n\r\f,.:;?![]'");
        /*while(tokenizer.hasMoreElements())
        {
            if(tokenizer.nextToken()=="Suicide" || tokenizer.nextToken()=="suicide")
            {
                sensitive_status=1;
                break;
            }
        }*/
        if(sensitive_status==1) {
            Toast.makeText(getBaseContext(), "Your Tweet Contains a sensitive word suicide. Do you really want to post it?", Toast.LENGTH_LONG).show();
        }
        else {
            new Tweet(tweetText).execute(null, null, null);
        }
    }

    /** Called when the user clicks the Post button */
    public void cancelTweet(View view) {
        //Intent intent = new Intent(this, pendingFriends.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        //startActivity(intent);
    }
    /**
     * An AsyncTask that will be used to retrieve and display video query
     * results from Youtube.
     */
    private class Tweet extends AsyncTask<Void, Void, String> {

        String tweetText=new String();

        public Tweet(String tweetText) {
            this.tweetText=tweetText;
            Log.d("",tweetText);
        }

        @Override
        protected String doInBackground(Void... arg0) {
            try {

                // Add YouTube Choreo code here!


                // Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
                TembooSession session = new TembooSession("fiuseclab", "myFirstApp", "fea7fa83813941fc8b445a5b4ed22bb0");
                StatusesUpdate statusesUpdateChoreo = new StatusesUpdate(session);

// Get an InputSet object for the choreo
                StatusesUpdate.StatusesUpdateInputSet statusesUpdateInputs = statusesUpdateChoreo.newInputSet();

// Set credential to use for execution
                statusesUpdateInputs.setCredential("fiuseclab");

// Set inputs
                statusesUpdateInputs.set_StatusUpdate(tweetText);
                statusesUpdateInputs.set_AccessToken("2998883597-AeSHyWwzzPyHewZvkSmU01HI2Qf6HSTVXk7JY0n");
                statusesUpdateInputs.set_AccessTokenSecret("0qUQb7k5O4GNWOBLVECpzL1H7PUKYtmNUEO21BViC7cbR");
                statusesUpdateInputs.set_ConsumerSecret("oJXqvcGjRQtzAxeOFqLGRXKcpIHVuwyaPzApGzXgiTjbAI8NQZ");
                statusesUpdateInputs.set_ConsumerKey("N6TCV53c2qtf3IZkSUlapA6wF");

// Execute Choreo
                StatusesUpdate.StatusesUpdateResultSet statusesUpdateResults = statusesUpdateChoreo.execute(statusesUpdateInputs);
                return statusesUpdateResults.get_Response();

            } catch(Exception e) {
                // if an exception occurred, show an error message
            }
            return null;
        }

        protected void onPostExecute(String result) {

            try {

                // Add YouTube parsing/display code here!
                //JSONObject nytJSON = new JSONObject(result);
                //JSONObject firstResult = nytJSON.getJSONArray("ids").getJSONObject(0);

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
                //new DownloadImageTask((ImageView) findViewById(R.id.imageView))
                        //.execute(image);
                Toast.makeText(getBaseContext(), "Status Updated Successfully", Toast.LENGTH_LONG).show();
            } catch(Exception e) {
                // if an exception occurred, show an error message
                Toast.makeText(getBaseContext(), "Error in Updating Status", Toast.LENGTH_LONG).show();
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