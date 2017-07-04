package com.patrickwallin.projects.collegeinformation.data;

import android.content.ContentValues;
import android.database.Cursor;

import org.parceler.Parcel;

/**
 * Created by piwal on 6/8/2017.
 */

@Parcel
public class ProgramData {
    int mId;
    String mTitle;
    String mUrlName;

    public ProgramData() {}

    public ProgramData(Cursor cursor) {
        if(cursor != null) {
            setId(cursor.getInt(ProgramContract.ProgramEntry.COL_PROGRAM_ID));
            setTitle(cursor.getString(ProgramContract.ProgramEntry.COL_PROGRAM_TITLE));
            setUrlName(cursor.getString(ProgramContract.ProgramEntry.COL_PROGRAM_URL_NAME));
        }
    }


    public ProgramData(int id, String title, String urlName) {
        mId = id;
        mTitle = title;
        mUrlName = urlName;
    }

    public int getId() { return mId; }
    public void setId(int id) { mId = id; }

    public String getTitle() { return mTitle; }
    public void setTitle(String title) { mTitle = title; }

    public String getUrlName() { return mUrlName; }
    public void setUrlName(String urlName) { mUrlName = urlName; }

    public ContentValues getProgramContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ProgramContract.ProgramEntry.COLUMN_PROGRAM_ID, getId());
        contentValues.put(ProgramContract.ProgramEntry.COLUMN_PROGRAM_TITLE, getTitle());
        contentValues.put(ProgramContract.ProgramEntry.COLUMN_PROGRAM_URL_NAME, getUrlName());

        return contentValues;
    }


}
