package com.patrickwallin.projects.collegeinformation;

import android.content.pm.ActivityInfo;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class ResultDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(!getResources().getBoolean(R.bool.is_this_tablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        if(savedInstanceState == null) {
            ResultDetailActivityFragment resultDetailActivityFragment = new ResultDetailActivityFragment();
            if(getIntent().hasExtra("resultdetailinfo")) {
                Parcelable parcelable = getIntent().getParcelableExtra("resultdetailinfo");
                Bundle bundleResult = new Bundle();
                bundleResult.putParcelable("resultdetailinfo",parcelable);
                resultDetailActivityFragment.setArguments(bundleResult);
            }
            getSupportFragmentManager().beginTransaction().add(R.id.activity_result_detail_page_container,resultDetailActivityFragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
