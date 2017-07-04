package com.patrickwallin.projects.collegeinformation;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patrickwallin.projects.collegeinformation.adapter.DegreesAdapter;
import com.patrickwallin.projects.collegeinformation.asynctask.FetchDegreesTask;
import com.patrickwallin.projects.collegeinformation.data.DegreesData;
import com.patrickwallin.projects.collegeinformation.data.SearchQueryInputContract;
import com.patrickwallin.projects.collegeinformation.data.SearchQueryInputData;
import com.patrickwallin.projects.collegeinformation.utilities.CursorAndDataConverter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by piwal on 6/6/2017.
 */

public class SearchDegreesActivityFragment extends Fragment {
    @BindView(R.id.degrees_recycler_view)
    RecyclerView degrees_recycler_view;

    private List<DegreesData> mDegreesData;
    private Context mContext;
    private DegreesAdapter mDegreesAdapter;

    public SearchDegreesActivityFragment() {}

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
        View rootView = inflater.inflate(R.layout.activity_search_degrees_fragment,container,false);

        ButterKnife.bind(this,rootView);

        GridLayoutManager sglm = new GridLayoutManager(mContext,2);

        degrees_recycler_view.setLayoutManager(sglm);
        degrees_recycler_view.setHasFixedSize(true);

        setUpData();
        setUpAdapter();
        loadData();

        Log.i("oncreateview","oncreateview-searchdegreesactivityfragment");
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("test","onresume-searchdegreesactivityfragment");

    }

    private void setUpData(){
        mDegreesData = new ArrayList<>();
    }

    private void setUpAdapter() {
        mDegreesAdapter = new DegreesAdapter(mDegreesData,mContext);
        degrees_recycler_view.setAdapter(mDegreesAdapter);
    }

    private void loadData() {
        FetchDegreesTask fetchDegreesTask = new FetchDegreesTask(mContext,mDegreesAdapter);
        fetchDegreesTask.execute();
    }
}
