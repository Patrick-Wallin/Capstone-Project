package com.patrickwallin.projects.collegeinformation.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by piwal on 6/7/2017.
 */

public class CollegeInfoDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "collegeinfo.db";
    private static int DATABASE_VERSION = 1;

    public CollegeInfoDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DegreeContract.getCreateTableStatement());
        db.execSQL(ProgramContract.getCreateTableStatement());
        db.execSQL(VersionContract.getCreateTableStatement());
        db.execSQL(StatesContract.getCreateTableStatement());
        db.execSQL(RegionsContract.getCreateTableStatement());
        db.execSQL(NameContract.getCreateTableStatement());
        db.execSQL(FavoriteCollegeContract.getCreateTableStatement());
        db.execSQL(SearchQueryInputContract.getCreateTableStatement());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DegreeContract.DegreeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ProgramContract.ProgramEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + VersionContract.VersionEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + StatesContract.StateEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RegionsContract.RegionEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + NameContract.NameEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteCollegeContract.FavoriteCollegeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SearchQueryInputContract.SearchQueryInputEntry.TABLE_NAME);
        onCreate(db);
    }
}
