package com.susankya.schoolvalley;

/**
 * Created by ajay on 10/3/2015.
 */
public class Marks {


    private String subject, marks;

    public boolean isPassed()
    {

 return (Double.parseDouble(marks)>=50.0);
    }
    public boolean isPassed(String r)
    {
        if(marks.equals("FAIL"))
            return false;
        else return true;
    }
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

}
