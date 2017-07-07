
package com.patrickwallin.projects.collegeinformation.firebasestorage;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.patrickwallin.projects.collegeinformation.BuildConfig;
import com.patrickwallin.projects.collegeinformation.MainActivity;
import com.patrickwallin.projects.collegeinformation.R;
import com.patrickwallin.projects.collegeinformation.asynctask.FetchDegreesTask;
import com.patrickwallin.projects.collegeinformation.asynctask.FetchRecordIntoTableTask;
import com.patrickwallin.projects.collegeinformation.data.DegreeContract;
import com.patrickwallin.projects.collegeinformation.data.DegreesData;
import com.patrickwallin.projects.collegeinformation.data.NameContract;
import com.patrickwallin.projects.collegeinformation.data.NameData;
import com.patrickwallin.projects.collegeinformation.data.ProgramContract;
import com.patrickwallin.projects.collegeinformation.data.ProgramData;
import com.patrickwallin.projects.collegeinformation.data.RegionData;
import com.patrickwallin.projects.collegeinformation.data.RegionsContract;
import com.patrickwallin.projects.collegeinformation.data.StateData;
import com.patrickwallin.projects.collegeinformation.data.StatesContract;
import com.patrickwallin.projects.collegeinformation.data.VersionContract;
import com.patrickwallin.projects.collegeinformation.data.VersionData;
import com.patrickwallin.projects.collegeinformation.utilities.DebugInfo;
import com.patrickwallin.projects.collegeinformation.utilities.NetworkUtils;
import com.patrickwallin.projects.collegeinformation.utilities.OpenJsonUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import timber.log.Timber;

/**
 * Created by piwal on 6/19/2017.
 */

public class FirebaseStorageProcessor {
    Context mContext;
    TextView mSplashLoadingTextView;
    String mOriginalLoadingMessage;

    String mTableName;
    int mVersionNumber;

    public FirebaseStorageProcessor(Context context, String tableName, int versionNumber, TextView splashLoadingTextView) {
        mContext = context;
        mSplashLoadingTextView = splashLoadingTextView;
        mOriginalLoadingMessage = "Loading...\n";

        mTableName = tableName;
        mVersionNumber = versionNumber;
    }

    public void execute() {
        try {
            final File jsonFile = File.createTempFile(mContext.getResources().getString(R.string.temp_file_name), mContext.getResources().getString(R.string.json_ext));
            NetworkUtils networkUtils = new NetworkUtils(mContext);
            StorageReference storageReference = networkUtils.getStorageReference(mTableName);
            com.google.firebase.storage.FileDownloadTask taskDownloadFile = storageReference.getFile(jsonFile);
            Tasks.await(taskDownloadFile);
            //synchronized (taskDownloadFile) {
                taskDownloadFile.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        if (mTableName.equalsIgnoreCase(DegreeContract.DegreeEntry.TABLE_NAME)) {
                            List<DegreesData> degreesDataList = OpenJsonUtils.getDegreeDataFromJson(jsonFile);
                            mContext.getContentResolver().delete(DegreeContract.DegreeEntry.CONTENT_URI, null, null);
                            if (degreesDataList != null && !degreesDataList.isEmpty()) {
                                String message = mOriginalLoadingMessage + " Degrees: 1 of " + String.valueOf(degreesDataList.size());
                                mSplashLoadingTextView.setText(message);
                                for (int i = 0; i < degreesDataList.size(); i++) {
                                    message = mOriginalLoadingMessage + " Degrees: " + String.valueOf(i + 1) + " of " + String.valueOf(degreesDataList.size());
                                    ((TextView)((MainActivity)mContext).findViewById(R.id.splash_loading_text_view)).setText(message);
                                    //mSplashLoadingTextView.setText(message);
                                    //new FetchRecordIntoTableTask(mContext,DegreeContract.DegreeEntry.CONTENT_URI, degreesDataList.get(i).getDegreesContentValues(),message,mSplashLoadingTextView).execute();
                                    mContext.getContentResolver().insert(DegreeContract.DegreeEntry.CONTENT_URI, degreesDataList.get(i).getDegreesContentValues());
                                }
                            }
                            VersionData versionData = new VersionData(VersionContract.VERSION_ID_DEGREES, DegreeContract.PATH_DEGREES, mVersionNumber,mVersionNumber);
                            ContentValues contentValues = versionData.getVersionContentValues();
                            mContext.getContentResolver().update(VersionContract.VersionEntry.CONTENT_URI, contentValues, VersionContract.VersionEntry.COLUMN_VERSION_ID + " = " + VersionContract.VERSION_ID_DEGREES, null);
                        } else {
                            if (mTableName.equalsIgnoreCase(ProgramContract.ProgramEntry.TABLE_NAME)) {
                                List<ProgramData> programDataList = OpenJsonUtils.getProgramDataFromJson(jsonFile);
                                mContext.getContentResolver().delete(ProgramContract.ProgramEntry.CONTENT_URI, null, null);
                                if (programDataList != null && !programDataList.isEmpty()) {
                                    String message = mOriginalLoadingMessage + " Programs: 1 of " + String.valueOf(programDataList.size());
                                    mSplashLoadingTextView.setText(message);

                                    for (int i = 0; i < programDataList.size(); i++) {
                                        message = mOriginalLoadingMessage + " Programs: " + String.valueOf(i + 1) + " of " + String.valueOf(programDataList.size());
                                        //mSplashLoadingTextView.setText(message);
                                        ((TextView)((MainActivity)mContext).findViewById(R.id.splash_loading_text_view)).setText(message);
                                        //new FetchRecordIntoTableTask(mContext,ProgramContract.ProgramEntry.CONTENT_URI, programDataList.get(i).getProgramContentValues(),message,mSplashLoadingTextView).execute();
                                        mContext.getContentResolver().insert(ProgramContract.ProgramEntry.CONTENT_URI, programDataList.get(i).getProgramContentValues());
                                    }
                                }
                                VersionData versionData = new VersionData(VersionContract.VERSION_ID_PROGRAMS, ProgramContract.PATH_PROGRAMS, mVersionNumber,mVersionNumber);
                                ContentValues contentValues = versionData.getVersionContentValues();
                                mContext.getContentResolver().update(VersionContract.VersionEntry.CONTENT_URI, contentValues, VersionContract.VersionEntry.COLUMN_VERSION_ID + " = " + VersionContract.VERSION_ID_PROGRAMS, null);

                            } else {
                                if (mTableName.equalsIgnoreCase(StatesContract.StateEntry.TABLE_NAME)) {
                                    List<StateData> stateDataList = OpenJsonUtils.getStateDataFromJson(jsonFile);
                                    mContext.getContentResolver().delete(StatesContract.StateEntry.CONTENT_URI, null, null);
                                    if (stateDataList != null && !stateDataList.isEmpty()) {
                                        String message = mOriginalLoadingMessage + " States: 1 of " + String.valueOf(stateDataList.size());
                                        mSplashLoadingTextView.setText(message);
                                        for (int i = 0; i < stateDataList.size(); i++) {
                                            message = mOriginalLoadingMessage + " States: " + String.valueOf(i + 1) + " of " + String.valueOf(stateDataList.size());
                                            //mSplashLoadingTextView.setText(message);
                                            ((TextView)((MainActivity)mContext).findViewById(R.id.splash_loading_text_view)).setText(message);
                                            //new FetchRecordIntoTableTask(mContext,StatesContract.StateEntry.CONTENT_URI, stateDataList.get(i).getStateContentValues(),message,mSplashLoadingTextView).execute();
                                            mContext.getContentResolver().insert(StatesContract.StateEntry.CONTENT_URI, stateDataList.get(i).getStateContentValues());
                                        }
                                    }
                                    VersionData versionData = new VersionData(VersionContract.VERSION_ID_STATES, StatesContract.PATH_STATES, mVersionNumber,mVersionNumber);
                                    ContentValues contentValues = versionData.getVersionContentValues();
                                    mContext.getContentResolver().update(VersionContract.VersionEntry.CONTENT_URI, contentValues, VersionContract.VersionEntry.COLUMN_VERSION_ID + " = " + VersionContract.VERSION_ID_STATES, null);

                                } else {
                                    if (mTableName.equalsIgnoreCase(RegionsContract.RegionEntry.TABLE_NAME)) {
                                        List<RegionData> regionDataList = OpenJsonUtils.getRegionDataFromJson(jsonFile);
                                        mContext.getContentResolver().delete(RegionsContract.RegionEntry.CONTENT_URI, null, null);
                                        if (regionDataList != null && !regionDataList.isEmpty()) {
                                            String message = mOriginalLoadingMessage + " Regions: 1 of " + String.valueOf(regionDataList.size());
                                            mSplashLoadingTextView.setText(message);
                                            for (int i = 0; i < regionDataList.size(); i++) {
                                                message = mOriginalLoadingMessage + " Regions: " + String.valueOf(i + 1) + " of " + String.valueOf(regionDataList.size());
                                                //mSplashLoadingTextView.setText(message);
                                                ((TextView)((MainActivity)mContext).findViewById(R.id.splash_loading_text_view)).setText(message);
                                                //new FetchRecordIntoTableTask(mContext,RegionsContract.RegionEntry.CONTENT_URI, regionDataList.get(i).getRegionContentValues(),message,mSplashLoadingTextView).execute();
                                                mContext.getContentResolver().insert(RegionsContract.RegionEntry.CONTENT_URI, regionDataList.get(i).getRegionContentValues());
                                            }
                                        }
                                        VersionData versionData = new VersionData(VersionContract.VERSION_ID_REGIONS, RegionsContract.PATH_REGIONS, mVersionNumber,mVersionNumber);
                                        ContentValues contentValues = versionData.getVersionContentValues();
                                        mContext.getContentResolver().update(VersionContract.VersionEntry.CONTENT_URI, contentValues, VersionContract.VersionEntry.COLUMN_VERSION_ID + " = " + VersionContract.VERSION_ID_REGIONS, null);

                                    } else {
                                        if (mTableName.equalsIgnoreCase(NameContract.NameEntry.TABLE_NAME)) {

                                            DebugInfo di = null;
                                            if (BuildConfig.DEBUG) {
                                                di = new DebugInfo();
                                                di.start();
                                            }

                                            List<NameData> nameDataList = OpenJsonUtils.getNameDataFromJson(jsonFile);

                                            mContext.getContentResolver().delete(NameContract.NameEntry.CONTENT_URI, null, null);
                                            if (nameDataList != null && !nameDataList.isEmpty()) {
                                                String message = mOriginalLoadingMessage + " Name: 1 of " + String.valueOf(nameDataList.size());
                                                mSplashLoadingTextView.setText(message);
                                                for (int i = 0; i < nameDataList.size(); i++) {
                                                    message = mOriginalLoadingMessage + " Name: " + String.valueOf(i + 1) + " of " + String.valueOf(nameDataList.size());
                                                    //mSplashLoadingTextView.setText(message);
                                                    ((TextView)((MainActivity)mContext).findViewById(R.id.splash_loading_text_view)).setText(message);
                                                    //new FetchRecordIntoTableTask(mContext,NameContract.NameEntry.CONTENT_URI,nameDataList.get(i).getNamesContentValues(),message,mSplashLoadingTextView).execute();
                                                    mContext.getContentResolver().insert(NameContract.NameEntry.CONTENT_URI, nameDataList.get(i).getNamesContentValues());
                                                }
                                            }

                                            if (BuildConfig.DEBUG) {
                                                di.end();
                                                Timber.i("Time length for processing FirebaseStorageProcessor (%s): %s", mTableName, di.getMinAndSecElaspedTime());
                                            }

                                            VersionData versionData = new VersionData(VersionContract.VERSION_ID_NAMES, NameContract.PATH_NAMES, mVersionNumber,mVersionNumber);
                                            ContentValues contentValues = versionData.getVersionContentValues();
                                            mContext.getContentResolver().update(VersionContract.VersionEntry.CONTENT_URI, contentValues, VersionContract.VersionEntry.COLUMN_VERSION_ID + " = " + VersionContract.VERSION_ID_NAMES, null);

                                        }


                                    }

                                }

                            }

                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // do nothing!
                    }
                });

            //}
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }
}
