package com.example.demo.converter;

import com.example.demo.constant.EventStatus;
import com.example.demo.exception.CustomException;
import com.example.demo.request.requests.CreateLeaveRequest;
import com.example.demo.response.ResponseObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DateTimeConvert {
    public static String convertLongToDate(Long agr) {
        Date date = new Date(agr);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String takeTimeReply(long createdTime) {

        long now = System.currentTimeMillis();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(now - createdTime);

        if (minutes > 60 * 24) {
            return (minutes / (60 * 24)) + " days ago";
        }
        if (minutes > 60) {
            return (minutes / 60) + " hours ago";
        }
        if (minutes < 60) {
            return minutes + " minutes ago";
        }
        return "Just now";
    }

    public static boolean checkInYear(Long timeBegin) {
        int thisYear = ZonedDateTime.now().getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeBegin);
        int yearBegin = calendar.get(Calendar.YEAR);
        return thisYear == yearBegin;
    }

    public static int getCurrentYear() {
        return LocalDateTime.now().getYear();
    }

    public static EventStatus isEventOver(Long timeBegin, Long timeEnd) {
        Date now = new Date(System.currentTimeMillis());
        Date start = new Date(timeBegin);
        Date end = new Date(timeEnd);
        if (now.before(start)) {
            return EventStatus.INCOMMING;
        } else if (now.after(end)) {
            return EventStatus.HAPPENED;
        }
        return EventStatus.HAPPENING;
    }

    public static long fromStringToMillis(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException ex) {
            throw new CustomException(ResponseObject.STATUS_CODE_BAD_REQUEST, ResponseObject.MESSAGE_FAIL_TO_PARSE_FROM_STRING_TO_LONG);
        }
        return date.getTime();
    }

    public static Double calculateDayOff(CreateLeaveRequest leaveRequest) {
        long startMillis = fromStringToMillis(leaveRequest.getDayStart());
        long endMillis = fromStringToMillis(leaveRequest.getDayEnd());
        LocalDate startDate = Instant.ofEpochMilli(startMillis).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = Instant.ofEpochMilli(endMillis).atZone(ZoneId.systemDefault()).toLocalDate();
        double result = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        if (leaveRequest.getShiftStart().equals(leaveRequest.getShiftEnd())) {
            if (startDate.equals(endDate)) {
                return 0.5;
            }
            return result - 0.5;
        }
        return result;
    }


}
