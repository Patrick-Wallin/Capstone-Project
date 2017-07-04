package com.patrickwallin.projects.collegeinformation.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piwal on 6/7/2017.
 */

public class SearchQueryInputContract {
    public static final String SCHEME = "content://";
    public static final String CONTENT_AUTHORITY = "com.patrickwallin.projects.collegeinformation";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + CONTENT_AUTHORITY);
    public static final String PATH_QUERY_INPUT = "search_query_input";

    public static final class SearchQueryInputEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_QUERY_INPUT).build();

        public static final String TABLE_NAME = PATH_QUERY_INPUT;

        public static final String COLUMN_QUERY_ID = "query_id";
        public static final String COLUMN_QUERY_NAME = "query_name";
        public static final String COLUMN_QUERY_VALUE = "query_value";

        public static final int COL_QUERY_ID = 1;
        public static final int COL_QUERY_NAME = 2;
        public static final int COL_QUERY_VALUE = 3;
    }

    public static String getCreateTableStatement() {
        StringBuilder createTableTableStatement = new StringBuilder();

        createTableTableStatement.append("CREATE TABLE IF NOT EXISTS ");
        createTableTableStatement.append(PATH_QUERY_INPUT);
        createTableTableStatement.append(" (");
        createTableTableStatement.append(SearchQueryInputEntry._ID);
        createTableTableStatement.append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        createTableTableStatement.append(SearchQueryInputEntry.COLUMN_QUERY_ID);
        createTableTableStatement.append(" INTEGER NOT NULL, ");
        createTableTableStatement.append(SearchQueryInputEntry.COLUMN_QUERY_NAME);
        createTableTableStatement.append(" TEXT NOT NULL, ");
        createTableTableStatement.append(SearchQueryInputEntry.COLUMN_QUERY_VALUE);
        createTableTableStatement.append(" TEXT NOT NULL ");
        createTableTableStatement.append(")");

        return createTableTableStatement.toString();
    }

    public static List<SearchQueryInputData> getSearchQueryInputData(Context context) {
        List<SearchQueryInputData> lSearchQueryInputData = new ArrayList<SearchQueryInputData>();

        Cursor cursor = context.getContentResolver().query(
                SearchQueryInputEntry.CONTENT_URI,
                null, null, null, null);
        if(cursor != null) {
            while (cursor.moveToNext()) {
                SearchQueryInputData searchQueryInputData = new SearchQueryInputData(cursor);
                lSearchQueryInputData.add(searchQueryInputData);
            }
            cursor.close();
        }

        return lSearchQueryInputData;
    }
}
