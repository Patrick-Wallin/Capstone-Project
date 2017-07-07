package com.patrickwallin.projects.collegeinformation;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
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
import com.patrickwallin.projects.collegeinformation.adapter.NamesAdapter;
import com.patrickwallin.projects.collegeinformation.adapter.ProgramsAdapter;
import com.patrickwallin.projects.collegeinformation.asynctask.FetchCollegeNamesTask;
import com.patrickwallin.projects.collegeinformation.asynctask.FetchProgramsTask;
import com.patrickwallin.projects.collegeinformation.data.NameContract;
import com.patrickwallin.projects.collegeinformation.data.NameData;
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

public class SearchNamesActivityFragment extends Fragment implements SearchView.OnQueryTextListener, AsyncListener {
    @BindView(R.id.names_recycler_view)
    RecyclerView names_recycler_view;

    private List<NameData> mNameData;
    private Context mContext;
    private NamesAdapter mNamesAdapter;
    private Snackbar mSnackbar;

    public SearchNamesActivityFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!getResources().getBoolean(R.bool.is_this_tablet)){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_search_names_fragment,container,false);

        setHasOptionsMenu(true);

        ButterKnife.bind(this,rootView);

        LinearLayoutManager llm = new LinearLayoutManager(mContext);

        names_recycler_view.setLayoutManager(llm);
        names_recycler_view.setHasFixedSize(true);

        Drawable dividerDrawable = ContextCompat.getDrawable(mContext, R.drawable.divider_line);

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
        names_recycler_view.addItemDecoration(dividerItemDecoration);

        setUpData();
        setUpAdapter();

        return rootView;
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
                        mNamesAdapter.setNameData(mNameData);
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
        mNameData = new ArrayList<>();
    }

    private void setUpAdapter() {
        mNamesAdapter = new NamesAdapter(mNameData,mContext);
        names_recycler_view.setAdapter(mNamesAdapter);
    }

    public void checkVersion() {
        List<VersionData> versionDataList = VersionContract.getVersionData(mContext, VersionContract.VERSION_ID_NAMES);
        if(versionDataList != null && !versionDataList.isEmpty()) {
            final int currentVersion = versionDataList.get(0).getVersionNumber();
            final int priorVersion = versionDataList.get(0).getPriorVersionNumber();
            if(currentVersion != priorVersion) {
                try {
                    final File jsonFile = File.createTempFile(mContext.getResources().getString(R.string.temp_file_name), mContext.getResources().getString(R.string.json_ext));
                    NetworkUtils networkUtils = new NetworkUtils(mContext);
                    StorageReference storageReference = networkUtils.getStorageReference(NameContract.NameEntry.TABLE_NAME);
                    storageReference.getFile(jsonFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            returnString("Loading data... Please wait.");
                            List<NameData> nameDataList = OpenJsonUtils.getNameDataFromJson(jsonFile);
                            mContext.getContentResolver().delete(NameContract.NameEntry.CONTENT_URI, null, null);
                            if (nameDataList != null && !nameDataList.isEmpty()) {
                                String message = "Loading data... 1 of " + String.valueOf(nameDataList.size());
                                returnString(message);
                                for (int i = 0; i < nameDataList.size(); i++) {
                                    message = "Loading data... " + String.valueOf(i + 1) + " of " + String.valueOf(nameDataList.size());
                                    returnString(message);
                                    mContext.getContentResolver().insert(NameContract.NameEntry.CONTENT_URI, nameDataList.get(i).getNamesContentValues());
                                }
                            }
                            VersionData versionData = new VersionData(VersionContract.VERSION_ID_NAMES, NameContract.PATH_NAMES, currentVersion,currentVersion);
                            ContentValues contentValues = versionData.getVersionContentValues();
                            mContext.getContentResolver().update(VersionContract.VersionEntry.CONTENT_URI, contentValues, VersionContract.VersionEntry.COLUMN_VERSION_ID + " = " + VersionContract.VERSION_ID_NAMES, null);

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
                loadData();
            }
        }

    }

    private void loadData() {
        FetchCollegeNamesTask fetchCollegeNamesTask = new FetchCollegeNamesTask(mContext,mNamesAdapter,this);
        fetchCollegeNamesTask.execute();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkVersion();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //Log.i(SearchProgramsActivityFragment.class.getSimpleName(),newText);
        if(mNameData == null || (mNameData != null && mNameData.isEmpty())) {
            mNameData = mNamesAdapter.getNameData();
        }
        final List<NameData> filteredModelList = filterProgramList(mNameData, newText);

        mNamesAdapter.setNameData(filteredModelList);

        return true;
    }

    private List<NameData> filterProgramList(List<NameData> data, String query) {
        final List<NameData> filteredModelList = new ArrayList<>();

        query = query.toLowerCase();

        Log.i(SearchNamesActivityFragment.class.getSimpleName(),"Query: " + query);

        for (NameData model : data) {
            final String text = model.getName().toLowerCase();
            Log.i(SearchNamesActivityFragment.class.getSimpleName(),text);
            if (text.contains(query)) {
                Log.i(SearchNamesActivityFragment.class.getSimpleName(),"make it");
                filteredModelList.add(model);
            }
        }

        return filteredModelList;
    }

    @Override
    public void returnString(String message) {
        if(mSnackbar == null)
            mSnackbar = Snackbar.make(getView().findViewById(R.id.root_search_names), message, Snackbar.LENGTH_INDEFINITE);
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
