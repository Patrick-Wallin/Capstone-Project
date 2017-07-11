package com.patrickwallin.projects.collegeinformation;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.patrickwallin.projects.collegeinformation.asynctask.FetchSearchQueryInputTask;
import com.patrickwallin.projects.collegeinformation.data.NameContract;
import com.patrickwallin.projects.collegeinformation.data.NameData;
import com.patrickwallin.projects.collegeinformation.data.SearchQueryInputContract;
import com.patrickwallin.projects.collegeinformation.data.SearchQueryInputData;
import com.patrickwallin.projects.collegeinformation.utilities.CursorAndDataConverter;

import java.util.List;

/**
 * Created by piwal on 6/22/2017.
 */

public class SearchNamesActivity extends AppCompatActivity implements OnGoBackChangeListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_names);

        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null) {
            SearchNamesActivityFragment searchNamesActivityFragment = new SearchNamesActivityFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_search_names_page_container,searchNamesActivityFragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                boolean bFinish = false;
                final String sqlWhere = SearchQueryInputContract.SearchQueryInputEntry.COLUMN_QUERY_ID + " = " + FetchSearchQueryInputTask.SEARCH_QUERY_NAMES_ID;
                Cursor cursor = this.getContentResolver().query(SearchQueryInputContract.SearchQueryInputEntry.CONTENT_URI,null,sqlWhere,null,null);
                if(cursor != null && cursor.moveToFirst()) {
                    List<SearchQueryInputData> searchQueryInputDataList = CursorAndDataConverter.getSearchQueryInputDataFromCursor(cursor);
                    if (searchQueryInputDataList != null && searchQueryInputDataList.size() > 0) {
                        final SearchQueryInputData searchQueryInputData = searchQueryInputDataList.get(0);
                        String value = searchQueryInputData.getValue().trim();
                        if(!value.isEmpty()) {
                            if(Integer.valueOf(value.trim()) > -1) {
                                String nameOfCollege = "";
                                String sqlWhereNameId = NameContract.NameEntry.COLUMN_NAME_ID + " = " + value.trim();
                                Cursor nameCursor = this.getContentResolver().query(NameContract.NameEntry.CONTENT_URI,null,sqlWhereNameId,null,null);
                                if(nameCursor != null && nameCursor.moveToFirst()) {
                                    NameData nameData = new NameData(nameCursor);
                                    nameOfCollege = nameData.getName();
                                }
                                if(nameCursor != null)
                                    nameCursor.close();

                                bFinish = true;
                                new AlertDialog.Builder(this)
                                        .setTitle(getString(R.string.question_sure_to_select) + nameOfCollege + getString(R.string.question_mark))
                                        .setMessage(getString(R.string.info_message_cancel_name))
                                        .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                finish();
                                            }
                                        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                SearchQueryInputData updateSearchQueryInputData = searchQueryInputData;
                                                updateSearchQueryInputData.setValue("");
                                                ContentValues cv = updateSearchQueryInputData.getSearchQueryInputContentValues();
                                                SearchNamesActivity.this.getContentResolver().update(SearchQueryInputContract.SearchQueryInputEntry.CONTENT_URI,
                                                        cv,sqlWhere,null);
                                                dialog.dismiss();
                                                finish();
                                            }
                                        }).show();

                            }
                        }
                    }
                }
                if(cursor != null)
                    cursor.close();
                if(!bFinish)
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
