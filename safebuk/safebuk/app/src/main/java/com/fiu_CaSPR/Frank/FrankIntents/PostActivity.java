package com.fiu_CaSPR.Frank.FrankIntents;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fiu_CaSPR.Frank.DataStructures.Post;
import com.fiu_CaSPR.Frank.Utility.FacebookPostThread;
import com.fiu_CaSPR.Frank.safebuk.R;
import com.temboo.core.TembooException;
import com.temboo.core.TembooSession;

import java.security.Key;
import java.security.SecureRandom;

/**
 * Created by frankhu on 6/16/15.
 */
public class PostActivity extends Activity {

    TextView dangerTextView;
    TextView score;
    Button postButton;
    public static EditText editText;
    private TembooSession session;
    public static final String KEY_SCORE = "score";

    public static int backgroundColor = Color.GREEN;
    private final static String FACEBOOK_APP_ID = "1429957367301208";
    private final static String FACEBOOK_APP_SECRET = "a8f104eb7601f6ad197b22f474b049f9";
    // Replace with your Temboo credentials.
    private static final String TEMBOO_ACCOUNT_NAME = "fiuseclab";
    private static final String TEMBOO_APP_KEY_NAME = "safebuk";
    private static final String TEMBOO_APP_KEY_VALUE = "Xa31WNulJ4EzrlkT08rkyNkiiUvzWNvT";
    private Post post = new Post();
    SharedPreferences preferences;

    public PostActivity() {

        // Initialize Temboo session
        try {
            session = new TembooSession(TEMBOO_ACCOUNT_NAME, TEMBOO_APP_KEY_NAME, TEMBOO_APP_KEY_VALUE);
        } catch (TembooException te) {
            Log.e("error:", te.toString());
        }
        // Generates a secure custom callback ID
        SecureRandom random = new SecureRandom();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        setContentView(R.layout.post_activity);
        postButton = (Button) findViewById(R.id.postButton);
        postButton.setOnClickListener(postButtonListener);
        dangerTextView = (TextView) findViewById(R.id.dangerText);
        dangerTextView.setText("No Danger");
        score = (TextView) findViewById(R.id.points);
        int s = loadScore(PostActivity.this);
        try {
            score.setText(Integer.toString(s));
        } catch (Exception e) {
            saveScore(this, -1);
            score.setText("0");
        }


        editText = (EditText) findViewById(R.id.editText);
        editText.setBackgroundColor(backgroundColor);
        editText.addTextChangedListener(new TextWatcher() {
            String previousText = editText.getText().toString();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int color = post.getScoreColor(PostActivity.editText.getText().toString());
                PostActivity.editText.setBackgroundColor(color);
                previousText = s.toString();
                if (color == Color.RED) {
                    dangerTextView.setText("Dangerous Post");
                    dangerTextView.setTextColor(Color.RED);
                } else if (color == Color.YELLOW) {
                    dangerTextView.setText("Be Cautious About Post");
                    dangerTextView.setTextColor(Color.YELLOW);
                } else {
                    dangerTextView.setText("No Danger");
                    dangerTextView.setTextColor(Color.GREEN);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    View.OnClickListener postButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            int i = Integer.parseInt(score.getText().toString());
            score.setText(Integer.toString(i + 1));
            saveScore(PostActivity.this, (i + 1));
            new FacebookPostThread(PostActivity.this, editText.getText().toString(), session, dangerTextView.getCurrentTextColor()).execute();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static void saveScore(Context context, int score){
        SharedPreferences sharedPreferences = context.getSharedPreferences("FrankIntents", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_SCORE, score);
        editor.commit();
    }
    public static int loadScore(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("FrankIntents", Context.MODE_PRIVATE);
        int score = sharedPreferences.getInt(KEY_SCORE, 0);
        return score;
    }
}
