package com.patrickwallin.projects.collegeinformation.asynctask;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.patrickwallin.projects.collegeinformation.adapter.ProgramsAdapter;
import com.patrickwallin.projects.collegeinformation.data.ProgramContract;
import com.patrickwallin.projects.collegeinformation.data.ProgramData;
import com.patrickwallin.projects.collegeinformation.utilities.AsyncListener;
import com.patrickwallin.projects.collegeinformation.utilities.CursorAndDataConverter;

import java.util.List;

/**
 * Created by piwal on 6/22/2017.
 */

public class FetchProgramsTask extends AsyncTask<Void, Void, List<ProgramData>> {
    private Context mContext;
    private ProgramsAdapter mProgramsAdapter;
    private AsyncListener listener;

    public FetchProgramsTask(Context context, ProgramsAdapter programsAdapter, AsyncListener listener) {
        mContext = context;
        mProgramsAdapter = programsAdapter;
        this.listener = listener;
    }
    @Override
    protected List<ProgramData> doInBackground(Void... params) {
        Cursor programCursor;

        programCursor = mContext.getContentResolver().query(
                ProgramContract.ProgramEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        if(programCursor != null && programCursor.moveToFirst())
            return CursorAndDataConverter.getProgramDataFromCursor(programCursor);

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.returnString("Loading data... Please wait.");
    }

    @Override
    protected void onPostExecute(List<ProgramData> programDatas) {
        if(programDatas != null) {
            mProgramsAdapter.setProgramData(programDatas);
        }
        listener.closeScreen();
    }
}
