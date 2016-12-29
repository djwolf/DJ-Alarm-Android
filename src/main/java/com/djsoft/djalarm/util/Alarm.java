package com.djsoft.djalarm.util;

/**
 * Created by djwolf on 12/28/2016.
 */

public class Alarm {
    private int day, hour, minute; //days start with sunday at 0
    private boolean complete = false;

    public Alarm()
    {
        day = 1;
        hour = 12;
        minute = 0;
    }

    public Alarm(int d, int h, int mi, boolean a)
    {
        day = d;
        hour = h;
        minute = mi;
    }

    public void completeAlarm() {complete = true;}
    public void setDay(int d) { day = d;}
    public void setTime(int h, int m) {hour = h; minute = m;}

    public int getDay() {return day;}
    public int[] getTime() {return new int[]{hour, minute};}
}
