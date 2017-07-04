package com.patrickwallin.projects.collegeinformation.utilities;

import android.content.Context;
import android.database.Cursor;

import com.patrickwallin.projects.collegeinformation.data.DegreeContract;
import com.patrickwallin.projects.collegeinformation.data.DegreesData;
import com.patrickwallin.projects.collegeinformation.data.NameContract;
import com.patrickwallin.projects.collegeinformation.data.NameData;
import com.patrickwallin.projects.collegeinformation.data.ProgramContract;
import com.patrickwallin.projects.collegeinformation.data.ProgramData;
import com.patrickwallin.projects.collegeinformation.data.RegionData;
import com.patrickwallin.projects.collegeinformation.data.RegionsContract;
import com.patrickwallin.projects.collegeinformation.data.SearchQueryInputData;
import com.patrickwallin.projects.collegeinformation.data.StateData;
import com.patrickwallin.projects.collegeinformation.data.StatesContract;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

/**
 * Created by piwal on 6/21/2017.
 */

public class CursorAndDataConverter {
    public static List<DegreesData> getDegreeDataFromCursor(Cursor cursor) {
        List<DegreesData> degreesDataList = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst()) {
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                degreesDataList.add(new DegreesData(cursor));
            }
        }


        return degreesDataList;
    }

    public static List<ProgramData> getProgramDataFromCursor(Cursor cursor) {
        List<ProgramData> programDataList = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst()) {
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                programDataList.add(new ProgramData(cursor));
            }
        }


        return programDataList;
    }

    public static List<StateData> getStateDataFromCursor(Cursor cursor) {
        List<StateData> stateDataList = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst()) {
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                stateDataList.add(new StateData(cursor));
            }
        }


        return stateDataList;
    }

    public static List<RegionData> getRegionDataFromCursor(Cursor cursor) {
        List<RegionData> regionDataList = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst()) {
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                regionDataList.add(new RegionData(cursor));
            }
        }


        return regionDataList;
    }

    public static List<NameData> getNameDataFromCursor(Cursor cursor) {
        List<NameData> nameDataList = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst()) {
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                nameDataList.add(new NameData(cursor));
            }
        }


        return nameDataList;
    }

    public static List<SearchQueryInputData> getSearchQueryInputDataFromCursor(Cursor cursor) {
        List<SearchQueryInputData> searchQueryInputDataList = new ArrayList<>();

        if(cursor != null && cursor.moveToFirst()) {
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                searchQueryInputDataList.add(new SearchQueryInputData(cursor));
            }
        }


        return searchQueryInputDataList;
    }

    public static String getValueBasedOnIDAndTable(Context context, String tableName, String value) {
        String result = "";

        if(tableName.equalsIgnoreCase(DegreeContract.DegreeEntry.TABLE_NAME)) {
            Cursor degreesCursor;

            String[] ids = value.split(",",-1);

            StringBuilder sqlWhere = new StringBuilder();

            for(int i = 0; i < ids.length; i++) {
                if(!sqlWhere.toString().isEmpty()) {
                    sqlWhere.append(" OR ");
                }
                sqlWhere.append(DegreeContract.DegreeEntry.COLUMN_DEGREE_ID);
                sqlWhere.append(" = ");
                sqlWhere.append(String.valueOf(ids[i]));
            }

            degreesCursor = context.getContentResolver().query(
                    DegreeContract.DegreeEntry.CONTENT_URI,
                    null,
                    sqlWhere.toString(),
                    null,
                    null);

            if(degreesCursor != null && degreesCursor.moveToFirst()) {
                List<DegreesData> degreesDataList = getDegreeDataFromCursor(degreesCursor);
                result = degreesDataList.get(0).getName();
            }
        }else {
            if(tableName.equalsIgnoreCase(ProgramContract.ProgramEntry.TABLE_NAME)) {
                Cursor programsCursor;

                String[] ids = value.split(",",-1);

                StringBuilder sqlWhere = new StringBuilder();

                for(int i = 0; i < ids.length; i++) {
                    if(!sqlWhere.toString().isEmpty()) {
                        sqlWhere.append(" OR ");
                    }
                    sqlWhere.append(ProgramContract.ProgramEntry.COLUMN_PROGRAM_ID);
                    sqlWhere.append(" = ");
                    sqlWhere.append(String.valueOf(ids[i]));
                }

                programsCursor = context.getContentResolver().query(
                        ProgramContract.ProgramEntry.CONTENT_URI,
                        null,
                        sqlWhere.toString(),
                        null,
                        null);

                if(programsCursor != null && programsCursor.moveToFirst()) {
                    List<ProgramData> programDataList = getProgramDataFromCursor(programsCursor);
                    result = programDataList.get(0).getTitle();
                }
            }else {
                if(tableName.equalsIgnoreCase(NameContract.NameEntry.TABLE_NAME)) {
                    Cursor namesCursor;

                    String[] ids = value.split(",",-1);

                    StringBuilder sqlWhere = new StringBuilder();

                    for(int i = 0; i < ids.length; i++) {
                        if(!sqlWhere.toString().isEmpty()) {
                            sqlWhere.append(" OR ");
                        }
                        sqlWhere.append(NameContract.NameEntry.COLUMN_NAME_ID);
                        sqlWhere.append(" = ");
                        sqlWhere.append(String.valueOf(ids[i]));
                    }

                    namesCursor = context.getContentResolver().query(
                            NameContract.NameEntry.CONTENT_URI,
                            null,
                            sqlWhere.toString(),
                            null,
                            null);

                    if(namesCursor != null && namesCursor.moveToFirst()) {
                        List<NameData> nameDataList = getNameDataFromCursor(namesCursor);
                        result = nameDataList.get(0).getName();
                    }
                }else {
                    if(tableName.equalsIgnoreCase(StatesContract.StateEntry.TABLE_NAME)) {
                        Cursor statesCursor;

                        String[] ids = value.split(",",-1);

                        StringBuilder sqlWhere = new StringBuilder();

                        for(int i = 0; i < ids.length; i++) {
                            if(!sqlWhere.toString().isEmpty()) {
                                sqlWhere.append(" OR ");
                            }
                            sqlWhere.append(StatesContract.StateEntry.COLUMN_STATE_ID);
                            sqlWhere.append(" = ");
                            sqlWhere.append(String.valueOf(ids[i]));
                        }

                        statesCursor = context.getContentResolver().query(
                                StatesContract.StateEntry.CONTENT_URI,
                                null,
                                sqlWhere.toString(),
                                null,
                                null);

                        if(statesCursor != null && statesCursor.moveToFirst()) {
                            List<StateData> stateDataList = getStateDataFromCursor(statesCursor);
                            StringBuilder fullResult = new StringBuilder();
                            for(int i = 0; i < stateDataList.size(); i++) {
                                if(!fullResult.toString().trim().isEmpty())
                                    fullResult.append(",");
                                fullResult.append(stateDataList.get(i).getName());
                            }
                            result = fullResult.toString();
                        }
                    }else {
                        if(tableName.equalsIgnoreCase(RegionsContract.RegionEntry.TABLE_NAME)) {
                            Cursor regionsCursor;

                            String[] ids = value.split(",",-1);

                            StringBuilder sqlWhere = new StringBuilder();

                            for(int i = 0; i < ids.length; i++) {
                                if(!sqlWhere.toString().isEmpty()) {
                                    sqlWhere.append(" OR ");
                                }
                                sqlWhere.append(RegionsContract.RegionEntry.COLUMN_REGION_ID);
                                sqlWhere.append(" = ");
                                sqlWhere.append(String.valueOf(ids[i]));
                            }

                            regionsCursor = context.getContentResolver().query(
                                    RegionsContract.RegionEntry.CONTENT_URI,
                                    null,
                                    sqlWhere.toString(),
                                    null,
                                    null);

                            if(regionsCursor != null && regionsCursor.moveToFirst()) {
                                List<RegionData> regionDataList = getRegionDataFromCursor(regionsCursor);
                                StringBuilder fullResult = new StringBuilder();
                                for(int i = 0; i < regionDataList.size(); i++) {
                                    if(!fullResult.toString().trim().isEmpty())
                                        fullResult.append(",");
                                    fullResult.append(regionDataList.get(i).getName());
                                }
                                result = fullResult.toString();
                            }
                        }

                    }

                }
            }

        }



        return result;
    }
}
