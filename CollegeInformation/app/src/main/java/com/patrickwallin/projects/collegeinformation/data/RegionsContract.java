package com.patrickwallin.projects.collegeinformation.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by piwal on 6/24/2017.
 */

public class RegionsContract {
    public static final String SCHEME = "content://";
    public static final String CONTENT_AUTHORITY = "com.patrickwallin.projects.collegeinformation";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + CONTENT_AUTHORITY);
    public static final String PATH_REGIONS = "regions";

    public static final class RegionEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REGIONS).build();

        public static final String TABLE_NAME = PATH_REGIONS;

        public static final String COLUMN_REGION_ID = "region_id";
        public static final String COLUMN_REGION_NAME = "region_name";
        public static final String COLUMN_REGION_DESCRIPTION = "region_description";

        public static final int COL_REGION_ID = 1;
        public static final int COL_REGION_NAME = 2;
        public static final int COL_REGION_DESCRIPTION = 3;
    }

    public static String getCreateTableStatement() {
        StringBuilder createTableTableStatement = new StringBuilder();

        createTableTableStatement.append("CREATE TABLE IF NOT EXISTS ");
        createTableTableStatement.append(PATH_REGIONS);
        createTableTableStatement.append(" (");
        createTableTableStatement.append(RegionEntry._ID);
        createTableTableStatement.append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        createTableTableStatement.append(RegionEntry.COLUMN_REGION_ID);
        createTableTableStatement.append(" INTEGER NOT NULL, ");
        createTableTableStatement.append(RegionEntry.COLUMN_REGION_NAME);
        createTableTableStatement.append(" TEXT NOT NULL, ");
        createTableTableStatement.append(RegionEntry.COLUMN_REGION_DESCRIPTION);
        createTableTableStatement.append(" TEXT NOT NULL ");
        createTableTableStatement.append(")");

        return createTableTableStatement.toString();
    }
}

