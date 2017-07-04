package com.patrickwallin.projects.collegeinformation.asynctask;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.patrickwallin.projects.collegeinformation.adapter.DegreesAdapter;
import com.patrickwallin.projects.collegeinformation.data.DegreeContract;
import com.patrickwallin.projects.collegeinformation.data.DegreesData;
import com.patrickwallin.projects.collegeinformation.utilities.CursorAndDataConverter;
import com.patrickwallin.projects.collegeinformation.utilities.OpenJsonUtils;

import java.io.File;
import java.util.List;

/**
 * Created by piwal on 6/19/2017.
 */

public class FetchDegreesTask extends AsyncTask<Void, Void, List<DegreesData>> {
    private Context mContext;
    private DegreesAdapter mDegreeAdapter;

    public FetchDegreesTask(Context context, DegreesAdapter degreesAdapter) {
        mContext = context;
        mDegreeAdapter= degreesAdapter;
    }

    @Override
    protected List<DegreesData> doInBackground(Void... params) {
        Cursor degreeCursor;

        degreeCursor = mContext.getContentResolver().query(
                DegreeContract.DegreeEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        if(degreeCursor != null && degreeCursor.moveToFirst())
            return CursorAndDataConverter.getDegreeDataFromCursor(degreeCursor);

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<DegreesData> degreesDatas) {
        if(degreesDatas != null) {
            mDegreeAdapter.setDegreeData(degreesDatas);
        }
    }
}
