package com.susankya.rnk;

import java.util.UUID;

/**
 * Created by Administrator on 8/4/2015.
 */
public class notice {
    private String Title="UNTITLED",Description,Category;
 private int Notice_num;
        public int likeNum;
    public boolean liked=false;
    public int seen,totalSeenCount;
    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
    public boolean isFooter=false;
    public String  sizeMB;
    private String fileSize;

    public String getMonthDay() {
        return monthDay;
    }

    public void setMonthDay(String monthDay) {
        this.monthDay = monthDay;
    }

    private String monthDay;
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    private String filename;
    public boolean isOpenDialog() {
        return openDialog;
    }

    public void setOpenDialog(boolean openDialog) {
        this.openDialog = openDialog;
    }

    boolean openDialog=false;

    public String getAttachmenturl() {
        return attachmenturl;
    }

    public void setAttachmenturl(String attachmenturl) {
        this.attachmenturl = attachmenturl;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public boolean isHasAttachment() {
        return hasAttachment;
    }

    public void setHasAttachment(boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }

    public boolean isHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    private String attachmenturl,imgurl;
    private boolean hasAttachment=false;
    private boolean hasImage=false;



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String date,time;

private UUID id;
    public String getTitle() {
        return Title;
    }


    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public int getNotice_num() {
        return Notice_num;
    }

    public void setNotice_num(int notice_num) {
        Notice_num = notice_num;
    }


    @Override
    public String toString(){
        return Title;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public notice(){
   Title="Untitled";
        id= UUID.randomUUID();
        Description="Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....Description wil be here.....";
   Category="Normal";

    }
}
