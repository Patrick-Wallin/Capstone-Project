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
import com.patrickwallin.projects.collegeinformation.adapter.StatesAdapter;
import com.patrickwallin.projects.collegeinformation.asynctask.FetchStatesTask;
import com.patrickwallin.projects.collegeinformation.data.StateData;
import com.patrickwallin.projects.collegeinformation.data.StatesContract;
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

public class SearchLocationsStatesActivityFragment extends Fragment implements AsyncListener {
    @BindView(R.id.states_recycler_view)
    RecyclerView states_recycler_view;

    private List<StateData> mStatesData;
    private Context mContext;
    private StatesAdapter mStatesAdapter;
    private Snackbar mSnackbar;

    public SearchLocationsStatesActivityFragment() {}

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
        View rootView = inflater.inflate(R.layout.activity_search_locations_states_fragment,container,false);

        ButterKnife.bind(this,rootView);

        LinearLayoutManager llm = new LinearLayoutManager(mContext);

        states_recycler_view.setLayoutManager(llm);
        states_recycler_view.setHasFixedSize(true);

        Drawable dividerDrawable = ContextCompat.getDrawable(mContext, R.drawable.divider_line);

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
        states_recycler_view.addItemDecoration(dividerItemDecoration);

        setUpData();
        setUpAdapter();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkVersion();
    }

    private void setUpData(){
        mStatesData = new ArrayList<>();
    }

    private void setUpAdapter() {
        mStatesAdapter = new StatesAdapter(mStatesData,mContext);
        states_recycler_view.setAdapter(mStatesAdapter);
    }

    private void checkVersion() {
        List<VersionData> versionDataList = VersionContract.getVersionData(mContext, VersionContract.VERSION_ID_STATES);
        if(versionDataList != null && !versionDataList.isEmpty()) {
            final int currentVersion = versionDataList.get(0).getVersionNumber();
            final int priorVersion = versionDataList.get(0).getPriorVersionNumber();
            if(currentVersion != priorVersion) {
                NetworkUtils networkUtils = new NetworkUtils(mContext);
                if (networkUtils.isNetworkConnected()) {
                    try {
                        final File jsonFile = File.createTempFile(mContext.getResources().getString(R.string.temp_file_name), mContext.getResources().getString(R.string.json_ext));
                        StorageReference storageReference = networkUtils.getStorageReference(StatesContract.StateEntry.TABLE_NAME);
                        storageReference.getFile(jsonFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                List<StateData> statesDataList = OpenJsonUtils.getStateDataFromJson(jsonFile);
                                mContext.getContentResolver().delete(StatesContract.StateEntry.CONTENT_URI, null, null);
                                if (statesDataList != null && !statesDataList.isEmpty()) {
                                    String message = getString(R.string.loading_data) + getString(R.string.of) + String.valueOf(statesDataList.size());
                                    returnString(message);
                                    for (int i = 0; i < statesDataList.size(); i++) {
                                        message = getString(R.string.loading_data) + String.valueOf(i + 1) + getString(R.string.of) + String.valueOf(statesDataList.size());
                                        returnString(message);
                                        mContext.getContentResolver().insert(StatesContract.StateEntry.CONTENT_URI, statesDataList.get(i).getStateContentValues());
                                    }
                                }
                                VersionData versionData = new VersionData(VersionContract.VERSION_ID_STATES, StatesContract.PATH_STATES, currentVersion, currentVersion);
                                ContentValues contentValues = versionData.getVersionContentValues();
                                mContext.getContentResolver().update(VersionContract.VersionEntry.CONTENT_URI, contentValues, VersionContract.VersionEntry.COLUMN_VERSION_ID + " = " + VersionContract.VERSION_ID_STATES, null);

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

    @Override
    public void returnString(String message) {
        if(mSnackbar == null)
            mSnackbar = Snackbar.make(getView().findViewById(R.id.root_search_states), message, Snackbar.LENGTH_INDEFINITE);
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

    private void loadData() {
        FetchStatesTask fetchStatesTask = new FetchStatesTask(mContext,mStatesAdapter,this);
        fetchStatesTask.execute();
    }
}
