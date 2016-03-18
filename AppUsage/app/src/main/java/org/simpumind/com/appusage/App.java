package org.simpumind.com.appusage;

import android.app.Application;

import com.jaredrummler.android.processes.ProcessManager;
import com.squareup.picasso.Picasso;

/**
 * Created by simpumind on 3/1/16.
 */
public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();
        ProcessManager.setLoggingEnabled(true);
        Picasso.setSingletonInstance(new Picasso.Builder(this)
                .addRequestHandler(new AppIconRequestHandler(this))
                .build());
    }
}
