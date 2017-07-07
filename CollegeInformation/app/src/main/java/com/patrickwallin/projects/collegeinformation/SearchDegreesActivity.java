package com.patrickwallin.projects.collegeinformation;

import android.content.pm.ActivityInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.patrickwallin.projects.collegeinformation.utilities.AsyncListener;

public class SearchDegreesActivity extends AppCompatActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_degrees);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(!getResources().getBoolean(R.bool.is_this_tablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        if(savedInstanceState == null) {
            SearchDegreesActivityFragment searchDegreesActivityFragment = new SearchDegreesActivityFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.activity_search_degrees_page_container,searchDegreesActivityFragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:

                //NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }


}
