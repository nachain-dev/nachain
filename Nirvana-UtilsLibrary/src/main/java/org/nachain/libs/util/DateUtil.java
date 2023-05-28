package org.nachain.libs.util;

import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class DateUtil {

    public static String yyyyMMdd = "yyyy-MM-dd";

    public static String yyyyMMdd_HHmmss = "yyyy-MM-dd HH:mm:ss";

    public static String yyyyMMdd_HHmmss_NCName = "yyyy-MM-dd_HH-mm-ss";


    public static long Minute_1_Millisecond = 60000;


    public static long Hours_1_Millisecond = 3600000;


    public static long Day_1_Millisecond = 86400000;


    public static String getDate(int day) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = new Date();
        long nowTime = nowDate.getTime();
        long dateTime = (24 * 60 * 60 * 1000) * day;
        long newTime = nowTime + dateTime;
        Date newDate = new Date(newTime);

        return formatter.format(newDate);
    }


    public static Date addDate(Date date, long millisecond) {

        if (millisecond < 0) {
            try {
                throw new Exception("addDate error, millisecond=" + millisecond);
            } catch (Exception e) {
                log.error("addDate error", e);
            }
        }

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(getMillis(date) + millisecond);

        return c.getTime();
    }


    public static Date subtractDate(Date date, long millisecond) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(getMillis(date) - millisecond);

        return c.getTime();
    }


    public static long diffDate(Date date1, Date date2) {
        return getMillis(date1) - getMillis(date2);
    }


    public static long diffDateBySecond(Date date1, Date date2) {

        long millis = diffDate(date1, date2);
        return millis / 1000;
    }


    public static long diffDateByMinute(Date date1, Date date2) {

        long millis = diffDate(date1, date2);
        return millis / 1000 / 60;
    }


    public static long diffDateByHour(Date date1, Date date2) {

        long minute = diffDateByMinute(date1, date2);
        return minute / 60;
    }


    public static long diffDateByDay(Date startDate, Date endDate) {
        String strStartDate = DateUtil.formatByDate(startDate);
        String strEndDate = DateUtil.formatByDate(endDate);

        Date _startDate = DateUtil.parseByDate(strStartDate);
        Date _endDate = DateUtil.parseByDate(strEndDate);

        return (_endDate.getTime() - _startDate.getTime()) / Day_1_Millisecond;
    }


    public static int getDayOfWeek(Date defaultDate) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(defaultDate);

        int nowWeekDay = calendar.get(Calendar.DAY_OF_WEEK);

        if (nowWeekDay == 1) {
            nowWeekDay = 7;
        } else {
            nowWeekDay = nowWeekDay - 1;
        }

        return nowWeekDay;
    }


    public static Date getWeekDay(Date defaultDate, int weekDay) {
        Date returnValue;

        int nowWeekDay = getDayOfWeek(defaultDate);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(defaultDate);
        calendar.add(GregorianCalendar.DATE, weekDay - nowWeekDay);
        returnValue = calendar.getTime();

        return returnValue;
    }


    public static Date getNextWeekDay(Date defaultDate, int weekDay) {
        Date returnValue;


        Date date = getWeekDay(defaultDate, 7);


        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        calendar.add(Calendar.DATE, weekDay);
        returnValue = calendar.getTime();

        return returnValue;
    }


    public static int getDayOfMonth(Date date) {
        int returnValue = 0;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        returnValue = calendar.get(Calendar.DAY_OF_MONTH);

        return returnValue;
    }


    public static Date getMonthFirstDay(Date defaultDate) {
        Date returnValue;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(defaultDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        returnValue = calendar.getTime();

        return returnValue;
    }


    public static Date getMonthEndDay(Date defaultDate) {
        Date returnValue;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(defaultDate);

        calendar.get(Calendar.MILLISECOND);

        calendar.set(Calendar.MILLISECOND, 0);
        int year = calendar.get(Calendar.YEAR);

        int month = calendar.get(Calendar.MONTH);

        calendar.set(year, month + 1, 1);

        calendar.add(Calendar.DATE, -1);
        returnValue = calendar.getTime();

        return returnValue;
    }


    public static Date getPreviousMonthEndDay(Date defaultDate) {
        Date returnValue;


        Date date = getMonthFirstDay(defaultDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.DATE, -1);
        returnValue = calendar.getTime();

        return returnValue;
    }


    public static Date getNextMonthFirstDay(Date defaultDate) {
        Date returnValue;


        Date date = getMonthEndDay(defaultDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.DATE, 1);
        returnValue = calendar.getTime();

        return returnValue;
    }


    public static Date getQuarterFirstDay(Date defaultDate) {
        Date returnValue;


        int quarter = getQuarter(defaultDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(defaultDate);

        calendar.set(Calendar.MONTH, quarter * 3 - 2 - 1);

        returnValue = getMonthFirstDay(calendar.getTime());

        return returnValue;
    }


    public static Date getQuarterEndDay(Date defaultDate) {
        Date returnValue;


        int quarter = getQuarter(defaultDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(defaultDate);

        calendar.set(Calendar.MONTH, quarter * 3 - 1);

        returnValue = getMonthEndDay(calendar.getTime());

        return returnValue;
    }


    public static int getQuarter(Date defaultDate) {
        int quarter = 0;


        int month = getMonth(defaultDate);

        if (month >= 1 && month <= 3) {
            quarter = 1;
        } else if (month >= 4 && month <= 6) {
            quarter = 2;
        } else if (month >= 7 && month <= 9) {
            quarter = 3;
        } else if (month >= 9 && month <= 12) {
            quarter = 4;
        }

        return quarter;
    }


    public static Date getYearFirstDay(Date defaultDate) {
        Date returnValue;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(defaultDate);
        calendar.set(Calendar.MONTH, 0);
        returnValue = getMonthFirstDay(calendar.getTime());

        return returnValue;
    }


    public static Date getYearEndDay(Date defaultDate) {
        Date returnValue;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(defaultDate);
        calendar.set(Calendar.MONTH, 11);
        returnValue = getMonthEndDay(calendar.getTime());

        return returnValue;
    }


    public static boolean isLastDayOfWeek(Date date) {

        if (DateUtil.getDayOfWeek(date) == 7) {
            return true;
        }

        return false;
    }


    public static boolean isLastDayOfMonth(Date date) {

        if (DateUtil.getDayOfMonth(DateUtil.getMonthEndDay(date)) == DateUtil.getDayOfMonth(date)) {
            return true;
        }

        return false;
    }


    public static Date keepDateClearTime(Date date) {
        Date returnValue;

        returnValue = DateUtil.parseByDate(DateUtil.formatByDate(date));

        return returnValue;
    }


    @Deprecated
    public static Date keepHourClearMinuteSecond(Date date) {
        return keepByHour(date, 1);
    }


    public static Date keepByHour(Date date, int perHour) {
        Date returnValue;

        long millisecond = date.getTime();


        long hourTotal = (long) (millisecond / 1000 / 60 / 60);


        long perTotal = hourTotal / perHour;


        long newHourTotal = perTotal * perHour;


        long newMillisecond = newHourTotal * 60 * 60 * 1000;


        returnValue = new Date(newMillisecond);

        System.out.println(perTotal + ", " + perHour);

        System.out.println(hourTotal + ", " + newHourTotal);

        System.out.println(millisecond + ", " + newMillisecond);

        return returnValue;
    }


    public static Date keepByMinute(Date date, int perMinute) {
        Date returnValue;

        long millisecond = date.getTime();


        long minuteTotal = (long) (millisecond / 1000 / 60);


        long perTotal = minuteTotal / perMinute;


        long newMinuteTotal = perTotal * perMinute;


        long newMillisecond = newMinuteTotal * 60 * 1000;


        returnValue = new Date(newMillisecond);

        return returnValue;
    }


    public static Date keepBySecond(Date date, int perSecond) {
        Date returnValue;

        long millisecond = date.getTime();


        long secondTotal = (long) (millisecond / 1000);


        long perTotal = secondTotal / perSecond;


        long newSecondTotal = perTotal * perSecond;


        long newMillisecond = newSecondTotal * 1000;


        returnValue = new Date(newMillisecond);

        return returnValue;
    }


    public static String buildTimeByMillis(long millisecond) {
        StringBuffer returnValue = new StringBuffer();


        final long hourSecond = 3600000;


        int hour = (int) (millisecond / hourSecond);
        if (hour > 0) {
            millisecond = millisecond - hour * hourSecond;
        }

        int minute = (int) (millisecond / 60000);
        if (minute > 0) {
            millisecond = millisecond - minute * 60000;
        }

        int second = (int) (millisecond / 1000);
        if (second > 0) {
            millisecond = millisecond - second * 1000;
        }

        returnValue.append(hour < 10 ? "0" : "");
        returnValue.append(hour);
        returnValue.append(":");
        returnValue.append(minute < 10 ? "0" : "");
        returnValue.append(minute);
        returnValue.append(":");
        returnValue.append(second < 10 ? "0" : "");
        returnValue.append(second);
        returnValue.append(" ");
        returnValue.append(millisecond);

        return returnValue.toString();
    }


    public static String buildTimeByMillis_NoMillis(long millisecond) {
        return buildTimeByMillis(millisecond).substring(0, 8);
    }


    public static String buildTimeByMillis_HHmmss(long millisecond) {

        String time = buildTimeByMillis(millisecond);

        int endIndex = time.indexOf(" ");

        if (time.contains(" ")) {
            time = time.substring(0, endIndex);
        }

        return time;
    }


    public static Date parse(String dateStr, String format) {
        Date date = null;

        if (dateStr == null || dateStr.equals(""))
            return null;

        try {
            DateFormat df = new SimpleDateFormat(format);
            date = (Date) df.parse(dateStr);
        } catch (Exception e) {
            log.error("parse error", e);
        }
        return date;
    }


    public static Date parseByDate(String dateStr) {
        return parse(dateStr, "yyyy-MM-dd");
    }


    public static Date parseByDateTime(String dateStr) {
        return parse(dateStr, "yyyy-MM-dd HH:mm:ss");
    }


    public static Date parseByUTCDateTimeSSS(String dateStr) throws ParseException {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");


        return df.parse(dateStr);
    }


    public static Date parseByUTCDateTime(String dateStr) throws ParseException {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");


        return df.parse(dateStr);
    }


    public static Date parseByDateTimeSSS(String dateStr) {
        return parse(dateStr, "yyyy-MM-dd HH:mm:ss.SSS");
    }


    public static Date sqlDate2Date(java.sql.Date date) {
        return date;
    }


    public static java.sql.Date date2SqlDate(Date date) {
        if (date != null)
            return new java.sql.Date(date.getTime());
        else
            return null;
    }


    public static java.sql.Date parseSqlDate(String dateStr, String format) {
        Date date = parse(dateStr, format);
        return date2SqlDate(date);
    }


    public static java.sql.Date parseSqlDateByDate(String dateStr) {
        return parseSqlDate(dateStr, "yyyy-MM-dd");
    }


    public static java.sql.Date parseSqlDateByDateTime(String dateStr) {
        return parseSqlDate(dateStr, "yyyy-MM-dd HH:mm:ss");
    }


    public static Timestamp parseTimestamp(String dateStr, String format) {
        Date date = parse(dateStr, format);
        if (date != null) {
            return new Timestamp(date.getTime());
        } else {
            return null;
        }
    }


    public static Timestamp parseTimestampByDate(String dateStr) {
        return parseTimestamp(dateStr, "yyyy-MM-dd");
    }


    public static Timestamp parseTimestampByDateTime(String dateStr) {
        return parseTimestamp(dateStr, "yyyy-MM-dd HH:mm:ss");
    }


    public static String formatGMT(Date date) {
        String returnValue;

        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.US);
        returnValue = formatter.format(date);

        return returnValue;
    }


    public static String format(Date date, String format) {
        String result = "";
        try {
            if (date != null) {
                DateFormat df = new SimpleDateFormat(format);
                result = df.format(date);
            }
        } catch (Exception e) {
            log.error("format error", e);
        }
        return result;
    }

    public static String format(String format) {
        return format(new Date(), format);
    }


    public static String formatByTime(Date date) {
        return format(date, "HH:mm:ss");
    }

    public static String formatByTime() {
        return formatByTime(new Date());
    }


    public static String formatByDate(Date date) {
        return format(date, "yyyy-MM-dd");
    }

    public static String formatByDate() {
        return formatByDate(new Date());
    }


    public static String formatByDateTime(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }


    public static String formatByDateTime() {
        return formatByDateTime(new Date());
    }


    public static String formatByDateTimeSSS(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss.SSS");
    }

    public static String formatByDateTimeSSS() {
        return formatByDateTimeSSS(new Date());
    }


    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }


    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }


    public static int getHour(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.HOUR_OF_DAY);
    }


    public static int getMinute(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MINUTE);
    }


    public static int getSecond(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.SECOND);
    }


    public static long getMillis(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getTimeInMillis();
    }


    public static ArrayList getMonthList(String beginDateStr, String endDateStr) {

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");

        String sRet = "";


        Date beginDate = null;
        Date endDate = null;

        GregorianCalendar beginGC = null;
        GregorianCalendar endGC = null;
        ArrayList list = new ArrayList();

        try {

            beginDate = f.parse(beginDateStr);
            endDate = f.parse(endDateStr);


            beginGC = new GregorianCalendar();
            beginGC.setTime(beginDate);

            endGC = new GregorianCalendar();
            endGC.setTime(endDate);


            while (beginGC.getTime().compareTo(endGC.getTime()) <= 0) {
                sRet = beginGC.get(Calendar.YEAR) + "-" + (beginGC.get(Calendar.MONTH) + 1);
                list.add(sRet);

                beginGC.add(Calendar.MONTH, 1);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static ArrayList<String> getDayList(String beginDateStr, String endDateStr) {

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");


        Date beginDate = null;
        Date endDate = null;

        Calendar beginGC = null;
        Calendar endGC = null;
        ArrayList<String> list = new ArrayList<String>();

        try {

            beginDate = f.parse(beginDateStr);
            endDate = f.parse(endDateStr);


            beginGC = Calendar.getInstance();
            beginGC.setTime(beginDate);

            endGC = Calendar.getInstance();
            endGC.setTime(endDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


            while (beginGC.getTime().compareTo(endGC.getTime()) <= 0) {

                list.add(sdf.format(beginGC.getTime()));

                beginGC.add(Calendar.DAY_OF_MONTH, 1);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static ArrayList<String> getDayListWithHMS(String beginDateStr, String endDateStr) {

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        Date beginDate = null;
        Date endDate = null;

        Calendar beginGC = null;
        Calendar endGC = null;
        ArrayList<String> list = new ArrayList<String>();

        try {

            beginDate = f.parse(beginDateStr);
            endDate = f.parse(endDateStr);


            beginGC = Calendar.getInstance();
            beginGC.setTime(beginDate);

            endGC = Calendar.getInstance();
            endGC.setTime(endDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


            while (beginGC.getTime().compareTo(endGC.getTime()) <= 0) {

                list.add(sdf.format(beginGC.getTime()));

                beginGC.add(Calendar.DAY_OF_MONTH, 1);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static int getWeekCountInYear(int year) {
        Calendar c = new GregorianCalendar();
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
        int count = getWeekOfYear(c.getTime());
        return count;
    }


    public static int getWeekOfYear(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);

        return c.get(Calendar.WEEK_OF_YEAR);
    }


    public static Date getFirstDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);

        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);

        return getFirstDayOfWeek(cal.getTime());
    }


    public static Date getLastDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);

        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);

        return getLastDayOfWeek(cal.getTime());
    }


    public static Date getFirestDayOfMonth(int year, int month) {
        month = month - 1;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);

        int day = c.getActualMinimum(c.DAY_OF_MONTH);

        c.set(Calendar.DAY_OF_MONTH, day);
        return c.getTime();

    }


    public static Date getLastDayOfMonth(int year, int month) {
        month = month - 1;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        int day = c.getActualMaximum(c.DAY_OF_MONTH);
        c.set(Calendar.DAY_OF_MONTH, day);
        return c.getTime();
    }


    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        return c.getTime();
    }


    public static Date getLastDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6);
        return c.getTime();
    }


    public static String simplifyDate(Date date) {
        String returnValue = "";

        if (date == null) {
            return returnValue;
        }

        Date nowDate = new Date();
        long dateNum = date.getTime();
        long nowNum = nowDate.getTime();

        long second = (nowNum - dateNum) / 1000;


        if (DateUtil.formatByDate(date).equals(DateUtil.formatByDate(nowDate))) {

            if (second < 60) {
                returnValue = second + "second before";
            } else if (second < 3600) {
                returnValue = second / 60 + " minute before";
            } else {
                long timeNum = second;


                long h = timeNum / 60 / 60;

                timeNum = timeNum - h * 60 * 60;


                long s = timeNum / 60;

                timeNum = timeNum - s * 60;

                returnValue = h + "hour " + s + " minute before";
            }
        } else if (DateUtil.getYear(date) == DateUtil.getYear(nowDate)) {
            returnValue = DateUtil.format(date, "M-d");
        } else {
            returnValue = DateUtil.format(date, "yyyy-MM-dd");
        }

        return returnValue;
    }


    public static String shortSimplifyDate(Date date) {
        String simplifyDate = simplifyDate(date);

        int in;
        in = simplifyDate.indexOf("hour");
        if (in != -1) {
            simplifyDate = simplifyDate.substring(0, in + 2) + " before";
        }

        return simplifyDate;
    }


    public static Date unixTime2Date(long unixTime) {
        return new Date(unixTime * 1000);
    }


    public static long toUnixTime(Date date) {
        return date.getTime() / 1000;
    }


    public static java.sql.Time parseSqlTime(String timeText) {
        Date d = null;
        try {
            d = new SimpleDateFormat("hh:mm:ss").parse(timeText);
        } catch (Exception e) {
            try {
                d = new SimpleDateFormat("hh:mm").parse(timeText);
            } catch (ParseException e1) {
                log.error("parseSqlTime error", e1);
            }
        }
        return new java.sql.Time(d.getTime());
    }


    public static Date getTodayZero(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        return zero;
    }


    public static Date getTodayTwelve(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date zero = calendar.getTime();
        return zero;
    }


    public static long getMinutes(Date date) {
        return date.getTime() / 1000 / 60;

    }


    public static Date getNextSecondTime(Date date, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + second);
        return calendar.getTime();
    }


    public static Date getNextDayTime(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + days);
        return calendar.getTime();
    }


    public static boolean isSameDay(Date date1, Date date2) {
        String dateStr1 = DateUtil.formatByDate(date1);
        String dateStr2 = DateUtil.formatByDate(date2);
        if (dateStr1.equals(dateStr2)) {
            return true;
        }
        return false;
    }


    public static List<Date> getDateListBetween(Date date1, Date date2) {
        List<Date> dates = new ArrayList<>();
        while (true) {

            if (isSameDay(date1, date2)) {
                dates.add(date1);
                break;
            }
            if (date1.after(date2)) {
                break;
            }

            if (date1.before(date2)) {
                dates.add(date1);
                date1 = DateUtil.getNextDayTime(date1, 1);
            }

        }
        return dates;

    }


    public static boolean belongCalendar(Date now, Date startTime, Date endTime) {

        Calendar date = Calendar.getInstance();
        date.setTime(now);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        }

        return false;
    }


}