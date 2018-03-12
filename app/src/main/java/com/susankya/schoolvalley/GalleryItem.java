package com.susankya.schoolvalley;

import android.graphics.Bitmap;

/**
 * Created by Aditya on 1/6/2017.
 */
public class GalleryItem {

    int imgSN,collegeSN,img_no,album_no;
    String caption;

    public boolean isBitmapFetched() {
        return bitmapFetched;
    }

    public void setBitmapFetched(boolean bitmapFetched) {
        this.bitmapFetched = bitmapFetched;
    }

    boolean bitmapFetched=false;

    public Bitmap getLoadedBitmap() {
        return loadedBitmap;
    }

    public void setLoadedBitmap(Bitmap loadedBitmap) {
        this.loadedBitmap = loadedBitmap;
    }

    Bitmap loadedBitmap;

    public String getURL() {
        return FragmentCodes.MAIN_DATABASE+URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public int getImgSN() {
        return imgSN;
    }

    public void setImgSN(int imgSN) {
        this.imgSN = imgSN;
    }

    public int getCollegeSN() {
        return collegeSN;
    }

    public void setCollegeSN(int collegeSN) {
        this.collegeSN = collegeSN;
    }

    public int getImg_no() {
        return img_no;
    }

    public void setImg_no(int img_no) {
        this.img_no = img_no;
    }

    public int getAlbum_no() {
        return album_no;
    }

    public void setAlbum_no(int album_no) {
        this.album_no = album_no;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    String URL;
}
