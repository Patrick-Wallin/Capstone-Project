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

import com.patrickwallin.projects.collegeinformation.adapter.StatesAdapter;
import com.patrickwallin.projects.collegeinformation.asynctask.FetchStatesTask;
import com.patrickwallin.projects.collegeinformation.data.StateData;
import com.patrickwallin.projects.collegeinformation.utilities.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by piwal on 6/25/2017.
 */

public class SearchLocationsStatesActivityFragment extends Fragment {
    @BindView(R.id.states_recycler_view)
    RecyclerView states_recycler_view;

    private List<StateData> mStatesData;
    private Context mContext;
    private StatesAdapter mStatesAdapter;

    public SearchLocationsStatesActivityFragment() {}

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
        View rootView = inflater.inflate(R.layout.activity_search_locations_states_fragment,container,false);

        ButterKnife.bind(this,rootView);

        LinearLayoutManager llm = new LinearLayoutManager(mContext);

        states_recycler_view.setLayoutManager(llm);
        states_recycler_view.setHasFixedSize(true);

        Drawable dividerDrawable = ContextCompat.getDrawable(mContext, R.drawable.divider_line);

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
        states_recycler_view.addItemDecoration(dividerItemDecoration);

        setUpData();
        setUpAdapter();
        loadData();

        return rootView;
    }

    private void setUpData(){
        mStatesData = new ArrayList<>();
    }

    private void setUpAdapter() {
        mStatesAdapter = new StatesAdapter(mStatesData,mContext);
        states_recycler_view.setAdapter(mStatesAdapter);
    }

    private void loadData() {
        FetchStatesTask fetchStatesTask = new FetchStatesTask(mContext,mStatesAdapter);
        fetchStatesTask.execute();
    }
}
