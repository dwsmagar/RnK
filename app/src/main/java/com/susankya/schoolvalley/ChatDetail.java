package com.susankya.schoolvalley;

/**
 * Created by Abhinav Dev on 2/8/2017.
 */

public class ChatDetail {
   private String name,text,time,date;
    private String userSn;
    private int chat_no;

    public int getChat_no() {
        return chat_no;
    }

    public void setChat_no(int chat_no) {
        this.chat_no = chat_no;
    }

    private String collegeSn;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSentBy() {

        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public String getCollegeSn() {
        return collegeSn;
    }

    public void setCollegeSn(String collegeSn) {
        this.collegeSn = collegeSn;
    }

    private String sentBy;

    public String getUserSn() {
        return userSn;
    }

    public void setUserSn(String userSn) {
        this.userSn = userSn;
    }

    public ChatDetail(){
    this.name="Abhinav";
    this.text="hello";
    this.time="10:56 AM";
        this.sentBy="";
    this.date="Date";
        this.userSn="5";
        this.status="sending";

}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
