package com.patrickwallin.projects.collegeinformation.data;

import android.content.ContentValues;
import android.database.Cursor;

import org.parceler.Parcel;

/**
 * Created by piwal on 6/25/2017.
 */

@Parcel
public class FavoriteCollegeData {
    int mId;
    String mName;
    String mState;
    String mCity;
    String mZip;
    String mLatitude;
    String mLongitude;
    String mWebsite;
    String mImageLink;
    int mCostInState;
    int mCostOutState;
    double mPctFedLoans;
    double mPctPellGrants;
    double mSATCriticalReading25;
    double mSATCriticalReading75;
    double mSATMath25;
    double mSATMath75;
    double mSATWriting25;
    double mSATWriting75;
    double mACT25;
    double mACT75;

    public FavoriteCollegeData() {}

    public FavoriteCollegeData(Cursor cursor) {
        if(cursor != null) {
            if(cursor.getColumnIndex(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_ID) > -1)
                setId(cursor.getInt(FavoriteCollegeContract.FavoriteCollegeEntry.COL_FAVORITE_ID));
            else
                setId(0);
            if(cursor.getColumnIndex(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_NAME) > -1)
                setName(cursor.getString(FavoriteCollegeContract.FavoriteCollegeEntry.COL_FAVORITE_NAME));
            else
                setName("");
            if(cursor.getColumnIndex(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_STATE) > -1)
                setState(cursor.getString(FavoriteCollegeContract.FavoriteCollegeEntry.COL_FAVORITE_STATE));
            else
                setState("");
            if(cursor.getColumnIndex(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_CITY) > -1)
                setCity(cursor.getString(FavoriteCollegeContract.FavoriteCollegeEntry.COL_FAVORITE_CITY));
            else
                setCity("");
            if(cursor.getColumnIndex(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_ZIP) > -1)
                setZip(cursor.getString(FavoriteCollegeContract.FavoriteCollegeEntry.COL_FAVORITE_ZIP));
            else
                setZip("");
            if(cursor.getColumnIndex(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_LATITUDE) > -1)
                setLatitude(cursor.getString(FavoriteCollegeContract.FavoriteCollegeEntry.COL_FAVORITE_LATITUDE));
            else
                setLatitude("");
            if(cursor.getColumnIndex(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_LONGITUDE) > -1)
                setLongitude(cursor.getString(FavoriteCollegeContract.FavoriteCollegeEntry.COL_FAVORITE_LONGITUDE));
            else
                setLongitude("");
            if(cursor.getColumnIndex(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_WEBSITE) > -1)
                setWebsite(cursor.getString(FavoriteCollegeContract.FavoriteCollegeEntry.COL_FAVORITE_WEBSITE));
            else
                setWebsite("");
            setImageLink(cursor.getString(FavoriteCollegeContract.FavoriteCollegeEntry.COL_FAVORITE_IMAGE_LINK));
            setCostInState(cursor.getInt(FavoriteCollegeContract.FavoriteCollegeEntry.COL_FAVORITE_COST_IN_STATE));
            setCostOutState(cursor.getInt(FavoriteCollegeContract.FavoriteCollegeEntry.COL_FAVORITE_COST_OUT_STATE));
            setPctFedLoans(cursor.getDouble(FavoriteCollegeContract.FavoriteCollegeEntry.COL_FAVORITE_PCT_FED_LOANS));
            setPctPellGrants(cursor.getDouble(FavoriteCollegeContract.FavoriteCollegeEntry.COL_FAVORITE_PCT_PELL_GRANTS));
            setSATCriticalReading25(cursor.getDouble(FavoriteCollegeContract.FavoriteCollegeEntry.COL_FAVORITE_SAT_CRITICAL_READING_25));
            setSATCriticalReading75(cursor.getDouble(FavoriteCollegeContract.FavoriteCollegeEntry.COL_FAVORITE_SAT_CRITICAL_READING_75));
            setSATMath25(cursor.getDouble(FavoriteCollegeContract.FavoriteCollegeEntry.COL_FAVORITE_SAT_MATH_25));
            setSATMath75(cursor.getDouble(FavoriteCollegeContract.FavoriteCollegeEntry.COL_FAVORITE_SAT_MATH_75));
            setSATWriting25(cursor.getDouble(FavoriteCollegeContract.FavoriteCollegeEntry.COL_FAVORITE_SAT_WRITING_25));
            setSATWriting75(cursor.getDouble(FavoriteCollegeContract.FavoriteCollegeEntry.COL_FAVORITE_SAT_WRITING_75));
            setACT25(cursor.getDouble(FavoriteCollegeContract.FavoriteCollegeEntry.COL_FAVORITE_ACT_25));
            setACT75(cursor.getDouble(FavoriteCollegeContract.FavoriteCollegeEntry.COL_FAVORITE_ACT_75));
        }
    }

    public FavoriteCollegeData(int id, String name, String state, String city, String zip, String latitude, String longitude, String website, String imageLink,
                               int costInState, int costOutState, double pctFedLoans, double pctPellGrants, double satCriticalReading25, double satCriticalReading75,
                               double satMath25, double satMath75, double satWriting25, double satWriting75, double act25, double act75) {
        mId = id;
        mName = name;
        mState = state;
        mCity = city;
        mZip = zip;
        mLatitude = latitude;
        mLongitude = longitude;
        mWebsite = website;
        mImageLink = imageLink;
        mCostInState = costInState;
        mCostOutState = costOutState;
        mPctFedLoans = pctFedLoans;
        mPctPellGrants = pctPellGrants;
        mSATCriticalReading25 = satCriticalReading25;
        mSATCriticalReading75 = satCriticalReading75;
        mSATMath25 = satMath25;
        mSATMath75 = satMath75;
        mSATWriting25 = satWriting25;
        mSATWriting75 = satWriting75;
        mACT25 = act25;
        mACT75 = act75;
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

    public String getLatitude() { return mLatitude; }
    public void setLatitude(String latitude) { mLatitude = latitude; }

    public String getLongitude() { return mLongitude; }
    public void setLongitude(String longitude) { mLongitude = longitude; }

    public String getWebsite() { return mWebsite; }
    public void setWebsite(String website) { mWebsite = website; }

    public String getImageLink() { return mImageLink; }
    public void setImageLink(String imageLink) { mImageLink = imageLink; }

    public int getCostInState() { return mCostInState; }
    public void setCostInState(int costInState) { mCostInState = costInState; }

    public int getCostOutState() { return mCostOutState; }
    public void setCostOutState(int costOutState) { mCostOutState = costOutState; }

    public double getPctFedLoans() { return mPctFedLoans; }
    public void setPctFedLoans(double pctFedLoans) { mPctFedLoans = pctFedLoans; }

    public double getPctPellGrants() { return mPctPellGrants; }
    public void setPctPellGrants(double pctPellGrants) { mPctPellGrants = pctPellGrants; }

    public double getSATCriticalReading25() { return mSATCriticalReading25; }
    public void setSATCriticalReading25(double satCriticalReading25) { mSATCriticalReading25 = satCriticalReading25; }

    public double getSATCriticalReading75() { return mSATCriticalReading75; }
    public void setSATCriticalReading75(double satCriticalReading75) { mSATCriticalReading75 = satCriticalReading75; }

    public double getSATMath25() { return mSATMath25; }
    public void setSATMath25(double satMath25) { mSATMath25 = satMath25; }

    public double getSATMath75() { return mSATMath75; }
    public void setSATMath75(double satMath75) { mSATMath75 = satMath75; }

    public double getSATWriting25() { return mSATWriting25; }
    public void setSATWriting25(double satWriting25) { mSATWriting25 = satWriting25; }

    public double getSATWriting75() { return mSATWriting75; }
    public void setSATWriting75(double satWriting75) { mSATWriting75 = satWriting75; }

    public double getACT25() { return mACT25; }
    public void setACT25(double act25) { mACT25 = act25; }

    public double getACT75() { return mACT75; }
    public void setACT75(double act75) { mACT75 = act75; }

    public ContentValues getFavoriteCollegeContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_ID, getId());
        contentValues.put(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_NAME, getName());
        contentValues.put(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_STATE, getState());
        contentValues.put(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_CITY, getCity());
        contentValues.put(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_ZIP, getZip());
        contentValues.put(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_LATITUDE, getLatitude());
        contentValues.put(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_LONGITUDE, getLongitude());
        contentValues.put(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_WEBSITE, getWebsite());
        contentValues.put(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_IMAGE_LINK, getImageLink());
        contentValues.put(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_COST_IN_STATE, getCostInState());
        contentValues.put(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_COST_OUT_STATE, getCostOutState());
        contentValues.put(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_PCT_FED_LOANS, getPctFedLoans());
        contentValues.put(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_PCT_PELL_GRANTS, getPctPellGrants());
        contentValues.put(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_SAT_CRITICAL_READING_25, getSATCriticalReading25());
        contentValues.put(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_SAT_CRITICAL_READING_75, getSATCriticalReading75());
        contentValues.put(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_SAT_MATH_25, getSATMath25());
        contentValues.put(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_SAT_MATH_75, getSATMath75());
        contentValues.put(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_SAT_WRITING_25, getSATWriting25());
        contentValues.put(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_SAT_WRITING_75, getSATWriting75());
        contentValues.put(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_ACT_25, getACT25());
        contentValues.put(FavoriteCollegeContract.FavoriteCollegeEntry.COLUMN_FAVORITE_ACT_75, getACT75());

        return contentValues;
    }
}
