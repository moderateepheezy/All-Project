package org.simpumind.com.businessdirectory;

/**
 * Created by simpumind on 11/18/15.
 */
public class DirectoryData {

    public String name;
    public String email;
    public String phoneNo;
    public int id_;
    public String address;

    public DirectoryData(String name, String email, String phoneNo, int id_, String address) {
        this.name = name;
        this.email = email;
        this.id_ = id_;
        this.address = address;
        this.phoneNo = phoneNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public int getId_() {
        return id_;
    }

    public void setId_(int id_) {
        this.id_ = id_;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
