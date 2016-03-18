package org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.model;

/**
 * Created by simpumind on 12/7/15.
 */
public class Baking {

    private String bakerName;
    private String startTime;
    private String endTime;
    private String duration;

    private String timeCreated;

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getBakerName() {
        return bakerName;
    }

    public void setBakerName(String bakerName) {
        this.bakerName = bakerName;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}

