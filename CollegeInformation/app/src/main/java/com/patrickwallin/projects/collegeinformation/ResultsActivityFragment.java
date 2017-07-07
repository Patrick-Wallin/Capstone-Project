package com.patrickwallin.projects.collegeinformation;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.patrickwallin.projects.collegeinformation.asynctask.FetchResultTask;
import com.patrickwallin.projects.collegeinformation.data.FavoriteCollegeData;
import com.patrickwallin.projects.collegeinformation.utilities.CursorAndDataConverter;
import com.patrickwallin.projects.collegeinformation.utilities.NetworkUtils;
import com.patrickwallin.projects.collegeinformation.utilities.OpenJsonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by piwal on 6/27/2017.
 */

public class ResultsActivityFragment extends Fragment implements StringRequestListener {
    @BindView(R.id.results_recycler_view)
    RecyclerView results_recycler_view;

    private List<FavoriteCollegeData> mFavoriteCollegeData;
    private Context mContext;
    private FavoriteCollegeAdapter mFavoriteCollegeAdapter;

    private boolean mFirstTimeLoadingData = true;
    private int mTotalNumberOfRecordsInResults = 0;
    private int mNumberOfRecordPerPage = 100;
    private String mResultRequestString = "";

    public ResultsActivityFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // if(!getResources().getBoolean(R.bool.is_this_tablet)){
       //     getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
      //  }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_result_fragment,container,false);

        ButterKnife.bind(this,rootView);

        //LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        //llm.setOrientation(LinearLayoutManager.VERTICAL);
        if(!getResources().getBoolean(R.bool.is_this_tablet)) {
            StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            // GridLayoutManager sglm = new GridLayoutManager(mContext, 2);
            results_recycler_view.setLayoutManager(sglm);
        }else {
            StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            // GridLayoutManager sglm = new GridLayoutManager(mContext, 2);
            results_recycler_view.setLayoutManager(sglm);

        }
        //results_recycler_view.setLayoutManager(llm);
        results_recycler_view.setHasFixedSize(true);

        setUpData();
        setUpAdapter();
        loadData();

        return rootView;
    }

    private void setUpData(){
        mFavoriteCollegeData = new ArrayList<>();
    }

    private void setUpAdapter() {
        mFavoriteCollegeAdapter = new FavoriteCollegeAdapter(mFavoriteCollegeData,mContext);
        results_recycler_view.setAdapter(mFavoriteCollegeAdapter);
    }

    private void loadData() {
        setUpData();

        mFirstTimeLoadingData = true;

        NetworkUtils networkUtils = new NetworkUtils(mContext);

        mResultRequestString = networkUtils.buildQueryBasedOnQueryInput(false);

        AndroidNetworking.get(mResultRequestString)
                .setPriority(Priority.LOW)
                .build()
                .getAsString(this);
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

    private void updateCollegeImageLinkFromAnotherWebsite() {
        NetworkUtils networkUtils = new NetworkUtils(mContext);
        String urlString = networkUtils.buildSchoolQuery();
        if(mFavoriteCollegeData != null && !mFavoriteCollegeData.isEmpty()) {
            for(int i = 0; i < mFavoriteCollegeData.size(); i++) {
                final int record = i;
                AndroidNetworking.get(urlString)
                        .addPathParameter("unitid",String.valueOf(mFavoriteCollegeData.get(record).getId()))
                        .setPriority(Priority.LOW)
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                if(response != null && !response.trim().isEmpty()) {
                                    String imageLink = OpenJsonUtils.getImageLinkFromJson(response);
                                    Log.i("imagelink",mFavoriteCollegeData.get(record).getName() + " " + imageLink);
                                    mFavoriteCollegeData.get(record).setImageLink(imageLink);
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                // fail!  do nothing since it might not be existed!
                            }
                        });
                if((i+1) >= mFavoriteCollegeData.size()) {
                    mFavoriteCollegeAdapter.setFavoriteData(mFavoriteCollegeData);

                }
            }
        }


    }

    @Override
    public void onResponse(String response) {
        //Log.i("OnResponse",response);
        if(response != null && !response.trim().isEmpty()) {
            if (mFirstTimeLoadingData) {
                mTotalNumberOfRecordsInResults = OpenJsonUtils.getTotalRecords(response);
                mFirstTimeLoadingData = false;
            }
            int pageNumber = OpenJsonUtils.getPageNumber(response);
            int limitNumberPerPage = Integer.valueOf(mContext.getString(R.string.limit_number_per_page).trim());
            if(mTotalNumberOfRecordsInResults > 0 && pageNumber >= 0) {
                //if(pageNumber == 0) { // we already have this!
                    List<FavoriteCollegeData> favoriteCollegeDataList = OpenJsonUtils.getFavoriteCollegeDataFromJson(response);
                    mFavoriteCollegeData.addAll(favoriteCollegeDataList);
                //}
                int totalNumberOfPages = mTotalNumberOfRecordsInResults/limitNumberPerPage;
                if(totalNumberOfPages > pageNumber) {
                    loadData(++pageNumber);
                }else {// need to update IMAGE LINK!
                    updateCollegeImageLinkFromAnotherWebsite();

                }

            }

        }

    }

    @Override
    public void onError(ANError anError) {
        Log.i("onError",anError.getMessage());
    }


}
