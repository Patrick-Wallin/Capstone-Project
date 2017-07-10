package com.patrickwallin.projects.collegeinformation;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.patrickwallin.projects.collegeinformation.asynctask.FetchSearchQueryInputTask;
import com.patrickwallin.projects.collegeinformation.asynctask.FetchVersionsTask;
import com.patrickwallin.projects.collegeinformation.data.CollegeInfoDBHelper;
import com.patrickwallin.projects.collegeinformation.data.VersionContract;
import com.patrickwallin.projects.collegeinformation.data.VersionData;
import com.patrickwallin.projects.collegeinformation.firebasestorage.FirebaseStorageProcessor;
import com.patrickwallin.projects.collegeinformation.utilities.NetworkUtils;
import com.patrickwallin.projects.collegeinformation.utilities.OpenJsonUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import timber.log.Timber;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private static final int REQUEST_RUNTIME_PERMISSION = 1;

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
                Timber.d("We got it from Widget!");
                idFromWidget = intent.getIntExtra(this.getString(R.string.id),0);
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
            if(idFromWidget > 0) {
                try {
                    final File jsonFile = File.createTempFile(getResources().getString(R.string.temp_file_name), getResources().getString(R.string.json_ext));
                    StorageReference storageReference = networkUtils.getStorageReference(VersionContract.VersionEntry.TABLE_NAME);
                    storageReference.getFile(jsonFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            List<VersionData> versionDataList = OpenJsonUtils.getVersionDataFromJson(jsonFile);
                            FetchVersionsTask fetchVersionsTask = new FetchVersionsTask(MainActivity.this, mSplashLoadingTextView, versionDataList);
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
                Intent intentResultsActivity = new Intent(MainActivity.this, ResultsActivity.class);
                intentResultsActivity.putExtra("favoriteresults",true);
                MainActivity.this.startActivity(intentResultsActivity);
            }
        } else {
            networkUtils.showAlertMessageAboutNoInternetConnection(false);
            mSplashLoadingTextView.setText(getString(R.string.no_internet_connection_title) + "\n " + getString(R.string.no_internet_connection_message));
        }


    }
}
