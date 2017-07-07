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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by piwal on 6/8/2017.
 */

public class OpenJsonUtils {
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        return versionDataList;
    }

    public static List<VersionData> getVersionDataFromJson(String versionJson) {
        List<VersionData> parsedVersionsData = new ArrayList<>();

        try {
            JSONObject jsonVersionData = new JSONObject(versionJson);
            if(jsonVersionData != null) {
                if(jsonVersionData.has("versions")) {
                    if(jsonVersionData.get("versions") instanceof JSONArray) {
                        JSONArray arrayVersions = jsonVersionData.getJSONArray("versions");

                        if(arrayVersions != null && arrayVersions.length() > 0) {
                            for(int i = 0; i < arrayVersions.length(); i++) {
                                if(arrayVersions.get(i) instanceof JSONObject) {
                                    JSONObject jsonObject = (JSONObject) arrayVersions.get(i);
                                    if(jsonObject.has("name") && jsonObject.has("version")) {
                                        VersionData versionData = new VersionData(0, jsonObject.getString("name"), jsonObject.getInt("version"),-1);
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        return degreesDataList;
    }

    public static List<DegreesData> getDegreeDataFromJson(String degreeJson) {
        List<DegreesData> parsedDegreesData = new ArrayList<>();

        try {
            JSONObject jsonDegreeData = new JSONObject(degreeJson);
            if(jsonDegreeData != null) {
                if(jsonDegreeData.has("degree")) {
                    if(jsonDegreeData.get("degree") instanceof JSONArray) {
                        JSONArray arrayDegrees = jsonDegreeData.getJSONArray("degree");

                        if(arrayDegrees != null && arrayDegrees.length() > 0) {
                            for(int i = 0; i < arrayDegrees.length(); i++) {
                                if(arrayDegrees.get(i) instanceof JSONObject) {
                                    JSONObject jsonObject = (JSONObject) arrayDegrees.get(i);
                                    if(jsonObject.has("id") && jsonObject.has("title")) {
                                        DegreesData degreesData = new DegreesData(jsonObject.getInt("id"), jsonObject.getString("title"));
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
        } catch (IOException e) {
            e.printStackTrace();
        }


        return programDataList;
    }

    public static List<ProgramData> getProgramDataFromJson(String programJson) {
        List<ProgramData> parsedProgramData = new ArrayList<>();

        try {
            JSONObject jsonProgramData = new JSONObject(programJson);
            if(jsonProgramData != null) {
                if(jsonProgramData.has("program")) {
                    if(jsonProgramData.get("program") instanceof JSONArray) {
                        JSONArray arrayPrograms = jsonProgramData.getJSONArray("program");

                        if(arrayPrograms != null && arrayPrograms.length() > 0) {
                            for(int i = 0; i < arrayPrograms.length(); i++) {
                                if(arrayPrograms.get(i) instanceof JSONObject) {
                                    JSONObject jsonObject = (JSONObject) arrayPrograms.get(i);
                                    if(jsonObject.has("id") && jsonObject.has("title") && jsonObject.has("url_name")) {
                                        ProgramData programData = new ProgramData(jsonObject.getInt("id"), jsonObject.getString("title"), jsonObject.getString("url_name"));
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
        } catch (IOException e) {
            e.printStackTrace();
        }


        return stateDataList;
    }

    public static List<StateData> getStateDataFromJson(String stateJson) {
        List<StateData> parsedStateData = new ArrayList<>();

        try {
            JSONObject jsonStateData = new JSONObject(stateJson);
            if(jsonStateData != null) {
                if(jsonStateData.has("state")) {
                    if(jsonStateData.get("state") instanceof JSONArray) {
                        JSONArray arrayStates = jsonStateData.getJSONArray("state");

                        if(arrayStates != null && arrayStates.length() > 0) {
                            for(int i = 0; i < arrayStates.length(); i++) {
                                if(arrayStates.get(i) instanceof JSONObject) {
                                    JSONObject jsonObject = (JSONObject) arrayStates.get(i);
                                    if(jsonObject.has("id") && jsonObject.has("name")) {
                                        StateData stateData = new StateData(jsonObject.getInt("id"), jsonObject.getString("name"));
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
        } catch (IOException e) {
            e.printStackTrace();
        }


        return regionDataList;
    }

    public static List<RegionData> getRegionDataFromJson(String regionJson) {
        List<RegionData> parsedRegionData = new ArrayList<>();

        try {
            JSONObject jsonRegionData = new JSONObject(regionJson);
            if(jsonRegionData != null) {
                if(jsonRegionData.has("region")) {
                    if(jsonRegionData.get("region") instanceof JSONArray) {
                        JSONArray arrayRegions = jsonRegionData.getJSONArray("region");

                        if(arrayRegions != null && arrayRegions.length() > 0) {
                            for(int i = 0; i < arrayRegions.length(); i++) {
                                if(arrayRegions.get(i) instanceof JSONObject) {
                                    JSONObject jsonObject = (JSONObject) arrayRegions.get(i);
                                    if(jsonObject.has("id") && jsonObject.has("name") && jsonObject.has("description")) {
                                        RegionData regionData = new RegionData(jsonObject.getInt("id"), jsonObject.getString("name"), jsonObject.getString("description"));
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
                if(jsonNameData.has("metadata")) {
                    if(jsonNameData.get("metadata") instanceof JSONObject) {
                        JSONObject jsonMetadata = jsonNameData.getJSONObject("metadata");
                        if(jsonMetadata.has("total")) {
                            totalRecords = jsonMetadata.getInt("total");
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
                if(jsonNameData.has("metadata")) {
                    if(jsonNameData.get("metadata") instanceof JSONObject) {
                        JSONObject jsonMetadata = jsonNameData.getJSONObject("metadata");
                        if(jsonMetadata.has("page")) {
                            pageNumber = jsonMetadata.getInt("page");
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
                if(strname.equalsIgnoreCase("name")) {
                    jsonReader.beginArray();

                    int id = 0;

                    while (jsonReader.hasNext()) {
                        jsonReader.beginObject();
                        NameData nd = new NameData();

                        while (jsonReader.hasNext()) {
                            String name = jsonReader.nextName();
                            if(name.equals("id")) {
                                nd.setId(jsonReader.nextInt());
                            } else if (name.equals("name")) {
                                nd.setName(jsonReader.nextString());
                            }else if (name.equals("city")) {
                                nd.setCity(jsonReader.nextString());
                            }else if(name.equals("state")) {
                                nd.setState(jsonReader.nextString());
                            }else if(name.equals("zip")) {
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


        /*
        try {
            StringBuilder result = new StringBuilder();
            Scanner sc = new Scanner(fileName);
            while(sc.hasNextLine()){
                result.append(sc.nextLine());
            }
            sc.close();
            nameDataList = getNameDataFromJson(result.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */


        return nameDataList;
    }

    public static List<NameData> getNameDataFromJson(String nameJson) {
        List<NameData> parsedNameData = new ArrayList<>();

        //Gson gson = new Gson();
        //NameDataFromFirebase json = gson.fromJson(nameJson, NameDataFromFirebase.class);
        try {
            JSONObject jsonNameData = new JSONObject(nameJson);
            if(jsonNameData != null) {
                if(jsonNameData.has("name")) {
                    if(jsonNameData.get("name") instanceof JSONArray) {
                        JSONArray arrayResults = jsonNameData.getJSONArray("name");
                        if(arrayResults != null && arrayResults.length() > 0) {
                            for(int i = 0; i < arrayResults.length(); i++) {
                                if(arrayResults.get(i) instanceof JSONObject) {
                                    JSONObject jsonObject = (JSONObject) arrayResults.get(i);
                                    if(jsonObject.has("id") && jsonObject.has("zip") &&
                                            jsonObject.has("name") && jsonObject.has("state") &&
                                            jsonObject.has("city")) {
                                        NameData nameData = new NameData(jsonObject.getInt("id"), jsonObject.getString("name"),
                                                jsonObject.getString("state"), jsonObject.getString("city"),
                                                String.valueOf(jsonObject.getInt("zip")));
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

                                    if(jsonObject.has("id") && jsonObject.has("school.name") &&
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
                                            id = jsonObject.getInt("id");
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
                if(jsonData.has("result")) {
                    if(jsonData.get("result") instanceof JSONObject) {
                        JSONObject objectResult = jsonData.getJSONObject("result");
                        if(objectResult != null) {
                            if(objectResult.has("img")) {
                                imageLink = objectResult.getString("img");
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
