package aq.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * ！！！重要说明：jdk1.8提供的日期和时间处理类功能很丰富，基本无须增强常用功能，尤其是日期格式转换、日期加减等
 * 常用的日期时间工具类  基于jdk1.8
 * Created by ywb on 2017-02-17.
 */
public class DateTime {

    //格式 yyyy
    public static final String DATE_FORMAT_YYYY = "yyyy";

    //格式 yyyyMM
    public static final String DATE_FORMAT_YYYYMM = "yyyyMM";

    //格式 yyyy-MM
    public static final String DATE_FORMAT_YYYY_MM = "yyyy-MM";

    //格式 yyyyMMdd
    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";

    //格式 yyyy-MM-dd
    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

    //格式 yyyy.MM.dd
    public static final String DATE_FORMAT_POINT_YYYYMMDD = "yyyy.MM.dd";

    //格式 yyyy年MM月dd日
    public static final String DATE_FORMAT_ZH_YYYYMMDD = "yyyy年MM月dd日";

    //格式 yyyyMMddHHmm
    public static final String DATE_FORMAT_YYYYMMDDHHMM = "yyyyMMddHHmm";

    //格式 yyyy-MM-dd HH:mm
    public static final String DATE_FORMAT_YYYY_MM_DDHHMM = "yyyy-MM-dd HH:mm";

    //格式 yyyyMMddHHmmss
    public static final String DATE_FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    //格式 yyyy-MM-dd HH:mm:ss
    public static final String DATE_FORMAT_YYYY_MM_DDHHMMSS = "yyyy-MM-dd HH:mm:ss";

    //格式 yyyyMMddHHmmssSSS
    public static final String DATE_FORMAT_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";

    //yyyyMMddHHmmss
    public static final DateTimeFormatter FORMATTER_YYYYMMDDHHMMSS = DateTimeFormatter.ofPattern(DATE_FORMAT_YYYYMMDDHHMMSS);

    //yyyy-MM-dd HH:mm:ss
    public static final DateTimeFormatter FORMATTER_YYYY_MM_DD_HHMMSS = DateTimeFormatter.ofPattern(DATE_FORMAT_YYYY_MM_DDHHMMSS);

    //获取当前日期  yyyy-MM-dd
    public static LocalDate getDefaultLocalDate(){
        return LocalDate.now();
    }

    //获取时间 HH:mm:ss.SSS
    public static LocalTime getDefaultLocalTime(){
        return LocalTime.now();
    }

    //获取当前日期+时间   yyyy-MM-dd HH:mm:ss
    public static LocalDateTime getDefaultLocalDateTime(){
        return LocalDateTime.now();
    }

    //获取当前日期+时间   yyyy-MM-dd HH:mm:ss
    public static String getLocalDateTime(){
        return getDefaultLocalDateTime().format(FORMATTER_YYYY_MM_DD_HHMMSS);
    }

    //获取当前日期+时间   自定义格式
    public static String getLocalDateTime(DateTimeFormatter formatter){
        return getDefaultLocalDateTime().format(formatter);
    }

    //Date 转 LocalDate
    public static LocalDate dateToLocalDate(Date date){
        return dateToLocalDateTime(date).toLocalDate();
    }

    //Date 转 LocalDateTime
    public static LocalDateTime  dateToLocalDateTime(Date date){
        long nanoOfSecond = (date.getTime() % 1000) * 1000000;
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(date.getTime() / 1000, (int) nanoOfSecond, ZoneOffset.of("+8"));

        return localDateTime;
    }

    // Timestamp 转 LocalDateTime
    public static LocalDateTime timestampToLocalDateTime(Timestamp timestamp){
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(timestamp.getTime() / 1000, timestamp.getNanos(), ZoneOffset.of("+8"));
        return localDateTime;
    }
    //Timestamp 转 LocalDateTime （yyyy-MM-dd 00:00:00:000）
    public static LocalDateTime zerolizedTime(Timestamp date) {
        LocalDateTime localDateTime = timestampToLocalDateTime(date);
        return LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), 0, 0, 0, 0);
    }

    //Timestamp 转 LocalDateTime （yyyy-MM-dd 23:59:59:999）
    public static LocalDateTime getEndTime(Timestamp date){
        LocalDateTime localDateTime = timestampToLocalDateTime(date);
        return LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), 23, 59, 59, 999 * 1000000);
    }

    //LocalDateTime 转 Timestamp
    public static Timestamp localDateTimeToTimestamp(LocalDateTime localDateTime){
        return Timestamp.valueOf(localDateTime);
    }

    /**
     * 增加或减少年/月/周/天/小时/分/秒数
     *
     * @param chronoUnit 例：ChronoUnit.DAYS
     * @param num
     * @return LocalDateTime
     */
    public static LocalDateTime addTime(Date date, ChronoUnit chronoUnit, int num){
        long nanoOfSecond = (date.getTime() % 1000) * 1000000;
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(date.getTime() / 1000, (int) nanoOfSecond, ZoneOffset.of("+8"));
        return localDateTime.plus(num, chronoUnit);
    }

    /**
     * 比较2个时间
     * @param date1
     * @param date2
     * @return
     */
    public static Boolean compareDate(Date date1, Date date2){
        long time1 = date1.getTime();
        long time2 = date2.getTime();
        if(time1>time2){
            return true;
        }else {
            return false;
        }
    }
    /**
     * 比较2个时间
     * @return
     */
    public static Date compareDate(String date){
        String dates = date.replace("Z", " UTC");//注意是空格+UTC
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式
        try {
            return format.parse(dates);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateFormat(Date date, String format) {
        if (!StringUtil.isEmpty(date)) {
            SimpleDateFormat df = new SimpleDateFormat(format);
            return df.format(date);
        } else {
            return null;
        }
    }

    /**
     * 返回当前时间
     * @return
     */
    public static String getNowTime(String format){
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        String time=sdf.format(new Date());
        return time;
    }

    //4、获取上个月的第一天
    public static String getBeforeFirstMonthdate()throws Exception{
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar= Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return format.format(calendar.getTime());
    }

    //5、获取上个月的最后一天
    public static String getBeforeLastMonthdate()throws Exception{
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar=Calendar.getInstance();
        int month=calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return format.format(calendar.getTime());
    }

    public static String getCurrentYearMonth()throws Exception{
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM");
        Calendar calendar=Calendar.getInstance();
        int month=calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, month-1);
        return format.format(calendar.getTime());
    }

}
