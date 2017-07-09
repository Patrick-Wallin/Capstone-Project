package com.patrickwallin.projects.collegeinformation.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.patrickwallin.projects.collegeinformation.MainSearchActivityFragment;
import com.patrickwallin.projects.collegeinformation.OnSearchOptionSelectionChangeListener;
import com.patrickwallin.projects.collegeinformation.R;
import com.patrickwallin.projects.collegeinformation.ResultsActivity;
import com.patrickwallin.projects.collegeinformation.SearchDegreesActivity;
import com.patrickwallin.projects.collegeinformation.SearchLocationsActivity;
import com.patrickwallin.projects.collegeinformation.SearchNamesActivity;
import com.patrickwallin.projects.collegeinformation.SearchProgramsActivity;
import com.patrickwallin.projects.collegeinformation.asynctask.FetchResultTask;
import com.patrickwallin.projects.collegeinformation.data.MainSearchPage;
import com.patrickwallin.projects.collegeinformation.viewholder.MainSearchPageViewHolder;

import java.util.List;

import timber.log.Timber;

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

            /*
            DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();

            //displayMetrics.widthPixels // Width
            //displayMetrics.heightPixels
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;
            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, displayMetrics);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.height = (height - px) / 4; //height recycleviewer

            holder.itemView.setLayoutParams(params);
            */

            holder.mNameTextView.setText(mMainSearchPage.get(position).getName());
            holder.mSearchImageView.setImageResource(mMainSearchPage.get(position).getImageId());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnSearchOptionSelectionChangeListener listener = (OnSearchOptionSelectionChangeListener) mContext;
                    listener.OnSelectionChanged(mainSearchPage.getImageId());

                    /*
                    if(mainSearchPage.getImageId() == R.drawable.ic_graduation_cap) {
                        Intent intentDegreesActivity = new Intent(mContext, SearchDegreesActivity.class);
                        mContext.startActivity(intentDegreesActivity);
                    }else {
                        if(mainSearchPage.getImageId() == R.drawable.ic_college_book) {
                            Intent intentProgramActivity = new Intent(mContext, SearchProgramsActivity.class);
                            mContext.startActivity(intentProgramActivity);
                        }else {
                            if(mainSearchPage.getImageId() == R.drawable.ic_map_location) {
                                Intent intentLocationActivity = new Intent(mContext, SearchLocationsActivity.class);
                                mContext.startActivity(intentLocationActivity);
                            }else {
                                if(mainSearchPage.getImageId() == R.drawable.ic_college_building) {
                                    Intent intentNamesActivity = new Intent(mContext, SearchNamesActivity.class);
                                    mContext.startActivity(intentNamesActivity);
                                }
                            }
                        }
                    }
                    */
                    //Timber.d(mainSearchPage.getName());
                    Log.d("test","Test"+mainSearchPage.getName());
                }
            });




        }
    }

    @Override
    public int getItemCount() {
        return (mMainSearchPage == null ? 0 : mMainSearchPage.size());
    }
}
