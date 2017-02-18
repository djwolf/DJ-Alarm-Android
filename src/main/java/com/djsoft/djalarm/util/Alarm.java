package com.djsoft.djalarm.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.PreferenceManager;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by djwolf on 12/28/2016.
 */

public class Alarm implements Serializable {
    private int day, hour, minute, month, year;
    private boolean complete = false;
    private static SharedPreferences settings;
    private static Context appContext;
    public static Calendar alCal;

    public Alarm(Context c)
    {
        hour = 0;
        minute = 0;
        appContext = c;
        settings = PreferenceManager.getDefaultSharedPreferences(c);
        //SharedPreferences.Editor sharedPrefEdit = settings.edit();

        //determine the date of the alarm
        Calendar calInstance = Calendar.getInstance();
        int curHour = calInstance.HOUR;
        int curMinute = calInstance.MINUTE;
        if (hour > curHour)
        {
            day = calInstance.DATE;
        } else if (hour < curHour) {
            calInstance.add(calInstance.DATE,1);
            day = calInstance.DATE;
        } else if (hour == curHour)
        {
            if (minute < curMinute) {
                calInstance.add(calInstance.DATE,1);
                day = calInstance.DATE;
            } else
            {
                day = calInstance.DATE;
            }
        }
        month = calInstance.MONTH;
        year = calInstance.YEAR;
        System.out.println("DATE DEBUG(Empty Constructor):\nday: " + day + "\nmonth: " + month + "\nyear: " + year);

        /*
        sharedPrefEdit.putInt("ALARM_HOUR",hour);
        sharedPrefEdit.putInt("ALARM_MINUTE",minute);
        sharedPrefEdit.putInt("ALARM_DAY",day);
        sharedPrefEdit.putInt("ALARM_MONTH",month);
        sharedPrefEdit.putInt("ALARM_YEAR",year);
        */
        System.out.println("CALLING CLASS: " + new Exception().getStackTrace()[1].getClassName());
    }

    public Alarm(int h, int mi, Context c)
    {
        appContext = c;
        settings = PreferenceManager.getDefaultSharedPreferences(c);
        hour = h;
        minute = mi;
        SharedPreferences.Editor sharedPrefEdit = settings.edit();

        //determine the date of the alarm
        Calendar calInstance = Calendar.getInstance();
        Date dateInstance = calInstance.getTime();
        int curHour = calInstance.get(calInstance.HOUR_OF_DAY);
        int curMinute = calInstance.get(calInstance.MINUTE);
        System.out.println("Cur Hour: " + curHour + "hour: " + hour);
        if (curHour < hour)
        {
            day = calInstance.get(calInstance.DATE);
        } else if (curHour > hour) {
            System.out.println("Adding day");
            calInstance.add(calInstance.DATE,1);
            day = calInstance.get(calInstance.DATE);
        } else if (curHour == hour)
        {
            if (minute < curMinute) {
                System.out.println("Adding day");
                calInstance.add(calInstance.DATE,1);
                day = calInstance.get(calInstance.DATE);
            } else
            {
                day = calInstance.get(calInstance.DATE);
            }
        }
        month = calInstance.get(Calendar.MONTH);
        year = calInstance.get(Calendar.YEAR);
        System.out.println("DATE DEBUG(Proper):\nday: " + day + "\nmonth: " + month + "\nyear: " + year);

        sharedPrefEdit.putInt("ALARM_HOUR",hour);
        sharedPrefEdit.putInt("ALARM_MINUTE",minute);
        sharedPrefEdit.putInt("ALARM_DAY",day);
        sharedPrefEdit.putInt("ALARM_MONTH",month);
        sharedPrefEdit.putInt("ALARM_YEAR",year);
        sharedPrefEdit.apply();
        System.out.println("CALLING CLASS: " + new Exception().getStackTrace()[1].getClassName());

        alCal = Calendar.getInstance();
        alCal.set(year,month,day,hour,minute);
    }

    public Alarm(int h, int mi, int d, int m, int y, Context c)
    {
        hour = h;
        minute = mi;
        appContext = c;
        alCal = Calendar.getInstance();
        alCal.set(y,m,d,h,mi);
    }

    public void completeAlarm() {complete = true;}

    public int[] getTime() {return new int[]{hour, minute};}
}