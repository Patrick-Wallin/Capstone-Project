package com.patrickwallin.projects.collegeinformation.asynctask;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.patrickwallin.projects.collegeinformation.R;
import com.patrickwallin.projects.collegeinformation.adapter.NamesAdapter;
import com.patrickwallin.projects.collegeinformation.data.NameContract;
import com.patrickwallin.projects.collegeinformation.data.NameData;
import com.patrickwallin.projects.collegeinformation.utilities.AsyncListener;
import com.patrickwallin.projects.collegeinformation.utilities.CursorAndDataConverter;

import java.util.List;

/**
 * Created by piwal on 6/25/2017.
 */

public class FetchCollegeNamesTask extends AsyncTask<Void, Void, List<NameData>> {
    private Context mContext;
    private NamesAdapter mNamesAdapter;
    private AsyncListener listener;

    public FetchCollegeNamesTask(Context context, NamesAdapter namesAdapter, AsyncListener listener) {
        mContext = context;
        mNamesAdapter = namesAdapter;
        this.listener = listener;
    }

    @Override
    protected List<NameData> doInBackground(Void... params) {
        Cursor programCursor;
        List<NameData> nameDataList = null;

        programCursor = mContext.getContentResolver().query(
                NameContract.NameEntry.CONTENT_URI,
                null,
                null,
                null,
                NameContract.NameEntry.COLUMN_NAME_NAME);
        if(programCursor != null && programCursor.moveToFirst())
            nameDataList = CursorAndDataConverter.getNameDataFromCursor(programCursor);
        if(programCursor != null)
            programCursor.close();

        return nameDataList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.returnString(mContext.getString(R.string.loadingandwait));
    }

    @Override
    protected void onPostExecute(List<NameData> nameDatas) {
        if(nameDatas != null) {
            mNamesAdapter.setNameData(nameDatas);
        }
        listener.closeScreen();
    }
}
