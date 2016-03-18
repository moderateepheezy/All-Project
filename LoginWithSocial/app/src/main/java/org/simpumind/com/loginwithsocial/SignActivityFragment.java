package org.simpumind.com.loginwithsocial;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * A placeholder fragment containing a simple view.
 */
public class SignActivityFragment extends Fragment {

    private TwitterLoginButton loginButton;
    TwitterSession session;
    static String screenname,twitterImage,location,timeZone,description;
    private static final String TWITTER_KEY = "wFHuJnU6kqMOA240XPoHTqgEy";
    private static final String TWITTER_SECRET = "THx2Q8yWOJU7736E0U7Iif9ceK3uhTRvaPL2tNJai1eL6ZTnl5";

    private CallbackManager callbackManager;
    //private TextView textView;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private static final PDetails fbUser = new PDetails();

    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
           // displayWelcomeMessage(profile);

            GraphRequest data_request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject json_object,
                                GraphResponse response) {

                            Intent intent = new Intent(getActivity(), LoginSuccess.class);
                            intent.putExtra("jsondata",json_object.toString());
                            startActivity(intent);
                        }
                    });
            Bundle permission_param = new Bundle();
            permission_param.putString("fields", "id,name,email,gender,picture.width(120).height(120)");
            data_request.setParameters(permission_param);
            data_request.executeAsync();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

    public SignActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

         accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };

         profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                displayWelcomeMessage(currentProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginButton = (TwitterLoginButton) view.findViewById(R.id.login_button_twitter);
        LoginButton loginFacebookButton = (LoginButton) view.findViewById(R.id.login_button);
      //  textView = (TextView) view.findViewById(R.id.name);
        loginFacebookButton.setReadPermissions(Arrays.asList("basic_info","email"));
        loginFacebookButton.setFragment(this);
        loginFacebookButton.registerCallback(callbackManager, mCallback);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.e("Bullshit!!!", "this worked i guess");
                session = Twitter.getSessionManager().getActiveSession();
                loginWithTwitter();
                Twitter.getApiClient(session).getAccountService()
                        .verifyCredentials(true, false, new Callback<User>() {
                            @Override
                            public void success(Result<User> result) {
                                Log.e("Bullshit!!!", "this worked i guess");
                                User user = result.data;
                                twitterImage = user.profileImageUrl;
                                screenname = user.screenName;
                                location = user.location;
                                timeZone = user.timeZone;
                                description = user.email;
                                loginWithTwitter();
                            }

                            @Override
                            public void failure(TwitterException e) {
                                Toast.makeText(getActivity(), "Failed!!! " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();

                                Log.e("Bullshit!!!", "this worked i guess" + e.getMessage());
                            }
                        });
                loginWithTwitter();
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
    }

    private void loginWithTwitter() {
        Intent intent = new Intent(getActivity().getApplicationContext(),
                LoginSuccess.class);
        intent.putExtra("name", screenname);
        intent.putExtra("email", description);
        intent.putExtra("pic", twitterImage);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayWelcomeMessage(profile);
    }

    public void displayWelcomeMessage(Profile profile){
        if(profile != null){
            //textView.setText(profile.getName());
        }
    }

}
