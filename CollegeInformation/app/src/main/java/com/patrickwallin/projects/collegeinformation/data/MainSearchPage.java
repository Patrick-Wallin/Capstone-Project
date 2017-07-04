package com.patrickwallin.projects.collegeinformation.data;

/**
 * Created by piwal on 6/4/2017.
 */

public class MainSearchPage {
    String mName;
    int mImageId;

    public MainSearchPage(String name, int imageId) {
        mName = name;
        mImageId = imageId;
    }

    public String getName() {
        return mName;
    }

    public int getImageId() {
        return mImageId;
    }

}
