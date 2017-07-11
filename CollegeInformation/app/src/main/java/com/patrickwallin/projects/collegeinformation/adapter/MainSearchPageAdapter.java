package com.patrickwallin.projects.collegeinformation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.patrickwallin.projects.collegeinformation.OnSearchOptionSelectionChangeListener;
import com.patrickwallin.projects.collegeinformation.R;
import com.patrickwallin.projects.collegeinformation.data.MainSearchPage;
import com.patrickwallin.projects.collegeinformation.viewholder.MainSearchPageViewHolder;

import java.util.List;

/**
 * Created by piwal on 6/4/2017.
 */

public class MainSearchPageAdapter extends RecyclerView.Adapter<MainSearchPageViewHolder> {
    List<MainSearchPage> mMainSearchPage;
    Context mContext;

    public MainSearchPageAdapter(List<MainSearchPage> mainSearchPage, Context context) {
        mMainSearchPage = mainSearchPage;
        mContext = context;
    }

    @Override
    public MainSearchPageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_search_cardview_activity, parent, false);
        MainSearchPageViewHolder mainSearchPageViewHolder = new MainSearchPageViewHolder(v);
        return mainSearchPageViewHolder;
    }

    @Override
    public void onBindViewHolder(MainSearchPageViewHolder holder, final int position) {
        if(mMainSearchPage != null && position >= 0) {
            final MainSearchPage mainSearchPage = mMainSearchPage.get(position);
            holder.mNameTextView.setText(mMainSearchPage.get(position).getName());
            holder.mSearchImageView.setImageResource(mMainSearchPage.get(position).getImageId());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnSearchOptionSelectionChangeListener listener = (OnSearchOptionSelectionChangeListener) mContext;
                    listener.OnSelectionChanged(mainSearchPage.getImageId());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (mMainSearchPage == null ? 0 : mMainSearchPage.size());
    }
}
