package com.patrickwallin.projects.collegeinformation.viewholder;

import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.patrickwallin.projects.collegeinformation.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by piwal on 6/22/2017.
 */

public class SearchNamesViewHolder extends RecyclerView.ViewHolder {
    @Nullable
    @BindView(R.id.name_name_text_view) public TextView mNameTextView;
    @BindView(R.id.name_city_text_view) public TextView mCityTextView;
    @BindView(R.id.name_state_text_view) public TextView mStateTextView;
    @BindView(R.id.name_zip_text_view) public TextView mZipTextView;

    public SearchNamesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
