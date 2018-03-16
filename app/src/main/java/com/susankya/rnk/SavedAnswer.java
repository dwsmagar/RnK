package com.susankya.rnk;

/**
 * Created by ajay on 1/11/2016.
 */
public class SavedAnswer {

    int postNumber;
    String dateTime;
    String answer;

    public SavedAnswer(int pN,String dT,String answer)
    {
        postNumber=pN;
        dateTime=dT;
        this.answer=answer;
    }
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getPostNumber() {
        return postNumber;
    }

    public void setPostNumber(int postNumber) {
        this.postNumber = postNumber;
    }

    String question;
}
