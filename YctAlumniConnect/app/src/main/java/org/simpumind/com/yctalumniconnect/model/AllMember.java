package org.simpumind.com.yctalumniconnect.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by simpumind on 1/6/16.
 */
public class AllMember implements Parcelable{

    public String userName;
    public String firstName;
    public String lastName;
    public String email;
    public String dOB;
    public String course;
    public String address;
    public String phone;
    public String sex;
    public String yearOfDegree;
    public String middleName;

    public AllMember(){}

    private AllMember(Parcel in){
        userName = in.readString();
        firstName = in.readString();
        middleName = in.readString();
        lastName = in.readString();
        sex = in.readString();
        phone = in.readString();
        email = in.readString();
        course = in.readString();
        address = in.readString();
        yearOfDegree = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(userName);
        parcel.writeString(firstName);
        parcel.writeString(middleName);
        parcel.writeString(lastName);
        parcel.writeString(sex);
        parcel.writeString(phone);
        parcel.writeString(email);
        parcel.writeString(course);
        parcel.writeString(address);
        parcel.writeString(yearOfDegree);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getdOB() {
        return dOB;
    }

    public void setdOB(String dOB) {
        this.dOB = dOB;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getYearOfDegree() {
        return yearOfDegree;
    }

    public void setYearOfDegree(String yearOfDegree) {
        this.yearOfDegree = yearOfDegree;
    }

    public static final Creator<AllMember> CREATOR = new Creator<AllMember>() {
        @Override
        public AllMember createFromParcel(Parcel source) {
            return new AllMember(source);
        }

        @Override
        public AllMember[] newArray(int size) {
            return new AllMember[size];
        }
    };
}
