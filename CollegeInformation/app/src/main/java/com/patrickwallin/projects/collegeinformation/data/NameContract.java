package com.patrickwallin.projects.collegeinformation.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by piwal on 6/25/2017.
 */

public class NameContract {
    public static final String SCHEME = "content://";
    public static final String CONTENT_AUTHORITY = "com.patrickwallin.projects.collegeinformation";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + CONTENT_AUTHORITY);
    public static final String PATH_NAMES = "names";

    public static final class NameEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NAMES).build();

        public static final String TABLE_NAME = PATH_NAMES;

        public static final String COLUMN_NAME_ID = "name_id";
        public static final String COLUMN_NAME_NAME = "name_name";
        public static final String COLUMN_NAME_STATE = "name_state";
        public static final String COLUMN_NAME_CITY = "name_city";
        public static final String COLUMN_NAME_ZIP = "name_zip";
        public static final String COLUMN_NAME_IMAGE_LINK = "name_image_link";

        public static final int COL_NAME_ID = 1;
        public static final int COL_NAME_NAME = 2;
        public static final int COL_NAME_STATE = 3;
        public static final int COL_NAME_CITY = 4;
        public static final int COL_NAME_ZIP = 5;
        public static final int COL_NAME_IMAGE_LINK = 6;
    }

    public static String getCreateTableStatement() {
        StringBuilder createTableTableStatement = new StringBuilder();

        createTableTableStatement.append("CREATE TABLE IF NOT EXISTS ");
        createTableTableStatement.append(PATH_NAMES);
        createTableTableStatement.append(" (");
        createTableTableStatement.append(NameEntry._ID);
        createTableTableStatement.append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        createTableTableStatement.append(NameEntry.COLUMN_NAME_ID);
        createTableTableStatement.append(" INTEGER NOT NULL, ");
        createTableTableStatement.append(NameEntry.COLUMN_NAME_NAME);
        createTableTableStatement.append(" TEXT NOT NULL, ");
        createTableTableStatement.append(NameEntry.COLUMN_NAME_STATE);
        createTableTableStatement.append(" TEXT NOT NULL, ");
        createTableTableStatement.append(NameEntry.COLUMN_NAME_CITY);
        createTableTableStatement.append(" TEXT NOT NULL, ");
        createTableTableStatement.append(NameEntry.COLUMN_NAME_ZIP);
        createTableTableStatement.append(" TEXT NOT NULL, ");
        createTableTableStatement.append(NameEntry.COLUMN_NAME_IMAGE_LINK);
        createTableTableStatement.append(" TEXT NOT NULL ");
        createTableTableStatement.append(")");

        return createTableTableStatement.toString();
    }
}
