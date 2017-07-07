package com.patrickwallin.projects.collegeinformation.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piwal on 6/10/2017.
 */

public class VersionContract {
    public static final String SCHEME = "content://";
    public static final String CONTENT_AUTHORITY = "com.patrickwallin.projects.collegeinformation";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + CONTENT_AUTHORITY);
    public static final String PATH_VERSIONS = "versions";

    public static final int VERSION_ID_DEGREES = 1;
    public static final int VERSION_ID_PROGRAMS = 2;
    public static final int VERSION_ID_STATES = 3;
    public static final int VERSION_ID_REGIONS = 4;
    public static final int VERSION_ID_NAMES = 5;

    public static final class VersionEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_VERSIONS).build();

        public static final String TABLE_NAME = PATH_VERSIONS;

        public static final String COLUMN_VERSION_ID = "version_id";
        public static final String COLUMN_VERSION_TABLE_NAME = "version_table_name";
        public static final String COLUMN_VERSION_NUMBER = "version_number";
        public static final String COLUMN_PRIOR_VERSION_NUMBER = "prior_version_number";

        public static final int COL_VERSION_ID = 1;
        public static final int COL_VERSION_TABLE_NAME = 2;
        public static final int COL_VERSION_NUMBER = 3;
        public static final int COL_PRIOR_VERSION_NUMBER = 4;
    }

    public static String getCreateTableStatement() {
        StringBuilder createTableTableStatement = new StringBuilder();

        createTableTableStatement.append("CREATE TABLE IF NOT EXISTS ");
        createTableTableStatement.append(PATH_VERSIONS);
        createTableTableStatement.append(" (");
        createTableTableStatement.append(VersionEntry._ID);
        createTableTableStatement.append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        createTableTableStatement.append(VersionEntry.COLUMN_VERSION_ID);
        createTableTableStatement.append(" INTEGER NOT NULL, ");
        createTableTableStatement.append(VersionEntry.COLUMN_VERSION_TABLE_NAME);
        createTableTableStatement.append(" TEXT NOT NULL, ");
        createTableTableStatement.append(VersionEntry.COLUMN_VERSION_NUMBER);
        createTableTableStatement.append(" INTEGER NOT NULL, ");
        createTableTableStatement.append(VersionEntry.COLUMN_PRIOR_VERSION_NUMBER);
        createTableTableStatement.append(" INTEGER NOT NULL ");
        createTableTableStatement.append(")");

        return createTableTableStatement.toString();
    }

    public static List<VersionData> getVersionData(Context context) {
        List<VersionData> lVersionData = new ArrayList<VersionData>();

        Cursor cursor = context.getContentResolver().query(
                VersionEntry.CONTENT_URI,
                null, null, null, null);
        if(cursor != null) {
            while (cursor.moveToNext()) {
                VersionData versionData = new VersionData(cursor);
                lVersionData.add(versionData);
            }
            cursor.close();
        }

        return lVersionData;
    }

    public static List<VersionData> getVersionData(Context context, int whichVersionData) {
        List<VersionData> lVersionData = new ArrayList<VersionData>();

        String WHERE = VersionEntry.COLUMN_VERSION_ID + " = " + whichVersionData;

        Cursor cursor = context.getContentResolver().query(
                VersionEntry.CONTENT_URI,
                null, WHERE, null, null);
        if(cursor != null) {
            while (cursor.moveToNext()) {
                VersionData versionData = new VersionData(cursor);
                lVersionData.add(versionData);
            }
            cursor.close();
        }

        return lVersionData;
    }
}
