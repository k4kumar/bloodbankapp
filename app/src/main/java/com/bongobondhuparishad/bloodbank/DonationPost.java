package com.bongobondhuparishad.bloodbank;

import java.io.Serializable;

public class DonationPost implements Serializable {
    private String publish_date;
    private String location;
    private String hospital;
    private String contact;
    private String blood_group;
    private String details;
    private String username;

    public DonationPost(String publish_date, String location, String hospital, String contact, String blood_group, String details, String username) {
        this.publish_date = publish_date;
        this.location = location;
        this.hospital = hospital;
        this.contact = contact;
        this.blood_group = blood_group;
        this.details = details;
        this.username = username;
    }

    public String getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
