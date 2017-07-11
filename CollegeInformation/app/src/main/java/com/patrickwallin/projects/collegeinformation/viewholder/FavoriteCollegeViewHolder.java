package com.patrickwallin.projects.collegeinformation.viewholder;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.patrickwallin.projects.collegeinformation.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by piwal on 6/22/2017.
 */

public class FavoriteCollegeViewHolder extends RecyclerView.ViewHolder {
    @Nullable
    @BindView(R.id.college_image_view) public ImageView mCollegeImageView;
    @BindView(R.id.college_name_text_view) public TextView mCollegeNameTextView;
    @BindView(R.id.college_city_text_view) public TextView mCollegeCityTextView;

    public FavoriteCollegeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
