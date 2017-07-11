package com.patrickwallin.projects.collegeinformation.data;

import android.content.ContentValues;
import android.database.Cursor;

import org.parceler.Parcel;

/**
 * Created by piwal on 6/25/2017.
 */

@Parcel
public class NameData {
    int mId;
    String mName;
    String mState;
    String mCity;
    String mZip;
    String mImageLink;

    public NameData() {}

    public NameData(Cursor cursor) {
        if(cursor != null) {
            setId(cursor.getInt(NameContract.NameEntry.COL_NAME_ID));
            setName(cursor.getString(NameContract.NameEntry.COL_NAME_NAME));
            setState(cursor.getString(NameContract.NameEntry.COL_NAME_STATE));
            setCity(cursor.getString(NameContract.NameEntry.COL_NAME_CITY));
            setZip(cursor.getString(NameContract.NameEntry.COL_NAME_ZIP));
            setImageLink(cursor.getString(NameContract.NameEntry.COL_NAME_IMAGE_LINK));
        }
    }

    public NameData(int id, String name, String state, String city, String zip, String imageLink) {
        mId = id;
        mName = name;
        mState = state;
        mCity = city;
        mZip = zip;
        mImageLink = imageLink;
    }

    public int getId() { return mId; }
    public void setId(int id) { mId = id; }

    public String getName() { return mName; }
    public void setName(String name) { mName = name; }

    public String getState() { return mState; }
    public void setState(String state) { mState = state; }

    public String getCity() { return mCity; }
    public void setCity(String city) { mCity = city; }

    public String getZip() { return mZip; }
    public void setZip(String zip) { mZip = zip; }

    public String getImageLink() { return mImageLink; }
    public void setImageLink(String imageLink) { mImageLink = imageLink; }


    public ContentValues getNamesContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(NameContract.NameEntry.COLUMN_NAME_ID, getId());
        contentValues.put(NameContract.NameEntry.COLUMN_NAME_NAME, getName());
        contentValues.put(NameContract.NameEntry.COLUMN_NAME_STATE, getState());
        contentValues.put(NameContract.NameEntry.COLUMN_NAME_CITY, getCity());
        contentValues.put(NameContract.NameEntry.COLUMN_NAME_ZIP, getZip());
        contentValues.put(NameContract.NameEntry.COLUMN_NAME_IMAGE_LINK, getImageLink());

        return contentValues;
    }
}
