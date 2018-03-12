package com.susankya.schoolvalley.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventItem {
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("unique_code")
    @Expose
    public Integer uniqueCode;
    @SerializedName("barcode")
    @Expose
    public String barcode;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("price")
    @Expose
    public Integer price;
    @SerializedName("organized_by")
    @Expose
    public String organizedBy;
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("time")
    @Expose
    public String time;
    @SerializedName("picture")
    @Expose
    public String picture;
    @SerializedName("location")
    @Expose
    public String location;
    @SerializedName("created_on")
    @Expose
    public String createdOn;
    @SerializedName("updated_on")
    @Expose
    public String updatedOn;

    public int eventBanner;

    public EventItem(String name, String date, String time, String picture, String location) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    public EventItem(String name, String description, Integer price, String organized_by, String date, String time, String picture, String location) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.organizedBy = organized_by;
        this.date = date;
        this.time = time;
        this.picture = picture;
        this.location = location;
    }

    public EventItem() {
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public Integer getUniqueCode() {
        return uniqueCode;
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

    public String getOrganizedBy() {
        return organizedBy;
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

    public String getCreatedOn() {
        return createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public int getEventBanner() {
        return eventBanner;
    }
}
