package com.susankya.rnk.Models;

import java.util.HashMap;
import java.util.Map;

public class EventItem {
    public Integer id;
    public String name;
    public Integer branch;
    public Integer unique_code ;
    public String barcode;
    public String description;
    public Integer price;
    public String organized_by;
    public String date;
    public String time;
    public String picture;
    public String location;
    public String created_on;
    public String updated_on;
    public Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getBranch() {
        return branch;
    }

    public Integer getUnique_code() {
        return unique_code;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPrice() {
        return price;
    }

    public String getOrganized_by() {
        return organized_by;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPicture() {
        return picture;
    }

    public String getLocation() {
        return location;
    }

    public String getCreated_on() {
        return created_on;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }
}
