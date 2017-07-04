package com.patrickwallin.projects.collegeinformation.viewholder;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.patrickwallin.projects.collegeinformation.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by piwal on 6/25/2017.
 */

public class SearchLocationStatesViewHolder extends RecyclerView.ViewHolder {
    @Nullable
    @BindView(R.id.states_name_text_view) public TextView mNameTextView;
    @Nullable
    @BindView(R.id.selected_location_state_check_box) public CheckBox mSelectedLocationCheckBox;

    public SearchLocationStatesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
