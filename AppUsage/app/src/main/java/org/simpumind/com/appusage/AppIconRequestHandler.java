package org.simpumind.com.appusage;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;

import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.IOException;

import static org.simpumind.com.appusage.util.Utils.drawableToBitmap;
import static com.squareup.picasso.Picasso.LoadedFrom.DISK;

/**
 * Created by simpumind on 3/1/16.
 */

public class AppIconRequestHandler extends RequestHandler {

    public static final String SCHEME_PNAME = "pname";

    private final PackageManager pm;
    private final int dpi;
    private Bitmap defaultAppIcon;

    public AppIconRequestHandler(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        dpi = am.getLauncherLargeIconDensity();
        pm = context.getPackageManager();
    }

    @Override public boolean canHandleRequest(Request data) {
        return data.uri != null && TextUtils.equals(data.uri.getScheme(), SCHEME_PNAME);
    }

    @Override public Result load(Request request, int networkPolicy) throws IOException {
        try {
            return new RequestHandler.Result(getFullResIcon(request.uri.toString().split(":")[1]), DISK);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    private Bitmap getFullResIcon(String packageName) throws PackageManager.NameNotFoundException {
        return getFullResIcon(pm.getApplicationInfo(packageName, 0));
    }

    private Bitmap getFullResIcon(ApplicationInfo info) {
        try {
            Resources resources = pm.getResourcesForApplication(info.packageName);
            if (resources != null) {
                int iconId = info.icon;
                if (iconId != 0) {
                    return getFullResIcon(resources, iconId);
                }
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return getFullResDefaultActivityIcon();
    }

    private Bitmap getFullResIcon(Resources resources, int iconId) {
        final Drawable drawable;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                drawable = resources.getDrawableForDensity(iconId, dpi, null);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                drawable = resources.getDrawableForDensity(iconId, dpi);
            } else {
                drawable = resources.getDrawable(iconId);
            }
        } catch (Resources.NotFoundException e) {
            return getFullResDefaultActivityIcon();
        }
        return drawableToBitmap(drawable);
    }

    private Bitmap getFullResDefaultActivityIcon() {
        if (defaultAppIcon == null) {
            Drawable drawable;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                drawable = Resources.getSystem().getDrawableForDensity(
                        android.R.mipmap.sym_def_app_icon, dpi);
            } else {
                drawable = Resources.getSystem().getDrawable(
                        android.R.drawable.sym_def_app_icon);
            }
            defaultAppIcon = drawableToBitmap(drawable);
        }
        return defaultAppIcon;
    }

}
