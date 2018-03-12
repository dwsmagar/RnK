package com.susankya.schoolvalley;

/**
 * Created by Caredaworld on 2/16/2017.
 */

public class ChatListItemDetail {
    private String user_no,chat_no,text,firstname,lastname,userid;
    boolean hasNew=false;

    public boolean isHasNew() {
        return hasNew;
    }

    public void setHasNew(boolean hasNew) {
        this.hasNew = hasNew;
    }

    public String getUser_no() {
        return user_no;
    }

    public void setUser_no(String user_no) {
        this.user_no = user_no;
    }

    public String getChat_no() {
        return chat_no;
    }

    public void setChat_no(String chat_no) {
        this.chat_no = chat_no;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
