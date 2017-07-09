package com.patrickwallin.projects.collegeinformation.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.patrickwallin.projects.collegeinformation.R;
import com.patrickwallin.projects.collegeinformation.asynctask.FetchSearchQueryInputTask;
import com.patrickwallin.projects.collegeinformation.data.SearchQueryInputContract;
import com.patrickwallin.projects.collegeinformation.data.SearchQueryInputData;
import com.patrickwallin.projects.collegeinformation.data.StateData;
import com.patrickwallin.projects.collegeinformation.utilities.CursorAndDataConverter;
import com.patrickwallin.projects.collegeinformation.viewholder.SearchLocationStatesViewHolder;

import java.util.List;

/**
 * Created by piwal on 6/25/2017.
 */

public class StatesAdapter extends RecyclerView.Adapter<SearchLocationStatesViewHolder>  {
    private List<StateData> mStatesData;
    private int mSelectedId = -1;
    private int mSelectedPosition = 0;
    private Context mContext;

    public StatesAdapter(List<StateData> statesData, Context context) {
        mStatesData = statesData;
        mContext = context;
    }

    @Override
    public SearchLocationStatesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_location_state_item_activity, parent, false);
        SearchLocationStatesViewHolder searchLocationStatesViewHolder = new SearchLocationStatesViewHolder(v);
        return searchLocationStatesViewHolder;
    }

    @Override
    public void onBindViewHolder(final SearchLocationStatesViewHolder holder, int position) {
        if(mStatesData != null && position >= 0) {
            final StateData stateData = mStatesData.get(position);
            holder.mNameTextView.setText(stateData.getName());

            holder.mSelectedLocationCheckBox.setChecked(stateData.getSelected());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mSelectedLocationCheckBox.setChecked(!stateData.getSelected());
                    stateData.setSelected(holder.mSelectedLocationCheckBox.isChecked());
                    mSelectedId = stateData.getId();
                    if(mSelectedId == -1 && stateData.getSelected()) {
                        if(stateData.getSelected()) {
                            for(int i = 1; i < mStatesData.size(); i++) {
                                mStatesData.get(i).setSelected(true);
                            }
                            notifyDataSetChanged();
                        }

                        SearchQueryInputData searchQueryInputData = new SearchQueryInputData(FetchSearchQueryInputTask.SEARCH_QUERY_STATES_ID,"states",String.valueOf(stateData.getId()));
                        mContext.getContentResolver().update(SearchQueryInputContract.SearchQueryInputEntry.CONTENT_URI,searchQueryInputData.getSearchQueryInputContentValues(),
                                SearchQueryInputContract.SearchQueryInputEntry.COLUMN_QUERY_ID + " = " + String.valueOf(FetchSearchQueryInputTask.SEARCH_QUERY_STATES_ID), null);

                    }else {
                        StringBuilder selectedValues = new StringBuilder();
                        for(int i = 1; i < mStatesData.size(); i++) {
                            if(mStatesData.get(i).getSelected()) {
                                if(!selectedValues.toString().trim().isEmpty())
                                    selectedValues.append(",");
                                selectedValues.append(String.valueOf(mStatesData.get(i).getId()));
                            }
                        }

                        SearchQueryInputData searchQueryInputData = new SearchQueryInputData(FetchSearchQueryInputTask.SEARCH_QUERY_STATES_ID,"states",selectedValues.toString());
                        mContext.getContentResolver().update(SearchQueryInputContract.SearchQueryInputEntry.CONTENT_URI,searchQueryInputData.getSearchQueryInputContentValues(),
                                SearchQueryInputContract.SearchQueryInputEntry.COLUMN_QUERY_ID + " = " + String.valueOf(FetchSearchQueryInputTask.SEARCH_QUERY_STATES_ID), null);

                    }
                }
            });

            holder.mSelectedLocationCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.itemView.performClick();
                }
            });
            /*
            holder.mSelectedLocationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //holder.mSelectedLocationCheckBox.setChecked(!stateData.getSelected());
                    //stateData.setSelected(isChecked);
                    //mSelectedId = stateData.getId();
                }
            });
            */

        }
    }

    @Override
    public int getItemCount() {
        return (mStatesData == null ? 0 : mStatesData.size());
    }

    public void setStateData(List<StateData> stateData) {
        mStatesData = stateData;

        String[] selectedStateId = null;

        Cursor cursor = mContext.getContentResolver().query(SearchQueryInputContract.SearchQueryInputEntry.CONTENT_URI,null,
                SearchQueryInputContract.SearchQueryInputEntry.COLUMN_QUERY_ID + " = " + String.valueOf(FetchSearchQueryInputTask.SEARCH_QUERY_STATES_ID), null,null);
        if(cursor != null && cursor.moveToFirst()) {
            List<SearchQueryInputData> searchQueryInputDataList = CursorAndDataConverter.getSearchQueryInputDataFromCursor(cursor);
            if(searchQueryInputDataList != null && searchQueryInputDataList.size() > 0) {
                String value = searchQueryInputDataList.get(0).getValue().trim();
                selectedStateId = value.split(",",-1);
            }
        }
        if(cursor != null)
            cursor.close();

        if(mStatesData != null && mStatesData.size() > 0) {
            if(selectedStateId.length > 0) {
                if(selectedStateId[0].equalsIgnoreCase("-1")) {
                    for (int i = 0; i < mStatesData.size(); i++) {
                        mStatesData.get(i).setSelected(true);
                    }
                }else {
                    for (int i = 0; i < mStatesData.size(); i++) {
                        for(int x = 0; x < selectedStateId.length; x++) {
                            if(!selectedStateId[x].trim().isEmpty()) {
                                if (mStatesData.get(i).getId() == Integer.valueOf(selectedStateId[x].trim())) {
                                    mStatesData.get(i).setSelected(true);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}
