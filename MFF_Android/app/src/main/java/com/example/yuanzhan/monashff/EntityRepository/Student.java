package com.example.yuanzhan.monashff.EntityRepository;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by YuanZhan on 25/04/2017.
 */

public class Student implements Parcelable {
    private Long stdId;
    private String fname;
    private String lname;
    private Date dob;
    private Boolean gender;
    private String course;
    private Boolean mode;
    private String address;
    private String suburb;
    private String nationality;
    private String lang;
    private String favouriteSport;
    private String favouriteMovie;
    private String favouriteUnit;
    private String currentJob;
    private String emailAddr;
    private String pwd;
    private Date subscriptDatetime;

    public Student(Parcel in) {
        this.stdId = in.readLong();
        this.fname = in.readString();
        this.lname = in.readString();
        this.dob = new Date(in.readLong());
        this.gender = in.readByte() != 0;
        this.course = in.readString();
        this.mode = in.readByte() != 0;
        this.address = in.readString();
        this.suburb = in.readString();
        this.nationality = in.readString();
        this.lang = in.readString();
        this.favouriteSport = in.readString();
        this.favouriteMovie = in.readString();
        this.favouriteUnit = in.readString();
        this.currentJob = in.readString();
        this.emailAddr = in.readString();
        this.pwd = in.readString();
        this.subscriptDatetime = new Date(in.readLong());
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    public Student() {
    }

    public Student(Long stdId) {
        this.stdId = stdId;
    }

    public Student(Long stdId, String fname,
                   String lname, Date dob, Boolean gender,
                   String course, Boolean mode, String address,
                   String suburb, String nationality, String lang,
                   String favouriteSport, String favouriteMovie, String favouriteUnit,
                   String currentJob, String emailAddr, String pwd, Date subscriptDatetime) {
        this.stdId = stdId;
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
        this.gender = gender;
        this.course = course;
        this.mode = mode;
        this.address = address;
        this.suburb = suburb;
        this.nationality = nationality;
        this.lang = lang;
        this.favouriteSport = favouriteSport;
        this.favouriteMovie = favouriteMovie;
        this.favouriteUnit = favouriteUnit;
        this.currentJob = currentJob;
        this.emailAddr = emailAddr;
        this.pwd = pwd;
        this.subscriptDatetime = subscriptDatetime;
    }

    public Long getStdId() {
        return stdId;
    }

    public void setStdId(Long stdId) {
        this.stdId = stdId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Boolean getMode() {
        return mode;
    }

    public void setMode(Boolean mode) {
        this.mode = mode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getFavouriteSport() {
        return favouriteSport;
    }

    public void setFavouriteSport(String favouriteSport) {
        this.favouriteSport = favouriteSport;
    }

    public String getFavouriteMovie() {
        return favouriteMovie;
    }

    public void setFavouriteMovie(String favouriteMovie) {
        this.favouriteMovie = favouriteMovie;
    }

    public String getFavouriteUnit() {
        return favouriteUnit;
    }

    public void setFavouriteUnit(String favouriteUnit) {
        this.favouriteUnit = favouriteUnit;
    }

    public String getCurrentJob() {
        return currentJob;
    }

    public void setCurrentJob(String currentJob) {
        this.currentJob = currentJob;
    }

    public String getEmailAddr() {
        return emailAddr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Date getSubscriptDatetime() {
        return subscriptDatetime;
    }

    public void setSubscriptDatetime(Date subscriptDatetime) {
        this.subscriptDatetime = subscriptDatetime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(stdId);
        dest.writeString(fname);
        dest.writeString(lname);
        dest.writeLong(dob.getTime());
        dest.writeByte((byte) (gender ? 1 : 0));// write boolean
        dest.writeString(course);
        dest.writeByte((byte) (mode ? 1 : 0)); // write boolean
        dest.writeString(address);
        dest.writeString(suburb);
        dest.writeString(nationality);
        dest.writeString(lang);
        dest.writeString(favouriteSport);
        dest.writeString(favouriteMovie);
        dest.writeString(favouriteUnit);
        dest.writeString(currentJob);
        dest.writeString(emailAddr);
        dest.writeString(pwd);
        dest.writeLong(subscriptDatetime.getTime());
    }


}
