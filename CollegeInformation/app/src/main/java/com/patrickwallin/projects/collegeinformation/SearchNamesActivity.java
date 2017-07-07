package com.patrickwallin.projects.collegeinformation;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by piwal on 6/22/2017.
 */

public class SearchNamesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_names);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(!getResources().getBoolean(R.bool.is_this_tablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        if(savedInstanceState == null) {
            SearchNamesActivityFragment searchNamesActivityFragment = new SearchNamesActivityFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_search_names_page_container,searchNamesActivityFragment).commit();
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
