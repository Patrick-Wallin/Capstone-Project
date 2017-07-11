package com.patrickwallin.projects.collegeinformation.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.patrickwallin.projects.collegeinformation.R;
import com.patrickwallin.projects.collegeinformation.data.FavoriteCollegeContract;
import com.patrickwallin.projects.collegeinformation.data.FavoriteCollegeData;

/**
 * Created by piwal on 7/8/2017.
 */

public class CollegeWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String TAG = "CollegeWidgetRemoteViewsFactory";

    private Cursor mCursorFavoriteCollegeData = null;
    private Context mContext;

    public CollegeWidgetRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {}

    @Override
    public void onDataSetChanged() {
        if(mCursorFavoriteCollegeData != null)
            mCursorFavoriteCollegeData.close();
/*
        String[] projection = new String[] {
                FavoriteCollegeContract.FavoriteCollegeEntry._ID,
                FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_NAME,
                FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_CITY,
                FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_STATE };
*/
        final long identityToken = Binder.clearCallingIdentity();

        mCursorFavoriteCollegeData = mContext.getContentResolver().query(FavoriteCollegeContract.FavoriteCollegeEntry.CONTENT_URI,null, null, null, null);

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if(mCursorFavoriteCollegeData != null) {
            mCursorFavoriteCollegeData.close();
            mCursorFavoriteCollegeData = null;
        }

    }

    @Override
    public int getCount() {
        return (mCursorFavoriteCollegeData != null ? mCursorFavoriteCollegeData.getCount() : 0);
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if(position == AdapterView.INVALID_POSITION ||
                mCursorFavoriteCollegeData == null || !mCursorFavoriteCollegeData.moveToPosition(position)) {
            return null;
        }

        mCursorFavoriteCollegeData.moveToPosition(position);
        FavoriteCollegeData favoriteCollegeData = new FavoriteCollegeData(mCursorFavoriteCollegeData);

        RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.college_widget_list_item);
        view.setTextViewText(R.id.college_name_text_view,favoriteCollegeData.getName());
        view.setTextViewText(R.id.college_city_text_view,favoriteCollegeData.getCity());
        view.setTextViewText(R.id.college_state_text_view,favoriteCollegeData.getState());

        Intent intent = new Intent();
        intent.putExtra(mContext.getString(R.string.id),favoriteCollegeData.getId());
        view.setOnClickFillInIntent(R.id.widget_row_item,intent);

        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        if(mCursorFavoriteCollegeData.moveToPosition(position)) {
            // do get id?
        }
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
