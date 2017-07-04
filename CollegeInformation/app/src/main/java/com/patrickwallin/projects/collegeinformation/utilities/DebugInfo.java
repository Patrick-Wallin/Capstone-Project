package com.patrickwallin.projects.collegeinformation.utilities;

import java.util.concurrent.TimeUnit;

/**
 * Created by piwal on 7/3/2017.
 */

public class DebugInfo {
    private long startTime;
    private long endTime;

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void end() {
        endTime = System.currentTimeMillis();
    }

    public long getElaspedTime() {
        return endTime-startTime;
    }

    public String getMinAndSecElaspedTime() {
        long elaspedTime = getElaspedTime();
        String result = String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(elaspedTime),
                TimeUnit.MILLISECONDS.toSeconds(elaspedTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elaspedTime))
        );

        return result;
    }

}
