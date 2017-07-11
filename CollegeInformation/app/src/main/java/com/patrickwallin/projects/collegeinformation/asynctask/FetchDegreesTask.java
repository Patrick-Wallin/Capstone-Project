package com.patrickwallin.projects.collegeinformation.asynctask;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.patrickwallin.projects.collegeinformation.R;
import com.patrickwallin.projects.collegeinformation.adapter.DegreesAdapter;
import com.patrickwallin.projects.collegeinformation.data.DegreeContract;
import com.patrickwallin.projects.collegeinformation.data.DegreesData;
import com.patrickwallin.projects.collegeinformation.utilities.AsyncListener;
import com.patrickwallin.projects.collegeinformation.utilities.CursorAndDataConverter;

import java.util.List;

/**
 * Created by piwal on 6/19/2017.
 */

public class FetchDegreesTask extends AsyncTask<Void, Void, List<DegreesData>> {
    private Context mContext;
    private DegreesAdapter mDegreeAdapter;

    private AsyncListener listener;

    public FetchDegreesTask(Context context, DegreesAdapter degreesAdapter, AsyncListener listener) {
        mContext = context;
        mDegreeAdapter= degreesAdapter;
        this.listener = listener;
    }

    private Cursor getCursor() {
        return mContext.getContentResolver().query(
                DegreeContract.DegreeEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    protected List<DegreesData> doInBackground(Void... params) {
        final Cursor degreeCursor;
        List<DegreesData> degreesDataList = null;

        degreeCursor = getCursor();

        if(degreeCursor != null && degreeCursor.moveToFirst())
            degreesDataList = CursorAndDataConverter.getDegreeDataFromCursor(degreeCursor);

        if(degreeCursor != null)
            degreeCursor.close();

        return degreesDataList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.returnString(mContext.getString(R.string.loadingandwait));
    }

    @Override
    protected void onPostExecute(List<DegreesData> degreesDatas) {
        if(degreesDatas != null) {
            mDegreeAdapter.setDegreeData(degreesDatas);
        }
        listener.closeScreen();
    }
}
