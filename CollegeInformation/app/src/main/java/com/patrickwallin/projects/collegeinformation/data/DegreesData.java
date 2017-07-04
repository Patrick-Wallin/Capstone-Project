package com.patrickwallin.projects.collegeinformation.data;

import android.content.ContentValues;
import android.database.Cursor;

import org.parceler.Parcel;

/**
 * Created by piwal on 6/6/2017.
 */

@Parcel
public class DegreesData {
    int mId;
    String mName;

    public DegreesData() {}

    public DegreesData(Cursor cursor) {
        if(cursor != null) {
            setId(cursor.getInt(DegreeContract.DegreeEntry.COL_DEGREE_ID));
            setName(cursor.getString(DegreeContract.DegreeEntry.COL_DEGREE_TITLE));
        }
    }

    public DegreesData(int id, String name) {
        mId = id;
        mName = name;
    }

    public int getId() { return mId; }
    public void setId(int id) { mId = id; }

    public String getName() { return mName; }
    public void setName(String name) { mName = name; }

    public ContentValues getDegreesContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DegreeContract.DegreeEntry.COLUMN_DEGREE_ID, getId());
        contentValues.put(DegreeContract.DegreeEntry.COLUMN_DEGREE_TITLE, getName());

        return contentValues;
    }
}
