package com.patrickwallin.projects.collegeinformation;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.patrickwallin.projects.collegeinformation.adapter.MainSearchPageAdapter;
import com.patrickwallin.projects.collegeinformation.data.FavoriteCollegeContract;
import com.patrickwallin.projects.collegeinformation.data.MainSearchPage;
import com.patrickwallin.projects.collegeinformation.utilities.NetworkUtils;
import com.patrickwallin.projects.collegeinformation.utilities.OpenJsonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by piwal on 6/6/2017.
 */

public class MainSearchActivityFragment extends Fragment {
    @BindView(R.id.search_recycler_view) RecyclerView search_recycler_view;
    @BindView(R.id.find_college_button) Button find_college_button;
    @BindView(R.id.go_favorite_button) Button go_favorite_button;
    @BindView(R.id.search_query_text_view) TextView search_query_text_view;

    private List<MainSearchPage> mMainSearchPage;

    private Context mContext;
    private int mTotalRecordsInResults = 0;
    private int mTotalRecordsInFavorites = 0;
    private int mIdFromWidget = 0;

    public MainSearchActivityFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey(mContext.getString(R.string.id))) {
            mIdFromWidget = bundle.getInt(mContext.getString(R.string.id));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_main_search_fragment,container,false);

        ButterKnife.bind(this,rootView);

        if(getResources().getBoolean(R.bool.is_this_tablet)){
            int numberOfSpan = 4;
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            }else {
                if(getResources().getBoolean(R.bool.is_this_big_tablet)) {
                    numberOfSpan = 1;
                }else {
                    numberOfSpan = 2;
                }
            }

            GridLayoutManager sglm = new GridLayoutManager(mContext, numberOfSpan);

            search_recycler_view.setLayoutManager(sglm);
        }else {
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager sglm = new GridLayoutManager(mContext, 4);
                search_recycler_view.setLayoutManager(sglm);
            }else {
                GridLayoutManager sglm = new GridLayoutManager(mContext, 2);
                search_recycler_view.setLayoutManager(sglm);
            }

        }

        search_recycler_view.setHasFixedSize(true);

        find_college_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(mTotalRecordsInResults == 0) {
                Snackbar.make(getView().findViewById(R.id.article_coordinator_layout), getString(R.string.info_message_no_result), Snackbar.LENGTH_LONG).show();
            }else {
                Intent intentResultsActivity = new Intent(mContext, ResultsActivity.class);
                intentResultsActivity.putExtra("favoriteresults",false);
                mContext.startActivity(intentResultsActivity);
            }
            }
        });

        go_favorite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(mTotalRecordsInFavorites == 0) {
                Snackbar.make(getView().findViewById(R.id.article_coordinator_layout),getString(R.string.info_message_no_favorites),Snackbar.LENGTH_LONG).show();
            }else {
                Intent intentResultsActivity = new Intent(mContext, ResultsActivity.class);
                intentResultsActivity.putExtra(getString(R.string.favorite_results),true);
                intentResultsActivity.putExtra(getString(R.string.id),mIdFromWidget);
                mIdFromWidget = 0;
                mContext.startActivity(intentResultsActivity);
            }
            }
        });

        setUpData();
        setUpAdapter();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        search_query_text_view.setText(getString(R.string.info_message_total_records_zero));

        NetworkUtils networkUtils = new NetworkUtils(mContext);

        String mResultRequestString = networkUtils.buildQueryBasedOnQueryInput(true);

        AndroidNetworking.get(mResultRequestString)
                .setPriority(Priority.LOW)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        int totalRecords = OpenJsonUtils.getTotalRecords(response);
                        String queryAnswer = getString(R.string.info_message_total_records) + String.valueOf(totalRecords);
                        search_query_text_view.setText(queryAnswer);
                        mTotalRecordsInResults = totalRecords;
                        mTotalRecordsInFavorites = 0;
                        Cursor cursor = mContext.getContentResolver().query(FavoriteCollegeContract.FavoriteCollegeEntry.CONTENT_URI,new String[] {"count(*)"},null,null,null);
                        if(cursor != null && cursor.moveToFirst()) {
                            queryAnswer += "\n";
                            queryAnswer += getString(R.string.info_message_total_favorites) + String.valueOf(cursor.getInt(0));
                            mTotalRecordsInFavorites = cursor.getInt(0);
                            search_query_text_view.setText(queryAnswer);
                        }else {
                            queryAnswer += "\n";
                            queryAnswer +=  getString(R.string.info_message_total_favorites) + " 0";
                        }
                        if(cursor != null)
                            cursor.close();
                        if(mIdFromWidget > 0) {
                            go_favorite_button.performClick();
                            go_favorite_button.setPressed(true);
                            go_favorite_button.invalidate();
                            go_favorite_button.postDelayed(new Runnable() {
                                public void run() {
                                    go_favorite_button.setPressed(false);
                                    go_favorite_button.invalidate();
                                }
                            }, 800);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void setUpData(){
        mMainSearchPage = new ArrayList<>();
        mMainSearchPage.add(new MainSearchPage("Degrees", R.drawable.ic_graduation_cap));
        mMainSearchPage.add(new MainSearchPage("Programs", R.drawable.ic_college_book));
        mMainSearchPage.add(new MainSearchPage("Location", R.drawable.ic_map_location));
        mMainSearchPage.add(new MainSearchPage("Name", R.drawable.ic_college_building));
    }

    private void setUpAdapter() {
        MainSearchPageAdapter mainSearchPageAdapter = new MainSearchPageAdapter(mMainSearchPage,mContext);
        search_recycler_view.setAdapter(mainSearchPageAdapter);
    }

}
