package org.simpumind.com.bakerymanager.org.simpumind.bakery.staff.model;

/**
 * Created by simpumind on 12/7/15.
 */
public class DriverRequest {

    private String name;
    private String driver;
    private String noOfCake;
    private String noOfBurger;
    private String noOfSSardine;
    private String noOfBSardine;
    private String noOfRegularLoaves;
    private String address;
    private String totalAmount;
    private String timeCreated;

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNoOfCake() {
        return noOfCake;
    }

    public void setNoOfCake(String noOfCake) {
        this.noOfCake = noOfCake;
    }

    public String getNoOfBurger() {
        return noOfBurger;
    }

    public void setNoOfBurger(String noOfBurger) {
        this.noOfBurger = noOfBurger;
    }

    public String getNoOfSSardine() {
        return noOfSSardine;
    }

    public void setNoOfSSardine(String noOfSSardine) {
        this.noOfSSardine = noOfSSardine;
    }

    public String getNoOfBSardine() {
        return noOfBSardine;
    }

    public void setNoOfBSardine(String noOfBSardine) {
        this.noOfBSardine = noOfBSardine;
    }

    public String getNoOfRegularLoaves() {
        return noOfRegularLoaves;
    }

    public void setNoOfRegularLoaves(String noOfRegularLoaves) {
        this.noOfRegularLoaves = noOfRegularLoaves;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }
}

