package com.patrickwallin.projects.collegeinformation;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.patrickwallin.projects.collegeinformation.adapter.FavoriteCollegeAdapter;
import com.patrickwallin.projects.collegeinformation.asynctask.CollegeDataLoader;
import com.patrickwallin.projects.collegeinformation.data.FavoriteCollegeContract;
import com.patrickwallin.projects.collegeinformation.data.FavoriteCollegeData;
import com.patrickwallin.projects.collegeinformation.utilities.NetworkUtils;
import com.patrickwallin.projects.collegeinformation.utilities.OpenJsonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by piwal on 6/27/2017.
 */

public class ResultsActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<FavoriteCollegeData>> {
    @BindView(R.id.results_recycler_view)
    RecyclerView results_recycler_view;

    private List<FavoriteCollegeData> mFavoriteCollegeData;
    private Context mContext;
    private FavoriteCollegeAdapter mFavoriteCollegeAdapter;

    private boolean mFirstTimeLoadingData = true;
    private int mTotalNumberOfRecordsInResults = 0;
    private String mResultRequestString = "";

    private boolean mFavoriteResults = false;
    private int mIdFromWidget = 0;
    private int LOADER_RESULTS_ACTIVITY_ID = 1;

    public ResultsActivityFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        mFavoriteResults = bundle.getBoolean(getString(R.string.favorite_results));
        if(bundle.containsKey(getString(R.string.id))) {
            mIdFromWidget = bundle.getInt(getString(R.string.id));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_result_fragment,container,false);

        ButterKnife.bind(this,rootView);

        if(!getResources().getBoolean(R.bool.is_this_tablet)) {
            int numberOfColumn = 2;

            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                numberOfColumn = 3;
                StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(numberOfColumn, StaggeredGridLayoutManager.VERTICAL);
                results_recycler_view.setLayoutManager(sglm);
            }else {
                StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(numberOfColumn, StaggeredGridLayoutManager.VERTICAL);
                results_recycler_view.setLayoutManager(sglm);
            }
        }else {
            StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            results_recycler_view.setLayoutManager(sglm);

        }
        results_recycler_view.setHasFixedSize(true);

        setUpData();
        setUpAdapter();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //loadData();
    }

    private void setUpData(){
        mFavoriteCollegeData = new ArrayList<>();
    }

    private void setUpAdapter() {
        mFavoriteCollegeAdapter = new FavoriteCollegeAdapter(mFavoriteCollegeData,mContext);
        mIdFromWidget = 0;
        results_recycler_view.setAdapter(mFavoriteCollegeAdapter);
        getLoaderManager().initLoader(LOADER_RESULTS_ACTIVITY_ID,null,this);
    }

    // move the following to CollegeDataLoader!
    /*
    private void loadData() {
        //setUpData();

        if(mFavoriteResults) {
            Cursor cursorFavorites = mContext.getContentResolver().query(FavoriteCollegeContract.FavoriteCollegeEntry.CONTENT_URI,null,null,null,null);
            if(cursorFavorites != null && cursorFavorites.moveToFirst()) {
                for (cursorFavorites.moveToFirst(); !cursorFavorites.isAfterLast(); cursorFavorites.moveToNext()) {
                    FavoriteCollegeData favoriteCollegeData = new FavoriteCollegeData(cursorFavorites);
                    mFavoriteCollegeData.add(favoriteCollegeData);
                }
                mFavoriteCollegeAdapter.setFavoriteData(mFavoriteCollegeData);
                getLoaderManager().restartLoader(LOADER_RESULTS_ACTIVITY_ID,null,this);

            }
            if(cursorFavorites != null)
                cursorFavorites.close();

            if(mIdFromWidget > 0) {

            }
        }else {
            NetworkUtils networkUtils = new NetworkUtils(mContext);
            if (networkUtils.isNetworkConnected()) {
                mFirstTimeLoadingData = true;
                mResultRequestString = networkUtils.buildQueryBasedOnQueryInput(false);
                AndroidNetworking.get(mResultRequestString)
                        .setPriority(Priority.LOW)
                        .build()
                        .getAsString(this);
            }else {
                networkUtils.showAlertMessageAboutNoInternetConnection(true);
            }
        }
    }

    private void loadData(int pageNumber) {
        String newResultRequestString = "";

        NetworkUtils networkUtils = new NetworkUtils(mContext);
        newResultRequestString = networkUtils.modifyPageNumberInQueryBasedOnPageNumber(mResultRequestString,pageNumber);

        AndroidNetworking.get(newResultRequestString)
                .setPriority(Priority.LOW)
                .build()
                .getAsString(this);
    }

    @Override
    public void onResponse(String response) {
        if(response != null && !response.trim().isEmpty()) {
            if (mFirstTimeLoadingData) {
                mTotalNumberOfRecordsInResults = OpenJsonUtils.getTotalRecords(response);
                mFirstTimeLoadingData = false;
            }
            int pageNumber = OpenJsonUtils.getPageNumber(response);
            int limitNumberPerPage = Integer.valueOf(mContext.getString(R.string.limit_number_per_page).trim());
            if(mTotalNumberOfRecordsInResults > 0 && pageNumber >= 0) {
                List<FavoriteCollegeData> favoriteCollegeDataList = OpenJsonUtils.getFavoriteCollegeDataFromJson(response);
                mFavoriteCollegeData.addAll(favoriteCollegeDataList);

                int totalNumberOfPages = mTotalNumberOfRecordsInResults/limitNumberPerPage;
                if(totalNumberOfPages > pageNumber) {
                    loadData(++pageNumber);
                }else {
                    mFavoriteCollegeAdapter.setFavoriteData(mFavoriteCollegeData);
                    getLoaderManager().restartLoader(LOADER_RESULTS_ACTIVITY_ID,null,this);

                }

            }

        }

    }

    @Override
    public void onError(ANError anError) {
        Log.i("onError",anError.getMessage());
    }
    */

    @Override
    public Loader<List<FavoriteCollegeData>> onCreateLoader(int id, Bundle args) {
        return new CollegeDataLoader(mContext,mFavoriteResults);
    }

    @Override
    public void onLoadFinished(Loader<List<FavoriteCollegeData>> loader, List<FavoriteCollegeData> data) {
        mFavoriteCollegeAdapter.setFavoriteData(data);
    }

    @Override
    public void onLoaderReset(Loader<List<FavoriteCollegeData>> loader) {
        mFavoriteCollegeAdapter.setFavoriteData(new ArrayList<FavoriteCollegeData>());
    }
}
