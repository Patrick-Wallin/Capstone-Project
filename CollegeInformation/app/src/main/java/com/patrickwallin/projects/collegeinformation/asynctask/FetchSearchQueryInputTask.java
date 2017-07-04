package com.patrickwallin.projects.collegeinformation.asynctask;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.patrickwallin.projects.collegeinformation.data.DegreeContract;
import com.patrickwallin.projects.collegeinformation.data.NameContract;
import com.patrickwallin.projects.collegeinformation.data.ProgramContract;
import com.patrickwallin.projects.collegeinformation.data.RegionsContract;
import com.patrickwallin.projects.collegeinformation.data.SearchQueryInputContract;
import com.patrickwallin.projects.collegeinformation.data.SearchQueryInputData;
import com.patrickwallin.projects.collegeinformation.data.StatesContract;
import com.patrickwallin.projects.collegeinformation.data.VersionContract;
import com.patrickwallin.projects.collegeinformation.data.VersionData;
import com.patrickwallin.projects.collegeinformation.firebasestorage.FirebaseStorageProcessor;
import com.patrickwallin.projects.collegeinformation.utilities.NetworkUtils;
import com.patrickwallin.projects.collegeinformation.utilities.OpenJsonUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by piwal on 6/10/2017.
 */

public class FetchSearchQueryInputTask extends AsyncTask<Void, Void, List<SearchQueryInputData>> {
    Context mContext;

    private List<String> SEARCH_INPUT_LIST = new ArrayList<String>();

    public static int SEARCH_QUERY_DEGREES_ID = 0;
    public static int SEARCH_QUERY_PROGRAMS_ID = 1;
    public static int SEARCH_QUERY_STATES_ID = 2;
    public static int SEARCH_QUERY_REGIONS_ID = 3;
    public static int SEARCH_QUERY_ZIPS_ID = 4;
    public static int SEARCH_QUERY_NAMES_ID = 5;

    public FetchSearchQueryInputTask(Context context) {
        mContext = context;
        SEARCH_INPUT_LIST.add("degrees");
        SEARCH_INPUT_LIST.add("programs");
        SEARCH_INPUT_LIST.add("states");
        SEARCH_INPUT_LIST.add("regions");
        SEARCH_INPUT_LIST.add("zips");
        SEARCH_INPUT_LIST.add("names");
    }

    @Override
    protected List<SearchQueryInputData> doInBackground(Void... params) {
        List<SearchQueryInputData> lSearchQueryInputDataResult = null;

        lSearchQueryInputDataResult = SearchQueryInputContract.getSearchQueryInputData(mContext);

        return lSearchQueryInputDataResult;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(final List<SearchQueryInputData> searchQueryInputDatas) {
        super.onPostExecute(searchQueryInputDatas);

        if(searchQueryInputDatas != null) {
            if(searchQueryInputDatas.size() >= 0) {
                for (int i = 0; i < searchQueryInputDatas.size(); i++) {
                    for(int x = 0; x < SEARCH_INPUT_LIST.size(); x++) {
                        if(SEARCH_INPUT_LIST.get(x).equalsIgnoreCase(searchQueryInputDatas.get(i).getName())) {
                            SEARCH_INPUT_LIST.remove(x);
                            break;
                        }
                    }
                }
            }

            if(SEARCH_INPUT_LIST.size() > 0) {
                for(int i = 0; i < SEARCH_INPUT_LIST.size(); i++) {
                    SearchQueryInputData searchQueryInputData = new SearchQueryInputData(i,SEARCH_INPUT_LIST.get(i),"");
                    ContentValues contentValues = searchQueryInputData.getSearchQueryInputContentValues();
                    mContext.getContentResolver().insert(SearchQueryInputContract.SearchQueryInputEntry.CONTENT_URI,contentValues);
                }
            }
        }

        //Intent intentSearchActivity = new Intent(mContext, MainSearchActivity.class);
        //startActivity(intentSearchActivity);

    }
}
