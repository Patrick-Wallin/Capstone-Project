package com.patrickwallin.projects.collegeinformation;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.patrickwallin.projects.collegeinformation.adapter.RegionsAdapter;
import com.patrickwallin.projects.collegeinformation.asynctask.FetchRegionsTask;
import com.patrickwallin.projects.collegeinformation.data.RegionData;
import com.patrickwallin.projects.collegeinformation.data.RegionsContract;
import com.patrickwallin.projects.collegeinformation.data.VersionContract;
import com.patrickwallin.projects.collegeinformation.data.VersionData;
import com.patrickwallin.projects.collegeinformation.utilities.AsyncListener;
import com.patrickwallin.projects.collegeinformation.utilities.DividerItemDecoration;
import com.patrickwallin.projects.collegeinformation.utilities.NetworkUtils;
import com.patrickwallin.projects.collegeinformation.utilities.OpenJsonUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by piwal on 6/25/2017.
 */

public class SearchLocationsRegionsActivityFragment extends Fragment implements AsyncListener {
    @BindView(R.id.regions_recycler_view)
    RecyclerView regions_recycler_view;

    private List<RegionData> mRegionsData;
    private Context mContext;
    private RegionsAdapter mRegionsAdapter;
    private Snackbar mSnackbar;

    public SearchLocationsRegionsActivityFragment() {}

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
        View rootView = inflater.inflate(R.layout.activity_search_locations_regions_fragment,container,false);

        ButterKnife.bind(this,rootView);

        LinearLayoutManager llm = new LinearLayoutManager(mContext);

        regions_recycler_view.setLayoutManager(llm);
        regions_recycler_view.setHasFixedSize(true);

        Drawable dividerDrawable = ContextCompat.getDrawable(mContext, R.drawable.divider_line);

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
        regions_recycler_view.addItemDecoration(dividerItemDecoration);

        setUpData();
        setUpAdapter();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkVersion();
    }

    private void checkVersion() {
        List<VersionData> versionDataList = VersionContract.getVersionData(mContext, VersionContract.VERSION_ID_REGIONS);
        if(versionDataList != null && !versionDataList.isEmpty()) {
            final int currentVersion = versionDataList.get(0).getVersionNumber();
            final int priorVersion = versionDataList.get(0).getPriorVersionNumber();
            if(currentVersion != priorVersion) {
                NetworkUtils networkUtils = new NetworkUtils(mContext);
                if (networkUtils.isNetworkConnected()) {
                    try {
                        final File jsonFile = File.createTempFile(mContext.getResources().getString(R.string.temp_file_name), mContext.getResources().getString(R.string.json_ext));
                        StorageReference storageReference = networkUtils.getStorageReference(RegionsContract.RegionEntry.TABLE_NAME);
                        storageReference.getFile(jsonFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                List<RegionData> regionsDataList = OpenJsonUtils.getRegionDataFromJson(jsonFile);
                                mContext.getContentResolver().delete(RegionsContract.RegionEntry.CONTENT_URI, null, null);
                                if (regionsDataList != null && !regionsDataList.isEmpty()) {
                                    String message = getString(R.string.loading_data) + getString(R.string.one_of) + String.valueOf(regionsDataList.size());
                                    returnString(message);
                                    for (int i = 0; i < regionsDataList.size(); i++) {
                                        message = getString(R.string.loading_data) + String.valueOf(i + 1) + getString(R.string.of) + String.valueOf(regionsDataList.size());
                                        returnString(message);
                                        mContext.getContentResolver().insert(RegionsContract.RegionEntry.CONTENT_URI, regionsDataList.get(i).getRegionContentValues());
                                    }
                                }
                                VersionData versionData = new VersionData(VersionContract.VERSION_ID_REGIONS, RegionsContract.PATH_REGIONS, currentVersion, currentVersion);
                                ContentValues contentValues = versionData.getVersionContentValues();
                                mContext.getContentResolver().update(VersionContract.VersionEntry.CONTENT_URI, contentValues, VersionContract.VersionEntry.COLUMN_VERSION_ID + " = " + VersionContract.VERSION_ID_REGIONS, null);

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

    private void setUpData(){
        mRegionsData = new ArrayList<>();
    }

    private void setUpAdapter() {
        mRegionsAdapter = new RegionsAdapter(mRegionsData,mContext);
        regions_recycler_view.setAdapter(mRegionsAdapter);
    }

    private void loadData() {
        FetchRegionsTask fetchRegionsTask = new FetchRegionsTask(mContext,mRegionsAdapter,this);
        fetchRegionsTask.execute();
    }

    @Override
    public void returnString(String message) {
        if(mSnackbar == null)
            mSnackbar = Snackbar.make(getView().findViewById(R.id.root_search_regions), message, Snackbar.LENGTH_INDEFINITE);
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
