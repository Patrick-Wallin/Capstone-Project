package com.patrickwallin.projects.collegeinformation;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by piwal on 6/27/2017.
 */

public class ResultsActivity extends AppCompatActivity implements OnResultOptionSelectionChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(!getResources().getBoolean(R.bool.is_this_tablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        //if(savedInstanceState == null) {
        if (findViewById(R.id.activity_result_page_container) != null) {
            ResultsActivityFragment resultsActivityFragment = new ResultsActivityFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_result_page_container,resultsActivityFragment).commit();
        }else {
            ResultsActivityFragment resultsActivityFragment = new ResultsActivityFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.results_fragment,resultsActivityFragment).commit();
            ResultDetailActivityFragment resultDetailActivityFragment = new ResultDetailActivityFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.results_detail_fragment,resultDetailActivityFragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void OnSelectionChanged(Bundle bundle) {
        if (findViewById(R.id.activity_result_page_container) != null) {
            Intent intentResultDetailActivity = new Intent(this, ResultDetailActivity.class);
            intentResultDetailActivity.putExtras(bundle);
            this.startActivity(intentResultDetailActivity);
        }else {
            OnDataSelectionChangeListener listener = (OnDataSelectionChangeListener) this;
            listener.OnDataSelectionChanged(bundle);

            //FragmentManager fragmentManager = getSupportFragmentManager();
            //ResultDetailActivityFragment currentFragment = (ResultDetailActivityFragment)fragmentManager.findFragmentById(R.id.results_detail_fragment);

            //currentFragment.setArguments(bundle);
        }
    }
}
