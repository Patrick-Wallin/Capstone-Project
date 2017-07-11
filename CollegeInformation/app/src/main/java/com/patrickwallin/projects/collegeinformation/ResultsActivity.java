package com.patrickwallin.projects.collegeinformation;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piwal on 6/27/2017.
 */

public class ResultsActivity extends AppCompatActivity implements OnResultOptionSelectionChangeListener, OnGoBackChangeListener {
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

        if(savedInstanceState == null) {
            boolean favoriteResults = false;
            int idFromWidget = 0;
            Intent intent = getIntent();
            if(intent != null) {
                if(intent.hasExtra(getString(R.string.favorite_results)))
                    favoriteResults = intent.getBooleanExtra(getString(R.string.favorite_results),false);
                if(intent.hasExtra(getString(R.string.id)))
                    idFromWidget = intent.getIntExtra(getString(R.string.id),0);
            }
            Bundle bundle = new Bundle();
            bundle.putBoolean(getString(R.string.favorite_results),favoriteResults);
            bundle.putInt(getString(R.string.id),idFromWidget);
            ResultsActivityFragment resultsActivityFragment = new ResultsActivityFragment();
            resultsActivityFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_result_page_container, resultsActivityFragment).commit();
        }else {

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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

    @Override
    public void OnSelectionChanged(Bundle bundle, ImageView imageView) {
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
    }

    @Override
    public void OnGoBackChanged() {
        onBackPressed();
    }
}
