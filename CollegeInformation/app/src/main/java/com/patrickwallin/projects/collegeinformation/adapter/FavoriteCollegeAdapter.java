package com.patrickwallin.projects.collegeinformation.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.patrickwallin.projects.collegeinformation.OnResultOptionSelectionChangeListener;
import com.patrickwallin.projects.collegeinformation.R;
import com.patrickwallin.projects.collegeinformation.ResultDetailActivity;
import com.patrickwallin.projects.collegeinformation.data.FavoriteCollegeData;
import com.patrickwallin.projects.collegeinformation.data.NameContract;
import com.patrickwallin.projects.collegeinformation.data.NameData;
import com.patrickwallin.projects.collegeinformation.data.ProgramData;
import com.patrickwallin.projects.collegeinformation.utilities.NetworkUtils;
import com.patrickwallin.projects.collegeinformation.utilities.OpenJsonUtils;
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

    public void updateImageLink(String imageLink, int position, int id) {
        mFavoriteCollegeData.get(position).setImageLink(imageLink);
        String sqlWhere = NameContract.NameEntry.COLUMN_NAME_ID + " = " + String.valueOf(id);
        Cursor cursorName = mContext.getContentResolver().query(NameContract.NameEntry.CONTENT_URI,null,sqlWhere,null,null);
        if(cursorName != null && cursorName.moveToFirst()) {
            NameData nameData = new NameData(cursorName);
            ContentValues cv = nameData.getNamesContentValues();
            cv.put(NameContract.NameEntry.COLUMN_NAME_IMAGE_LINK,imageLink);
            mContext.getContentResolver().update(NameContract.NameEntry.CONTENT_URI,cv,sqlWhere,null);
        }


    }

    @Override
    public FavoriteCollegeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_item_activity, parent, false);
        FavoriteCollegeViewHolder favoriteCollegeViewHolder = new FavoriteCollegeViewHolder(v);
        return favoriteCollegeViewHolder;
    }

    @Override
    public void onBindViewHolder(final FavoriteCollegeViewHolder holder, final int position) {
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

                String imageLink = "";
                String sqlWhere = NameContract.NameEntry.COLUMN_NAME_ID + " = " + String.valueOf(favoriteCollegeData.getId());
                Cursor cursorName = mContext.getContentResolver().query(NameContract.NameEntry.CONTENT_URI,null,sqlWhere,null,null);
                if(cursorName != null && cursorName.moveToFirst()) {
                    NameData nameData = new NameData(cursorName);
                    imageLink = nameData.getImageLink().trim();
                }
                if(imageLink.isEmpty()) {
                    NetworkUtils networkUtils = new NetworkUtils(mContext);
                    String urlString = networkUtils.buildSchoolQuery();

                    AndroidNetworking.get(urlString)
                            .addPathParameter("unitid", String.valueOf(favoriteCollegeData.getId()))
                            .setPriority(Priority.LOW)
                            .build()
                            .getAsString(new StringRequestListener() {
                                @Override
                                public void onResponse(String response) {
                                    if (response != null && !response.trim().isEmpty()) {
                                        String imageLink = OpenJsonUtils.getImageLinkFromJson(response);
                                        updateImageLink(imageLink, position, favoriteCollegeData.getId());
                                        if (!imageLink.trim().isEmpty()) {
                                            Picasso.with(mContext)
                                                    .load(imageLink)
                                                    .placeholder(R.drawable.no_image_available_building)
                                                    .into(holder.mCollegeImageView);
                                        }
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {

                                    // fail!  do nothing since it might not be existed!
                                }
                            });
                }else {
                    mFavoriteCollegeData.get(position).setImageLink(imageLink);
                    Picasso.with(mContext)
                            .load(imageLink)
                            .placeholder(R.drawable.no_image_available_building)
                            .into(holder.mCollegeImageView);
                }
            }

            String transitionPhotoName = mContext.getResources().getString(R.string.transition_photo).trim() + String.valueOf(favoriteCollegeData.getId()).trim();
            holder.mCollegeImageView.setTransitionName(transitionPhotoName);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundleResult = new Bundle();
                    bundleResult.putParcelable("resultdetailinfo", Parcels.wrap(favoriteCollegeData));

                    //Intent intentResultDetailActivity = new Intent(mContext, ResultDetailActivity.class);
                    //intentResultDetailActivity.putExtras(bundleResult);
                    //mContext.startActivity(intentResultDetailActivity);

                    OnResultOptionSelectionChangeListener listener = (OnResultOptionSelectionChangeListener) mContext;
                    listener.OnSelectionChanged(bundleResult,holder.mCollegeImageView);
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
