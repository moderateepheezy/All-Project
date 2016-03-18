package org.simpumind.com.bakerymanager;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by simpumind on 12/3/15.
 */

public class BakApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, getString(R.string.parse_app_id),getString(R.string.parse_client_key));

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }
}
