package com.patrickwallin.projects.collegeinformation.viewholder;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.patrickwallin.projects.collegeinformation.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by piwal on 6/4/2017.
 */

public class MainSearchPageViewHolder extends RecyclerView.ViewHolder {
    @Nullable @BindView(R.id.name_text_view) public TextView mNameTextView;
    @Nullable @BindView(R.id.search_image_view) public ImageView mSearchImageView;

    public MainSearchPageViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this,itemView);
    }
}
