package com.patrickwallin.projects.collegeinformation.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patrickwallin.projects.collegeinformation.R;
import com.patrickwallin.projects.collegeinformation.data.ProgramData;
import com.patrickwallin.projects.collegeinformation.data.SearchQueryInputContract;
import com.patrickwallin.projects.collegeinformation.data.SearchQueryInputData;
import com.patrickwallin.projects.collegeinformation.utilities.CursorAndDataConverter;
import com.patrickwallin.projects.collegeinformation.viewholder.SearchProgramsViewHolder;

import java.util.List;

/**
 * Created by piwal on 6/22/2017.
 */

public class ProgramsAdapter extends RecyclerView.Adapter<SearchProgramsViewHolder> {
    private List<ProgramData> mProgramData;

    private int mSelectedPosition = 0;

    private Context mContext;

    public ProgramsAdapter(List<ProgramData> programData, Context context) {
        mProgramData = programData;
        mContext = context;
    }

    @Override
    public SearchProgramsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_programs_item_activity, parent, false);
        SearchProgramsViewHolder programsViewHolder = new SearchProgramsViewHolder(v);
        return programsViewHolder;
    }

    @Override
    public void onBindViewHolder(SearchProgramsViewHolder holder, final int position) {
        if(mProgramData != null && position >= 0) {
            final ProgramData programData = mProgramData.get(position);
            holder.mTitleTextView.setText(programData.getTitle());
            if(mSelectedPosition == position) {
                holder.itemView.setSelected(true);
                holder.itemView.setBackgroundColor(Color.BLUE);
                holder.mTitleTextView.setTextColor(Color.WHITE);
                SearchQueryInputData searchQueryInputData = new SearchQueryInputData(1,"programs",String.valueOf(programData.getId()));
                mContext.getContentResolver().update(SearchQueryInputContract.SearchQueryInputEntry.CONTENT_URI,searchQueryInputData.getSearchQueryInputContentValues(),
                        SearchQueryInputContract.SearchQueryInputEntry.COLUMN_QUERY_ID + " = 1", null);
            }else {
                holder.itemView.setBackgroundColor(Color.WHITE);
                holder.mTitleTextView.setTextColor(Color.BLACK);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(mSelectedPosition);
                    mSelectedPosition = position;
                    notifyItemChanged(mSelectedPosition);
                }
            });

            /*
            holder.mSearchDegreeCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(mSelectedPosition);
                    mSelectedPosition = position;
                    notifyItemChanged(mSelectedPosition);

                }
            });
            */
        }
    }

    @Override
    public int getItemCount() {
        return (mProgramData == null ? 0 : mProgramData.size());
    }

    public void setProgramData(List<ProgramData> programData) {
        mProgramData = programData;

        // check default degree id!
        int selectedProgramId = -1;
        int selectedPosition = 0;
        Cursor cursor = mContext.getContentResolver().query(SearchQueryInputContract.SearchQueryInputEntry.CONTENT_URI,null,
                SearchQueryInputContract.SearchQueryInputEntry.COLUMN_QUERY_ID + " = 1", null,null);
        if(cursor != null && cursor.moveToFirst()) {
            List<SearchQueryInputData> searchQueryInputDataList = CursorAndDataConverter.getSearchQueryInputDataFromCursor(cursor);
            if(searchQueryInputDataList != null && searchQueryInputDataList.size() > 0) {
                String value = searchQueryInputDataList.get(0).getValue().trim();
                selectedProgramId = Integer.valueOf((value.isEmpty() ? "0" : value));
                Log.i("Position: ", String.valueOf(selectedProgramId));
            }
        }

        if(mProgramData != null && mProgramData.size() > 0) {
            for (int i = 0; i < mProgramData.size(); i++) {
                if (mProgramData.get(i).getId() == selectedProgramId) {
                    selectedPosition = i;
                    break;
                }
            }
        }
        mSelectedPosition = selectedPosition;

        notifyDataSetChanged();
    }

    public List<ProgramData> getProgramData() {
        return mProgramData;
    }
}
