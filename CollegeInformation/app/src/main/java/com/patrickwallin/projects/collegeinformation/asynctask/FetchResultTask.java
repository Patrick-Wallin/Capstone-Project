package com.patrickwallin.projects.collegeinformation.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.patrickwallin.projects.collegeinformation.adapter.FavoriteCollegeAdapter;
import com.patrickwallin.projects.collegeinformation.data.FavoriteCollegeData;
import com.patrickwallin.projects.collegeinformation.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

/**
 * Created by piwal on 6/29/2017.
 */

public class FetchResultTask extends AsyncTask<Void, Void, List<FavoriteCollegeData>> {
    private Context mContext;
    private FavoriteCollegeAdapter mFavoriteCollegeAdapter;

    public FetchResultTask(Context context, FavoriteCollegeAdapter favoriteCollegeAdapter) {
        mContext = context;
        mFavoriteCollegeAdapter = favoriteCollegeAdapter;
    }
    @Override
    protected List<FavoriteCollegeData> doInBackground(Void... params) {
        NetworkUtils networkUtils = new NetworkUtils(mContext);

        String resultRequestString = networkUtils.buildQueryBasedOnQueryInput(false);

        if(resultRequestString != null && !resultRequestString.isEmpty()) {
           // String jsonResultResponse = networkUtils.getResponseFromHttpURL(resultRequestString);
        }


        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<FavoriteCollegeData> favoriteCollegeDatas) {
        if(favoriteCollegeDatas != null) {
            mFavoriteCollegeAdapter.setFavoriteData(favoriteCollegeDatas);
        }
    }
}
