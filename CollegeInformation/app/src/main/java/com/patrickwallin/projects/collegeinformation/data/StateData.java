package com.patrickwallin.projects.collegeinformation.data;

import android.content.ContentValues;
import android.database.Cursor;

import org.parceler.Parcel;

/**
 * Created by piwal on 6/24/2017.
 */

@Parcel
public class StateData {
    int mId;
    String mName;
    boolean mSelected;

    public StateData() {}

    public StateData(Cursor cursor) {
        if(cursor != null) {
            setId(cursor.getInt(StatesContract.StateEntry.COL_STATE_ID));
            setName(cursor.getString(StatesContract.StateEntry.COL_STATE_NAME));
        }
    }


    public StateData(int id, String name) {
        mId = id;
        mName = name;
    }

    public int getId() { return mId; }
    public void setId(int id) { mId = id; }

    public String getName() { return mName; }
    public void setName(String name) { mName = name; }

    public boolean getSelected() { return mSelected; }
    public void setSelected(boolean selected) { mSelected = selected; }

    public ContentValues getStateContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(StatesContract.StateEntry.COLUMN_STATE_ID, getId());
        contentValues.put(StatesContract.StateEntry.COLUMN_STATE_NAME, getName());

        return contentValues;
    }


}
