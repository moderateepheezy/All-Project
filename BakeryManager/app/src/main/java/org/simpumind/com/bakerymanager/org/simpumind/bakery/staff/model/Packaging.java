package org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.model;

/**
 * Created by simpumind on 12/7/15.
 */
public class Packaging {

    private String packagerName;
    private String startTime;
    private String endTime;
    private String timeCreated;

    public String getPackagerName() {
        return packagerName;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public void setPackagerName(String mixerName) {
        this.packagerName = mixerName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}

