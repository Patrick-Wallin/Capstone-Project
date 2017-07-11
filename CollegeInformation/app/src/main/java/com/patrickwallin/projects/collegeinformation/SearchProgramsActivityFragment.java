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
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.patrickwallin.projects.collegeinformation.adapter.ProgramsAdapter;
import com.patrickwallin.projects.collegeinformation.asynctask.FetchProgramsTask;
import com.patrickwallin.projects.collegeinformation.data.ProgramContract;
import com.patrickwallin.projects.collegeinformation.data.ProgramData;
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
 * Created by piwal on 6/22/2017.
 */

public class SearchProgramsActivityFragment extends Fragment implements SearchView.OnQueryTextListener, AsyncListener {
    @BindView(R.id.programs_recycler_view)
    RecyclerView programs_recycler_view;

    private List<ProgramData> mProgramsData;
    private Context mContext;
    private ProgramsAdapter mProgramsAdapter;
    private Snackbar mSnackbar;

    public SearchProgramsActivityFragment() {}

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
        View rootView = inflater.inflate(R.layout.activity_search_programs_fragment,container,false);

        setHasOptionsMenu(true);

        ButterKnife.bind(this,rootView);

        LinearLayoutManager llm = new LinearLayoutManager(mContext);

        programs_recycler_view.setLayoutManager(llm);
        programs_recycler_view.setHasFixedSize(true);

        Drawable dividerDrawable = ContextCompat.getDrawable(mContext, R.drawable.divider_line);

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
        programs_recycler_view.addItemDecoration(dividerItemDecoration);

        setUpData();
        setUpAdapter();

        return rootView;
    }

    public void checkVersion() {
        List<VersionData> versionDataList = VersionContract.getVersionData(mContext,VersionContract.VERSION_ID_PROGRAMS);
        if(versionDataList != null && !versionDataList.isEmpty()) {
            final int currentVersion = versionDataList.get(0).getVersionNumber();
            final int priorVersion = versionDataList.get(0).getPriorVersionNumber();
            if(currentVersion != priorVersion) {
                NetworkUtils networkUtils = new NetworkUtils(mContext);
                if (networkUtils.isNetworkConnected()) {
                    try {
                        final File jsonFile = File.createTempFile(mContext.getResources().getString(R.string.temp_file_name), mContext.getResources().getString(R.string.json_ext));
                        StorageReference storageReference = networkUtils.getStorageReference(ProgramContract.ProgramEntry.TABLE_NAME);
                        storageReference.getFile(jsonFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                List<ProgramData> programDataList = OpenJsonUtils.getProgramDataFromJson(jsonFile);
                                mContext.getContentResolver().delete(ProgramContract.ProgramEntry.CONTENT_URI, null, null);
                                if (programDataList != null && !programDataList.isEmpty()) {
                                    String message =  getString(R.string.loading_data) + getString(R.string.one_of) + String.valueOf(programDataList.size());
                                    returnString(message);
                                    for (int i = 0; i < programDataList.size(); i++) {
                                        message = getString(R.string.loading_data) + String.valueOf(i + 1) + getString(R.string.of) + String.valueOf(programDataList.size());
                                        returnString(message);
                                        mContext.getContentResolver().insert(ProgramContract.ProgramEntry.CONTENT_URI, programDataList.get(i).getProgramContentValues());
                                    }
                                }
                                VersionData versionData = new VersionData(VersionContract.VERSION_ID_PROGRAMS, ProgramContract.PATH_PROGRAMS, currentVersion, currentVersion);
                                ContentValues contentValues = versionData.getVersionContentValues();
                                mContext.getContentResolver().update(VersionContract.VersionEntry.CONTENT_URI, contentValues, VersionContract.VersionEntry.COLUMN_VERSION_ID + " = " + VersionContract.VERSION_ID_PROGRAMS, null);

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkVersion();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        mProgramsAdapter.setProgramData(mProgramsData);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true; // Return true to expand action view
                    }
                });

        super.onCreateOptionsMenu(menu,inflater);
    }

    private void setUpData(){
        mProgramsData = new ArrayList<>();
    }

    private void setUpAdapter() {
        mProgramsAdapter = new ProgramsAdapter(mProgramsData, mContext);
        programs_recycler_view.setAdapter(mProgramsAdapter);
    }

    private void loadData() {
        FetchProgramsTask fetchProgramsTask = new FetchProgramsTask(mContext,mProgramsAdapter,this);
        fetchProgramsTask.execute();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.i(SearchProgramsActivityFragment.class.getSimpleName(),newText);
        if(mProgramsData == null || (mProgramsData != null && mProgramsData.isEmpty())) {
            mProgramsData = mProgramsAdapter.getProgramData();
        }
        final List<ProgramData> filteredModelList = filterProgramList(mProgramsData, newText);

        mProgramsAdapter.setProgramData(filteredModelList);

        return true;
    }

    private List<ProgramData> filterProgramList(List<ProgramData> data, String query) {
        final List<ProgramData> filteredModelList = new ArrayList<>();

        query = query.toLowerCase();

        for (ProgramData model : data) {
            final String text = model.getTitle().toLowerCase();
            Log.i(SearchProgramsActivityFragment.class.getSimpleName(),text);
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }

        return filteredModelList;
    }

    @Override
    public void returnString(String message) {
        if(mSnackbar == null)
            mSnackbar = Snackbar.make(getView().findViewById(R.id.root_search_programs), message, Snackbar.LENGTH_INDEFINITE);
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
