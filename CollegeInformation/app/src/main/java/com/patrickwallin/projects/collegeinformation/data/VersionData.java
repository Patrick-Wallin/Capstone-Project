package com.patrickwallin.projects.collegeinformation.data;

import android.content.ContentValues;
import android.database.Cursor;

import org.parceler.Parcel;

/**
 * Created by piwal on 6/10/2017.
 */

@Parcel
public class VersionData {
    int mId;
    int mVersionNumber;
    String mTableName;

    public VersionData() {}

    public VersionData(Cursor cursorData) {
        if(cursorData != null) {
            setId(cursorData.getInt(VersionContract.VersionEntry.COL_VERSION_ID));
            setVersionNumber(cursorData.getInt(VersionContract.VersionEntry.COL_VERSION_NUMBER));
            setTableName(cursorData.getString(VersionContract.VersionEntry.COL_VERSION_TABLE_NAME));
        }
    }


    public VersionData(int id, String tableName, int versionNumber) {
        mId = id;
        mTableName = tableName;
        mVersionNumber = versionNumber;
    }

    public int getId() { return mId; }
    public void setId(int id) { mId = id; }

    public String getTableName() { return mTableName; }
    public void setTableName(String tableName) { mTableName = tableName; }

    public int getVersionNumber() { return mVersionNumber; }
    public void setVersionNumber(int versionNumber) { mVersionNumber = versionNumber; }

    public ContentValues getVersionContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(VersionContract.VersionEntry.COLUMN_VERSION_ID, getId());
        contentValues.put(VersionContract.VersionEntry.COLUMN_VERSION_NUMBER, getVersionNumber());
        contentValues.put(VersionContract.VersionEntry.COLUMN_VERSION_TABLE_NAME, getTableName());

        return contentValues;
    }
}
