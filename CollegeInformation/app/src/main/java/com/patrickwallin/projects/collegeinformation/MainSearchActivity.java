package com.patrickwallin.projects.collegeinformation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainSearchActivity extends AppCompatActivity implements OnSearchOptionSelectionChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);

        MainSearchActivityFragment mainSearchActivityFragment = new MainSearchActivityFragment();
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(getString(R.string.id))) {
            Bundle bundle = new Bundle();
            bundle.putInt(getString(R.string.id),intent.getIntExtra(getString(R.string.id),0));
            mainSearchActivityFragment.setArguments(bundle);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_search_page_container, mainSearchActivityFragment).commit();
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
    }

}
