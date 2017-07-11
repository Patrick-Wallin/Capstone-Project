package com.patrickwallin.projects.collegeinformation;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.patrickwallin.projects.collegeinformation.adapter.DegreesAdapter;
import com.patrickwallin.projects.collegeinformation.asynctask.FetchDegreesTask;
import com.patrickwallin.projects.collegeinformation.data.DegreeContract;
import com.patrickwallin.projects.collegeinformation.data.DegreesData;
import com.patrickwallin.projects.collegeinformation.data.VersionContract;
import com.patrickwallin.projects.collegeinformation.data.VersionData;
import com.patrickwallin.projects.collegeinformation.utilities.AsyncListener;
import com.patrickwallin.projects.collegeinformation.utilities.NetworkUtils;
import com.patrickwallin.projects.collegeinformation.utilities.OpenJsonUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by piwal on 6/6/2017.
 */

public class SearchDegreesActivityFragment extends Fragment implements AsyncListener {
    @BindView(R.id.degrees_recycler_view)
    RecyclerView degrees_recycler_view;

    private List<DegreesData> mDegreesData;
    private Context mContext;
    private DegreesAdapter mDegreesAdapter;
    private Snackbar mSnackbar;

    public SearchDegreesActivityFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_search_degrees_fragment,container,false);

        ButterKnife.bind(this,rootView);

        GridLayoutManager sglm = new GridLayoutManager(mContext,2);

        degrees_recycler_view.setLayoutManager(sglm);
        degrees_recycler_view.setHasFixedSize(true);

        setUpData();
        setUpAdapter();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkVersion();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setUpData(){
        mDegreesData = new ArrayList<>();
    }

    private void setUpAdapter() {
        mDegreesAdapter = new DegreesAdapter(mDegreesData,mContext);
        degrees_recycler_view.setAdapter(mDegreesAdapter);
    }

    private void checkVersion() {
        List<VersionData> versionDataList = VersionContract.getVersionData(mContext,VersionContract.VERSION_ID_DEGREES);
        if(versionDataList != null && !versionDataList.isEmpty()) {
            final int currentVersion = versionDataList.get(0).getVersionNumber();
            final int priorVersion = versionDataList.get(0).getPriorVersionNumber();
            if(currentVersion != priorVersion) {
                NetworkUtils networkUtils = new NetworkUtils(mContext);
                if (networkUtils.isNetworkConnected()) {
                    try {
                        final File jsonFile = File.createTempFile(mContext.getResources().getString(R.string.temp_file_name), mContext.getResources().getString(R.string.json_ext));
                        StorageReference storageReference = networkUtils.getStorageReference(DegreeContract.DegreeEntry.TABLE_NAME);
                        storageReference.getFile(jsonFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                List<DegreesData> degreesDataList = OpenJsonUtils.getDegreeDataFromJson(jsonFile);
                                mContext.getContentResolver().delete(DegreeContract.DegreeEntry.CONTENT_URI, null, null);
                                if (degreesDataList != null && !degreesDataList.isEmpty()) {
                                    String message = getString(R.string.loading_data) + getString(R.string.one_of) + String.valueOf(degreesDataList.size());
                                    returnString(message);
                                    for (int i = 0; i < degreesDataList.size(); i++) {
                                        message = getString(R.string.loading_data) + " " + String.valueOf(i + 1) + getString(R.string.of) + String.valueOf(degreesDataList.size());
                                        returnString(message);
                                        mContext.getContentResolver().insert(DegreeContract.DegreeEntry.CONTENT_URI, degreesDataList.get(i).getDegreesContentValues());
                                    }
                                }
                                VersionData versionData = new VersionData(VersionContract.VERSION_ID_DEGREES, DegreeContract.PATH_DEGREES, currentVersion, currentVersion);
                                ContentValues contentValues = versionData.getVersionContentValues();
                                mContext.getContentResolver().update(VersionContract.VersionEntry.CONTENT_URI, contentValues, VersionContract.VersionEntry.COLUMN_VERSION_ID + " = " + VersionContract.VERSION_ID_DEGREES, null);

                                loadData();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                returnString("Failed loading data");
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    networkUtils.showAlertMessageAboutNoInternetConnection(true);
                }
            }else {
                loadData();
            }
        }

    }

    private void loadData() {
        FetchDegreesTask fetchDegreesTask = new FetchDegreesTask(mContext,mDegreesAdapter,this);
        fetchDegreesTask.execute();
    }

    @Override
    public void returnString(String message) {
        if(mSnackbar == null)
            mSnackbar = Snackbar.make(getView().findViewById(R.id.root_search_degrees), message, Snackbar.LENGTH_INDEFINITE);
        else
            mSnackbar.setText(message);
        if(!mSnackbar.isShown())
            mSnackbar.show();
    }

    @Override
    public void closeScreen() {
        if(mSnackbar != null && mSnackbar.isShown()) {
            mSnackbar.dismiss();
        }
    }
}
