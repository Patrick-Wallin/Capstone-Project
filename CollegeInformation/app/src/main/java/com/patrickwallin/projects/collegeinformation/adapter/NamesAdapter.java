package com.patrickwallin.projects.collegeinformation.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patrickwallin.projects.collegeinformation.R;
import com.patrickwallin.projects.collegeinformation.asynctask.FetchSearchQueryInputTask;
import com.patrickwallin.projects.collegeinformation.data.NameData;
import com.patrickwallin.projects.collegeinformation.data.ProgramData;
import com.patrickwallin.projects.collegeinformation.data.SearchQueryInputContract;
import com.patrickwallin.projects.collegeinformation.data.SearchQueryInputData;
import com.patrickwallin.projects.collegeinformation.utilities.CursorAndDataConverter;
import com.patrickwallin.projects.collegeinformation.viewholder.SearchNamesViewHolder;
import com.patrickwallin.projects.collegeinformation.viewholder.SearchProgramsViewHolder;

import java.util.List;
import java.util.jar.Attributes;

/**
 * Created by piwal on 6/22/2017.
 */

public class NamesAdapter extends RecyclerView.Adapter<SearchNamesViewHolder> {
    private List<NameData> mNameData;

    private int mSelectedPosition = 0;
    private int mSelectedPositionBasedOnFullList = 0;

    private Context mContext;
    private RecyclerView mNameRecyclerView;

    public NamesAdapter(List<NameData> nameData, Context context, RecyclerView nameRecyclerView) {
        mNameData = nameData;
        mContext = context;
        mNameRecyclerView = nameRecyclerView;
    }

    @Override
    public SearchNamesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_names_item_activity, parent, false);
        SearchNamesViewHolder searchNamesViewHolder = new SearchNamesViewHolder(v);
        return searchNamesViewHolder;
    }

    @Override
    public void onBindViewHolder(SearchNamesViewHolder holder, final int position) {
        if(mNameData != null && position >= 0) {
            final NameData nameData = mNameData.get(position);
            holder.mNameTextView.setText(nameData.getName());
            holder.mCityTextView.setText(nameData.getCity());
            holder.mStateTextView.setText(nameData.getState());
            holder.mZipTextView.setText(nameData.getZip());

            if(mSelectedPosition == position) {
                holder.itemView.setSelected(true);
                holder.itemView.setBackgroundColor(Color.BLUE);
                holder.mNameTextView.setTextColor(Color.WHITE);
                holder.mCityTextView.setTextColor(Color.WHITE);
                holder.mStateTextView.setTextColor(Color.WHITE);
                holder.mZipTextView.setTextColor(Color.WHITE);
                SearchQueryInputData searchQueryInputData = new SearchQueryInputData(FetchSearchQueryInputTask.SEARCH_QUERY_NAMES_ID,"names",String.valueOf(nameData.getId()));
                mContext.getContentResolver().update(SearchQueryInputContract.SearchQueryInputEntry.CONTENT_URI,searchQueryInputData.getSearchQueryInputContentValues(),
                        SearchQueryInputContract.SearchQueryInputEntry.COLUMN_QUERY_ID + " = " + String.valueOf(FetchSearchQueryInputTask.SEARCH_QUERY_NAMES_ID), null);
            }else {
                holder.itemView.setBackgroundColor(Color.WHITE);
                holder.mNameTextView.setTextColor(Color.BLACK);
                holder.mCityTextView.setTextColor(Color.BLACK);
                holder.mStateTextView.setTextColor(Color.BLACK);
                holder.mZipTextView.setTextColor(Color.BLACK);

            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(mSelectedPosition);
                    mSelectedPosition = position;
                    notifyItemChanged(mSelectedPosition);
                    updatePositionBasedOnFullList(nameData.getId());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (mNameData == null ? 0 : mNameData.size());
    }

    public int setNameData(List<NameData> nameData) {
        mNameData = nameData;

        // check default degree id!
        int selectedNameId = -1;
        int selectedPosition = 0;
        Cursor cursor = mContext.getContentResolver().query(SearchQueryInputContract.SearchQueryInputEntry.CONTENT_URI,null,
                SearchQueryInputContract.SearchQueryInputEntry.COLUMN_QUERY_ID + " = " + String.valueOf(FetchSearchQueryInputTask.SEARCH_QUERY_NAMES_ID), null,null);
        if(cursor != null && cursor.moveToFirst()) {
            List<SearchQueryInputData> searchQueryInputDataList = CursorAndDataConverter.getSearchQueryInputDataFromCursor(cursor);
            if(searchQueryInputDataList != null && searchQueryInputDataList.size() > 0) {
                String value = searchQueryInputDataList.get(0).getValue().trim();
                selectedNameId = Integer.valueOf((value.isEmpty() ? "0" : value));
                Log.i("Position: ", String.valueOf(selectedNameId));
            }
        }
        if(cursor != null)
            cursor.close();

        if(mNameData != null && mNameData.size() > 0) {
            for (int i = 0; i < mNameData.size(); i++) {
                if (mNameData.get(i).getId() == selectedNameId) {
                    selectedPosition = i;
                    break;
                }
            }
        }
        mSelectedPosition = selectedPosition;
        mSelectedPositionBasedOnFullList = selectedPosition;

        LinearLayoutManager llm = (LinearLayoutManager) mNameRecyclerView.getLayoutManager();
        llm.scrollToPosition(mSelectedPosition);

        notifyDataSetChanged();

        return mSelectedPosition;
    }

    private void updatePositionBasedOnFullList(int id) {
        if(mNameData != null && mNameData.size() > 0) {
            for (int i = 0; i < mNameData.size(); i++) {
                if (mNameData.get(i).getId() == id) {
                    mSelectedPositionBasedOnFullList = i;
                    break;
                }
            }
        }

    }

    public List<NameData> getNameData() {
        return mNameData;
    }

    public int getSelectedPosition() { return mSelectedPositionBasedOnFullList; }
}
