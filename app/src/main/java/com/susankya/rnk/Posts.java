package com.susankya.rnk;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 8/28/2015.
 */
public class Posts {
    private Bitmap mImage,thumbnail;
private String thumbnailLocation;

    private int isAd;

    public String getWebsiteURL() {
        return websiteURL;
    }

    public void setWebsiteURL(String websiteURL) {
        this.websiteURL = websiteURL;
    }

    public int getIsAd() {
        return isAd;
    }

    public void setIsAd(int isAd) {
        this.isAd = isAd;
    }

    private String websiteURL;
    public String getThumbnailLocation() {
        return thumbnailLocation;
    }

    public void setThumbnailLocation(String thumbnailLocation) {
        this.thumbnailLocation = thumbnailLocation;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public boolean isThumbnailDownloaded() {
        return isThumbnailDownloaded;
    }

    public void setIsThumbnailDownloaded(boolean isThumbnailDownloaded) {
        this.isThumbnailDownloaded = isThumbnailDownloaded;
    }

    private boolean isThumbnailDownloaded;
    private int userNumber;
    private boolean deletePermission;
    private int isLiked;

    public int getUserVerified() {
        return userVerified;
    }

    public void setUserVerified(int userVerified) {
        this.userVerified = userVerified;
    }

    private int userVerified=0;
    private int hasImage=0;

    private int isBitmapDownload=0;
    private int NewsNo;
    private String username;


    private String fullname;
    private int like;
    private int fake;
    private int comment;

    public int getHasBest() {
        return hasBest;
    }

    public void setHasBest(int hasBest) {
        this.hasBest = hasBest;
    }

    private int hasBest;
    private Bitmap bitmap;
    private String bitmapLocation;

    public String getSharedPicLocation() {
        return sharedPicLocation;
    }

    public void setSharedPicLocation(String sharedPicLocation) {
        this.sharedPicLocation = sharedPicLocation;
    }

    private String sharedPicLocation;
    private String description;
    private String date;
    private String[] comments;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getHasImage() {
        return hasImage;
    }

    public void setHasImage(int hasImage) {
        this.hasImage = hasImage;
    }
    public int getIsBitmapDownload() {
        return isBitmapDownload;
    }

    public void setIsBitmapDownload(int isBitmapDownload) {
        this.isBitmapDownload = isBitmapDownload;
    }


    public Posts(){}

    public int getNewsNo() {
        return NewsNo;
    }

    public void setNewsNo(int newsNo) {
        NewsNo = newsNo;
    }



    public int getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(int userNumber) {
        this.userNumber = userNumber;
    }

    public boolean getDeletePermission() {
        return deletePermission;
    }

    public void setDeletePermission(boolean deletePermission) {
        this.deletePermission = deletePermission;
    }

    public int getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(int isLiked) {
        this.isLiked = isLiked;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public String getBitmapLocation() {
        return bitmapLocation;
    }

    public void setBitmapLocation(String bitmapLocation) {
        this.bitmapLocation = Utilities.encodeLinkSpace(bitmapLocation);
    }

    public Bitmap getBitmap() {

        return bitmap;

    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public int getFake() {
        return fake;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public void setFake(int fake) {

        this.fake = fake;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String[] getComments() {
        return comments;
    }

    public void setComments(String[] comments) {
        this.comments = comments;
    }


 }
