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
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.adapter.MixingAdapter;
import org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.model.Mixing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simpumind on 12/8/15.
 */
public class MixingFragment extends Fragment{

    private ListView listView;
    private MixingAdapter mAdapters;
    private List<ParseObject> ob;
    ProgressDialog mProgressDialog;

    private  List<Mixing> people = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.baking_list_fragment, container, false);



        listView = (ListView) convertView.findViewById(R.id.list);

        new RemoteDataTask().execute();

        return convertView;
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
            people = new ArrayList<>();
            try{
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Mixing");

                query.addDescendingOrder("createdAt");
                ob = query.find();
                for(ParseObject mixs : ob){
                    Mixing dr = new Mixing();
                    dr.setMixerName((String) mixs.get("mixerName"));
                    dr.setStartTime((String) mixs.get("startTime"));
                    dr.setEndTime((String) mixs.get("stopTime"));
                    dr.setTimeCreated((String) mixs.get("createdAt"));

                    people.add(dr);
                }
            }catch (com.parse.ParseException e){
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mAdapters = new MixingAdapter(getActivity(), people);

            listView.setAdapter(mAdapters);

            mProgressDialog.dismiss();
        }
    }
}
