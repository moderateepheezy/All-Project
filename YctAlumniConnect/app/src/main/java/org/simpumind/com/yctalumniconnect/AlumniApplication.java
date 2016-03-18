package org.simpumind.com.yctalumniconnect;

import com.parse.Parse;

/**
 * Created by simpumind on 12/21/15.
 */
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

public class AlumniApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //ParseObject.registerSubclass(DirectoryData.class);
       // Parse.enableLocalDatastore(this);
        // Required - Initialize the Parse SDK
        Parse.initialize(this, getString(R.string.parse_app_id),getString(R.string.parse_client_key));

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
