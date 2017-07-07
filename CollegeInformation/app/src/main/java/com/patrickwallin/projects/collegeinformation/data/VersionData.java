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
    int mPriorVersionNumber;
    String mTableName;

    public VersionData() {}

    public VersionData(Cursor cursorData) {
        if(cursorData != null) {
            setId(cursorData.getInt(VersionContract.VersionEntry.COL_VERSION_ID));
            setVersionNumber(cursorData.getInt(VersionContract.VersionEntry.COL_VERSION_NUMBER));
            setTableName(cursorData.getString(VersionContract.VersionEntry.COL_VERSION_TABLE_NAME));
            setPriorVersionNumber(cursorData.getInt(VersionContract.VersionEntry.COL_PRIOR_VERSION_NUMBER));
        }
    }


    public VersionData(int id, String tableName, int versionNumber, int priorVersionNumber) {
        mId = id;
        mTableName = tableName;
        mVersionNumber = versionNumber;
        mPriorVersionNumber = priorVersionNumber;
    }

    public int getId() { return mId; }
    public void setId(int id) { mId = id; }

    public String getTableName() { return mTableName; }
    public void setTableName(String tableName) { mTableName = tableName; }

    public int getVersionNumber() { return mVersionNumber; }
    public void setVersionNumber(int versionNumber) { mVersionNumber = versionNumber; }

    public int getPriorVersionNumber() { return mPriorVersionNumber; }
    public void setPriorVersionNumber(int priorVersionNumber) { mPriorVersionNumber = priorVersionNumber; }

    public ContentValues getVersionContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(VersionContract.VersionEntry.COLUMN_VERSION_ID, getId());
        contentValues.put(VersionContract.VersionEntry.COLUMN_VERSION_NUMBER, getVersionNumber());
        contentValues.put(VersionContract.VersionEntry.COLUMN_VERSION_TABLE_NAME, getTableName());
        contentValues.put(VersionContract.VersionEntry.COLUMN_PRIOR_VERSION_NUMBER, getPriorVersionNumber());

        return contentValues;
    }
}
