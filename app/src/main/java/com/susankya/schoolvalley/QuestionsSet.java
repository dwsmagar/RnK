package com.susankya.schoolvalley;

/**
 * Created by ajay on 12/19/2015.
 */
public class QuestionsSet {

    public String getQuestionsCode() {
        return questionsCode;
    }

    public void setQuestionsCode(String questionsCode) {
        this.questionsCode = questionsCode;
    }

    String questionsCode;

    public int getSizeOfSet() {
        return sizeOfSet;
    }

    public void setSizeOfSet(int sizeOfSet) {
        this.sizeOfSet = sizeOfSet;
    }

    int sizeOfSet;

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    boolean downloaded=false;

}
