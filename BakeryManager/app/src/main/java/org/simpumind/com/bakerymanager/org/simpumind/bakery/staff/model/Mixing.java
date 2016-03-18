package org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.model;

/**
 * Created by simpumind on 12/7/15.
 */
public class Mixing {

    private String mixerName;
    private String startTime;
    private String endTime;
    private String timeCreated;

    public String getMixerName() {
        return mixerName;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public void setMixerName(String mixerName) {
        this.mixerName = mixerName;
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

