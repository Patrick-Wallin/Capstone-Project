package com.patrickwallin.projects.collegeinformation.asynctask;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.patrickwallin.projects.collegeinformation.adapter.RegionsAdapter;
import com.patrickwallin.projects.collegeinformation.adapter.StatesAdapter;
import com.patrickwallin.projects.collegeinformation.data.RegionData;
import com.patrickwallin.projects.collegeinformation.data.RegionsContract;
import com.patrickwallin.projects.collegeinformation.data.StateData;
import com.patrickwallin.projects.collegeinformation.data.StatesContract;
import com.patrickwallin.projects.collegeinformation.utilities.CursorAndDataConverter;

import java.util.List;

/**
 * Created by piwal on 6/25/2017.
 */

public class FetchRegionsTask extends AsyncTask<Void, Void, List<RegionData>> {
    private Context mContext;
    private RegionsAdapter mRegionsAdapter;

    public FetchRegionsTask(Context context, RegionsAdapter regionsAdapter) {
        mContext = context;
        mRegionsAdapter = regionsAdapter;
    }

    @Override
    protected List<RegionData> doInBackground(Void... params) {
        Cursor regionCursor;
        List<RegionData> regionDataList = null;

        regionCursor = mContext.getContentResolver().query(
                RegionsContract.RegionEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        if(regionCursor != null && regionCursor.moveToFirst())
            regionDataList = CursorAndDataConverter.getRegionDataFromCursor(regionCursor);
        if(regionCursor != null)
            regionCursor.close();

        return regionDataList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<RegionData> regionDatas) {
        if(regionDatas != null) {
            mRegionsAdapter.setRegionData(regionDatas);
        }
    }
}
