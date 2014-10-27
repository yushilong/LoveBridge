
package com.lovebridge.library.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
@SuppressWarnings("unused")
public class YARFriendlyTime
{
    // close service between 1 clock and 8 clock
    private static final int NIGHT_START_TIME_HOUR = 1;
    private static final int NIGHT_END_TIME_HOUR = 7;
    private static int MILL_MIN = 1000 * 60;
    private static int MILL_HOUR = MILL_MIN * 60;
    private static int MILL_DAY = MILL_HOUR * 24;
    private static String JUST_NOW = "刚刚";
    private static String MIN = "分钟前";
    private static String HOUR = "小时前";
    private static String DAY = "天前";
    private static String MONTH = "月前";
    private static String YEAR = "年前";
    private static String YESTER_DAY = "昨天";
    private static String THE_DAY_BEFORE_YESTER_DAY = "前天";
    private static String TODAY = "今天";
    private static String DATE_FORMAT = "M月d日 HH:mm";
    private static String YEAR_FORMAT = "yyyy年 M月d日 HH:mm";
    private static Calendar msgCalendar = null;
    private static java.text.SimpleDateFormat dayFormat = null;
    private static java.text.SimpleDateFormat dateFormat = null;
    private static java.text.SimpleDateFormat yearFormat = null;

    public static String getListTime(long time)
    {
        long now = System.currentTimeMillis();
        long msg = time;
        Calendar nowCalendar = Calendar.getInstance();
        if (msgCalendar == null)
            msgCalendar = Calendar.getInstance();
        msgCalendar.setTimeInMillis(time);
        long calcMills = now - msg;
        long calSeconds = calcMills / 1000;
        if (calSeconds < 60)
        {
            return JUST_NOW;
        }
        long calMins = calSeconds / 60;
        if (calMins < 60)
        {
            return new StringBuilder().append(calMins).append(MIN).toString();
        }
        long calHours = calMins / 60;
        if (calHours < 24 && isSameDay(nowCalendar, msgCalendar))
        {
            if (dayFormat == null)
                dayFormat = new java.text.SimpleDateFormat("HH:mm");
            String result = dayFormat.format(msgCalendar.getTime());
            return new StringBuilder().append(TODAY).append(" ").append(result).toString();
        }
        long calDay = calHours / 24;
        if (calDay < 31)
        {
            if (isYesterDay(nowCalendar, msgCalendar))
            {
                if (dayFormat == null)
                    dayFormat = new java.text.SimpleDateFormat("HH:mm");
                String result = dayFormat.format(msgCalendar.getTime());
                return new StringBuilder(YESTER_DAY).append(" ").append(result).toString();
            }
            else if (isTheDayBeforeYesterDay(nowCalendar, msgCalendar))
            {
                if (dayFormat == null)
                    dayFormat = new java.text.SimpleDateFormat("HH:mm");
                String result = dayFormat.format(msgCalendar.getTime());
                return new StringBuilder(THE_DAY_BEFORE_YESTER_DAY).append(" ").append(result).toString();
            }
            else
            {
                if (dateFormat == null)
                    dateFormat = new java.text.SimpleDateFormat(DATE_FORMAT);
                String result = dateFormat.format(msgCalendar.getTime());
                return new StringBuilder(result).toString();
            }
        }
        long calMonth = calDay / 31;
        if (calMonth < 12 && isSameYear(nowCalendar, msgCalendar))
        {
            if (dateFormat == null)
                dateFormat = new java.text.SimpleDateFormat(DATE_FORMAT);
            String result = dateFormat.format(msgCalendar.getTime());
            return new StringBuilder().append(result).toString();
        }
        if (yearFormat == null)
            yearFormat = new java.text.SimpleDateFormat(YEAR_FORMAT);
        String result = yearFormat.format(msgCalendar.getTime());
        return new StringBuilder().append(result).toString();
    }

    private static boolean isSameHalfDay(Calendar now , Calendar msg)
    {
        int nowHour = now.get(Calendar.HOUR_OF_DAY);
        int msgHOur = msg.get(Calendar.HOUR_OF_DAY);
        if (nowHour <= 12 & msgHOur <= 12)
        {
            return true;
        }
        else if (nowHour >= 12 & msgHOur >= 12)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static String getWeek(String sdate)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String mydate1 = "";
        Date date = null;
        try
        {
            date = format.parse(sdate);
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        finally
        {
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return new SimpleDateFormat("EEEE").format(c.getTime());
    }

    public static String getWeekNumStr(String strNum)
    {
        String str = strNum;
        if ("1".equals(str))
        {
            str = "周日";
        }
        else if ("2".equals(str))
        {
            str = "周一";
        }
        else if ("3".equals(str))
        {
            str = "周二";
        }
        else if ("4".equals(str))
        {
            str = "周三";
        }
        else if ("5".equals(str))
        {
            str = "周四";
        }
        else if ("6".equals(str))
        {
            str = "周五";
        }
        else if ("7".equals(str))
        {
            str = "周六";
        }
        return str;
    }

    public static String getWeekStr(String sdate)
    {
        String str = "";
        str = getWeek(sdate);
        if (str.contains("星期"))
        {
            str = str.replace("星期", "周");
            return str;
        }
        return getWeekNumStr(str);
    }

    public static String getToday()
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String mydate1 = format.format(new Date());
        return mydate1;
    }

    private boolean isNowNight()
    {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        return hour >= NIGHT_START_TIME_HOUR && hour <= NIGHT_END_TIME_HOUR;
    }

    private static boolean isSameDay(Calendar now , Calendar msg)
    {
        int nowDay = now.get(Calendar.DAY_OF_YEAR);
        int msgDay = msg.get(Calendar.DAY_OF_YEAR);
        return nowDay == msgDay;
    }

    private static boolean isYesterDay(Calendar now , Calendar msg)
    {
        int nowDay = now.get(Calendar.DAY_OF_YEAR);
        int msgDay = msg.get(Calendar.DAY_OF_YEAR);
        return (nowDay - msgDay) == 1;
    }

    private static boolean isTheDayBeforeYesterDay(Calendar now , Calendar msg)
    {
        int nowDay = now.get(Calendar.DAY_OF_YEAR);
        int msgDay = msg.get(Calendar.DAY_OF_YEAR);
        return (nowDay - msgDay) == 2;
    }

    private static boolean isSameYear(Calendar now , Calendar msg)
    {
        int nowYear = now.get(Calendar.YEAR);
        int msgYear = msg.get(Calendar.YEAR);
        return nowYear == msgYear;
    }
}
