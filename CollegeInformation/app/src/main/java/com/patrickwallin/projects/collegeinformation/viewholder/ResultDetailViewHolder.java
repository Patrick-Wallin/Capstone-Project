package com.patrickwallin.projects.collegeinformation.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by piwal on 7/1/2017.
 */

public class ResultDetailViewHolder extends RecyclerView.ViewHolder {
    public ResultDetailViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
