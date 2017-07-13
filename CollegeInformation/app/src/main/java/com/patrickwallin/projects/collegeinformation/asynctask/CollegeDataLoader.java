package com.patrickwallin.projects.collegeinformation.asynctask;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.patrickwallin.projects.collegeinformation.R;
import com.patrickwallin.projects.collegeinformation.data.FavoriteCollegeContract;
import com.patrickwallin.projects.collegeinformation.data.FavoriteCollegeData;
import com.patrickwallin.projects.collegeinformation.utilities.NetworkUtils;
import com.patrickwallin.projects.collegeinformation.utilities.OpenJsonUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by piwal on 7/12/2017.
 */

public class CollegeDataLoader extends AsyncTaskLoader<List<FavoriteCollegeData>> {
    private static final String LOG_TAG = CollegeDataLoader.class.getName();
    private boolean mFavoriteResults;
    private Context mContext;
    private boolean mFirstTimeLoadingData = true;
    private int mTotalNumberOfRecordsInResults = 0;
    private String mResultRequestString = "";
    private List<FavoriteCollegeData> mFavoriteCollegeData = new ArrayList<>();

    public CollegeDataLoader(Context context, boolean favoriteResults) {
        super(context);
        mContext = context;
        mFavoriteResults = favoriteResults;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<FavoriteCollegeData> loadInBackground() {
        loadData();
        return mFavoriteCollegeData;
    }

    private void loadData() {
        if(mFavoriteResults) {
            Cursor cursorFavorites = mContext.getContentResolver().query(FavoriteCollegeContract.FavoriteCollegeEntry.CONTENT_URI,null,null,null,null);
            if(cursorFavorites != null && cursorFavorites.moveToFirst()) {
                for (cursorFavorites.moveToFirst(); !cursorFavorites.isAfterLast(); cursorFavorites.moveToNext()) {
                    FavoriteCollegeData favoriteCollegeData = new FavoriteCollegeData(cursorFavorites);
                    mFavoriteCollegeData.add(favoriteCollegeData);
                }
            }
            if(cursorFavorites != null)
                cursorFavorites.close();
        }else {
            NetworkUtils networkUtils = new NetworkUtils(mContext);
            if (networkUtils.isNetworkConnected()) {
                mFirstTimeLoadingData = true;
                mResultRequestString = networkUtils.buildQueryBasedOnQueryInput(false);
                URL collegeDataRequestURL = networkUtils.buildUrl(mResultRequestString);
                if(collegeDataRequestURL != null) {
                    processGettingData(collegeDataRequestURL);
                }
            }else {
                networkUtils.showAlertMessageAboutNoInternetConnection(true);
            }
        }
    }

    private void loadData(int pageNumber) {
        String newResultRequestString = "";

        NetworkUtils networkUtils = new NetworkUtils(mContext);
        newResultRequestString = networkUtils.modifyPageNumberInQueryBasedOnPageNumber(mResultRequestString,pageNumber);
        URL collegeDataRequestURL = networkUtils.buildUrl(newResultRequestString);
        if(collegeDataRequestURL != null)
            processGettingData(collegeDataRequestURL);
    }

    public void processGettingData(URL url) {
        NetworkUtils networkUtils = new NetworkUtils(mContext);
        try {
            String jsonCollegeData = networkUtils.getResponseFromHttpUrl(url);
            if(jsonCollegeData != null && !jsonCollegeData.trim().isEmpty()) {
                if (mFirstTimeLoadingData) {
                    mTotalNumberOfRecordsInResults = OpenJsonUtils.getTotalRecords(jsonCollegeData);
                    mFirstTimeLoadingData = false;
                }
                int pageNumber = OpenJsonUtils.getPageNumber(jsonCollegeData);
                int limitNumberPerPage = Integer.valueOf(mContext.getString(R.string.limit_number_per_page).trim());
                if(mTotalNumberOfRecordsInResults > 0 && pageNumber >= 0) {
                    List<FavoriteCollegeData> favoriteCollegeDataList = OpenJsonUtils.getFavoriteCollegeDataFromJson(jsonCollegeData);
                    mFavoriteCollegeData.addAll(favoriteCollegeDataList);

                    int totalNumberOfPages = mTotalNumberOfRecordsInResults/limitNumberPerPage;
                    if(totalNumberOfPages > pageNumber) {
                        loadData(++pageNumber);
                    }else {
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
