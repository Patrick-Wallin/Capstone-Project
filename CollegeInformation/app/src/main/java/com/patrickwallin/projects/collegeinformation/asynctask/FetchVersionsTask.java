package com.patrickwallin.projects.collegeinformation.asynctask;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;

import com.patrickwallin.projects.collegeinformation.BuildConfig;
import com.patrickwallin.projects.collegeinformation.MainSearchActivity;
import com.patrickwallin.projects.collegeinformation.R;
import com.patrickwallin.projects.collegeinformation.data.DegreeContract;
import com.patrickwallin.projects.collegeinformation.data.NameContract;
import com.patrickwallin.projects.collegeinformation.data.ProgramContract;
import com.patrickwallin.projects.collegeinformation.data.RegionsContract;
import com.patrickwallin.projects.collegeinformation.data.StatesContract;
import com.patrickwallin.projects.collegeinformation.data.VersionContract;
import com.patrickwallin.projects.collegeinformation.data.VersionData;
import com.patrickwallin.projects.collegeinformation.utilities.DebugInfo;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by piwal on 6/10/2017.
 */

public class FetchVersionsTask extends AsyncTask<Void, Void, List<VersionData>> {
    Context mContext;
    List<VersionData> mVersionDataListFromFirebase;

    private List<String> VERSION_LIST_TABLE = new ArrayList<String>();
    private List<Integer> VERSION_LIST_ID = new ArrayList<Integer>();

    public FetchVersionsTask(Context context, List<VersionData> versionDataList) {
        mContext = context;
        mVersionDataListFromFirebase = versionDataList;

        VERSION_LIST_TABLE.add(DegreeContract.PATH_DEGREES);
        VERSION_LIST_TABLE.add(ProgramContract.PATH_PROGRAMS);
        VERSION_LIST_TABLE.add(StatesContract.PATH_STATES);
        VERSION_LIST_TABLE.add(RegionsContract.PATH_REGIONS);
        VERSION_LIST_TABLE.add(NameContract.PATH_NAMES);

        VERSION_LIST_ID.add(VersionContract.VERSION_ID_DEGREES);
        VERSION_LIST_ID.add(VersionContract.VERSION_ID_PROGRAMS);
        VERSION_LIST_ID.add(VersionContract.VERSION_ID_STATES);
        VERSION_LIST_ID.add(VersionContract.VERSION_ID_REGIONS);
        VERSION_LIST_ID.add(VersionContract.VERSION_ID_NAMES);
    }

    @Override
    protected List<VersionData> doInBackground(Void... params) {
        List<VersionData> lVersionDataResult = null;

        DebugInfo di = null;
        if(BuildConfig.DEBUG) {
            di = new DebugInfo();
            di.start();
        }

        lVersionDataResult = VersionContract.getVersionData(mContext);
        final List<VersionData> versionDatas = lVersionDataResult;

        List<String> versionListTable = new ArrayList<String>();
        if(lVersionDataResult != null) {
            if(lVersionDataResult.size() >= 0) {
                for (int i = 0; i < lVersionDataResult.size(); i++) {
                    for(int x = 0; x < VERSION_LIST_TABLE.size(); x++) {
                        if(VERSION_LIST_TABLE.get(x).equalsIgnoreCase(lVersionDataResult.get(i).getTableName())) {
                            VERSION_LIST_TABLE.remove(x);
                            break;
                        }
                    }
                }
            }

            if(VERSION_LIST_TABLE.size() > 0) {
                for(int i = 0; i < VERSION_LIST_TABLE.size(); i++) {
                    lVersionDataResult.add(new VersionData(VERSION_LIST_ID.get(i),VERSION_LIST_TABLE.get(i),0,0));
                    ContentValues contentValues = lVersionDataResult.get(lVersionDataResult.size()-1).getVersionContentValues();
                    mContext.getContentResolver().insert(VersionContract.VersionEntry.CONTENT_URI,contentValues);
                }
            }
        }


        if(mVersionDataListFromFirebase != null && !mVersionDataListFromFirebase.isEmpty() && versionDatas != null && !versionDatas.isEmpty()) {
            for(int i = 0; i < versionDatas.size(); i++) {
                for(int x = 0; x < mVersionDataListFromFirebase.size(); x++) {
                    if(versionDatas.get(i).getTableName().equalsIgnoreCase(mVersionDataListFromFirebase.get(x).getTableName())) {
                        if(versionDatas.get(i).getVersionNumber() != mVersionDataListFromFirebase.get(x).getVersionNumber()) {
                            ContentValues cv = versionDatas.get(i).getVersionContentValues();
                            cv.put(VersionContract.VersionEntry.COLUMN_VERSION_NUMBER, mVersionDataListFromFirebase.get(x).getVersionNumber());
                            mContext.getContentResolver().update(VersionContract.VersionEntry.CONTENT_URI, cv, VersionContract.VersionEntry.COLUMN_VERSION_ID + " = " + versionDatas.get(i).getId(),null);
                        }
                        break;
                    }
                }
            }

        }

        if(BuildConfig.DEBUG) {
            di.end();
            Timber.i(mContext.getString(R.string.debugandtracktime), di.getMinAndSecElaspedTime());
        }

        return lVersionDataResult;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(final List<VersionData> versionDatas) {
        super.onPostExecute(versionDatas);

        Intent intentSearchActivity = new Intent(mContext, MainSearchActivity.class);
        mContext.startActivity(intentSearchActivity);
    }
}
