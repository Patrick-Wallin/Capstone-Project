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
import com.patrickwallin.projects.collegeinformation.asynctask.FetchCollegeNamesTask;
import com.patrickwallin.projects.collegeinformation.asynctask.InsertNamesTask;
import com.patrickwallin.projects.collegeinformation.data.NameContract;
import com.patrickwallin.projects.collegeinformation.data.NameData;
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
import timber.log.Timber;

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

    private static int mDisplayedItemPosition = 0;

    public SearchNamesActivityFragment() {}

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
        View rootView = inflater.inflate(R.layout.activity_search_names_fragment,container,false);

        setHasOptionsMenu(true);

        ButterKnife.bind(this,rootView);

        LinearLayoutManager llm = new LinearLayoutManager(mContext);

        names_recycler_view.setLayoutManager(llm);
        names_recycler_view.setHasFixedSize(true);

        names_recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llm = (LinearLayoutManager) names_recycler_view.getLayoutManager();
                mDisplayedItemPosition = llm.findFirstVisibleItemPosition();
            }
        });

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
        searchView.setQueryHint("Type keywords here");
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        LinearLayoutManager llm = (LinearLayoutManager) names_recycler_view.getLayoutManager();
                        llm.scrollToPosition(mNamesAdapter.getSelectedPosition());
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
        mNamesAdapter = new NamesAdapter(mNameData,mContext,names_recycler_view);
        names_recycler_view.setAdapter(mNamesAdapter);
    }

    public void checkVersion() {
        List<VersionData> versionDataList = VersionContract.getVersionData(mContext, VersionContract.VERSION_ID_NAMES);
        if(versionDataList != null && !versionDataList.isEmpty()) {
            final int currentVersion = versionDataList.get(0).getVersionNumber();
            final int priorVersion = versionDataList.get(0).getPriorVersionNumber();
            if(currentVersion != priorVersion) {
                NetworkUtils networkUtils = new NetworkUtils(mContext);
                if (networkUtils.isNetworkConnected()) {
                    try {
                        final File jsonFile = File.createTempFile(mContext.getResources().getString(R.string.temp_file_name), mContext.getResources().getString(R.string.json_ext));
                        StorageReference storageReference = networkUtils.getStorageReference(NameContract.NameEntry.TABLE_NAME);
                        storageReference.getFile(jsonFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                returnString("Loading data... Please wait.");
                                Snackbar.make(getView().findViewById(R.id.root_search_names), getString(R.string.loading_data), Snackbar.LENGTH_INDEFINITE).show();
                                List<NameData> nameDataList = OpenJsonUtils.getNameDataFromJson(jsonFile);
                                nameDataList.add(new NameData(-1, "Any", "", "", "", ""));

                                mDisplayedItemPosition = mNamesAdapter.setNameData(nameDataList);
                                mContext.getContentResolver().delete(NameContract.NameEntry.CONTENT_URI, null, null);

                                new InsertNamesTask(mContext, nameDataList, mNamesAdapter).execute();

                                VersionData versionData = new VersionData(VersionContract.VERSION_ID_NAMES, NameContract.PATH_NAMES, currentVersion, currentVersion);
                                ContentValues contentValues = versionData.getVersionContentValues();
                                mContext.getContentResolver().update(VersionContract.VersionEntry.CONTENT_URI, contentValues, VersionContract.VersionEntry.COLUMN_VERSION_ID + " = " + VersionContract.VERSION_ID_NAMES, null);

                                //loadData();
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
        FetchCollegeNamesTask fetchCollegeNamesTask = new FetchCollegeNamesTask(mContext,mNamesAdapter,this);
        fetchCollegeNamesTask.execute();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        checkVersion();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(mNameData == null || (mNameData != null && mNameData.isEmpty())) {
            mNameData = mNamesAdapter.getNameData();
        }
        final List<NameData> filteredModelList = filterProgramList(mNameData, query);

        mNamesAdapter.setNameData(filteredModelList);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
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

        for (NameData model : data) {
            final String text = model.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }

        return filteredModelList;
    }

    @Override
    public void returnString(String message) {
        // spoke with a person over forum about this...  He could see snackbar showign up on his device.  But not here on my emulator.
        // We tried to solve it but he said it worked fine on his emulator and device and told me to do not worry about that.
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
