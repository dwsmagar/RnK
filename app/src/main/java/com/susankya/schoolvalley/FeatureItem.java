package com.susankya.schoolvalley;

import android.graphics.Bitmap;

/**
 * Created by ajay on 12/9/2015.
 */
public class FeatureItem {
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    private String text;
    Bitmap image;
}
