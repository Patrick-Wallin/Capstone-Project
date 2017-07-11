package com.patrickwallin.projects.collegeinformation.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patrickwallin.projects.collegeinformation.R;
import com.patrickwallin.projects.collegeinformation.asynctask.FetchSearchQueryInputTask;
import com.patrickwallin.projects.collegeinformation.data.RegionData;
import com.patrickwallin.projects.collegeinformation.data.SearchQueryInputContract;
import com.patrickwallin.projects.collegeinformation.data.SearchQueryInputData;
import com.patrickwallin.projects.collegeinformation.utilities.CursorAndDataConverter;
import com.patrickwallin.projects.collegeinformation.viewholder.SearchLocationRegionsViewHolder;

import java.util.List;

/**
 * Created by piwal on 6/25/2017.
 */

public class RegionsAdapter extends RecyclerView.Adapter<SearchLocationRegionsViewHolder>  {
    private List<RegionData> mRegionsData;
    private int mSelectedId = -1;
    private Context mContext;

    public RegionsAdapter(List<RegionData> regionDatas, Context context) {
        mRegionsData = regionDatas;
        mContext = context;
    }

    @Override
    public SearchLocationRegionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_location_region_item_activity, parent, false);
        SearchLocationRegionsViewHolder searchLocationRegionsViewHolder = new SearchLocationRegionsViewHolder(v);
        return searchLocationRegionsViewHolder;
    }

    @Override
    public void onBindViewHolder(final SearchLocationRegionsViewHolder holder, int position) {
        if(mRegionsData != null && position >= 0) {
            final RegionData regionData = mRegionsData.get(position);
            holder.mNameTextView.setText(regionData.getName());

            holder.mSelectedRegionCheckBox.setChecked(regionData.getSelected());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mSelectedRegionCheckBox.setChecked(!regionData.getSelected());
                    regionData.setSelected(holder.mSelectedRegionCheckBox.isChecked());
                    mSelectedId = regionData.getId();
                    if(mSelectedId == -1 && regionData.getSelected()) {
                        if(regionData.getSelected()) {
                            for(int i = 1; i < mRegionsData.size(); i++) {
                                mRegionsData.get(i).setSelected(true);
                            }
                            notifyDataSetChanged();
                        }

                        SearchQueryInputData searchQueryInputData = new SearchQueryInputData(FetchSearchQueryInputTask.SEARCH_QUERY_REGIONS_ID,"regions",String.valueOf(regionData.getId()));
                        mContext.getContentResolver().update(SearchQueryInputContract.SearchQueryInputEntry.CONTENT_URI,searchQueryInputData.getSearchQueryInputContentValues(),
                                SearchQueryInputContract.SearchQueryInputEntry.COLUMN_QUERY_ID + " = " + String.valueOf(FetchSearchQueryInputTask.SEARCH_QUERY_REGIONS_ID), null);

                    }else {
                        StringBuilder selectedValues = new StringBuilder();
                        for(int i = 1; i < mRegionsData.size(); i++) {
                            if(mRegionsData.get(i).getSelected()) {
                                if(!selectedValues.toString().trim().isEmpty())
                                    selectedValues.append(",");
                                selectedValues.append(String.valueOf(mRegionsData.get(i).getId()));
                            }
                        }

                        SearchQueryInputData searchQueryInputData = new SearchQueryInputData(FetchSearchQueryInputTask.SEARCH_QUERY_REGIONS_ID,"regions",selectedValues.toString());
                        mContext.getContentResolver().update(SearchQueryInputContract.SearchQueryInputEntry.CONTENT_URI,searchQueryInputData.getSearchQueryInputContentValues(),
                                SearchQueryInputContract.SearchQueryInputEntry.COLUMN_QUERY_ID + " = " + String.valueOf(FetchSearchQueryInputTask.SEARCH_QUERY_REGIONS_ID), null);

                    }                }
            });

            holder.mSelectedRegionCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.itemView.performClick();
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return (mRegionsData == null ? 0 : mRegionsData.size());
    }

    public void setRegionData(List<RegionData> regionData) {
        mRegionsData = regionData;

        String[] selectedRegionId = null;

        Cursor cursor = mContext.getContentResolver().query(SearchQueryInputContract.SearchQueryInputEntry.CONTENT_URI,null,
                SearchQueryInputContract.SearchQueryInputEntry.COLUMN_QUERY_ID + " = " + String.valueOf(FetchSearchQueryInputTask.SEARCH_QUERY_REGIONS_ID), null,null);
        if(cursor != null && cursor.moveToFirst()) {
            List<SearchQueryInputData> searchQueryInputDataList = CursorAndDataConverter.getSearchQueryInputDataFromCursor(cursor);
            if(searchQueryInputDataList != null && searchQueryInputDataList.size() > 0) {
                String value = searchQueryInputDataList.get(0).getValue().trim();
                selectedRegionId = value.split(",",-1);
            }
        }
        if(cursor != null)
            cursor.close();

        if(mRegionsData != null && mRegionsData.size() > 0) {
            if(selectedRegionId.length > 0) {
                if(selectedRegionId[0].equalsIgnoreCase("-1")) {
                    for (int i = 0; i < mRegionsData.size(); i++) {
                        mRegionsData.get(i).setSelected(true);
                    }
                }else {
                    for (int i = 0; i < mRegionsData.size(); i++) {
                        for(int x = 0; x < selectedRegionId.length; x++) {
                            if(!selectedRegionId[x].trim().isEmpty()) {
                                if (mRegionsData.get(i).getId() == Integer.valueOf(selectedRegionId[x].trim())) {
                                    mRegionsData.get(i).setSelected(true);
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
