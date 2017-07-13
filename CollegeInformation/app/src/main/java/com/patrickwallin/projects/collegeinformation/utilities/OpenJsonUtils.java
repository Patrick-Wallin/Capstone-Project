package com.patrickwallin.projects.collegeinformation.utilities;

import com.google.gson.stream.JsonReader;
import com.patrickwallin.projects.collegeinformation.data.DegreesData;
import com.patrickwallin.projects.collegeinformation.data.FavoriteCollegeData;
import com.patrickwallin.projects.collegeinformation.data.NameData;
import com.patrickwallin.projects.collegeinformation.data.ProgramData;
import com.patrickwallin.projects.collegeinformation.data.RegionData;
import com.patrickwallin.projects.collegeinformation.data.StateData;
import com.patrickwallin.projects.collegeinformation.data.VersionData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by piwal on 6/8/2017.
 */

public class OpenJsonUtils {
    private static String JSON_ELEMENT_RESULT = "result";
    private static String JSON_ELEMENT_METADATA = "metadata";
    private static String JSON_ELEMENT_TOTAL = "total";
    private static String JSON_ELEMENT_PAGE = "page";
    private static String JSON_ELEMENT_VERSIONS = "versions";
    private static String JSON_ELEMENT_VERSION = "version";
    private static String JSON_ELEMENT_NAME = "name";
    private static String JSON_ELEMENT_DEGREE = "degree";
    private static String JSON_ELEMENT_PROGRAM = "program";
    private static String JSON_ELEMENT_STATE = "state";
    private static String JSON_ELEMENT_REGION = "region";
    private static String JSON_ELEMENT_ID = "id";
    private static String JSON_ELEMENT_TITLE = "title";
    private static String JSON_ELEMENT_DESC = "description";
    private static String JSON_ELEMENT_URL_NAME = "url_name";
    private static String JSON_ELEMENT_CITY = "city";
    private static String JSON_ELEMENT_ZIP = "zip";
    private static String JSON_ELEMENT_IMAGE = "img";

    public static List<VersionData> getVersionDataFromJson(File fileName) {
        List<VersionData> versionDataList = null;

        try {
            StringBuilder result = new StringBuilder();
            Scanner sc = new Scanner(fileName);
            while(sc.hasNextLine()){
                result.append(sc.nextLine());
            }
            sc.close();
            versionDataList = getVersionDataFromJson(result.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return versionDataList;
    }

    public static List<VersionData> getVersionDataFromJson(String versionJson) {
        List<VersionData> parsedVersionsData = new ArrayList<>();

        try {
            JSONObject jsonVersionData = new JSONObject(versionJson);
            if(jsonVersionData != null) {
                if(jsonVersionData.has(JSON_ELEMENT_VERSIONS)) {
                    if(jsonVersionData.get(JSON_ELEMENT_VERSIONS) instanceof JSONArray) {
                        JSONArray arrayVersions = jsonVersionData.getJSONArray(JSON_ELEMENT_VERSIONS);

                        if(arrayVersions != null && arrayVersions.length() > 0) {
                            for(int i = 0; i < arrayVersions.length(); i++) {
                                if(arrayVersions.get(i) instanceof JSONObject) {
                                    JSONObject jsonObject = (JSONObject) arrayVersions.get(i);
                                    if(jsonObject.has(JSON_ELEMENT_NAME) && jsonObject.has(JSON_ELEMENT_VERSION)) {
                                        VersionData versionData = new VersionData(0, jsonObject.getString(JSON_ELEMENT_NAME), jsonObject.getInt(JSON_ELEMENT_VERSION),-1);
                                        parsedVersionsData.add(versionData);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parsedVersionsData;
    }

    public static List<DegreesData> getDegreeDataFromJson(File fileName) {
        List<DegreesData> degreesDataList = null;

        try {
            StringBuilder result = new StringBuilder();
            Scanner sc = new Scanner(fileName);
            while(sc.hasNextLine()){
                result.append(sc.nextLine());
            }
            sc.close();
            degreesDataList = getDegreeDataFromJson(result.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return degreesDataList;
    }

    public static List<DegreesData> getDegreeDataFromJson(String degreeJson) {
        List<DegreesData> parsedDegreesData = new ArrayList<>();

        try {
            JSONObject jsonDegreeData = new JSONObject(degreeJson);
            if(jsonDegreeData != null) {
                if(jsonDegreeData.has(JSON_ELEMENT_DEGREE)) {
                    if(jsonDegreeData.get(JSON_ELEMENT_DEGREE) instanceof JSONArray) {
                        JSONArray arrayDegrees = jsonDegreeData.getJSONArray(JSON_ELEMENT_DEGREE);

                        if(arrayDegrees != null && arrayDegrees.length() > 0) {
                            for(int i = 0; i < arrayDegrees.length(); i++) {
                                if(arrayDegrees.get(i) instanceof JSONObject) {
                                    JSONObject jsonObject = (JSONObject) arrayDegrees.get(i);
                                    if(jsonObject.has(JSON_ELEMENT_ID) && jsonObject.has(JSON_ELEMENT_TITLE)) {
                                        DegreesData degreesData = new DegreesData(jsonObject.getInt(JSON_ELEMENT_ID), jsonObject.getString(JSON_ELEMENT_TITLE));
                                        parsedDegreesData.add(degreesData);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parsedDegreesData;
    }

    public static List<ProgramData> getProgramDataFromJson(File fileName) {
        List<ProgramData> programDataList = null;

        try {
            StringBuilder result = new StringBuilder();
            Scanner sc = new Scanner(fileName);
            while(sc.hasNextLine()){
                result.append(sc.nextLine());
            }
            sc.close();
            programDataList = getProgramDataFromJson(result.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return programDataList;
    }

    public static List<ProgramData> getProgramDataFromJson(String programJson) {
        List<ProgramData> parsedProgramData = new ArrayList<>();

        try {
            JSONObject jsonProgramData = new JSONObject(programJson);
            if(jsonProgramData != null) {
                if(jsonProgramData.has(JSON_ELEMENT_PROGRAM)) {
                    if(jsonProgramData.get(JSON_ELEMENT_PROGRAM) instanceof JSONArray) {
                        JSONArray arrayPrograms = jsonProgramData.getJSONArray(JSON_ELEMENT_PROGRAM);

                        if(arrayPrograms != null && arrayPrograms.length() > 0) {
                            for(int i = 0; i < arrayPrograms.length(); i++) {
                                if(arrayPrograms.get(i) instanceof JSONObject) {
                                    JSONObject jsonObject = (JSONObject) arrayPrograms.get(i);
                                    if(jsonObject.has(JSON_ELEMENT_ID) && jsonObject.has(JSON_ELEMENT_TITLE) && jsonObject.has(JSON_ELEMENT_URL_NAME)) {
                                        ProgramData programData = new ProgramData(jsonObject.getInt(JSON_ELEMENT_ID), jsonObject.getString(JSON_ELEMENT_TITLE), jsonObject.getString(JSON_ELEMENT_URL_NAME));
                                        parsedProgramData.add(programData);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parsedProgramData;
    }

    public static List<StateData> getStateDataFromJson(File fileName) {
        List<StateData> stateDataList = null;

        try {
            StringBuilder result = new StringBuilder();
            Scanner sc = new Scanner(fileName);
            while(sc.hasNextLine()){
                result.append(sc.nextLine());
            }
            sc.close();
            stateDataList = getStateDataFromJson(result.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return stateDataList;
    }

    public static List<StateData> getStateDataFromJson(String stateJson) {
        List<StateData> parsedStateData = new ArrayList<>();

        try {
            JSONObject jsonStateData = new JSONObject(stateJson);
            if(jsonStateData != null) {
                if(jsonStateData.has(JSON_ELEMENT_STATE)) {
                    if(jsonStateData.get(JSON_ELEMENT_STATE) instanceof JSONArray) {
                        JSONArray arrayStates = jsonStateData.getJSONArray(JSON_ELEMENT_STATE);

                        if(arrayStates != null && arrayStates.length() > 0) {
                            for(int i = 0; i < arrayStates.length(); i++) {
                                if(arrayStates.get(i) instanceof JSONObject) {
                                    JSONObject jsonObject = (JSONObject) arrayStates.get(i);
                                    if(jsonObject.has(JSON_ELEMENT_ID) && jsonObject.has(JSON_ELEMENT_NAME)) {
                                        StateData stateData = new StateData(jsonObject.getInt(JSON_ELEMENT_ID), jsonObject.getString(JSON_ELEMENT_NAME));
                                        parsedStateData.add(stateData);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parsedStateData;
    }

    public static List<RegionData> getRegionDataFromJson(File fileName) {
        List<RegionData> regionDataList = null;

        try {
            StringBuilder result = new StringBuilder();
            Scanner sc = new Scanner(fileName);
            while(sc.hasNextLine()){
                result.append(sc.nextLine());
            }
            sc.close();
            regionDataList = getRegionDataFromJson(result.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return regionDataList;
    }

    public static List<RegionData> getRegionDataFromJson(String regionJson) {
        List<RegionData> parsedRegionData = new ArrayList<>();

        try {
            JSONObject jsonRegionData = new JSONObject(regionJson);
            if(jsonRegionData != null) {
                if(jsonRegionData.has(JSON_ELEMENT_REGION)) {
                    if(jsonRegionData.get(JSON_ELEMENT_REGION) instanceof JSONArray) {
                        JSONArray arrayRegions = jsonRegionData.getJSONArray(JSON_ELEMENT_REGION);

                        if(arrayRegions != null && arrayRegions.length() > 0) {
                            for(int i = 0; i < arrayRegions.length(); i++) {
                                if(arrayRegions.get(i) instanceof JSONObject) {
                                    JSONObject jsonObject = (JSONObject) arrayRegions.get(i);
                                    if(jsonObject.has(JSON_ELEMENT_ID) && jsonObject.has(JSON_ELEMENT_NAME) && jsonObject.has(JSON_ELEMENT_DESC)) {
                                        RegionData regionData = new RegionData(jsonObject.getInt(JSON_ELEMENT_ID), jsonObject.getString(JSON_ELEMENT_NAME), jsonObject.getString(JSON_ELEMENT_DESC));
                                        parsedRegionData.add(regionData);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parsedRegionData;
    }

    public static int getTotalRecords(String nameJson) {
        int totalRecords = 0;

        try {
            JSONObject jsonNameData = new JSONObject(nameJson);
            if(jsonNameData != null) {
                if(jsonNameData.has(JSON_ELEMENT_METADATA)) {
                    if(jsonNameData.get(JSON_ELEMENT_METADATA) instanceof JSONObject) {
                        JSONObject jsonMetadata = jsonNameData.getJSONObject(JSON_ELEMENT_METADATA);
                        if(jsonMetadata.has(JSON_ELEMENT_TOTAL)) {
                            totalRecords = jsonMetadata.getInt(JSON_ELEMENT_TOTAL);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return totalRecords;
    }

    public static int getPageNumber(String nameJson) {
        int pageNumber = 0;

        try {
            JSONObject jsonNameData = new JSONObject(nameJson);
            if(jsonNameData != null) {
                if(jsonNameData.has(JSON_ELEMENT_METADATA)) {
                    if(jsonNameData.get(JSON_ELEMENT_METADATA) instanceof JSONObject) {
                        JSONObject jsonMetadata = jsonNameData.getJSONObject(JSON_ELEMENT_METADATA);
                        if(jsonMetadata.has(JSON_ELEMENT_PAGE)) {
                            pageNumber = jsonMetadata.getInt(JSON_ELEMENT_PAGE);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return pageNumber;
    }

    public static List<NameData> getNameDataFromJson(File fileName) {
        List<NameData> nameDataList = new ArrayList<NameData>();

        try {
            FileReader reader = new FileReader(fileName);
            JsonReader jsonReader = new JsonReader(reader);

            //Gson gson = new GsonBuilder().create();

            // Read file in stream mode
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String strname = jsonReader.nextName();
                if(strname.equalsIgnoreCase(JSON_ELEMENT_NAME)) {
                    jsonReader.beginArray();

                    int id = 0;

                    while (jsonReader.hasNext()) {
                        jsonReader.beginObject();
                        NameData nd = new NameData();

                        while (jsonReader.hasNext()) {
                            String name = jsonReader.nextName();
                            if(name.equals(JSON_ELEMENT_ID)) {
                                nd.setId(jsonReader.nextInt());
                            } else if (name.equals(JSON_ELEMENT_NAME)) {
                                nd.setName(jsonReader.nextString());
                            }else if (name.equals(JSON_ELEMENT_CITY)) {
                                nd.setCity(jsonReader.nextString());
                            }else if(name.equals(JSON_ELEMENT_STATE)) {
                                nd.setState(jsonReader.nextString());
                            }else if(name.equals(JSON_ELEMENT_ZIP)) {
                                nd.setZip(String.valueOf(jsonReader.nextInt()));
                            }else
                                jsonReader.skipValue();
                        }

                        nameDataList.add(nd);

                        jsonReader.endObject();
                    }
                    jsonReader.endArray();
                }
            }
            jsonReader.close();
            reader.close();


        } catch (FileNotFoundException ex) {
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }

        return nameDataList;
    }

    public static List<NameData> getNameDataFromJson(String nameJson) {
        List<NameData> parsedNameData = new ArrayList<>();

        //Gson gson = new Gson();
        //NameDataFromFirebase json = gson.fromJson(nameJson, NameDataFromFirebase.class);
        try {
            JSONObject jsonNameData = new JSONObject(nameJson);
            if(jsonNameData != null) {
                if(jsonNameData.has(JSON_ELEMENT_NAME)) {
                    if(jsonNameData.get(JSON_ELEMENT_NAME) instanceof JSONArray) {
                        JSONArray arrayResults = jsonNameData.getJSONArray(JSON_ELEMENT_NAME);
                        if(arrayResults != null && arrayResults.length() > 0) {
                            for(int i = 0; i < arrayResults.length(); i++) {
                                if(arrayResults.get(i) instanceof JSONObject) {
                                    JSONObject jsonObject = (JSONObject) arrayResults.get(i);
                                    if(jsonObject.has(JSON_ELEMENT_ID) && jsonObject.has(JSON_ELEMENT_ZIP) &&
                                            jsonObject.has(JSON_ELEMENT_NAME) && jsonObject.has(JSON_ELEMENT_STATE) &&
                                            jsonObject.has(JSON_ELEMENT_CITY)) {
                                        NameData nameData = new NameData(jsonObject.getInt(JSON_ELEMENT_ID), jsonObject.getString(JSON_ELEMENT_NAME),
                                                jsonObject.getString(JSON_ELEMENT_STATE), jsonObject.getString(JSON_ELEMENT_CITY),
                                                String.valueOf(jsonObject.getInt(JSON_ELEMENT_ZIP)),"");
                                        parsedNameData.add(nameData);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parsedNameData;
    }

    public static List<FavoriteCollegeData> getFavoriteCollegeDataFromJson(String favoriteCollegeJson) {
        List<FavoriteCollegeData> parsedFavoriteCollegeData = new ArrayList<>();

        int id = 0;
        String schoolName = "";
        String schoolState = "";
        int schoolZip = 0;
        String schoolCity = "";
        double locationLat = 0.0f;
        double locationLon = 0.0f;
        String schoolUrl = "";
        double satScore25CriticalReading = 0.0f;
        double satScore75CriticalReading = 0.0f;
        double satScore25Math = 0.0f;
        double satScore75Math = 0.0f;
        double satScore25Writing = 0.0f;
        double satScore75Writing = 0.0f;
        double satACT25Cumulative = 0.0f;
        double satACT75Cumulative = 0.0f;
        int costTuitionInState = 0;
        int costTuitionOutState = 0;
        double pellGrantRate = 0.0f;
        double federalLoanRate = 0.0f;

        try {
            JSONObject jsonFavoriteCollegeData = new JSONObject(favoriteCollegeJson);
            if(jsonFavoriteCollegeData != null) {
                if(jsonFavoriteCollegeData.has("results")) {
                    if(jsonFavoriteCollegeData.get("results") instanceof JSONArray) {
                        JSONArray arrayResults = jsonFavoriteCollegeData.getJSONArray("results");
                        if(arrayResults != null && arrayResults.length() > 0) {
                            for(int i = 0; i < arrayResults.length(); i++) {
                                if(arrayResults.get(i) instanceof JSONObject) {
                                    JSONObject jsonObject = (JSONObject) arrayResults.get(i);

                                    if(jsonObject.has(JSON_ELEMENT_ID) && jsonObject.has("school.name") &&
                                            jsonObject.has("school.state") && jsonObject.has("school.zip") && jsonObject.has("school.city") &&
                                            jsonObject.has("location.lat") && jsonObject.has("location.lon") && jsonObject.has("school.school_url") &&
                                            jsonObject.has("2014.admissions.sat_scores.25th_percentile.critical_reading") &&
                                            jsonObject.has("2014.admissions.sat_scores.75th_percentile.critical_reading") &&
                                            jsonObject.has("2014.admissions.sat_scores.25th_percentile.math") &&
                                            jsonObject.has("2014.admissions.sat_scores.75th_percentile.math") &&
                                            jsonObject.has("2014.admissions.sat_scores.25th_percentile.writing") &&
                                            jsonObject.has("2014.admissions.sat_scores.75th_percentile.writing") &&
                                            jsonObject.has("2014.admissions.act_scores.25th_percentile.cumulative") &&
                                            jsonObject.has("2014.admissions.act_scores.75th_percentile.cumulative") &&
                                            jsonObject.has("2014.cost.tuition.in_state") &&
                                            jsonObject.has("2014.cost.tuition.out_of_state") &&
                                            jsonObject.has("2014.aid.pell_grant_rate") &&
                                            jsonObject.has("2014.aid.federal_loan_rate")
                                            ) {

                                        try {
                                            id = jsonObject.getInt(JSON_ELEMENT_ID);
                                        }catch(Exception e) {
                                            id = 0;
                                        }

                                        try {
                                            schoolName = jsonObject.getString("school.name");
                                        }catch(Exception e) {
                                            schoolName = "";
                                        }

                                        try {
                                            schoolState = jsonObject.getString("school.state");
                                        }catch(Exception e) {
                                            schoolState = "";
                                        }

                                        try {
                                            schoolZip = jsonObject.getInt("school.zip");
                                        }catch(Exception e) {
                                            schoolZip = 0;
                                        }

                                        try {
                                            schoolCity = jsonObject.getString("school.city");
                                        }catch(Exception e) {
                                            schoolCity = "";
                                        }

                                        try {
                                            schoolUrl = jsonObject.getString("school.school_url");
                                        }catch(Exception e) {
                                            schoolUrl = "";
                                        }

                                        try {
                                            locationLat = jsonObject.getDouble("location.lat");
                                        }catch(Exception e) {
                                            locationLat = 0.0f;
                                        }

                                        try {
                                            locationLon = jsonObject.getDouble("location.lon");
                                        }catch(Exception e) {
                                            locationLon = 0.0f;
                                        }

                                        try {
                                            satScore25CriticalReading = jsonObject.getDouble("2014.admissions.sat_scores.25th_percentile.critical_reading");
                                        }catch(Exception e) {
                                            satScore25CriticalReading = 0.0f;
                                        }

                                        try {
                                            satScore75CriticalReading = jsonObject.getDouble("2014.admissions.sat_scores.75th_percentile.critical_reading");
                                        }catch(Exception e) {
                                            satScore75CriticalReading = 0.0f;
                                        }

                                        try {
                                            satScore25Math = jsonObject.getDouble("2014.admissions.sat_scores.25th_percentile.math");
                                        }catch(Exception e) {
                                            satScore25Math = 0.0f;
                                        }

                                        try {
                                            satScore75Math = jsonObject.getDouble("2014.admissions.sat_scores.75th_percentile.math");
                                        }catch(Exception e) {
                                            satScore75Math = 0.0f;
                                        }

                                        try {
                                            satScore25Writing = jsonObject.getDouble("2014.admissions.sat_scores.25th_percentile.writing");
                                        }catch(Exception e) {
                                            satScore25Writing = 0.0f;
                                        }

                                        try {
                                            satScore75Writing = jsonObject.getDouble("2014.admissions.sat_scores.75th_percentile.writing");
                                        }catch(Exception e) {
                                            satScore75Writing = 0.0f;
                                        }

                                        try {
                                            satACT25Cumulative = jsonObject.getDouble("2014.admissions.act_scores.25th_percentile.cumulative");
                                        }catch(Exception e) {
                                            satACT25Cumulative = 0.0f;
                                        }

                                        try {
                                            satACT75Cumulative = jsonObject.getDouble("2014.admissions.act_scores.75th_percentile.cumulative");
                                        }catch(Exception e) {
                                            satACT75Cumulative = 0.0f;
                                        }

                                        try {
                                            costTuitionInState = jsonObject.getInt("2014.cost.tuition.in_state");
                                        }catch(Exception e) {
                                            costTuitionInState = 0;
                                        }

                                        try {
                                            costTuitionOutState = jsonObject.getInt("2014.cost.tuition.out_of_state");
                                        }catch(Exception e) {
                                            costTuitionOutState = 0;
                                        }

                                        try {
                                            pellGrantRate = jsonObject.getDouble("2014.aid.pell_grant_rate");
                                        }catch(Exception e) {
                                            pellGrantRate = 0.0f;
                                        }

                                        try {
                                            federalLoanRate = jsonObject.getDouble("2014.aid.federal_loan_rate");
                                        }catch(Exception e) {
                                            federalLoanRate = 0.0f;
                                        }

                                        FavoriteCollegeData favoriteCollegeData = new FavoriteCollegeData(id,schoolName,schoolState,schoolCity, String.valueOf(schoolZip),
                                                String.valueOf(locationLat), String.valueOf(locationLon), schoolUrl, "", costTuitionInState, costTuitionOutState,
                                                federalLoanRate, pellGrantRate, satScore25CriticalReading, satScore75CriticalReading, satScore25Math, satScore75Math,
                                                satScore25Writing, satScore75Writing, satACT25Cumulative, satACT75Cumulative);

                                        parsedFavoriteCollegeData.add(favoriteCollegeData);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parsedFavoriteCollegeData;
    }

    public static String getImageLinkFromJson(String json) {
        String imageLink = "";

        try {
            JSONObject jsonData = new JSONObject(json);
            if(jsonData != null) {
                if(jsonData.has(JSON_ELEMENT_RESULT)) {
                    if(jsonData.get(JSON_ELEMENT_RESULT) instanceof JSONObject) {
                        JSONObject objectResult = jsonData.getJSONObject(JSON_ELEMENT_RESULT);
                        if(objectResult != null) {
                            if(objectResult.has(JSON_ELEMENT_IMAGE)) {
                                imageLink = objectResult.getString(JSON_ELEMENT_IMAGE);
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return imageLink;
    }

}
