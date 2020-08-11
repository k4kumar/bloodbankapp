package com.bongobondhuparishad.bloodbank;

import java.io.Serializable;

public class PlasmaDonor implements Serializable {

    private String name;
    private String details;
    private int id;
    private String mobile;
    private String blood_group;
    private String reg_no;
    private String postive_date;
    private String recovery_date;


    public PlasmaDonor(String name, String details, int id, String mobile, String blood_group, String reg_no, String postive_date, String recovery_date) {
        this.name = name;
        this.details = details;
        this.id = id;
        this.mobile = mobile;
        this.blood_group = blood_group;
        this.reg_no = reg_no;
        this.postive_date = postive_date;
        this.recovery_date = recovery_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }

    public String getPostive_date() {
        return postive_date;
    }

    public void setPostive_date(String postive_date) {
        this.postive_date = postive_date;
    }

    public String getRecovery_date() {
        return recovery_date;
    }

    public void setRecovery_date(String recovery_date) {
        this.recovery_date = recovery_date;
    }
}
