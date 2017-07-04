package com.patrickwallin.projects.collegeinformation;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patrickwallin.projects.collegeinformation.adapter.RegionsAdapter;
import com.patrickwallin.projects.collegeinformation.adapter.StatesAdapter;
import com.patrickwallin.projects.collegeinformation.asynctask.FetchRegionsTask;
import com.patrickwallin.projects.collegeinformation.asynctask.FetchStatesTask;
import com.patrickwallin.projects.collegeinformation.data.RegionData;
import com.patrickwallin.projects.collegeinformation.data.StateData;
import com.patrickwallin.projects.collegeinformation.utilities.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by piwal on 6/25/2017.
 */

public class SearchLocationsRegionsActivityFragment extends Fragment {
    @BindView(R.id.regions_recycler_view)
    RecyclerView regions_recycler_view;

    private List<RegionData> mRegionsData;
    private Context mContext;
    private RegionsAdapter mRegionsAdapter;

    public SearchLocationsRegionsActivityFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!getResources().getBoolean(R.bool.is_this_tablet)){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_search_locations_regions_fragment,container,false);

        ButterKnife.bind(this,rootView);

        LinearLayoutManager llm = new LinearLayoutManager(mContext);

        regions_recycler_view.setLayoutManager(llm);
        regions_recycler_view.setHasFixedSize(true);

        Drawable dividerDrawable = ContextCompat.getDrawable(mContext, R.drawable.divider_line);

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
        regions_recycler_view.addItemDecoration(dividerItemDecoration);

        setUpData();
        setUpAdapter();
        loadData();

        return rootView;
    }

    private void setUpData(){
        mRegionsData = new ArrayList<>();
    }

    private void setUpAdapter() {
        mRegionsAdapter = new RegionsAdapter(mRegionsData,mContext);
        regions_recycler_view.setAdapter(mRegionsAdapter);
    }

    private void loadData() {
        FetchRegionsTask fetchRegionsTask = new FetchRegionsTask(mContext,mRegionsAdapter);
        fetchRegionsTask.execute();
    }
}
