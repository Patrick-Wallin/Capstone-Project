package com.patrickwallin.projects.collegeinformation.viewholder;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.patrickwallin.projects.collegeinformation.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by piwal on 6/22/2017.
 */

public class SearchProgramsViewHolder extends RecyclerView.ViewHolder {
    @Nullable
    @BindView(R.id.programs_title_text_view) public TextView mTitleTextView;

    public SearchProgramsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
