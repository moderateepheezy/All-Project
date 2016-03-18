package org.simpumind.com.businessdirectory.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;

import org.simpumind.com.businessdirectory.DirectoryData;
import org.simpumind.com.businessdirectory.MyData;
import org.simpumind.com.businessdirectory.R;
import org.simpumind.com.businessdirectory.TestRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class RecyclerViewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private static ArrayList<DirectoryData> people;

    private static final int ITEM_COUNT = 100;

    private List<Object> mContentItems = new ArrayList<>();

    public static RecyclerViewFragment newInstance() {
        return new RecyclerViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        people = new ArrayList<>();
        {
            for (int i = 0; i < MyData.nameArray.length ; i++) {
                people.add(new DirectoryData(
                        MyData.nameArray[i],
                        MyData.emailArray[i],
                        MyData.phoneNumber[i],
                        MyData.id_[i],
                        MyData.address[i]
                ));
            }
        }

        mAdapter = new RecyclerViewMaterialAdapter(new TestRecyclerViewAdapter(people));
        mRecyclerView.setAdapter(mAdapter);



        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);

    }
}
