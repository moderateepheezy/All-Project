package org.simpumind.com.schooloftechyctcoursebook;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by simpumind on 1/18/16.
 */
public class Apps extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}
