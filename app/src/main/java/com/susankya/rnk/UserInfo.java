package com.susankya.rnk;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

/**
 * Created by ajay on 9/2/2015.
 */
public class UserInfo {
String faculty;

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    boolean isSelected;


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(int userNumber) {
        this.userNumber = userNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String coverPic;

    public UserInfo(int u, String f) {
        userNumber = u;
        fullName = f;
    }

    //`user_no`, `firstName`, `lastName`, `password`, `userid`, `verifed`, `institution`,
// `level`, `dbName`, `gender`, `profile_picture_location`, `phone_number`, `location`, `intrest`, `hobby`
    int verified;
    String institution;

    public boolean isDownloadStarted() {
        return downloadStarted;
    }

    public void setDownloadStarted(boolean downloadStarted) {
        this.downloadStarted = downloadStarted;
    }

    public boolean isThumbnailDownloaded() {
        return thumbnailDownloaded;
    }

    public void setThumbnailDownloaded(boolean thumbnailDownloaded) {
        this.thumbnailDownloaded = thumbnailDownloaded;
    }

    public boolean thumbnailDownloaded, downloadStarted;

    public String getSeePostsFrom() {
        return seePostsFrom;
    }

    public void setSeePostsFrom(String seePostsFrom) {
        this.seePostsFrom = seePostsFrom;
    }

    String seePostsFrom;
    Bitmap profilePic;

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Bitmap getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Bitmap profilePic) {
        this.profilePic = profilePic;
    }

    Bitmap thumbnail;

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilepiclocation() {
        return profilepiclocation;
    }

    public void setProfilepiclocation(String profilepiclocation) {
        this.profilepiclocation = profilepiclocation;
    }

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    String level;
    String dbName;
    String gender;
    String profilepiclocation;

    public String getProfilePicThumbnailLocation() {
        return profilePicThumbnailLocation;
    }

    public void setProfilePicThumbnailLocation(String profilePicThumbnailLocation) {
        this.profilePicThumbnailLocation = profilePicThumbnailLocation;
    }

    String profilePicThumbnailLocation;
    String phoneNumber;
    String location;
    String interest;
    String hobby;
    int blocked;

    public int getBlocked() {
        return blocked;
    }

    public void setBlocked(int blocked) {
        this.blocked = blocked;
    }

    public String getCollegeSN() {
        return collegeSN;
    }

    public void setCollegeSN(String collegeSN) {
        this.collegeSN = collegeSN;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    String collegeSN, section, roll;

    public UserInfo() {

    }

    public UserInfo(String username, String pass) {
        userName = username;
        password = pass;
    }

    private int userNumber;
    private String fullName;
    private String firstName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    private String lastName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getPassword() {
        return password;
    }


    private String userName;
    private String password;

    public UserInfo getUserInfoFromSharedPreferences(Context activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserNumber(sharedPreferences.getInt("number", 0));
        userInfo.setFirstName(sharedPreferences.getString("firstName", "error"));
        userInfo.setLastName(sharedPreferences.getString("lastName", "error"));
        userInfo.setUserName(sharedPreferences.getString("username", "error"));
        return userInfo;
    }
}
