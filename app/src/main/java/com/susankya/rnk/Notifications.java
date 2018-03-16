package com.susankya.rnk;

import org.json.JSONObject;

/**
 * Created by ajay on 10/20/2015.
 */
public class Notifications {

    public static final String postLikeSingular="likes your question.";
    public static final String postLikePlural="like your question.";
    public static final String postComment="answered your question.";
    public static final String voteup="voted your answer.";
    public static final String best="awarded your answer as the best answer.";

    public String notificationText,time,firstname,lastName,date;
    public int seenValue,categoryNumber;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCategoryNumber() {
        return categoryNumber;
    }

    public void setCategoryNumber(int categoryNumber) {
        this.categoryNumber = categoryNumber;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    private int serialNumber;
    public int postNumber;
    public static Notifications notificationBuilder(JSONObject jsonObject)
    {
       String username="",category="",time="",firstName="",lastName="",date="";
        int seenValue=0,postNumber=0,serialNumber=0,categoryNumber=0;

        Notifications notifications=new Notifications();
        try
        {
            try {
                if(jsonObject.getString("date")!=null)/*  {
    "sn": "20",
    "firstName": "Tests",
    "lastName": "Tests",
    "userid": "tests",
    "postNumber": "26",
    "seenValue": "1",
    "category": "like",
    "categoryNumber": "5",
    "date": "2016-01-02",
    "time": "04:22:19"
  },*/
                {
                    date=jsonObject.getString("date");
                }
            }
            catch (Exception e)
            {

            }


            firstName=jsonObject.getString("firstName");
            lastName=jsonObject.getString("lastName");
            time=jsonObject.getString("time");
            categoryNumber=jsonObject.getInt("categoryNumber");
             username=jsonObject.getString("userid");
            category=jsonObject.getString("category");
            serialNumber=jsonObject.getInt("sn");
             seenValue=jsonObject.getInt("seenValue");
            postNumber=jsonObject.getInt("postNumber");


        }
        catch (Exception e)
        {

        }
        String colorfulName="<b><font color='#607D8B'>"+firstName+" "+lastName+" "+"</font></b>";
        if(category.equals("like"))
        {
            notifications.setNotificationText(colorfulName+ Notifications.postLikeSingular);
        }
        else if(category.equals("reply"))
        notifications.setNotificationText(colorfulName+"also gave an answer to this question.");
        else if(category.equals("comment")) {
            notifications.setNotificationText(colorfulName+ Notifications.postComment);
        }
        else if (category.equals("VoteUp"))
            notifications.setNotificationText(colorfulName+Notifications.voteup);
        else
            notifications.setNotificationText(colorfulName+Notifications.best);
        notifications.setTime(time);
        notifications.setPostNumber(postNumber);
        notifications.setSerialNumber(serialNumber);
        notifications.setSeenValue(seenValue);
        notifications.setCategoryNumber(categoryNumber);
        notifications.setFirstname(firstName);
        notifications.setLastName(lastName);
        notifications.setDate(date);
        return  notifications;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void parseJSONObject(JSONObject jsonObject)
    {


    }

    public int getPostNumber() {
        return postNumber;
    }

    public void setPostNumber(int postNumber) {
        this.postNumber = postNumber;
    }

    public int getSeenValue() {
        return seenValue;
    }

    public void setSeenValue(int seenValue) {
        this.seenValue = seenValue;
    }


}
