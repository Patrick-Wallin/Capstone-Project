package com.patrickwallin.projects.collegeinformation;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by piwal on 6/22/2017.
 */

public class SearchProgramsActivity extends AppCompatActivity implements OnGoBackChangeListener  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_programs);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //if(!getResources().getBoolean(R.bool.is_this_tablet)){
        //    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //}

        if(savedInstanceState == null) {
            SearchProgramsActivityFragment searchProgramsActivityFragment = new SearchProgramsActivityFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.activity_search_programs_page_container,searchProgramsActivityFragment).commit();
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
