package com.patrickwallin.projects.collegeinformation.data;

import android.net.Uri;
import android.provider.BaseColumns;

import java.util.jar.Attributes;

/**
 * Created by piwal on 6/25/2017.
 */

public class FavoriteCollegeContract {
    public static final String SCHEME = "content://";
    public static final String CONTENT_AUTHORITY = "com.patrickwallin.projects.collegeinformation";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + CONTENT_AUTHORITY);
    public static final String PATH_FAVORITE = "favorite_college";

    public static final class FavoriteCollegeEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build();

        public static final String TABLE_NAME = PATH_FAVORITE;

        public static final String COLUMN_FAVORITE_ID = "favorite_id";
        public static final String COLUMN_FAVORITE_NAME = "favorite_name";
        public static final String COLUMN_FAVORITE_STATE = "favorite_state";
        public static final String COLUMN_FAVORITE_CITY = "favorite_city";
        public static final String COLUMN_FAVORITE_ZIP = "favorite_zip";
        public static final String COLUMN_FAVORITE_LATITUDE = "favorite_latitude";
        public static final String COLUMN_FAVORITE_LONGITUDE = "favorite_longitude";
        public static final String COLUMN_FAVORITE_WEBSITE = "favorite_website";
        public static final String COLUMN_FAVORITE_IMAGE_LINK = "favorite_image_link";
        public static final String COLUMN_FAVORITE_COST_IN_STATE = "favorite_cost_in_state";
        public static final String COLUMN_FAVORITE_COST_OUT_STATE = "favorite_cost_out_state";
        public static final String COLUMN_FAVORITE_PCT_FED_LOANS = "favorite_pct_fed_loans";
        public static final String COLUMN_FAVORITE_PCT_PELL_GRANTS = "favorite_pct_pell_grants";
        public static final String COLUMN_FAVORITE_SAT_CRITICAL_READING_25 = "favorite_sat_critical_reading_25";
        public static final String COLUMN_FAVORITE_SAT_CRITICAL_READING_75 = "favorite_sat_critical_reading_75";
        public static final String COLUMN_FAVORITE_SAT_MATH_25 = "favorite_sat_math_25";
        public static final String COLUMN_FAVORITE_SAT_MATH_75 = "favorite_sat_math_75";
        public static final String COLUMN_FAVORITE_SAT_WRITING_25 = "favorite_sat_writing_25";
        public static final String COLUMN_FAVORITE_SAT_WRITING_75 = "favorite_sat_writing_75";
        public static final String COLUMN_FAVORITE_ACT_25 = "favorite_act_25";
        public static final String COLUMN_FAVORITE_ACT_75 = "favorite_act_75";

        /*
        public static final String COLUMN_NAME_LAT = "name_latitude";
        public static final String COLUMN_NAME_LON = "name_longitude";
        public static final String COLUMN_NAME_REGION_ID = "name_region_id";
        public static final String COLUMN_NAME_DEGREES_AWARDED_HIGHEST = "name_degrees_awarded_highest";
        public static final String COLUMN_NAME_STATE_ID = "name_state_id";
        */

        public static final int COL_FAVORITE_ID = 1;
        public static final int COL_FAVORITE_NAME = 2;
        public static final int COL_FAVORITE_STATE = 3;
        public static final int COL_FAVORITE_CITY = 4;
        public static final int COL_FAVORITE_ZIP = 5;
        public static final int COL_FAVORITE_LATITUDE = 6;
        public static final int COL_FAVORITE_LONGITUDE = 7;
        public static final int COL_FAVORITE_WEBSITE = 8;
        public static final int COL_FAVORITE_IMAGE_LINK = 9;
        public static final int COL_FAVORITE_COST_IN_STATE = 10;
        public static final int COL_FAVORITE_COST_OUT_STATE = 11;
        public static final int COL_FAVORITE_PCT_FED_LOANS = 12;
        public static final int COL_FAVORITE_PCT_PELL_GRANTS = 13;
        public static final int COL_FAVORITE_SAT_CRITICAL_READING_25 = 14;
        public static final int COL_FAVORITE_SAT_CRITICAL_READING_75 = 15;
        public static final int COL_FAVORITE_SAT_MATH_25 = 16;
        public static final int COL_FAVORITE_SAT_MATH_75 = 17;
        public static final int COL_FAVORITE_SAT_WRITING_25 = 18;
        public static final int COL_FAVORITE_SAT_WRITING_75 = 19;
        public static final int COL_FAVORITE_ACT_25 = 20;
        public static final int COL_FAVORITE_ACT_75 = 21;
        /*
        public static final int COL_NAME_LAT = 6;
        public static final int COL_NAME_LON = 7;
        public static final int COL_NAME_REGION_ID = 8;
        public static final int COL_NAME_DEGREES_AWARDED_HIGHEST = 9;
        public static final int COL_NAME_STATE_ID = 10;
        */
    }

    public static String getCreateTableStatement() {
        StringBuilder createTableTableStatement = new StringBuilder();

        createTableTableStatement.append("CREATE TABLE IF NOT EXISTS ");
        createTableTableStatement.append(PATH_FAVORITE);
        createTableTableStatement.append(" (");
        createTableTableStatement.append(FavoriteCollegeEntry._ID);
        createTableTableStatement.append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        createTableTableStatement.append(FavoriteCollegeEntry.COLUMN_FAVORITE_ID);
        createTableTableStatement.append(" INTEGER NOT NULL, ");
        createTableTableStatement.append(FavoriteCollegeEntry.COLUMN_FAVORITE_NAME);
        createTableTableStatement.append(" TEXT NOT NULL, ");
        createTableTableStatement.append(FavoriteCollegeEntry.COLUMN_FAVORITE_STATE);
        createTableTableStatement.append(" TEXT NOT NULL, ");
        createTableTableStatement.append(FavoriteCollegeEntry.COLUMN_FAVORITE_CITY);
        createTableTableStatement.append(" TEXT NOT NULL, ");
        createTableTableStatement.append(FavoriteCollegeEntry.COLUMN_FAVORITE_ZIP);
        createTableTableStatement.append(" TEXT NOT NULL, ");
        createTableTableStatement.append(FavoriteCollegeEntry.COLUMN_FAVORITE_LATITUDE);
        createTableTableStatement.append(" TEXT NOT NULL, ");
        createTableTableStatement.append(FavoriteCollegeEntry.COLUMN_FAVORITE_LONGITUDE);
        createTableTableStatement.append(" TEXT NOT NULL, ");
        createTableTableStatement.append(FavoriteCollegeEntry.COLUMN_FAVORITE_WEBSITE);
        createTableTableStatement.append(" TEXT NOT NULL, ");
        createTableTableStatement.append(FavoriteCollegeEntry.COLUMN_FAVORITE_IMAGE_LINK);
        createTableTableStatement.append(" TEXT NOT NULL, ");
        createTableTableStatement.append(FavoriteCollegeEntry.COLUMN_FAVORITE_COST_IN_STATE);
        createTableTableStatement.append(" INTEGER NOT NULL, ");
        createTableTableStatement.append(FavoriteCollegeEntry.COLUMN_FAVORITE_COST_OUT_STATE);
        createTableTableStatement.append(" INTEGER NOT NULL, ");
        createTableTableStatement.append(FavoriteCollegeEntry.COLUMN_FAVORITE_PCT_FED_LOANS);
        createTableTableStatement.append(" REAL NOT NULL, ");
        createTableTableStatement.append(FavoriteCollegeEntry.COLUMN_FAVORITE_PCT_PELL_GRANTS);
        createTableTableStatement.append(" REAL NOT NULL, ");
        createTableTableStatement.append(FavoriteCollegeEntry.COLUMN_FAVORITE_SAT_CRITICAL_READING_25);
        createTableTableStatement.append(" REAL NOT NULL, ");
        createTableTableStatement.append(FavoriteCollegeEntry.COLUMN_FAVORITE_SAT_CRITICAL_READING_75);
        createTableTableStatement.append(" REAL NOT NULL, ");
        createTableTableStatement.append(FavoriteCollegeEntry.COLUMN_FAVORITE_SAT_MATH_25);
        createTableTableStatement.append(" REAL NOT NULL, ");
        createTableTableStatement.append(FavoriteCollegeEntry.COLUMN_FAVORITE_SAT_MATH_75);
        createTableTableStatement.append(" REAL NOT NULL, ");
        createTableTableStatement.append(FavoriteCollegeEntry.COLUMN_FAVORITE_SAT_WRITING_25);
        createTableTableStatement.append(" REAL NOT NULL, ");
        createTableTableStatement.append(FavoriteCollegeEntry.COLUMN_FAVORITE_SAT_WRITING_75);
        createTableTableStatement.append(" REAL NOT NULL, ");
        createTableTableStatement.append(FavoriteCollegeEntry.COLUMN_FAVORITE_ACT_25);
        createTableTableStatement.append(" REAL NOT NULL, ");
        createTableTableStatement.append(FavoriteCollegeEntry.COLUMN_FAVORITE_ACT_75);
        createTableTableStatement.append(" REAL NOT NULL ");
        createTableTableStatement.append(")");

        return createTableTableStatement.toString();
    }
}
