package com.susankya.schoolvalley;

/**
 * Created by ajay on 10/24/2015.
 */
public class TimexTimer {
    public int hour=0;
    public int minute=60;
    public int second=0;

    public TimexTimer(int h, int m, int s)
    {
        hour=h;
        minute=m;
        second=s;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }


}
