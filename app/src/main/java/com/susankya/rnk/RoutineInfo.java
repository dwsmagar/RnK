package com.susankya.rnk;

/**
 * Created by ajay on 12/6/2015.
 */
public class RoutineInfo {

    String time;
    String subject;

    String start;
    String end;

    public RoutineInfo() {
        time=subject=teacher=end=start=section=day=collegeSN=periodNo="NA";
        periodNo="1";
        start="10:00 AM";
        end="12:00 PM";
        teacher="Teacher Name";
        subject="Subject";
        collegeSN="2";
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getCollegeSN() {
        return collegeSN;
    }

    public void setCollegeSN(String collegeSN) {
        this.collegeSN = collegeSN;
    }

    public String getPeriodNo() {
        return periodNo;
    }

    public void setPeriodNo(String periodNo) {
        this.periodNo = periodNo;
    }

    String teacher;
    String collegeSN;
    String periodNo;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String day;
    String section;
}
