package com.patrickwallin.projects.collegeinformation;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by piwal on 6/27/2017.
 */

public class ResultsActivity extends AppCompatActivity implements OnResultOptionSelectionChangeListener {
    private OnDataSelectionChangeListener listener;

    public ResultsActivity() {
        this.listener = null;
    }

    public void setDataSelectionChangeListener(OnDataSelectionChangeListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       // if(!getResources().getBoolean(R.bool.is_this_tablet)){
       //     setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
      //  }

        if(savedInstanceState == null) {
            //if (findViewById(R.id.activity_result_page_container) != null) {
            ResultsActivityFragment resultsActivityFragment = new ResultsActivityFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_result_page_container, resultsActivityFragment).commit();
        }else {

        }
        //}else {
        //    ResultsActivityFragment resultsActivityFragment = new ResultsActivityFragment();
        //    getSupportFragmentManager().beginTransaction().replace(R.id.results_fragment,resultsActivityFragment).commit();
         //   ResultDetailActivityFragment resultDetailActivityFragment = new ResultDetailActivityFragment();
         //   getSupportFragmentManager().beginTransaction().replace(R.id.results_detail_fragment,resultDetailActivityFragment).commit();
        //}
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
    public void OnSelectionChanged(Bundle bundle, ImageView imageView) {
        //if (findViewById(R.id.activity_result_page_container) != null) {
            Intent intentResultDetailActivity = new Intent(this, ResultDetailActivity.class);
            intentResultDetailActivity.putExtras(bundle);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                View statusBar = findViewById(android.R.id.statusBarBackground);
                View navigationBar = findViewById(android.R.id.navigationBarBackground);

                List<Pair<View, String>> pairs = new ArrayList<>();
                if(statusBar != null)
                    pairs.add(Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));
                if(navigationBar != null)
                    pairs.add(Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME));
                pairs.add(Pair.create((View)imageView, imageView.getTransitionName()));

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs.toArray(new Pair[pairs.size()]));
                startActivity(intentResultDetailActivity, options.toBundle());
            }else {
                startActivity(intentResultDetailActivity);
            }
            //this.startActivity(intentResultDetailActivity);
        /*
        }else {
            listener.OnDataSelectionChanged(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            ResultDetailActivityFragment fragment = (ResultDetailActivityFragment)fragmentManager.findFragmentById(R.id.results_detail_fragment);

            getSupportFragmentManager()
                    .beginTransaction()
                    .detach(fragment)
                    .attach(fragment)
                    .commit();
            //getSupportFragmentManager().beginTransaction().replace(R.id.results_detail_fragment,currentFragment).commit();
        }
        */
    }
}
