package org.simpumind.com.appusage.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.Hashtable;

/**
 * Created by simpumind on 3/1/16.
 */
public class AppNames {

    static final Hashtable<String, String> APP_NAME_CACHE = new Hashtable<>();

    public static String getLabel(PackageManager pm, PackageInfo packageInfo) {
        if (APP_NAME_CACHE.containsKey(packageInfo.packageName)) {
            return APP_NAME_CACHE.get(packageInfo.packageName);
        }
        String label = packageInfo.applicationInfo.loadLabel(pm).toString();
        APP_NAME_CACHE.put(packageInfo.packageName, label);
        return label;
    }
}
