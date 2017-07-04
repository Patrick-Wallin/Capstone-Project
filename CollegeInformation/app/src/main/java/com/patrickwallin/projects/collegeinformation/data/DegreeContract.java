package com.patrickwallin.projects.collegeinformation.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by piwal on 6/7/2017.
 */

public class DegreeContract {
    public static final String SCHEME = "content://";
    public static final String CONTENT_AUTHORITY = "com.patrickwallin.projects.collegeinformation";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + CONTENT_AUTHORITY);
    public static final String PATH_DEGREES = "degrees";

    public static final class DegreeEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_DEGREES).build();

        public static final String TABLE_NAME = PATH_DEGREES;

        public static final String COLUMN_DEGREE_ID = "degree_id";
        public static final String COLUMN_DEGREE_TITLE = "degree_title";

        public static final int COL_DEGREE_ID = 1;
        public static final int COL_DEGREE_TITLE = 2;
    }

    public static String getCreateTableStatement() {
        StringBuilder createTableTableStatement = new StringBuilder();

        createTableTableStatement.append("CREATE TABLE IF NOT EXISTS ");
        createTableTableStatement.append(PATH_DEGREES);
        createTableTableStatement.append(" (");
        createTableTableStatement.append(DegreeEntry._ID);
        createTableTableStatement.append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        createTableTableStatement.append(DegreeEntry.COLUMN_DEGREE_ID);
        createTableTableStatement.append(" INTEGER NOT NULL, ");
        createTableTableStatement.append(DegreeEntry.COLUMN_DEGREE_TITLE);
        createTableTableStatement.append(" TEXT NOT NULL");
        createTableTableStatement.append(")");

        return createTableTableStatement.toString();
    }
}
