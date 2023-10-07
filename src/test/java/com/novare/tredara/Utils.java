package com.novare.tredara;

import java.util.Calendar;
import java.util.Date;

public class Utils {

    public static Date getPastTime(){
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.HOUR_OF_DAY,-1);

        return Date.from(calendar.toInstant());
    }

    public static Date getFutureTime(){
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.HOUR_OF_DAY,1);

        return Date.from(calendar.toInstant());
    }

}
