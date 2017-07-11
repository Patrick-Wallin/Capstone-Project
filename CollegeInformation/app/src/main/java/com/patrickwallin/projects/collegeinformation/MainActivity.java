package com.patrickwallin.projects.collegeinformation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.patrickwallin.projects.collegeinformation.asynctask.FetchSearchQueryInputTask;
import com.patrickwallin.projects.collegeinformation.asynctask.FetchVersionsTask;
import com.patrickwallin.projects.collegeinformation.data.VersionContract;
import com.patrickwallin.projects.collegeinformation.data.VersionData;
import com.patrickwallin.projects.collegeinformation.utilities.NetworkUtils;
import com.patrickwallin.projects.collegeinformation.utilities.OpenJsonUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView mSplashLoadingTextView;
    private int idFromWidget = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        Intent intent = getIntent();
        if(intent != null) {
            if(intent.hasExtra(this.getString(R.string.id))) {
                idFromWidget = intent.getIntExtra(this.getString(R.string.id),0);
                Timber.d("We got it from Widget! %d",idFromWidget);
            }
        }
        mSplashLoadingTextView = (TextView) findViewById(R.id.splash_loading_text_view);

        mAuth = FirebaseAuth.getInstance();

        AndroidNetworking.initialize(getApplicationContext());

        FetchSearchQueryInputTask fetchSearchQueryInputTask = new FetchSearchQueryInputTask(MainActivity.this);
        fetchSearchQueryInputTask.execute();
    }

    @Override
    protected void onStart() {
        super.onStart();

        NetworkUtils networkUtils = new NetworkUtils(MainActivity.this);
        if (networkUtils.isNetworkConnected()) {
            if(idFromWidget <= 0) {
                try {
                    final File jsonFile = File.createTempFile(getResources().getString(R.string.temp_file_name), getResources().getString(R.string.json_ext));
                    StorageReference storageReference = networkUtils.getStorageReference(VersionContract.VersionEntry.TABLE_NAME);
                    storageReference.getFile(jsonFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            List<VersionData> versionDataList = OpenJsonUtils.getVersionDataFromJson(jsonFile);
                            FetchVersionsTask fetchVersionsTask = new FetchVersionsTask(MainActivity.this, versionDataList);
                            fetchVersionsTask.execute();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mSplashLoadingTextView.setText(e.getMessage());
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                Intent intentSearchActivity = new Intent(MainActivity.this, MainSearchActivity.class);
                intentSearchActivity.putExtra(getString(R.string.id), idFromWidget);
                idFromWidget = 0;
                MainActivity.this.startActivity(intentSearchActivity);
            }
        } else {
            networkUtils.showAlertMessageAboutNoInternetConnection(false);
            mSplashLoadingTextView.setText(getString(R.string.no_internet_connection_title) + "\n " + getString(R.string.no_internet_connection_message));
        }


    }
}
