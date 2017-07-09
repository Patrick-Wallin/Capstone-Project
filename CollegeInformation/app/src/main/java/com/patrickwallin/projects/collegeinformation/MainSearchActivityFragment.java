package com.patrickwallin.projects.collegeinformation;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
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
import com.patrickwallin.projects.collegeinformation.data.SearchQueryInputContract;
import com.patrickwallin.projects.collegeinformation.data.SearchQueryInputData;
import com.patrickwallin.projects.collegeinformation.utilities.CursorAndDataConverter;
import com.patrickwallin.projects.collegeinformation.utilities.DividerItemDecoration;
import com.patrickwallin.projects.collegeinformation.utilities.NetworkUtils;
import com.patrickwallin.projects.collegeinformation.utilities.OpenJsonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

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

    public MainSearchActivityFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if(!getResources().getBoolean(R.bool.is_this_tablet)){
          //  getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //}
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_main_search_fragment,container,false);

        ButterKnife.bind(this,rootView);

        //if(getResources().getBoolean(R.bool.is_this_tablet) && !getResources().getBoolean(R.bool.is_this_portrait)){
        if(getResources().getBoolean(R.bool.is_this_tablet)){
            //if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
              //  GridLayoutManager sglm = new GridLayoutManager(mContext, 4);
                //search_recycler_view.setLayoutManager(sglm);
            //}else {
            int numberOfSpan = 4;
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            }else {
                if(getResources().getBoolean(R.bool.is_this_big_tablet)) {
                    numberOfSpan = 1;
                }else {
                    numberOfSpan = 2;
                }
            }

            /*
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                GridLayoutManager sglm = new GridLayoutManager(mContext, numberOfSpan);
                search_recycler_view.setLayoutManager(sglm);
            }else {
                GridLayoutManager sglm = new GridLayoutManager(mContext, 4);
                search_recycler_view.setLayoutManager(sglm);
            }
            */

            GridLayoutManager sglm = new GridLayoutManager(mContext, numberOfSpan);

            search_recycler_view.setLayoutManager(sglm);

            /*
                LinearLayoutManager llm = new LinearLayoutManager(mContext);
                Drawable dividerDrawable = ContextCompat.getDrawable(mContext, R.drawable.divider_line);
                RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
                search_recycler_view.addItemDecoration(dividerItemDecoration);
                search_recycler_view.setLayoutManager(llm);
                */
            //}
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
                // check if any record first!
                if(mTotalRecordsInResults == 0) {
                    Snackbar.make(getView().findViewById(R.id.article_coordinator_layout), "No results based on search", Snackbar.LENGTH_LONG).show();
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
                // check if any record first!
                if(mTotalRecordsInFavorites == 0) {
                    Snackbar.make(getView().findViewById(R.id.article_coordinator_layout),"Unable to show since there is no favorite.",Snackbar.LENGTH_LONG).show();
                }else {
                    Intent intentResultsActivity = new Intent(mContext, ResultsActivity.class);
                    intentResultsActivity.putExtra("favoriteresults",true);
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
        search_query_text_view.setText("Total Records In Results: 0");

        NetworkUtils networkUtils = new NetworkUtils(mContext);

        String mResultRequestString = networkUtils.buildQueryBasedOnQueryInput(true);

        AndroidNetworking.get(mResultRequestString)
                .setPriority(Priority.LOW)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        int totalRecords = OpenJsonUtils.getTotalRecords(response);
                        String queryAnswer = "Total Records In Results: " + String.valueOf(totalRecords);
                        search_query_text_view.setText(queryAnswer);
                        mTotalRecordsInResults = totalRecords;
                        mTotalRecordsInFavorites = 0;
                        Cursor cursor = mContext.getContentResolver().query(FavoriteCollegeContract.FavoriteCollegeEntry.CONTENT_URI,new String[] {"count(*)"},null,null,null);
                        if(cursor != null && cursor.moveToFirst()) {
                            queryAnswer += "\n";
                            queryAnswer += "Total Records in Favorites: " + String.valueOf(cursor.getInt(0));
                            mTotalRecordsInFavorites = cursor.getInt(0);
                            search_query_text_view.setText(queryAnswer);
                        }
                        if(cursor != null)
                            cursor.close();
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });

        /*
        Cursor queryInputCursor;

        queryInputCursor = mContext.getContentResolver().query(
                SearchQueryInputContract.SearchQueryInputEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        if(queryInputCursor != null && queryInputCursor.moveToFirst()) {
            List<SearchQueryInputData> list = CursorAndDataConverter.getSearchQueryInputDataFromCursor(queryInputCursor);

            StringBuilder queryInputResult = new StringBuilder();
            for(int i = 0; i < list.size(); i++) {
                SearchQueryInputData searchQueryInputData = list.get(i);
                if(!searchQueryInputData.getValue().trim().isEmpty()) {
                    if (!queryInputResult.toString().isEmpty()) {
                        queryInputResult.append(System.getProperty("line.separator"));
                    }

                    queryInputResult.append(searchQueryInputData.getName());
                    queryInputResult.append(": ");

                    String result = CursorAndDataConverter.getValueBasedOnIDAndTable(mContext,searchQueryInputData.getName(),searchQueryInputData.getValue());
                    queryInputResult.append(result);
                }
            }

            search_query_text_view.setText(queryInputResult.toString());
        }
        */
        Log.i("test","Onresume - mainsearchactivityfragment");
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

    /*
    @Override
    public void OnSelectionChanged(String listTableName) {
        Timber.i("What does it say? %s",listTableName);
    }
    */
}
