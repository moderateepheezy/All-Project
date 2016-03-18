package org.simpumind.com.appusage.util;

import android.content.Context;
import android.os.AsyncTask;

import com.jaredrummler.android.processes.ProcessManager;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by simpumind on 3/1/16.
 */
public class AndroidAppProcessLoader extends AsyncTask<Void, Void, List<AndroidAppProcess>> {

    private final Listener listener;
    private final Context context;

    public AndroidAppProcessLoader(Context context, Listener listener) {
        this.context = context.getApplicationContext();
        this.listener = listener;
    }

    @Override protected List<AndroidAppProcess> doInBackground(Void... params) {
        List<AndroidAppProcess> processes = ProcessManager.getRunningAppProcesses();

        // sort by app name
        Collections.sort(processes, new Comparator<AndroidAppProcess>() {

            @Override
            public int compare(AndroidAppProcess lhs, AndroidAppProcess rhs) {
                return Utils.getName(context, lhs).compareToIgnoreCase(Utils.getName(context, rhs));
            }
        });

        return processes;
    }

    @Override protected void onPostExecute(List<AndroidAppProcess> androidAppProcesses) {
        listener.onComplete(androidAppProcesses);
    }

    public interface Listener {

        void onComplete(List<AndroidAppProcess> processes);
    }

}
