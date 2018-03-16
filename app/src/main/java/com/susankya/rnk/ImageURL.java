package com.susankya.rnk;

/**
 * Created by Aditya on 12/6/2016.
 */
public class ImageURL {

    private String URL;

    public ImageURL(String URL, String imageNum, String imageId) {
        this.URL = URL;
        this.imageNum = imageNum;
        this.imageId = imageId;
    }

    private String imageNum;

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getImageNum() {
        return imageNum;
    }

    public void setImageNum(String imageNum) {
        this.imageNum = imageNum;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    private String imageId;
}
