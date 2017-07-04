package com.patrickwallin.projects.collegeinformation.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patrickwallin.projects.collegeinformation.OnResultOptionSelectionChangeListener;
import com.patrickwallin.projects.collegeinformation.R;
import com.patrickwallin.projects.collegeinformation.ResultDetailActivity;
import com.patrickwallin.projects.collegeinformation.data.FavoriteCollegeData;
import com.patrickwallin.projects.collegeinformation.data.NameData;
import com.patrickwallin.projects.collegeinformation.data.ProgramData;
import com.patrickwallin.projects.collegeinformation.viewholder.FavoriteCollegeViewHolder;
import com.patrickwallin.projects.collegeinformation.viewholder.SearchNamesViewHolder;
import com.patrickwallin.projects.collegeinformation.viewholder.SearchProgramsViewHolder;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;
import java.util.jar.Attributes;

/**
 * Created by piwal on 6/22/2017.
 */

public class FavoriteCollegeAdapter extends RecyclerView.Adapter<FavoriteCollegeViewHolder> {
    private List<FavoriteCollegeData> mFavoriteCollegeData;
    private Context mContext;

    public FavoriteCollegeAdapter(List<FavoriteCollegeData> favoriteCollegeDatas, Context context) {
        mFavoriteCollegeData = favoriteCollegeDatas;
        mContext = context;
    }

    @Override
    public FavoriteCollegeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_item_activity, parent, false);
        FavoriteCollegeViewHolder favoriteCollegeViewHolder = new FavoriteCollegeViewHolder(v);
        return favoriteCollegeViewHolder;
    }

    @Override
    public void onBindViewHolder(FavoriteCollegeViewHolder holder, int position) {
        if(mFavoriteCollegeData != null && position >= 0) {
            final FavoriteCollegeData favoriteCollegeData = mFavoriteCollegeData.get(position);
            holder.mCollegeNameTextView.setText(favoriteCollegeData.getName());
            holder.mCollegeCityTextView.setText(favoriteCollegeData.getCity());
            if(!favoriteCollegeData.getImageLink().trim().isEmpty()) {
                Picasso.with(mContext)
                        .load(favoriteCollegeData.getImageLink().trim())
                        .placeholder(R.drawable.no_image_available_building)
                        .into(holder.mCollegeImageView);
            }else {
                Picasso.with(mContext)
                        .load(R.drawable.no_image_available_building)
                        .into(holder.mCollegeImageView);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundleResult = new Bundle();
                    bundleResult.putParcelable("resultdetailinfo", Parcels.wrap(favoriteCollegeData));

                    //Intent intentResultDetailActivity = new Intent(mContext, ResultDetailActivity.class);
                    //intentResultDetailActivity.putExtras(bundleResult);
                    //mContext.startActivity(intentResultDetailActivity);

                    OnResultOptionSelectionChangeListener listener = (OnResultOptionSelectionChangeListener) mContext;
                    listener.OnSelectionChanged(bundleResult);
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
        return (mFavoriteCollegeData == null ? 0 : mFavoriteCollegeData.size());
    }

    public void setFavoriteData(List<FavoriteCollegeData> favoriteData) {
        mFavoriteCollegeData = favoriteData;
        notifyDataSetChanged();
    }

    public List<FavoriteCollegeData> getFavoriteCollegeData() {
        return mFavoriteCollegeData;
    }
}
