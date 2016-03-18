package org.simpumind.com.yctalumniconnect;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simpumind on 12/23/15.
 */
public class ReminisceFragment extends Fragment{

    public static Fragment newInstance(Context context) {
        ReminisceFragment f = new ReminisceFragment();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.reminisce_fragment, null);

        RecyclerView recList = (RecyclerView) root.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        ReminisceAdapter ca = new ReminisceAdapter(createList(30));
        recList.setAdapter(ca);

        return root;
    }

    private List<ReminisceInfo> createList(int size) {

        List<ReminisceInfo> result = new ArrayList<ReminisceInfo>();
        result.add(new ReminisceInfo("Gboza", "Gboo-zaa", "Its a matra to hail,sometimes it comes with a number"));
        result.add(new ReminisceInfo("Twale", "Twa-lee", "Give respect to a particular person, because of their status"));
        result.add(new ReminisceInfo("Guru", "Guu-ruu", "explaining how good a perticular student is in his education"));
        result.add(new ReminisceInfo("", "Gboo-zaa", "Its a matra to hail, sometimes it comes with a number"));
        result.add(new ReminisceInfo("Gboza", "Gboo-zaa", "Its a matra to hail,sometimes it comes with a number"));
        result.add(new ReminisceInfo("Gboza", "Gboo-zaa", "Its a matra to hail,sometimes it comes with a number"));
        result.add(new ReminisceInfo("Gboza", "Gboo-zaa", "Its a matra to hail,sometimes it comes with a number"));
        result.add(new ReminisceInfo("Gboza", "Gboo-zaa", "Its a matra to hail,sometimes it comes with a number"));
        result.add(new ReminisceInfo("Gboza", "Gboo-zaa", "Its a matra to hail,sometimes it comes with a number"));
        result.add(new ReminisceInfo("Gboza", "Gboo-zaa", "Its a matra to hail,sometimes it comes with a number"));
        return result;
    }
}
