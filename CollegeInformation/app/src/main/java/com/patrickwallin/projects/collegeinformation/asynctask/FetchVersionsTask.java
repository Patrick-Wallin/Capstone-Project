package com.patrickwallin.projects.collegeinformation.asynctask;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.patrickwallin.projects.collegeinformation.BuildConfig;
import com.patrickwallin.projects.collegeinformation.MainSearchActivity;
import com.patrickwallin.projects.collegeinformation.data.DegreeContract;
import com.patrickwallin.projects.collegeinformation.data.NameContract;
import com.patrickwallin.projects.collegeinformation.data.ProgramContract;
import com.patrickwallin.projects.collegeinformation.data.RegionsContract;
import com.patrickwallin.projects.collegeinformation.data.StatesContract;
import com.patrickwallin.projects.collegeinformation.data.VersionContract;
import com.patrickwallin.projects.collegeinformation.data.VersionData;
import com.patrickwallin.projects.collegeinformation.firebasestorage.FirebaseStorageProcessor;
import com.patrickwallin.projects.collegeinformation.utilities.DebugInfo;
import com.patrickwallin.projects.collegeinformation.utilities.NetworkUtils;
import com.patrickwallin.projects.collegeinformation.utilities.OpenJsonUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import timber.log.Timber;

/**
 * Created by piwal on 6/10/2017.
 */

public class FetchVersionsTask extends AsyncTask<Void, Void, List<VersionData>> {
    Context mContext;
    TextView mSplashLoadingTextView;
    List<VersionData> mVersionDataList;

    private List<String> VERSION_LIST_TABLE = new ArrayList<String>();
    private List<Integer> VERSION_LIST_ID = new ArrayList<Integer>();

    public FetchVersionsTask(Context context, TextView splashLoadingTextView, List<VersionData> versionDataList) {
        mContext = context;
        mSplashLoadingTextView = splashLoadingTextView;
        mVersionDataList = versionDataList;

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
                    lVersionDataResult.add(new VersionData(VERSION_LIST_ID.get(i),VERSION_LIST_TABLE.get(i),0));
                    ContentValues contentValues = lVersionDataResult.get(lVersionDataResult.size()-1).getVersionContentValues();
                    mContext.getContentResolver().insert(VersionContract.VersionEntry.CONTENT_URI,contentValues);
                }
            }
        }


        if(mVersionDataList != null && !mVersionDataList.isEmpty() && versionDatas != null && !versionDatas.isEmpty()) {
            // compare version and if it is difference, then download other json!

            //mSplashLoadingTextView.setText("Loading 1 of " + String.valueOf(versionDatas.size()) + " data");

            for(int i = 0; i < versionDatas.size(); i++) {
                for(int x = 0; x < mVersionDataList.size(); x++) {
                    //mSplashLoadingTextView.setText("Loading " + String.valueOf(x+1) + " of " + String.valueOf(versionDatas.size()) + " data.");
                    if(versionDatas.get(i).getTableName().equalsIgnoreCase(mVersionDataList.get(x).getTableName())) {
                        if(versionDatas.get(i).getVersionNumber() != mVersionDataList.get(x).getVersionNumber()) {
                            //new FetchRecordIntoTableTask(mContext,versionDatas.get(i).getTableName(),versionDatas.get(i).getVersionNumber()).execute();
                            new FirebaseStorageProcessor(mContext,versionDatas.get(i).getTableName(),mVersionDataList.get(x).getVersionNumber(),mSplashLoadingTextView).execute();
                        }
                        break;
                    }
                }
            }

        }



/*
        try {
            final File jsonFile = File.createTempFile("jsonFromStorage","json");
            NetworkUtils networkUtils = new NetworkUtils(mContext);
            StorageReference storageReference = networkUtils.getStorageReference(VersionContract.VersionEntry.TABLE_NAME);
            com.google.firebase.storage.FileDownloadTask taskDownloadFile = storageReference.getFile(jsonFile);
            Tasks.await(taskDownloadFile);

            taskDownloadFile.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    List<VersionData> versionDataList  = OpenJsonUtils.getVersionDataFromJson(jsonFile);
                    if(versionDataList != null && !versionDataList.isEmpty() && versionDatas != null && !versionDatas.isEmpty()) {
                        // compare version and if it is difference, then download other json!

                        mSplashLoadingTextView.setText("Loading 1 of " + String.valueOf(versionDatas.size()) + " data");

                        for(int i = 0; i < versionDatas.size(); i++) {
                            for(int x = 0; x < versionDataList.size(); x++) {
                                mSplashLoadingTextView.setText("Loading " + String.valueOf(x+1) + " of " + String.valueOf(versionDatas.size()) + " data.");
                                if(versionDatas.get(i).getTableName().equalsIgnoreCase(versionDataList.get(x).getTableName())) {
                                    if(versionDatas.get(i).getVersionNumber() != versionDataList.get(x).getVersionNumber()) {
                                        //new FetchRecordIntoTableTask(mContext,versionDatas.get(i).getTableName(),versionDatas.get(i).getVersionNumber()).execute();
                                        new FirebaseStorageProcessor(mContext,versionDatas.get(i).getTableName(),versionDataList.get(x).getVersionNumber(),mSplashLoadingTextView).execute();
                                    }
                                    break;
                                }
                            }
                        }

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mContext,"Fail",Toast.LENGTH_LONG);
                }
            });



        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
*/

        if(BuildConfig.DEBUG) {
            di.end();
            Timber.i("Time length for getting version data: %s", di.getMinAndSecElaspedTime());
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

        /*
        try {
            final File jsonFile = File.createTempFile("jsonFromStorage","json");
            NetworkUtils networkUtils = new NetworkUtils(mContext);
            StorageReference storageReference = networkUtils.getStorageReference(VersionContract.VersionEntry.TABLE_NAME);
            storageReference.getFile(jsonFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    List<VersionData> versionDataList  = OpenJsonUtils.getVersionDataFromJson(jsonFile);
                    if(versionDataList != null && !versionDataList.isEmpty() && versionDatas != null && !versionDatas.isEmpty()) {
                        // compare version and if it is difference, then download other json!

                        mSplashLoadingTextView.setText("Loading 1 of " + String.valueOf(versionDatas.size()) + " data");

                        for(int i = 0; i < versionDatas.size(); i++) {
                            for(int x = 0; x < versionDataList.size(); x++) {
                                mSplashLoadingTextView.setText("Loading " + String.valueOf(x+1) + " of " + String.valueOf(versionDatas.size()) + " data.");
                                if(versionDatas.get(i).getTableName().equalsIgnoreCase(versionDataList.get(x).getTableName())) {
                                    if(versionDatas.get(i).getVersionNumber() != versionDataList.get(x).getVersionNumber()) {
                                        //new FetchRecordIntoTableTask(mContext,versionDatas.get(i).getTableName(),versionDatas.get(i).getVersionNumber()).execute();
                                        new FirebaseStorageProcessor(mContext,versionDatas.get(i).getTableName(),versionDataList.get(x).getVersionNumber(),mSplashLoadingTextView).execute();
                                    }
                                    break;
                                }
                            }
                        }

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mContext,"Fail",Toast.LENGTH_LONG);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        */

    }
}
