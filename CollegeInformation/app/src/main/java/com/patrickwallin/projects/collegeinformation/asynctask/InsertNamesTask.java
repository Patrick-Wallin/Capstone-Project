package com.patrickwallin.projects.collegeinformation.asynctask;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.patrickwallin.projects.collegeinformation.adapter.NamesAdapter;
import com.patrickwallin.projects.collegeinformation.data.NameContract;
import com.patrickwallin.projects.collegeinformation.data.NameData;
import com.patrickwallin.projects.collegeinformation.utilities.CursorAndDataConverter;

import java.util.List;

/**
 * Created by piwal on 7/7/2017.
 */

public class InsertNamesTask extends AsyncTask<Void, Void, List<NameData>> {
    private List<NameData> mNameDataList;
    private Context mContext;
    private NamesAdapter mNamesAdapter;

    public InsertNamesTask(Context context, List<NameData> nameDataList, NamesAdapter namesAdapter) {
        mContext = context;
        mNameDataList = nameDataList;
        mNamesAdapter = namesAdapter;
    }


    @Override
    protected List<NameData> doInBackground(Void... params) {
        List<NameData> nameDataList = null;

        if (mNameDataList != null && !mNameDataList.isEmpty()) {
            for (int i = 0; i < mNameDataList.size(); i++) {
                mNameDataList.get(i).setImageLink("");
                mContext.getContentResolver().insert(NameContract.NameEntry.CONTENT_URI, mNameDataList.get(i).getNamesContentValues());
            }
        }

        Cursor programCursor;

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
    protected void onPostExecute(List<NameData> nameDatas) {
        if(nameDatas != null) {
            mNamesAdapter.setNameData(nameDatas);
        }
    }
}
