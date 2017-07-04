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
 * Created by piwal on 6/6/2017.
 */

public class SearchDegreesViewHolder extends RecyclerView.ViewHolder {
    @Nullable
    @BindView(R.id.degrees_name_text_view) public TextView mNameTextView;
    @BindView(R.id.search_degrees_card_view) public CardView mSearchDegreeCardView;

    public SearchDegreesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
