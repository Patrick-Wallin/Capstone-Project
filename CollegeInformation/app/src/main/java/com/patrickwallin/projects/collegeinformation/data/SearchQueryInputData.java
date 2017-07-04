package com.patrickwallin.projects.collegeinformation.data;

import android.content.ContentValues;
import android.database.Cursor;

import org.parceler.Parcel;

/**
 * Created by piwal on 6/6/2017.
 */

@Parcel
public class SearchQueryInputData {
    int mId;
    String mName;
    String mInputValue;

    public SearchQueryInputData() {}

    public SearchQueryInputData(Cursor cursor) {
        if(cursor != null) {
            setId(cursor.getInt(SearchQueryInputContract.SearchQueryInputEntry.COL_QUERY_ID));
            setName(cursor.getString(SearchQueryInputContract.SearchQueryInputEntry.COL_QUERY_NAME));
            setValue(cursor.getString(SearchQueryInputContract.SearchQueryInputEntry.COL_QUERY_VALUE));
        }
    }

    public SearchQueryInputData(int id, String name, String inputValue) {
        mId = id;
        mName = name;
        mInputValue = inputValue;
    }

    public int getId() { return mId; }
    public void setId(int id) { mId = id; }

    public String getName() { return mName; }
    public void setName(String name) { mName = name; }

    public String getValue() { return mInputValue; }
    public void setValue(String value) { mInputValue = value; }

    public ContentValues getSearchQueryInputContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(SearchQueryInputContract.SearchQueryInputEntry.COLUMN_QUERY_ID, getId());
        contentValues.put(SearchQueryInputContract.SearchQueryInputEntry.COLUMN_QUERY_NAME, getName());
        contentValues.put(SearchQueryInputContract.SearchQueryInputEntry.COLUMN_QUERY_VALUE, getValue());

        return contentValues;
    }
}
