package com.susankya.rnk.Fragments;

import java.util.HashMap;
import java.util.Map;

public class Appointment {

    public Integer id;
    public String full_name;
    public Integer branch;
    public String address;
    public String telephone_no;
    public String mobile_no;
    public String email;
    public String purpose_of_visit;
    public String evidence_of_id;
    public String evidence_of_id_number;
    public String appointment;
    public String date;
    public String time;
    public String message;
    public String created_date;
    public Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getId() {
        return id;
    }

    public String getFull_name() {
        return full_name;
    }

    public Integer getBranch() {
        return branch;
    }

    public String getAddress() {
        return address;
    }

    public String getTelephone_no() {
        return telephone_no;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public String getEmail() {
        return email;
    }

    public String getPurpose_of_visit() {
        return purpose_of_visit;
    }

    public String getEvidence_of_id() {
        return evidence_of_id;
    }

    public String getEvidence_of_id_number() {
        return evidence_of_id_number;
    }

    public String getAppointment() {
        return appointment;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public String getCreated_date() {
        return created_date;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }
}
