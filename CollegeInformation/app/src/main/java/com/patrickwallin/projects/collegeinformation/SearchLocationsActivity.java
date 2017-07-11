package com.patrickwallin.projects.collegeinformation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class SearchLocationsActivity extends AppCompatActivity implements OnGoBackChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null) {
            SearchLocationsActivityFragment searchLocationsActivityFragment = new SearchLocationsActivityFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.activity_search_location_page_container,searchLocationsActivityFragment).commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
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
