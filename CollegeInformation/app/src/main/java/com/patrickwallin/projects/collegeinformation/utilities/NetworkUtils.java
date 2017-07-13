package com.patrickwallin.projects.collegeinformation.utilities;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.patrickwallin.projects.collegeinformation.OnGoBackChangeListener;
import com.patrickwallin.projects.collegeinformation.R;
import com.patrickwallin.projects.collegeinformation.data.DegreeContract;
import com.patrickwallin.projects.collegeinformation.data.NameContract;
import com.patrickwallin.projects.collegeinformation.data.NameData;
import com.patrickwallin.projects.collegeinformation.data.ProgramContract;
import com.patrickwallin.projects.collegeinformation.data.ProgramData;
import com.patrickwallin.projects.collegeinformation.data.RegionsContract;
import com.patrickwallin.projects.collegeinformation.data.SearchQueryInputContract;
import com.patrickwallin.projects.collegeinformation.data.SearchQueryInputData;
import com.patrickwallin.projects.collegeinformation.data.StatesContract;
import com.patrickwallin.projects.collegeinformation.data.VersionContract;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

/**
 * Created by piwal on 6/4/2017.
 */

public class NetworkUtils  {
    private Context mContext;

    public NetworkUtils(Context context) {
        mContext = context;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void showAlertMessageAboutNoInternetConnection(final boolean goBackToPreviousActivity) {
        new AlertDialog.Builder(mContext)
                .setTitle(mContext.getString(R.string.no_internet_connection_title))
                .setMessage(mContext.getString(R.string.no_internet_connection_message))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(goBackToPreviousActivity) {
                            OnGoBackChangeListener listener = (OnGoBackChangeListener) mContext;
                            listener.OnGoBackChanged();
                        }
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert).show();
    }

    public URL buildUrl(String inputAddress) {
        String uriAddress = inputAddress;

        Uri builtUri;

        builtUri = Uri.parse(uriAddress).buildUpon().build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput) {
                return scanner.next();
            }else {
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }


    public StorageReference getStorageReference(String jsonName) {
        StringBuilder jsonFileName = new StringBuilder();
        jsonFileName.append(mContext.getString(R.string.firebase_uri_address));
        jsonFileName.append(jsonName.trim());
        jsonFileName.append(".");
        jsonFileName.append(mContext.getString(R.string.json_ext));

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(jsonFileName.toString());

        return storageRef;
    }


    public String buildSchoolQuery() {
        String urlString = "";

        StringBuilder sql = new StringBuilder();
        sql.append(mContext.getString(R.string.url_specific_school_nearbycolleges));

        urlString = sql.toString();

        return urlString;
    }

    public String buildQueryBasedOnQueryInput(boolean totalOnly) {
        String urlString = "";

        StringBuilder sqlWhere = new StringBuilder();
        StringBuilder sqlNameWhere = new StringBuilder();

        Cursor cursor = mContext.getContentResolver().query(SearchQueryInputContract.SearchQueryInputEntry.CONTENT_URI,null,null, null,null);
        if(cursor != null && cursor.moveToFirst()) {
            List<SearchQueryInputData> searchQueryInputDataList = CursorAndDataConverter.getSearchQueryInputDataFromCursor(cursor);
            if (searchQueryInputDataList != null && searchQueryInputDataList.size() > 0) {
                for(int i = 0; i < searchQueryInputDataList.size(); i++) {
                    SearchQueryInputData searchQueryInputData = searchQueryInputDataList.get(i);
                    if(searchQueryInputData.getName().equalsIgnoreCase(DegreeContract.DegreeEntry.TABLE_NAME)) {
                        String value = searchQueryInputData.getValue().trim();
                        if(!value.isEmpty()) {
                            int highestDegreesAwarded = Integer.valueOf(value);
                            if (highestDegreesAwarded > -1) {
                                if (!sqlWhere.toString().trim().isEmpty()) {
                                    sqlWhere.append("&");
                                }
                                sqlWhere.append(mContext.getString(R.string.query_school_degrees));
                                sqlWhere.append("=");
                                sqlWhere.append(value);
                            }
                        }
                    }else {
                        if(searchQueryInputData.getName().equalsIgnoreCase(ProgramContract.ProgramEntry.TABLE_NAME)) {
                            String value = searchQueryInputData.getValue().trim();
                            if(!value.isEmpty()) {
                                int programId = Integer.valueOf(value);
                                if (programId > -1) {
                                    if (!sqlWhere.toString().trim().isEmpty()) {
                                        sqlWhere.append("&");
                                    }
                                    String programSQLWhere = ProgramContract.ProgramEntry.COLUMN_PROGRAM_ID + " = " + String.valueOf(programId);
                                    String programUrlAddress = "";
                                    Cursor cursorProgram = mContext.getContentResolver().query(ProgramContract.ProgramEntry.CONTENT_URI,null,programSQLWhere,null,null);
                                    if(cursorProgram != null && cursorProgram.moveToFirst()) {
                                        ProgramData programData = new ProgramData(cursorProgram);
                                        programUrlAddress = programData.getUrlName().trim();
                                    }
                                    if(cursorProgram != null)
                                        cursorProgram.close();

                                    String latestYearData = mContext.getString(R.string.latest_year_data);

                                    String programPercentage = mContext.getString(R.string.field_collegescorecard_percentage);

                                    sqlWhere.append(latestYearData);
                                    sqlWhere.append(".");
                                    sqlWhere.append(programPercentage);
                                    sqlWhere.append(programUrlAddress);
                                    sqlWhere.append(mContext.getString(R.string.field_collegescorecard_not_equal_to_zero));
                                }
                            }

                        }else {
                            if(searchQueryInputData.getName().equalsIgnoreCase(NameContract.NameEntry.TABLE_NAME)) {
                                String value = searchQueryInputData.getValue().trim();
                                if(!value.isEmpty()) {
                                    int nameId = Integer.valueOf(value);
                                    if (nameId > -1) {
                                        if (!sqlNameWhere.toString().trim().isEmpty()) {
                                            sqlNameWhere.append("&");
                                        }
                                        sqlNameWhere.append(mContext.getString(R.string.query_school_id));
                                        sqlNameWhere.append("=");
                                        sqlNameWhere.append(String.valueOf(nameId));
                                    }
                                }
                            }else {
                                if(searchQueryInputData.getName().equalsIgnoreCase(StatesContract.StateEntry.TABLE_NAME)) {
                                    String value = searchQueryInputData.getValue().trim();
                                    if (!value.isEmpty()) {
                                        String[] splitValue = value.split(",");
                                        boolean bAny = false;
                                        for (int iStates = 0; iStates < splitValue.length; iStates++) {
                                            if (splitValue[iStates].trim().equals("-1")) {
                                                bAny = true;
                                                break;
                                            }
                                        }
                                        if (!bAny) {
                                            if (!sqlWhere.toString().trim().isEmpty()) {
                                                sqlWhere.append("&");
                                            }
                                            sqlWhere.append(mContext.getString(R.string.query_school_states));
                                            sqlWhere.append("=");
                                            sqlWhere.append(value);
                                        }
                                    }
                                }else {
                                    if(searchQueryInputData.getName().equalsIgnoreCase(RegionsContract.RegionEntry.TABLE_NAME)) {
                                        String value = searchQueryInputData.getValue().trim();
                                        if(!value.isEmpty()) {
                                            String[] splitValue = value.split(",");
                                            boolean bAny = false;
                                            for (int iStates = 0; iStates < splitValue.length; iStates++) {
                                                if (splitValue[iStates].trim().equals("-1")) {
                                                    bAny = true;
                                                    break;
                                                }
                                            }
                                            if (!bAny) {
                                                if (!sqlWhere.toString().trim().isEmpty()) {
                                                    sqlWhere.append("&");
                                                }
                                                sqlWhere.append(mContext.getString(R.string.query_school_regions));
                                                sqlWhere.append("=");
                                                sqlWhere.append(value);
                                            }
                                        }

                                    }else {
                                        if(searchQueryInputData.getName().equalsIgnoreCase("zips")) {
                                            String value = searchQueryInputData.getValue().trim();
                                            if(!value.isEmpty()) {
                                                if (!sqlWhere.toString().trim().isEmpty()) {
                                                    sqlWhere.append("&");
                                                }
                                                sqlWhere.append(mContext.getString(R.string.query_school_zips));
                                                sqlWhere.append("=");
                                                sqlWhere.append(value);
                                            }

                                        }

                                    }
                                }
                            }


                        }


                    }

                }
            }

        }

        if(cursor != null)
            cursor.close();

        StringBuilder sqlSearch = new StringBuilder();
        sqlSearch.append(mContext.getString(R.string.url_data_gov));
        sqlSearch.append(mContext.getString(R.string.endpoint_get_all_data));
        sqlSearch.append("?");
        sqlSearch.append(mContext.getString(R.string.query_api_key));
        sqlSearch.append(mContext.getString(R.string.api_key));
        sqlSearch.append("&");
        if(!totalOnly)
            sqlSearch.append(mContext.getString(R.string.number_per_page));
        else
            sqlSearch.append(mContext.getString(R.string.number_per_page_one));
        sqlSearch.append("&");
        sqlSearch.append(mContext.getString(R.string.query_page_number));
        sqlSearch.append("0");
        if(!sqlNameWhere.toString().trim().isEmpty()) {
            sqlSearch.append("&");
            sqlSearch.append(sqlNameWhere.toString());
        }else {
            if (!sqlWhere.toString().trim().isEmpty()) {
                sqlSearch.append("&");
                sqlSearch.append(sqlWhere.toString());
            }
        }
        sqlSearch.append("&");
        if(!totalOnly)
            sqlSearch.append(mContext.getString(R.string.fields_for_results));
        else
            sqlSearch.append(mContext.getString(R.string.fields_for_getting_total_records));

        return sqlSearch.toString();
    }

    public String modifyPageNumberInQueryBasedOnPageNumber(String query, int pageNumber) {
        if(query != null && !query.trim().isEmpty()) {
            String[] splitQuery = query.split("&",-1);
            String perPageValue = "";
            for(int i = 0; i < splitQuery.length; i++) {
                if(splitQuery[i].trim().startsWith(mContext.getString(R.string.query_page_number))) {
                    perPageValue = splitQuery[i];
                    break;
                }
            }
            if(!perPageValue.isEmpty())
                query = query.replaceAll(perPageValue,mContext.getString(R.string.query_page_number)+String.valueOf(pageNumber));
        }
        return query;
    }

}
