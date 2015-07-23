package com.fiu_CaSPR.Frank.safebuk;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by ivan.minev on 22.1.2015 г..
 */
public class LoginChooserFragment extends android.support.v4.app.Fragment implements View.OnClickListener
{

    private Button loginButton;

    private Button registerButton;

    private LoginFragmentActionListener listener;

  /*
    @Override
    public void onClick(View v)
    {
        if(v.getId() == loginButton.getId())
        {
            //listener.onLoginClick();
            Toast.makeText(getActivity().getBaseContext(), "Your Message", Toast.LENGTH_LONG).show();
        }
        if(v.getId() == registerButton.getId())
        {
            //listener.onRegisterClick();
            show_list();
        }
    }

    private void show_list() {
        // Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
        TembooSession session = new TembooSession("fiuseclab", "myFirstApp", "fea7fa83813941fc8b445a5b4ed22bb0");

        ListFriends listFriendsChoreo = new ListFriends(session);

// Get an InputSet object for the choreo
        ListFriendsInputSet listFriendsInputs = listFriendsChoreo.newInputSet();

// Set inputs
        listFriendsInputs.set_ScreenName("fiuseclab");
        listFriendsInputs.set_AccessToken("2998883597-AeSHyWwzzPyHewZvkSmU01HI2Qf6HSTVXk7JY0n");
        listFriendsInputs.set_AccessTokenSecret("0qUQb7k5O4GNWOBLVECpzL1H7PUKYtmNUEO21BViC7cbR");
        listFriendsInputs.set_UserID("2998883597");
        listFriendsInputs.set_ConsumerSecret("oJXqvcGjRQtzAxeOFqLGRXKcpIHVuwyaPzApGzXgiTjbAI8NQZ");
        listFriendsInputs.set_ConsumerKey("N6TCV53c2qtf3IZkSUlapA6wF");

// Execute Choreo
        ListFriendsResultSet listFriendsResults = listFriendsChoreo.execute(listFriendsInputs);
    }


    public interface LoginFragmentActionListener
    {
        public void onLoginClick();

        public void onRegisterClick();
    }

    @Override
    public void onAttach(Activity activity)
    {

        super.onAttach(activity);
        this.listener = (LoginFragmentActionListener) activity;


    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.login_choice_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        loginButton = (Button) getActivity().findViewById(R.id.loginButton);
        registerButton = (Button) getActivity().findViewById(R.id.registerButton);

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        super.onActivityCreated(savedInstanceState);
    }
    */

    private TextView nytResultsView;
    private EditText movieNameInput;
    private LinearLayout youTubeResultsList;

    public static ErrorHandler errorHandler;

    public interface LoginFragmentActionListener
    {
        public void onLoginClick();

        public void onRegisterClick();
    }

    /**
     * The onCreate method is automatically called when this activity is first created.
     * Obtain references to relevant UI objects, and attach a click-handler to the UI button.
     */
    @Override
    public void onClick(View v)
    {
        if(v.getId() == loginButton.getId())
        {
            //listener.onLoginClick();
            Toast.makeText(getActivity().getBaseContext(), "Your Message", Toast.LENGTH_LONG).show();
        }
        if(v.getId() == registerButton.getId())
        {
            //listener.onRegisterClick();
            new YouTubeFetcherTask().execute(null, null, null);
        }
    }


    /**
     * An AsyncTask that will be used to retrieve and display video query
     * results from Youtube.
     */
    private class YouTubeFetcherTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... arg0) {
            try {

                // Add YouTube Choreo code here!

            } catch(Exception e) {
                // if an exception occurred, show an error message
                Message msg = LoginChooserFragment.errorHandler.obtainMessage();
                msg.obj = e.getMessage();
                LoginChooserFragment.errorHandler.sendMessage(msg);
            }
            return null;
        }

        protected void onPostExecute(String result) {

            try {

                // Add YouTube parsing/display code here!

            } catch(Exception e) {
                // if an exception occurred, show an error message
                Message msg = LoginChooserFragment.errorHandler.obtainMessage();
                msg.obj = e.getMessage();
                LoginChooserFragment.errorHandler.sendMessage(msg);			}
        }
    }


    /**
     * A simple utility Handler to display an error message as a Toast popup
     */

    private class ErrorHandler extends Handler {
        @Override
        public void handleMessage(Message msg)
        {
            Toast.makeText(getActivity().getBaseContext(), "Your Message", Toast.LENGTH_LONG).show();
        }
    }
}
