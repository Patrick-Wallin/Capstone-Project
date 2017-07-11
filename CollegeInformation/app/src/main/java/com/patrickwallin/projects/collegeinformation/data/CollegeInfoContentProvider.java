package com.patrickwallin.projects.collegeinformation.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by piwal on 6/7/2017.
 */

public class CollegeInfoContentProvider extends ContentProvider {
    public static final int DEGREES = 100;
    public static final int DEGREES_WITH_ID = 101;

    public static final int PROGRAMS = 200;
    public static final int PROGRAMS_WITH_ID = 201;

    public static final int VERSIONS = 300;
    public static final int VERSIONS_WITH_ID = 301;

    public static final int STATES = 400;
    public static final int STATES_WITH_ID = 401;

    public static final int REGIONS = 500;
    public static final int REGIONS_WITH_ID = 501;

    public static final int NAMES = 600;
    public static final int NAMES_WITH_ID = 601;

    public static final int FAVORITE = 700;
    public static final int FAVORITE_WITH_ID = 701;

    public static final int QUERYS = 800;
    public static final int QUERYS_WITH_ID = 801;

    private CollegeInfoDBHelper mCollegeInfoDBHelper;

    private static UriMatcher uriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(DegreeContract.CONTENT_AUTHORITY, DegreeContract.PATH_DEGREES, DEGREES);
        uriMatcher.addURI(DegreeContract.CONTENT_AUTHORITY, DegreeContract.PATH_DEGREES + "/#", DEGREES_WITH_ID);

        uriMatcher.addURI(ProgramContract.CONTENT_AUTHORITY, ProgramContract.PATH_PROGRAMS, PROGRAMS);
        uriMatcher.addURI(ProgramContract.CONTENT_AUTHORITY, ProgramContract.PATH_PROGRAMS + "/#", PROGRAMS_WITH_ID);

        uriMatcher.addURI(VersionContract.CONTENT_AUTHORITY, VersionContract.PATH_VERSIONS, VERSIONS);
        uriMatcher.addURI(VersionContract.CONTENT_AUTHORITY, VersionContract.PATH_VERSIONS + "/#", VERSIONS_WITH_ID);

        uriMatcher.addURI(StatesContract.CONTENT_AUTHORITY, StatesContract.PATH_STATES, STATES);
        uriMatcher.addURI(StatesContract.CONTENT_AUTHORITY, StatesContract.PATH_STATES + "/#", STATES_WITH_ID);

        uriMatcher.addURI(RegionsContract.CONTENT_AUTHORITY, RegionsContract.PATH_REGIONS, REGIONS);
        uriMatcher.addURI(RegionsContract.CONTENT_AUTHORITY, RegionsContract.PATH_REGIONS + "/#", REGIONS_WITH_ID);

        uriMatcher.addURI(NameContract.CONTENT_AUTHORITY, NameContract.PATH_NAMES, NAMES);
        uriMatcher.addURI(NameContract.CONTENT_AUTHORITY, NameContract.PATH_NAMES + "/#", NAMES_WITH_ID);

        uriMatcher.addURI(FavoriteCollegeContract.CONTENT_AUTHORITY, FavoriteCollegeContract.PATH_FAVORITE, FAVORITE);
        uriMatcher.addURI(FavoriteCollegeContract.CONTENT_AUTHORITY, FavoriteCollegeContract.PATH_FAVORITE + "/#", FAVORITE_WITH_ID);

        uriMatcher.addURI(SearchQueryInputContract.CONTENT_AUTHORITY, SearchQueryInputContract.PATH_QUERY_INPUT, QUERYS);
        uriMatcher.addURI(SearchQueryInputContract.CONTENT_AUTHORITY, SearchQueryInputContract.PATH_QUERY_INPUT + "/#", QUERYS_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mCollegeInfoDBHelper = new CollegeInfoDBHelper(context);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mCollegeInfoDBHelper.getReadableDatabase();

        int match = uriMatcher.match(uri);

        Cursor cursor = null;

        switch(match) {
            case DEGREES:
                cursor = db.query(DegreeContract.DegreeEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case DEGREES_WITH_ID:
                break;
            case PROGRAMS:
                cursor = db.query(ProgramContract.ProgramEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PROGRAMS_WITH_ID:
                break;
            case VERSIONS:
                cursor = db.query(VersionContract.VersionEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case VERSIONS_WITH_ID:
                break;
            case STATES:
                cursor = db.query(StatesContract.StateEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case STATES_WITH_ID:
                break;
            case REGIONS:
                cursor = db.query(RegionsContract.RegionEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case REGIONS_WITH_ID:
                break;
            case NAMES:
                cursor = db.query(NameContract.NameEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case NAMES_WITH_ID:
                break;
            case FAVORITE:
                cursor = db.query(FavoriteCollegeContract.FavoriteCollegeEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case FAVORITE_WITH_ID:
                break;
            case QUERYS:
                cursor = db.query(SearchQueryInputContract.SearchQueryInputEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case QUERYS_WITH_ID:
                break;
            default:
                throw  new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(cursor != null)
            cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = mCollegeInfoDBHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        Uri returnUri;

        switch (match) {
            case DEGREES:
                long degreeId = db.insert(DegreeContract.DegreeEntry.TABLE_NAME, null, values);
                if(degreeId > 0) {
                    returnUri = ContentUris.withAppendedId(DegreeContract.DegreeEntry.CONTENT_URI,degreeId);
                }else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            case PROGRAMS:
                long programId = db.insert(ProgramContract.ProgramEntry.TABLE_NAME, null, values);
                if(programId > 0) {
                    returnUri = ContentUris.withAppendedId(ProgramContract.ProgramEntry.CONTENT_URI,programId);
                }else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            case VERSIONS:
                long versionId = db.insert(VersionContract.VersionEntry.TABLE_NAME, null, values);
                if(versionId > 0) {
                    returnUri = ContentUris.withAppendedId(VersionContract.VersionEntry.CONTENT_URI,versionId);
                }else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            case STATES:
                long stateId = db.insert(StatesContract.StateEntry.TABLE_NAME, null, values);
                if(stateId > 0) {
                    returnUri = ContentUris.withAppendedId(StatesContract.StateEntry.CONTENT_URI,stateId);
                }else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            case REGIONS:
                long regionId = db.insert(RegionsContract.RegionEntry.TABLE_NAME, null, values);
                if(regionId > 0) {
                    returnUri = ContentUris.withAppendedId(RegionsContract.RegionEntry.CONTENT_URI,regionId);
                }else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            case NAMES:
                long nameId = db.insert(NameContract.NameEntry.TABLE_NAME, null, values);
                if(nameId > 0) {
                    returnUri = ContentUris.withAppendedId(NameContract.NameEntry.CONTENT_URI,nameId);
                }else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            case FAVORITE:
                long favoriteId = db.insert(FavoriteCollegeContract.FavoriteCollegeEntry.TABLE_NAME, null, values);
                if(favoriteId > 0) {
                    returnUri = ContentUris.withAppendedId(FavoriteCollegeContract.FavoriteCollegeEntry.CONTENT_URI,favoriteId);
                }else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            case QUERYS:
                long queryId = db.insert(SearchQueryInputContract.SearchQueryInputEntry.TABLE_NAME, null, values);
                if(queryId > 0) {
                    returnUri = ContentUris.withAppendedId(SearchQueryInputContract.SearchQueryInputEntry.CONTENT_URI,queryId);
                }else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mCollegeInfoDBHelper.getWritableDatabase();
        int rowsDeleted = 0;

        int match = uriMatcher.match(uri);

        switch (match) {
            case DEGREES:
                rowsDeleted = db.delete(DegreeContract.DegreeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PROGRAMS:
                rowsDeleted = db.delete(ProgramContract.ProgramEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case VERSIONS:
                rowsDeleted = db.delete(VersionContract.VersionEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case STATES:
                rowsDeleted = db.delete(StatesContract.StateEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REGIONS:
                rowsDeleted = db.delete(RegionsContract.RegionEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case NAMES:
                rowsDeleted = db.delete(NameContract.NameEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVORITE:
                rowsDeleted = db.delete(FavoriteCollegeContract.FavoriteCollegeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case QUERYS:
                rowsDeleted = db.delete(SearchQueryInputContract.SearchQueryInputEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsUpdated = 0;

        SQLiteDatabase db = mCollegeInfoDBHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        switch (match) {
            case DEGREES:
                rowsUpdated = db.update(DegreeContract.DegreeEntry.TABLE_NAME, values, selection,selectionArgs);
                break;
            case PROGRAMS:
                rowsUpdated = db.update(ProgramContract.ProgramEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case VERSIONS:
                rowsUpdated = db.update(VersionContract.VersionEntry.TABLE_NAME, values,selection, selectionArgs);
                break;
            case STATES:
                rowsUpdated = db.update(StatesContract.StateEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case REGIONS:
                rowsUpdated = db.update(RegionsContract.RegionEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case NAMES:
                rowsUpdated = db.update(NameContract.NameEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case FAVORITE:
                rowsUpdated = db.update(FavoriteCollegeContract.FavoriteCollegeEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case QUERYS:
                rowsUpdated = db.update(SearchQueryInputContract.SearchQueryInputEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);





        return rowsUpdated;
    }
}
