package com.novare.tredara.utils;

import java.util.Date;

public class DateUtils {
    public static String getTimeToBidEnd(Date endDateTime) {
        Date currentTime = new Date();
        if (endDateTime.after(currentTime)) {
            long timeToBidEndMillis = endDateTime.getTime() - currentTime.getTime();

            // Calculate days, hours, and minutes
            long seconds = timeToBidEndMillis / 1000;
            long days = seconds / 86400; // 86400 seconds in a day
            long hours = (seconds % 86400) / 3600; // 3600 seconds in an hour
            long minutes = ((seconds % 86400) % 3600) / 60; // 60 seconds in a minute

            return days + " days, " + hours + " hours, " + minutes + " minutes";
        } else {
            return "Bidding has ended";
        }
    }
}