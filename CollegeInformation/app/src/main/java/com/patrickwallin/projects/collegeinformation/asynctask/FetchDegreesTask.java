package com.patrickwallin.projects.collegeinformation.asynctask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.patrickwallin.projects.collegeinformation.MainActivity;
import com.patrickwallin.projects.collegeinformation.R;
import com.patrickwallin.projects.collegeinformation.SearchDegreesActivity;
import com.patrickwallin.projects.collegeinformation.SearchDegreesActivityFragment;
import com.patrickwallin.projects.collegeinformation.adapter.DegreesAdapter;
import com.patrickwallin.projects.collegeinformation.data.DegreeContract;
import com.patrickwallin.projects.collegeinformation.data.DegreesData;
import com.patrickwallin.projects.collegeinformation.data.VersionContract;
import com.patrickwallin.projects.collegeinformation.data.VersionData;
import com.patrickwallin.projects.collegeinformation.utilities.AsyncListener;
import com.patrickwallin.projects.collegeinformation.utilities.CursorAndDataConverter;
import com.patrickwallin.projects.collegeinformation.utilities.NetworkUtils;
import com.patrickwallin.projects.collegeinformation.utilities.OpenJsonUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

        degreeCursor = getCursor();

        if(degreeCursor != null && degreeCursor.moveToFirst())
            return CursorAndDataConverter.getDegreeDataFromCursor(degreeCursor);

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        listener.returnString("Loading data... Please wait.");
    }

    @Override
    protected void onPostExecute(List<DegreesData> degreesDatas) {
        if(degreesDatas != null) {
            mDegreeAdapter.setDegreeData(degreesDatas);
        }
        listener.closeScreen();
    }
}
