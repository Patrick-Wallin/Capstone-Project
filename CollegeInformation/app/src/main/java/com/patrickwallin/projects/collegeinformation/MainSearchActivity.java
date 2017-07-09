package com.patrickwallin.projects.collegeinformation;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.FrameLayout;

import com.patrickwallin.projects.collegeinformation.adapter.MainSearchPageAdapter;
import com.patrickwallin.projects.collegeinformation.data.MainSearchPage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainSearchActivity extends AppCompatActivity implements OnSearchOptionSelectionChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);

        //if(!getResources().getBoolean(R.bool.is_this_tablet)){
        //    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //}

        //if (findViewById(R.id.activity_search_page_container) != null) {
            MainSearchActivityFragment mainSearchActivityFragment = new MainSearchActivityFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_search_page_container, mainSearchActivityFragment).commit();
       // }else {
        //    MainSearchActivityFragment mainSearchActivityFragment = new MainSearchActivityFragment();
       //    getSupportFragmentManager().beginTransaction().replace(R.id.searches_fragment, mainSearchActivityFragment).commit();
        //    SearchDegreesActivityFragment searchDegreesActivityFragment = new SearchDegreesActivityFragment();
         //   getSupportFragmentManager().beginTransaction().replace(R.id.search_detail_fragment, searchDegreesActivityFragment).commit();
        //}
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    @Override
    public void OnSelectionChanged(int imageId) {
       // if (findViewById(R.id.activity_search_page_container) != null) {

            if(imageId == R.drawable.ic_graduation_cap) {
                Intent intentDegreesActivity = new Intent(this, SearchDegreesActivity.class);
                this.startActivity(intentDegreesActivity);
            }else {
                if(imageId == R.drawable.ic_college_book) {
                    Intent intentProgramActivity = new Intent(this, SearchProgramsActivity.class);
                    this.startActivity(intentProgramActivity);
                }else {
                    if(imageId == R.drawable.ic_map_location) {
                        Intent intentLocationActivity = new Intent(this, SearchLocationsActivity.class);
                        this.startActivity(intentLocationActivity);
                    }else {
                        if(imageId == R.drawable.ic_college_building) {
                            Intent intentNamesActivity = new Intent(this, SearchNamesActivity.class);
                            this.startActivity(intentNamesActivity);
                        }
                    }
                }
            }
/*
        }else {
            if(imageId == R.drawable.ic_graduation_cap) {
                SearchDegreesActivityFragment searchDegreesActivityFragment = new SearchDegreesActivityFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.search_detail_fragment, searchDegreesActivityFragment).commit();
            }else {
                if(imageId == R.drawable.ic_college_book) {
                    SearchProgramsActivityFragment searchProgramsActivityFragment = new SearchProgramsActivityFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.search_detail_fragment, searchProgramsActivityFragment).commit();
                }else {
                    if(imageId == R.drawable.ic_map_location) {
                        SearchLocationsActivityFragment searchLocationsActivityFragment = new SearchLocationsActivityFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.search_detail_fragment, searchLocationsActivityFragment).commit();
                    }else {
                        if(imageId == R.drawable.ic_college_building) {
                            SearchNamesActivityFragment searchNamesActivityFragment = new SearchNamesActivityFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.search_detail_fragment, searchNamesActivityFragment).commit();
                        }
                    }
                }
            }
        }
        */
    }

}
