package com.patrickwallin.projects.collegeinformation.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patrickwallin.projects.collegeinformation.R;
import com.patrickwallin.projects.collegeinformation.asynctask.FetchSearchQueryInputTask;
import com.patrickwallin.projects.collegeinformation.data.DegreesData;
import com.patrickwallin.projects.collegeinformation.data.SearchQueryInputContract;
import com.patrickwallin.projects.collegeinformation.data.SearchQueryInputData;
import com.patrickwallin.projects.collegeinformation.utilities.CursorAndDataConverter;
import com.patrickwallin.projects.collegeinformation.viewholder.SearchDegreesViewHolder;

import java.util.List;

/**
 * Created by piwal on 6/6/2017.
 */

public class DegreesAdapter extends RecyclerView.Adapter<SearchDegreesViewHolder> {
    private List<DegreesData> mDegreesData;
    private Context mContext;
    private int mSelectedPosition = 0;

    public DegreesAdapter(List<DegreesData> degreesData, Context context) {
        mDegreesData = degreesData;
        mContext = context;
    }

    @Override
    public SearchDegreesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_degrees_cardview_activity, parent, false);
        SearchDegreesViewHolder searchDegreesViewHolder = new SearchDegreesViewHolder(v);
        return searchDegreesViewHolder;
    }

    @Override
    public void onBindViewHolder(final SearchDegreesViewHolder holder, final int position) {
        if(mDegreesData != null && position >= 0) {
            final DegreesData degreesData = mDegreesData.get(position);

            holder.mNameTextView.setText(degreesData.getName());
            holder.mNameTextView.setTag(degreesData.getId());

            if(mSelectedPosition == position) {
                holder.mSearchDegreeCardView.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                holder.mNameTextView.setTextColor(Color.WHITE);
                SearchQueryInputData searchQueryInputData = new SearchQueryInputData(FetchSearchQueryInputTask.SEARCH_QUERY_DEGREES_ID,"degrees",String.valueOf(degreesData.getId()));
                mContext.getContentResolver().update(SearchQueryInputContract.SearchQueryInputEntry.CONTENT_URI,searchQueryInputData.getSearchQueryInputContentValues(),
                        SearchQueryInputContract.SearchQueryInputEntry.COLUMN_QUERY_ID + " = " + String.valueOf(FetchSearchQueryInputTask.SEARCH_QUERY_DEGREES_ID), null);
            }else {
                holder.mSearchDegreeCardView.setCardBackgroundColor(Color.WHITE);
                holder.mNameTextView.setTextColor(Color.BLACK);
            }

            holder.mSearchDegreeCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(mSelectedPosition);
                    mSelectedPosition = position;
                    notifyItemChanged(mSelectedPosition);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (mDegreesData == null ? 0 : mDegreesData.size());
    }

    public void setDegreeData(List<DegreesData> degreeData) {
        mDegreesData = degreeData;

        int selectedDegreeId = -1;
        int selectedPosition = 0;
        Cursor cursor = mContext.getContentResolver().query(SearchQueryInputContract.SearchQueryInputEntry.CONTENT_URI,null,
                SearchQueryInputContract.SearchQueryInputEntry.COLUMN_QUERY_ID + " = " + String.valueOf(FetchSearchQueryInputTask.SEARCH_QUERY_DEGREES_ID), null,null);
        if(cursor != null && cursor.moveToFirst()) {
            List<SearchQueryInputData> searchQueryInputDataList = CursorAndDataConverter.getSearchQueryInputDataFromCursor(cursor);
            if(searchQueryInputDataList != null && searchQueryInputDataList.size() > 0) {
                String value = searchQueryInputDataList.get(0).getValue().trim();
                selectedDegreeId = Integer.valueOf((value.isEmpty() ? "0" : value));
            }
        }
        if(cursor != null)
            cursor.close();

        if(mDegreesData != null && mDegreesData.size() > 0) {
            for (int i = 0; i < mDegreesData.size(); i++) {
                if (mDegreesData.get(i).getId() == selectedDegreeId) {
                    selectedPosition = i;
                    break;
                }
            }
        }
        mSelectedPosition = selectedPosition;

        notifyDataSetChanged();
    }

    public void setDegreePosition(int position) {
        mSelectedPosition = position;
        notifyDataSetChanged();
    }
}
