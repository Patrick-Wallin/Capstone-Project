package com.patrickwallin.projects.collegeinformation.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by piwal on 6/24/2017.
 */

public class StatesContract {
    public static final String SCHEME = "content://";
    public static final String CONTENT_AUTHORITY = "com.patrickwallin.projects.collegeinformation";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + CONTENT_AUTHORITY);
    public static final String PATH_STATES = "states";

    public static final class StateEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_STATES).build();

        public static final String TABLE_NAME = PATH_STATES;

        public static final String COLUMN_STATE_ID = "state_id";
        public static final String COLUMN_STATE_NAME = "state_name";

        public static final int COL_STATE_ID = 1;
        public static final int COL_STATE_NAME = 2;
    }

    public static String getCreateTableStatement() {
        StringBuilder createTableTableStatement = new StringBuilder();

        createTableTableStatement.append("CREATE TABLE IF NOT EXISTS ");
        createTableTableStatement.append(PATH_STATES);
        createTableTableStatement.append(" (");
        createTableTableStatement.append(StateEntry._ID);
        createTableTableStatement.append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        createTableTableStatement.append(StateEntry.COLUMN_STATE_ID);
        createTableTableStatement.append(" INTEGER NOT NULL, ");
        createTableTableStatement.append(StateEntry.COLUMN_STATE_NAME);
        createTableTableStatement.append(" TEXT NOT NULL ");
        createTableTableStatement.append(")");

        return createTableTableStatement.toString();
    }
}

