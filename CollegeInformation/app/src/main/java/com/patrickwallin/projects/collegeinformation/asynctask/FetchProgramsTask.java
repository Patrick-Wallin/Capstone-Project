package com.patrickwallin.projects.collegeinformation.asynctask;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.patrickwallin.projects.collegeinformation.R;
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
        List<ProgramData> programDataList = null;

        programCursor = mContext.getContentResolver().query(
                ProgramContract.ProgramEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        if(programCursor != null && programCursor.moveToFirst())
            programDataList = CursorAndDataConverter.getProgramDataFromCursor(programCursor);
        if(programCursor != null)
            programCursor.close();

        return programDataList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.returnString(mContext.getString(R.string.loadingandwait));
    }

    @Override
    protected void onPostExecute(List<ProgramData> programDatas) {
        if(programDatas != null) {
            mProgramsAdapter.setProgramData(programDatas);
        }
        listener.closeScreen();
    }
}
