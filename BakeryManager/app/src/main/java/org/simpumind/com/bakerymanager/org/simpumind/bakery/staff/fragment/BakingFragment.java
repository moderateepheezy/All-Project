package org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.simpumind.com.bakerymanager.R;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.adapter.BakingAdapter;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.adapter.MixingAdapter;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.model.Baking;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.model.Mixing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simpumind on 12/8/15.
 */
public class BakingFragment extends Fragment{

    private ListView listView;
    private BakingAdapter mAdapters;
    private List<ParseObject> ob;
    ProgressDialog mProgressDialog;

    private  List<Baking> bak = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View converView = inflater.inflate(R.layout.baking_list_fragment, container, false);

        new RemoteDataTask().execute();

        return converView;
    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("Baking Manager");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            bak = new ArrayList<>();
            try{
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Baking");

                query.orderByDescending("createdAt");
                ob = query.find();
                for(ParseObject br : ob){
                    Baking dr = new Baking();
                    dr.setBakerName((String) br.get("bakerName"));
                    dr.setStartTime((String) br.get("startTime"));
                    dr.setEndTime((String) br.get("stopTime"));
                    dr.setDuration((String) br.get("duration"));

                    bak.add(dr);
                }
            }catch (com.parse.ParseException e){
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            listView = (ListView) getActivity().findViewById(R.id.list);
            mAdapters = new BakingAdapter(getActivity(), bak);

            listView.setAdapter(mAdapters);

            mProgressDialog.dismiss();
        }
    }
}
