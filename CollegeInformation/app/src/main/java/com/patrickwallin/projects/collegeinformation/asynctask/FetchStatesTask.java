package com.patrickwallin.projects.collegeinformation.asynctask;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.patrickwallin.projects.collegeinformation.R;
import com.patrickwallin.projects.collegeinformation.adapter.StatesAdapter;
import com.patrickwallin.projects.collegeinformation.data.StateData;
import com.patrickwallin.projects.collegeinformation.data.StatesContract;
import com.patrickwallin.projects.collegeinformation.utilities.AsyncListener;
import com.patrickwallin.projects.collegeinformation.utilities.CursorAndDataConverter;

import java.util.List;

/**
 * Created by piwal on 6/25/2017.
 */

public class FetchStatesTask extends AsyncTask<Void, Void, List<StateData>> {
    private Context mContext;
    private StatesAdapter mStatesAdapter;
    private AsyncListener listener;

    public FetchStatesTask(Context context, StatesAdapter statesAdapter, AsyncListener listener) {
        mContext = context;
        mStatesAdapter = statesAdapter;
        this.listener = listener;
    }

    @Override
    protected List<StateData> doInBackground(Void... params) {
        Cursor stateCursor;
        List<StateData> stateDataList = null;

        stateCursor = mContext.getContentResolver().query(
                StatesContract.StateEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        if(stateCursor != null && stateCursor.moveToFirst())
            stateDataList = CursorAndDataConverter.getStateDataFromCursor(stateCursor);
        if(stateCursor != null)
            stateCursor.close();

        return stateDataList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.returnString(mContext.getString(R.string.loadingandwait));
    }

    @Override
    protected void onPostExecute(List<StateData> stateDatas) {
        if(stateDatas != null) {
            mStatesAdapter.setStateData(stateDatas);
        }
        listener.closeScreen();
    }
}
