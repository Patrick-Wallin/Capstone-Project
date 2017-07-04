package com.patrickwallin.projects.collegeinformation.data;

import android.content.ContentValues;
import android.database.Cursor;

import org.parceler.Parcel;

/**
 * Created by piwal on 6/24/2017.
 */

@Parcel
public class RegionData {
    int mId;
    String mName;
    String mDescription;
    boolean mSelected;

    public RegionData() {}

    public RegionData(Cursor cursor) {
        if(cursor != null) {
            setId(cursor.getInt(RegionsContract.RegionEntry.COL_REGION_ID));
            setName(cursor.getString(RegionsContract.RegionEntry.COL_REGION_NAME));
            setDescription(cursor.getString(RegionsContract.RegionEntry.COL_REGION_DESCRIPTION));
        }
    }

    public RegionData(int id, String name, String description) {
        mId = id;
        mName = name;
        mDescription = description;
    }

    public int getId() { return mId; }
    public void setId(int id) { mId = id; }

    public String getName() { return mName; }
    public void setName(String name) { mName = name; }

    public String getDescription() { return mDescription; }
    public void setDescription(String description) { mDescription = description; }

    public boolean getSelected() { return mSelected; }
    public void setSelected(boolean selected) { mSelected = selected; }

    public ContentValues getRegionContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(RegionsContract.RegionEntry.COLUMN_REGION_ID, getId());
        contentValues.put(RegionsContract.RegionEntry.COLUMN_REGION_NAME, getName());
        contentValues.put(RegionsContract.RegionEntry.COLUMN_REGION_DESCRIPTION, getDescription());

        return contentValues;
    }


}
