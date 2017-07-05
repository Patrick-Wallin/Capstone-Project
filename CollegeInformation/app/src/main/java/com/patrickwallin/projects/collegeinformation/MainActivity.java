package com.patrickwallin.projects.collegeinformation;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSplashLoadingTextView = (TextView) findViewById(R.id.splash_loading_text_view);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }


        mAuth = FirebaseAuth.getInstance();

        AndroidNetworking.initialize(getApplicationContext());

        FetchSearchQueryInputTask fetchSearchQueryInputTask = new FetchSearchQueryInputTask(MainActivity.this);
        fetchSearchQueryInputTask.execute();

        //Intent intentSearchActivity = new Intent(this, MainSearchActivity.class);
        //startActivity(intentSearchActivity);

    }

    void checkPermission() {
        //select which permission you want
        final String permission = WRITE_EXTERNAL_STORAGE;
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, REQUEST_RUNTIME_PERMISSION);
            }
        } else {
            // you have permission go ahead launch service
            //SomeTask();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            signInAnonymously();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_RUNTIME_PERMISSION:
                final int numOfRequest = grantResults.length;
                final boolean isGranted = numOfRequest == 1
                        && PackageManager.PERMISSION_GRANTED == grantResults[numOfRequest - 1];
                if (isGranted) {
                    // you have permission go ahead
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    signInAnonymously();
                } else {
                    // you dont have permission show toast
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

        //FirebaseUser currentUser = mAuth.getCurrentUser();

        Log.i("Test","OnStart Of MainActivity");

        signInAnonymously();

    }

    private void signInAnonymously() {
    /*
        mAuth.signInWithEmailAndPassword("patricktwallin@gmail.com", "Ryazan@900")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(MainActivity.this, "Name: " + user.getEmail(), Toast.LENGTH_LONG).show();

                            Toast.makeText(MainActivity.this, "Authentication Success.", Toast.LENGTH_SHORT).show();

                            FetchVersionsTask fetchVersionsTask = new FetchVersionsTask(MainActivity.this);
                            fetchVersionsTask.execute();




                        } else {
                            Toast.makeText(MainActivity.this, "Authentication failed.",

                                    Toast.LENGTH_SHORT).show();



                        }




                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Authentication failed: " + e.toString(),

                        Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(MainActivity.this, "Success? failed: " + authResult.toString(),

                        Toast.LENGTH_LONG).show();
            }
        });
        */


        if(mAuth != null) {
            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.e("MainActivity", "signInAnonymously:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                //Toast.makeText(MainActivity.this, "Name: " + user.getEmail(), Toast.LENGTH_LONG).show();

                                //Toast.makeText(MainActivity.this, "Authentication Success.", Toast.LENGTH_SHORT).show();

                                //FetchSearchQueryInputTask fetchSearchQueryInputTask = new FetchSearchQueryInputTask(MainActivity.this);
                                //fetchSearchQueryInputTask.execute();


                                try {
                                    final File jsonFile = File.createTempFile("jsonFromStorage", "json");
                                    NetworkUtils networkUtils = new NetworkUtils(MainActivity.this);
                                    StorageReference storageReference = networkUtils.getStorageReference(VersionContract.VersionEntry.TABLE_NAME);
                                    com.google.firebase.storage.FileDownloadTask taskDownloadFile = storageReference.getFile(jsonFile);
                                    synchronized (taskDownloadFile) {
                                        taskDownloadFile.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                List<VersionData> versionDataList = OpenJsonUtils.getVersionDataFromJson(jsonFile);

                                                FetchVersionsTask fetchVersionsTask = new FetchVersionsTask(MainActivity.this, mSplashLoadingTextView, versionDataList);
                                                fetchVersionsTask.execute();


                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_LONG);
                                            }
                                        });
                                    }


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                //FetchVersionsTask fetchVersionsTask = new FetchVersionsTask(MainActivity.this,mSplashLoadingTextView);
                                //fetchVersionsTask.execute();
                            } else {
                                Log.e("MainActivity", "signInAnonymously:failure", task.getException());

                                Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                                mSplashLoadingTextView.setText("Authentication by Firebase failed.");
                            }
                        }

                    });

            // [END signin_anonymously]
        }else {
            mSplashLoadingTextView.setText("Authentication by Firebase failed.");
        }


    }
}
