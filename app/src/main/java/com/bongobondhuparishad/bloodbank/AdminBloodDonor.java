package com.bongobondhuparishad.bloodbank;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminBloodDonor implements Serializable {
    private String name;
    private String details;
    private int id;
    private String mobile;
    private String blood_group;
    private String email;
    private String division;
    private String last_donation_date;
    private String reg_no;
    private String comment;
    private String emergency_contact;
    private boolean is_approved;
    private boolean has_donated;
    private String background_color;

    public AdminBloodDonor(String name,  int id, String mobile, String blood_group,
                           String email, String division, String last_donation_date, String reg_no,
                           String comment, String emergency_contact, boolean is_approved, boolean has_donated, String details) {
        final SimpleDateFormat out_format = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat in_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date input_date= new Date();

        try {
            input_date = in_format.parse(last_donation_date==null? "01-01-2020":last_donation_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        this.name = name;
        this.details = details;
        this.id = id;
        this.mobile = mobile;
        this.blood_group = blood_group;
        this.email = email==null? "":email;
        this.division = division;
        this.last_donation_date = out_format.format(input_date);
        this.reg_no = reg_no;
        this.comment = comment==null? "":comment;
        this.emergency_contact = emergency_contact==null? mobile:emergency_contact;
        this.is_approved = is_approved;
        this.has_donated = has_donated;
    }

    public boolean isIs_approved() {
        return is_approved;
    }

    public boolean isHas_donated() {
        return has_donated;
    }

    public String isBackground_color() {
        return background_color;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getLast_donation_date() {
        return last_donation_date==null? "1-Jan-2000":last_donation_date;
    }

    public void setLast_donation_date(String last_donation_date) {
        this.last_donation_date = last_donation_date;
    }

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmergency_contact() {
        return emergency_contact;
    }

    public void setEmergency_contact(String emergency_contact) {
        this.emergency_contact = emergency_contact;
    }

    public boolean is_approved() {
        return is_approved;
    }

    public void setIs_approved(boolean is_approved) {
        this.is_approved = is_approved;
    }

    public boolean has_donated() {
        return has_donated;
    }

    public void setHas_donated(boolean has_donated) {
        this.has_donated = has_donated;
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
}
