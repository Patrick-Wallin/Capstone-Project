package com.patrickwallin.projects.collegeinformation;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.patrickwallin.projects.collegeinformation.asynctask.FetchSearchQueryInputTask;
import com.patrickwallin.projects.collegeinformation.data.SearchQueryInputContract;
import com.patrickwallin.projects.collegeinformation.data.SearchQueryInputData;
import com.patrickwallin.projects.collegeinformation.utilities.CursorAndDataConverter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by piwal on 6/23/2017.
 */

public class SearchLocationsActivityFragment extends Fragment {
    private Context mContext;
    @BindView(R.id.selected_state_text_view) TextView mSelectedStateTextView;
    @BindView(R.id.selected_region_text_view) TextView mSelectedRegionTextView;
    @BindView(R.id.zip_code_edit_view) EditText mZipCodeEditView;

    public SearchLocationsActivityFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_search_location_fragment,container,false);

        ButterKnife.bind(this,rootView);

        mSelectedStateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentStatesActivity = new Intent(mContext, SearchLocationsStatesActivity.class);
                mContext.startActivity(intentStatesActivity);
            }
        });

        mSelectedRegionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegionsActivity = new Intent(mContext, SearchLocationsRegionsActivity.class);
                mContext.startActivity(intentRegionsActivity);
            }
        });

        String sqlWhere = SearchQueryInputContract.SearchQueryInputEntry.COLUMN_QUERY_ID + " = " + FetchSearchQueryInputTask.SEARCH_QUERY_ZIPS_ID;
        Cursor cursor = mContext.getContentResolver().query(SearchQueryInputContract.SearchQueryInputEntry.CONTENT_URI,null,sqlWhere,null,null);
        if(cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(SearchQueryInputContract.SearchQueryInputEntry.COLUMN_QUERY_VALUE);
            String zip = cursor.getString(columnIndex);
            mZipCodeEditView.setText(zip);
        }
        if(cursor != null)
            cursor.close();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        Cursor queryInputCursor;

        StringBuilder sqlWhere = new StringBuilder();
        sqlWhere.append(SearchQueryInputContract.SearchQueryInputEntry.COLUMN_QUERY_ID);
        sqlWhere.append(" = ");
        sqlWhere.append(String.valueOf(FetchSearchQueryInputTask.SEARCH_QUERY_STATES_ID));
        sqlWhere.append(" OR ");
        sqlWhere.append(SearchQueryInputContract.SearchQueryInputEntry.COLUMN_QUERY_ID);
        sqlWhere.append(" = ");
        sqlWhere.append(String.valueOf(FetchSearchQueryInputTask.SEARCH_QUERY_REGIONS_ID));


        queryInputCursor = mContext.getContentResolver().query(
                SearchQueryInputContract.SearchQueryInputEntry.CONTENT_URI,
                null,
                sqlWhere.toString(),
                null,
                null);
        if(queryInputCursor != null && queryInputCursor.moveToFirst()) {
            List<SearchQueryInputData> list = CursorAndDataConverter.getSearchQueryInputDataFromCursor(queryInputCursor);

            StringBuilder queryInputResult = new StringBuilder();
            for(int i = 0; i < list.size(); i++) {
                SearchQueryInputData searchQueryInputData = list.get(i);
                if(searchQueryInputData.getId() == FetchSearchQueryInputTask.SEARCH_QUERY_STATES_ID) {
                    if(searchQueryInputData.getValue().trim().isEmpty()) {
                        mSelectedStateTextView.setText("0 " + getString(R.string.selected));
                    }else {
                        String[] selectedValues = searchQueryInputData.getValue().trim().split(",",-1);
                        mSelectedStateTextView.setText(String.valueOf(selectedValues.length) + " " + getString(R.string.selected) );
                    }
                }else {
                    if(searchQueryInputData.getValue().trim().isEmpty()) {
                        mSelectedRegionTextView.setText("0 " + getString(R.string.selected));
                    }else {
                        String[] selectedValues = searchQueryInputData.getValue().trim().split(",",-1);
                        mSelectedRegionTextView.setText(String.valueOf(selectedValues.length) + " " + getString(R.string.selected));
                    }

                }
            }


        }

        if(queryInputCursor != null)
            queryInputCursor.close();
    }

    @Override
    public void onPause() {
        super.onPause();

        String sqlWhere = SearchQueryInputContract.SearchQueryInputEntry.COLUMN_QUERY_ID + " = " + FetchSearchQueryInputTask.SEARCH_QUERY_ZIPS_ID;
        SearchQueryInputData searchQueryInputData = new SearchQueryInputData(FetchSearchQueryInputTask.SEARCH_QUERY_ZIPS_ID,
                getString(R.string.zips),mZipCodeEditView.getText().toString());
        mContext.getContentResolver().update(SearchQueryInputContract.SearchQueryInputEntry.CONTENT_URI,searchQueryInputData.getSearchQueryInputContentValues(),
                sqlWhere,null);

    }

}
