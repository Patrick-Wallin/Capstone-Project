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
}
