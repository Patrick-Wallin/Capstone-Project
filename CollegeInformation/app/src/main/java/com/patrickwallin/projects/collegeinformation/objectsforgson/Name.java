package com.patrickwallin.projects.collegeinformation.objectsforgson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by piwal on 7/7/2017.
 */

public class Name {
    @SerializedName("id")
    int id;

    @SerializedName("name")
    String name;

    @SerializedName("city")
    String city;

    @SerializedName("state")
    String state;

    @SerializedName("zip")
    int zip;
}
