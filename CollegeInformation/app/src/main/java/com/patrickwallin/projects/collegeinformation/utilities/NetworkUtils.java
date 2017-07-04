package com.patrickwallin.projects.collegeinformation.utilities;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.patrickwallin.projects.collegeinformation.R;
import com.patrickwallin.projects.collegeinformation.data.DegreeContract;
import com.patrickwallin.projects.collegeinformation.data.NameContract;
import com.patrickwallin.projects.collegeinformation.data.NameData;
import com.patrickwallin.projects.collegeinformation.data.ProgramContract;
import com.patrickwallin.projects.collegeinformation.data.SearchQueryInputContract;
import com.patrickwallin.projects.collegeinformation.data.SearchQueryInputData;
import com.patrickwallin.projects.collegeinformation.data.VersionContract;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by piwal on 6/4/2017.
 */

public class NetworkUtils {
    private Context mContext;

    public NetworkUtils(Context context) {
        mContext = context;
    }

    public URL buildUrl(String jsonName) {
        String uriAddress = "";
        /// /https://firebasestorage.googleapis.com/v0/b/college-information.appspot.com/o/degrees.json?alt=media&token=94a65e8d-81e5-43ae-b1fa-06d2303ec89d
        Uri builtUri;

        if(jsonName.equalsIgnoreCase(VersionContract.VersionEntry.TABLE_NAME)) {
            uriAddress =  "https://firebasestorage.googleapis.com/v0/b/college-information.appspot.com/o/versions.json?alt=media&token=ec657d0b-2d36-4eed-9ec4-470ee17b018e";
        }

        builtUri = Uri.parse(uriAddress).buildUpon().build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public void insertAllNamesIntoTable() {
        final StringBuilder uriAddress = new StringBuilder();
        uriAddress.append(mContext.getString(R.string.url_data_gov));
        uriAddress.append(mContext.getString(R.string.endpoint_get_all_data));
        uriAddress.append("?");
        uriAddress.append(mContext.getString(R.string.query_api_key));
        uriAddress.append(mContext.getString(R.string.api_key));
        uriAddress.append("&_sort=school.name:asc");
        uriAddress.append("&");
        uriAddress.append(mContext.getString(R.string.fields_for_names));
        uriAddress.append("&");
        uriAddress.append(mContext.getString(R.string.query_page_number));
        uriAddress.append("{pageNumber}");

        AndroidNetworking.get(uriAddress.toString())
                .addPathParameter("pageNumber","0")
                .setPriority(Priority.LOW)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if(response != null && !response.trim().isEmpty()) {
                            mContext.getContentResolver().delete(NameContract.NameEntry.CONTENT_URI, null, null);

                            int totalRecords = OpenJsonUtils.getTotalRecords(response);

                            if(totalRecords > 0) {
                                List<NameData> nameDataList = OpenJsonUtils.getNameDataFromJson(response);
                                if (nameDataList != null && !nameDataList.isEmpty()) {
                                    for (int i = 0; i < nameDataList.size(); i++) {
                                        mContext.getContentResolver().insert(NameContract.NameEntry.CONTENT_URI, nameDataList.get(i).getNamesContentValues());
                                    }
                                }

                                int pages = totalRecords/20;
                                for(int i = 1; i <pages; i++) {
                                    insertRecordIntoNameBasedOnPage(uriAddress.toString(),i);
                                }
                            }
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        int test = 1;

                    }
                });




    }

    public void insertRecordIntoNameBasedOnPage(String uriAddress, int pageNumber) {
        AndroidNetworking.get(uriAddress.toString())
                .addPathParameter("pageNumber",String.valueOf(pageNumber))
                .setPriority(Priority.LOW)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if(response != null && !response.trim().isEmpty()) {
                            List<NameData> nameDataList = OpenJsonUtils.getNameDataFromJson(response);
                            if (nameDataList != null && !nameDataList.isEmpty()) {
                                for (int i = 0; i < nameDataList.size(); i++) {
                                    mContext.getContentResolver().insert(NameContract.NameEntry.CONTENT_URI, nameDataList.get(i).getNamesContentValues());
                                }
                            }

                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        // fail!
                    }
                });
    }


    public String getResponseFromURLAddressString(String uriAddressValue) {
        String uriAddress = uriAddressValue;
        final String jsonValue = "";

        AndroidNetworking.get(uriAddress)
                .setPriority(Priority.LOW)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        //jsonValue = response; ??? not working due to final??

                    }

                    @Override
                    public void onError(ANError anError) {
                        // fail!
                    }
                });
        return jsonValue;
    }

    public StorageReference getStorageReference(String jsonName) {
        StringBuilder jsonFileName = new StringBuilder();
        jsonFileName.append("gs://college-information.appspot.com/");
        jsonFileName.append(jsonName.trim());
        jsonFileName.append(".json");

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
                        /*
                        if(searchQueryInputData.getName().equalsIgnoreCase(ProgramContract.ProgramEntry.TABLE_NAME)) {
                            String value = searchQueryInputData.getValue().trim();
                            if(!value.isEmpty()) {
                                int programId = Integer.valueOf(value);
                                if (programId > -1) {
                                    if (!sqlWhere.toString().trim().isEmpty()) {
                                        sqlWhere.append("&");
                                    }
                                    //sqlWhere.append(mContext.getString(R.string.query_school_degrees));
                                    //sqlWhere.append(searchQueryInputData.getId());

                                    String latestYearData = mContext.getString(R.string.latest_year_data);

                                    String programCertificateLessOneYear = "academics.program.certificate_lt_1_yr.";
                                    String programCertificateLessTwoYear = "academics.program.certificate_lt_2_yr.";
                                    String programAssociate = "academics.program.assoc.";
                                    String programCertificateLessFourYear = "academics.program.certificate_lt_4_yr.";
                                    String programBachelors = "academics.program.bachelors.";

                                    String programPercentage = "academics.program_percentage.";

                                    sqlWhere.append(latestYearData);
                                    sqlWhere.append(programPercentage);
                                    sqlWhere.append("__not = 0.0");
                                }
                            }

                        }
                        */

                    }

                }
            }

        }

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
            sqlSearch.append("_per_page=1");
        sqlSearch.append("&");
        sqlSearch.append(mContext.getString(R.string.query_page_number));
        sqlSearch.append("0");
        if(!sqlWhere.toString().trim().isEmpty()) {
            sqlSearch.append("&");
            sqlSearch.append(sqlWhere.toString());
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