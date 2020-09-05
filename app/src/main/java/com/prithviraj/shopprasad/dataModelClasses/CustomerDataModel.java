package com.prithviraj.shopprasad.dataModelClasses;

public class CustomerDataModel {

    private String name, address, postOffice, landMark;
    private String phoneNumber, alternativePhoneNumber, pinNumber, profileImage;
    private int userID;

    public CustomerDataModel() {
    }

    public CustomerDataModel(String name, String address, String postOffice, String landMark, String phoneNumber, String alternativePhoneNumber, String pinNumber, int userID, String profileImage) {
        this.name = name;
        this.address = address;
        this.postOffice = postOffice;
        this.landMark = landMark;
        this.phoneNumber = phoneNumber;
        this.alternativePhoneNumber = alternativePhoneNumber;
        this.pinNumber = pinNumber;
        this.userID = userID;
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostOffice() {
        return postOffice;
    }

    public void setPostOffice(String postOffice) {
        this.postOffice = postOffice;
    }

    public String getLandMark() {
        return landMark;
    }

    public void setLandMark(String landMark) {
        this.landMark = landMark;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAlternativePhoneNumber() {
        return alternativePhoneNumber;
    }

    public void setAlternativePhoneNumber(String alternativePhoneNumber) {
        this.alternativePhoneNumber = alternativePhoneNumber;
    }

    public String getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(String pinNumber) {
        this.pinNumber = pinNumber;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
