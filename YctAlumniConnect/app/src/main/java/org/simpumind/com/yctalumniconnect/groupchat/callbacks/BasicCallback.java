package org.simpumind.com.yctalumniconnect.groupchat.callbacks;

import android.util.Log;

import com.pubnub.api.PubnubError;
import com.pubnub.api.Callback;

/**
 * Created by simpumind on 1/11/16.
 */
public class BasicCallback extends Callback{

    public BasicCallback(){

    }

    @Override
    public void successCallback(String channel, Object response) {
        Log.d("PUBNUB", "Success: " + response.toString());
    }

    @Override
    public void connectCallback(String channel, Object message) {
        Log.d("PUBNUB", "Connect: " + message.toString());
    }

    @Override
    public void errorCallback(String channel, PubnubError error) {
        Log.d("PUBNUB", "Error: " + error.toString());
    }
}
