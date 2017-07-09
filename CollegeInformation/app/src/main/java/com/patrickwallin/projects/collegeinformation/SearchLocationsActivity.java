package com.patrickwallin.projects.collegeinformation;

import android.content.pm.ActivityInfo;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class SearchLocationsActivity extends AppCompatActivity implements OnGoBackChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       // if(!getResources().getBoolean(R.bool.is_this_tablet)){
        //    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
       // }

        if(savedInstanceState == null) {
            SearchLocationsActivityFragment searchLocationsActivityFragment = new SearchLocationsActivityFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.activity_search_location_page_container,searchLocationsActivityFragment).commit();
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

    @Override
    public void OnGoBackChanged() {
        onBackPressed();
    }
}
