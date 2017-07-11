package com.patrickwallin.projects.collegeinformation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class SearchDegreesActivity extends AppCompatActivity implements OnGoBackChangeListener {
    public SearchDegreesActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_degrees);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null) {
            SearchDegreesActivityFragment searchDegreesActivityFragment = new SearchDegreesActivityFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_search_degrees_page_container,searchDegreesActivityFragment).commit();
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
