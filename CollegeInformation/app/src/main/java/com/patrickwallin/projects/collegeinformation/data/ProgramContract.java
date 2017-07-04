package com.patrickwallin.projects.collegeinformation.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by piwal on 6/8/2017.
 */

public class ProgramContract {
    public static final String SCHEME = "content://";
    public static final String CONTENT_AUTHORITY = "com.patrickwallin.projects.collegeinformation";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + CONTENT_AUTHORITY);
    public static final String PATH_PROGRAMS = "programs";

    public static final class ProgramEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PROGRAMS).build();

        public static final String TABLE_NAME = PATH_PROGRAMS;

        public static final String COLUMN_PROGRAM_ID = "program_id";
        public static final String COLUMN_PROGRAM_TITLE = "program_title";
        public static final String COLUMN_PROGRAM_URL_NAME = "program_url_name";

        public static final int COL_PROGRAM_ID = 1;
        public static final int COL_PROGRAM_TITLE = 2;
        public static final int COL_PROGRAM_URL_NAME = 3;
    }

    public static String getCreateTableStatement() {
        StringBuilder createTableTableStatement = new StringBuilder();

        createTableTableStatement.append("CREATE TABLE IF NOT EXISTS ");
        createTableTableStatement.append(PATH_PROGRAMS);
        createTableTableStatement.append(" (");
        createTableTableStatement.append(ProgramEntry._ID);
        createTableTableStatement.append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        createTableTableStatement.append(ProgramEntry.COLUMN_PROGRAM_ID);
        createTableTableStatement.append(" INTEGER NOT NULL, ");
        createTableTableStatement.append(ProgramEntry.COLUMN_PROGRAM_TITLE);
        createTableTableStatement.append(" TEXT NOT NULL, ");
        createTableTableStatement.append(ProgramEntry.COLUMN_PROGRAM_URL_NAME);
        createTableTableStatement.append(" TEXT NOT NULL ");
        createTableTableStatement.append(")");

        return createTableTableStatement.toString();
    }
}
