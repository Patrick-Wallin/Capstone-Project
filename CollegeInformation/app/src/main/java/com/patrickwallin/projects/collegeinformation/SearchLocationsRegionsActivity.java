package com.patrickwallin.projects.collegeinformation;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class SearchLocationsRegionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_locations_regions);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null) {
            SearchLocationsRegionsActivityFragment searchLocationsRegionsActivityFragment = new SearchLocationsRegionsActivityFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.activity_search_locations_regions_page_container,searchLocationsRegionsActivityFragment).commit();
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
