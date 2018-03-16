package com.susankya.rnk;

/**
 * Created by sanjog on 11/9/2015.
 */
public class Result{
    private String name,result,position,percentage;


    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    private int pos;

    private double phy;
    private double che;

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    private int marks;

    private double maths;
    private double bio_comp;
    private double nep;
    private int roll;
    private double eng;

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    private double total;

    public double getPhy() {
        return phy;
    }

    public double getChe() {
        return che;
    }

    public double getEng() {
        return eng;
    }

    public void setEng(double eng) {
        this.eng = eng;
    }

    public double getNep() {
        return nep;

    }

    public void setNep(double nep) {
        this.nep = nep;
    }

    public double getBio_comp() {
        return bio_comp;
    }

    public void setBio_comp(double bio_comp) {
        this.bio_comp = bio_comp;
    }

    public double getMaths() {
        return maths;
    }

    public void setMaths(double maths) {
        this.maths = maths;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public void setChe(double che) {
        this.che = che;
    }

    public void setPhy(double phy) {
        this.phy = phy;
    }

    public String getResult() {
        return result;

    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
    }
}