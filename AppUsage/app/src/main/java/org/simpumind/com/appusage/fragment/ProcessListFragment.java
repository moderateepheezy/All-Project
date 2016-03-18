package org.simpumind.com.appusage.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.jaredrummler.android.processes.models.AndroidAppProcess;

import org.simpumind.com.appusage.ProcessInfoDialog;
import org.simpumind.com.appusage.ProcessListAdapter;
import org.simpumind.com.appusage.util.AndroidAppProcessLoader;

import java.util.List;

/**
 * Created by simpumind on 3/1/16.
 */
public class ProcessListFragment extends ListFragment implements AndroidAppProcessLoader.Listener {

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setFastScrollEnabled(true);
        new AndroidAppProcessLoader(getActivity(), this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override public void onComplete(List<AndroidAppProcess> processes) {
        setListAdapter(new ProcessListAdapter(getActivity(), processes));
    }

    @Override public void onListItemClick(ListView l, View v, int position, long id) {
        AndroidAppProcess process = (AndroidAppProcess) getListAdapter().getItem(position);
        ProcessInfoDialog dialog = new ProcessInfoDialog();
        Bundle args = new Bundle();
        args.putParcelable("process", process);
        dialog.setArguments(args);
        dialog.show(getActivity().getSupportFragmentManager(), "ProcessInfoDialog");
    }

}
