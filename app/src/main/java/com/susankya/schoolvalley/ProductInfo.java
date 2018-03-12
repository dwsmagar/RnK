package com.susankya.schoolvalley;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Aditya on 11/25/2016.
 */
public class ProductInfo {

    private String name;

    public ArrayList<ImageURL> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageURL> images) {
        this.images = images;
    }

    private ArrayList<ImageURL> images;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    private Bitmap image;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    private String description;
    private String price;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    private String shopName;
    private String imageUrl;
    private int rating;
}
