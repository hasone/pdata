package com.cmcc.vrp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.joda.time.format.ISODateTimeFormat;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.vrp.province.model.Activities;

/**
 * DateUtil.java
 */
public class DateUtil {

    /**
     * 将date转化为指定字符串格式,格式不对返回null
     */
    public static String dateToString(Date date, String type) {
        if (date == null || type == null) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(type);
            return sdf.format(date);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }


    /**
     * 将时间字符串按照指定在格式转换为时间对象在实例
     * <p>
     *
     * @param format
     * @param date
     * @return
     * @throws
     * @Title:parse
     * @Description: TODO
     * @author: qihang
     */
    public static Date parse(String format, String date) {
        if (format == null || date == null) {
            return null;
        }


        if (StringUtils.isBlank(format) || StringUtils.isBlank(date)) {
            return null;
        }
        try {//尝试进行转化，不成功抛异常
            Date dateTemp = null;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            dateTemp = simpleDateFormat.parse(date);
            return dateTemp;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 将yyyy-MM-dd转化为Date类
     */
    public static Date converStrYMDToDate(String format) {
        if (format == null) {
            return new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(format);
        } catch (ParseException e) {
            return null;
        }
    }


    /**
     * 得到本月月初的时间
     */
    public static Date getBeginMonthOfDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        String dateStr = dateFormat.format(date);
        dateStr += "-01";
        return parse("yyyy-MM-dd", dateStr);
    }
    
    /**
     * 得到下月月末的最后一天23:59:59
     */
    public static Date getEndNextMonthOfDate(Date date) {
        Calendar calender = Calendar.getInstance();       
        calender.setTime(date);
        calender.add(Calendar.MONTH, 1);
        calender.set(Calendar.DAY_OF_MONTH, calender.getActualMaximum(Calendar.DAY_OF_MONTH));  
        calender.set(Calendar.HOUR_OF_DAY, 23);
        calender.set(Calendar.MINUTE, 59);
        calender.set(Calendar.SECOND, 59);
        return calender.getTime();
    }
    
    /** 
     * 获取从当前时间到当天最后一秒的时间差,以秒计数
     * @Title: secondsToEnd 
     */
    public static int secondsToEnd(DateTime now) {
        DateTime end = now.plusDays(1).withTimeAtStartOfDay();

        return Seconds.secondsBetween(now, end).getSeconds();
    }

    /**
     * 得到几天前的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 得到几天后的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 得到几分钟后的时间
     *
     * @param d
     * @param minutes
     * @return
     */
    public static Date getDateAfterMinutes(Date d, int minutes) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + Math.abs(minutes));
        return now.getTime();
    }

    /**
     * 得到几秒钟后的时间
     *
     * @param d
     * @param seconds
     * @return
     */
    public static Date getDateAfterSeconds(Date d, int seconds) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.SECOND, now.get(Calendar.SECOND) + Math.abs(seconds));
        return now.getTime();
    }
    
    /**
     * 得到几秒钟前的时间
     *
     * @param d
     * @param seconds
     * @return
     */
    public static Date getDateBeforeSeconds(Date d, int seconds) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.SECOND, now.get(Calendar.SECOND) - Math.abs(seconds));
        return now.getTime();
    }

    /**
     * 获取当月月初
     *
     * @throws ParseException
     */
    public static Date getAfterFewMonths(Date date, int fewMonths) {
        date = getBeginMonthOfDate(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, fewMonths);
        return calendar.getTime();
    }

    /**
     * 获取前后几年
     *
     * @throws ParseException
     */
    public static Date getAfterYears(Date date, int fewYears) {
        date = getBeginMonthOfDate(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, fewYears);
        return calendar.getTime();
    }

    /**
     * 获取前后几月
     *
     * @throws ParseException
     */
    public static Date getAfterMonths(Date date, int fewMonths) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, fewMonths);
        return calendar.getTime();
    }

    
    /**
     * 获取当天的0点0分0秒
     * @Title: getBeginOfDay 
     * @param date
     * @return
     * @Author: wujiamin
     * @date 2016年10月19日
     */
    public static Date getBeginOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);        
        return calendar.getTime();
    }

    public static Date getEndOfNextYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.YEAR, 1);
        int year = calendar.get(Calendar.YEAR);

        calendar.clear();
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        calendar.set(Calendar.YEAR, year);

        Date currYearLast = calendar.getTime();
        return getEndTimeOfDate(currYearLast);
    }

    /**
     * @Title: Date
     * @Description: 获取指定年份指定月份最后一天最后时间点
     * @return: void
     */
    public static Date getEndTimeOfMonth(int year, int month) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month - 1);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String date = sdf1.format(calendar.getTime());
            return sdf2.parse(date + " 23:59:59");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    public static String getShBossTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HHmmss");
        return df.format(new Date());
    }

    /**
     * 当前时间增加offset分钟
     *
     * @param start
     * @param offset
     * @return
     */
    public static Date addMins(Date start, int offset) {
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.add(Calendar.MINUTE, offset);
        return c.getTime();
    }

    /**
     * 解析ISO8601时间
     * 流量卡时间格式
     *
     * @param time
     * @return
     */
    public static Date parseISO8601Time(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getMdrcTime(String time) {
        return ISODateTimeFormat.dateTimeParser().parseDateTime(time).toDate();
    }

    /**
     * 获取当前ISO8601格式时间
     *
     * @return
     */
    public static String getRespTime() {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 前ISO8601格式时间
     *
     * @return
     */
    public static String date2ISO8601Time(Date date) {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
        return DateFormatUtils.format(date, pattern);
    }


    /**
     * 获取河南BOSS时间格式
     *
     * @return
     */
    public static String getHenanBossTime() {
        SimpleDateFormat haFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return haFormat.format(new Date());
    }

    /**
     * 获取江西BOSS时间格式
     *
     * @return
     */
    public static String getJiangXiBossTime() {
        SimpleDateFormat haFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return haFormat.format(new Date());
    }

    /**
     * 获取北京渠道的充值时间
     *
     * @return
     */
    public static String getBjBossTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    /**
     * 获取福建渠道的失效时间
     * 下个月失效
     *
     * @return
     */
    public static String getFjBossEndTime() {
        SimpleDateFormat df = new SimpleDateFormat("YYYYMMdd");
        return df.format(getAfterFewMonths(new Date(), 1));
    }

    /**
     * 获取广西BOSS请求时间
     *
     * @return
     */
    public static String getGxBossTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(new Date());
    }

    /**
     * 获取广西BOSS鉴权请求时间
     *
     * @return
     */
    public static String getGxBossATime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return df.format(new Date());
    }

    /**
     * 获取广西BOSS鉴权请求时间
     *
     * @return
     */
    public static String getHBBossTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(new Date());
    }
    
    /**
     * 获取DATE 23:59:59的时间，返回新的对象 
     *
     * @return
     */
    public static Date getEndTimeOfDate(Date date){
        if(date==null){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        
        return calendar.getTime();
    }
    
    /**
     * 
     * @Title: getCurrentDateOfFewMonths 
     * @Description: 获取前后几个的当前时间
     * @param date
     * @param fewMonths
     * @return
     * @throws ParseException
     * @return: Date
     */
    public static Date getCurrentDateOfFewMonths(Date date, int fewMonths){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, fewMonths);
        return calendar.getTime();
    }

    /**
     * 获取上月末N天 到本月末N天的日期
     * 1、如果当日是月末2天，开始日期取当月的倒数第二天
     * 2、如果当日非月末2天，开始日期取上月的倒数第二天
     * @param recordDateMinus  yyyy-MM-dd
     * @param days  N天
     * @return 获取上月末N天 到本月末N天的日期
     */
    public static String getMonthOfBeforeDay(String recordDateMinus, int days) {
        Date time1 = converStrYMDToDate(recordDateMinus);
        Calendar time = Calendar.getInstance();
        time.clear();
        time.setTime(time1);
        int day1 = time.get(Calendar.DAY_OF_MONTH);
        int day2 = time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数 

        if (day1 > day2 - days) {
            // 当日已经是月末日期，计算当月日期
            time.set(Calendar.DAY_OF_MONTH, day2 - (days - 1));
        } else {
            // 计算上月日期
            time.set(Calendar.MONTH, time.get(Calendar.MONTH) - 1);
            int day3 = time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数 
            time.set(Calendar.DAY_OF_MONTH, day3 - (days - 1));
        }

        return dateToString(time.getTime(), "yyyy-MM-dd");
    }
   
    /**
     * 计算两个时间时间差
     * @param date1 前
     * @param date2 后
     * @Description date2 - date1
     * @author qinqinyan
     * */
    public static String caculateTimeRange(Date date1, Date date2){
        long nDay = 1000*24*60*60;//一天的毫秒数
        long nHour = 1000*60*60;//一小时的毫秒数
        long nMinite = 1000*60;//一分钟的毫秒数
        long nSecond = 1000;//一秒钟的毫秒数

        long diff = date2.getTime() - date1.getTime();
        long day = diff/nDay; //计算差多少天
        long hour = diff%nDay/nHour; //计算差多少小时
        long min = diff%nDay%nHour/nMinite; //计算差多少分钟
        long sec = diff%nDay%nHour%nMinite/nSecond; //计算差多少秒
        return day+"天"+hour+"时"+min+"分"+sec+"秒";
    }

    /**
     * 计算date前hours个小时对应的日期
     * @param date
     * @param hours 小时个数
     */
    public static Date getDateByHours(Date date, int hours) {
        Calendar time = Calendar.getInstance();
        time.clear();
        time.setTime(date);

        int dayCount = hours/24;  //计算天数
        int hourCount = hours%24; //计算小时
        //System.out.println("dayCount:"+dayCount);
        //System.out.println("hourCount:"+hourCount);

        int year = time.get(Calendar.YEAR); //获取年份
        //System.out.println("year:"+year);
        int month = time.get(Calendar.MONTH)+1; //获取月份，因为因为从0开始计算
        //System.out.println("month:"+month);
        int day = time.get(Calendar.DAY_OF_MONTH); //本月日期
        //System.out.println("day:"+day);
        int hour = time.get(Calendar.HOUR_OF_DAY); //获取当前小时
        //System.out.println("hour:"+hour);

        //循环实现
        //计算时分秒
        while(hourCount>0){
            if(hourCount>=hour){
                if(day==1){
                    //当前日期为1号，涉及跨到前一月
                    if(month==1){
                        //当前月份为1月，涉及跨到前一年,月份强制转化成12月份，小时强制转化成23小时
                        year -= 1;
                        month = 12;
                        day = 31;
                    }else{
                        //当前月份为其他月，月份回退到上一个月，小时强制转化成23小时
                        month -= 1;
                        day = getDay(month, year);
                    }
                }else{
                    day -= 1;
                }
                hourCount -= hour;
                hour = 23;
            }else{
                hour -= hourCount;
                hourCount -= hourCount; //结束循环
            }
        }

        //计算年月日
        while (dayCount>0){
            if(dayCount>=day){
                if(month==1){
                    //回退到前一年,month强制到12月份,day强制到31号
                    year -= 1;
                    month = 12;
                    dayCount -= day;
                    day = 31;
                } else{
                    //只需要回退到前一个月
                    month -= 1;
                    dayCount -= day;
                    day = getDay(month, year);
                }
            }else{
                day -=  dayCount;
                dayCount -= dayCount; //结束循环
            }
        }

        //System.out.println("year:"+year);
        //System.out.println("month:"+month);
        //System.out.println("day:"+day);

        //System.out.println("hour:"+hour);
        //System.out.println("min:"+min);
        //System.out.println("second:"+second);

        time.set(Calendar.YEAR, year);
        time.set(Calendar.MONTH, month-1);
        time.set(Calendar.DAY_OF_MONTH, day);

        time.set(Calendar.HOUR_OF_DAY, hour);

        //System.out.println(dateToString(time.getTime(), "yyyy-MM-dd HH:mm:ss"));
        //return dateToString(time.getTime(), "yyyy-MM-dd HH:mm:ss");
        //System.out.println(time.getTime());
        return time.getTime();
    }

    private static int getDay(int month, int year){
        int day;
        if(month==1 || month==3 || month==5 || month==7 || month==8 || month==10 || month==12){
            day = 31;
        } else if(month==4 || month==6 || month==9 || month==11){
            day = 30;
        } else {
            if(isLeapYear(year)){
                //润年
                day = 29;
            }else{
                day = 28;
            }
        }
        return day;
    }

    public static boolean isLeapYear(int year){
        //判断是否闰年
        Calendar cal=Calendar.getInstance();
        cal.set(year,Calendar.DECEMBER,31);
        int days = cal.get(Calendar.DAY_OF_YEAR);
        //System.out.println("days:"+days);
        return 366!=days?false:true;
    }
    /**
     * 计算V网网龄
     * @throws ParseException 
     * 
     * */
    public static Integer getDayRange(String effDate) throws ParseException{
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
        Date compareDate = sdf.parse(effDate);
                       
        Calendar cal = Calendar.getInstance();
        cal.setTime(compareDate);
        long time1 = cal.getTimeInMillis();
           
        cal.setTime(today);
        long time2 = cal.getTimeInMillis();
        long betweenDays = (time2 - time1) / (1000 * 3600 * 24);
               
        return Integer.parseInt(String.valueOf(betweenDays));
    }
    /**
     * 计算V网网龄
     * @throws ParseException 
     * 
     * */
    public static int[] getDateRange(String effDate) throws ParseException{
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
        Date compareDate = sdf.parse(effDate);
                       
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(compareDate);
        //long time1 = cal.getTimeInMillis();
        Calendar cal2 = Calendar.getInstance();               
        cal2.setTime(today);
//        long time2 = cal.getTimeInMillis();
//        long betweenDays = (time2 - time1) / (1000 * 3600 * 24);
//               
//        return Integer.parseInt(String.valueOf(betweenDays));
        return getNeturalAge(cal1, cal2);
    }
    public static int[] getNeturalAge(Calendar calendarBirth,Calendar calendarNow) { 
        int diffYears = 0;
        int diffMonths;
        int diffDays;
        int dayOfBirth = calendarBirth.get(Calendar.DAY_OF_MONTH); 
        int dayOfNow = calendarNow.get(Calendar.DAY_OF_MONTH); 
        if (dayOfBirth <= dayOfNow) { 
            diffMonths = getMonthsOfAge(calendarBirth, calendarNow); 
            diffDays = dayOfNow - dayOfBirth; 
            if (diffMonths == 0) {
                diffDays++;
            } 
        } else { 
            if (isEndOfMonth(calendarBirth)) { 
                if (isEndOfMonth(calendarNow)) { 
                    diffMonths = getMonthsOfAge(calendarBirth, calendarNow); 
                    diffDays = 0; 
                } else { 
                    calendarNow.add(Calendar.MONTH, -1); 
                    diffMonths = getMonthsOfAge(calendarBirth, calendarNow); 
                    diffDays = dayOfNow + 1; 
                } 
            } else { 
                if (isEndOfMonth(calendarNow)) { 
                    diffMonths = getMonthsOfAge(calendarBirth, calendarNow); 
                    diffDays = 0; 
                } else { 
                    calendarNow.add(Calendar.MONTH, -1);// 上个月 
                    diffMonths = getMonthsOfAge(calendarBirth, calendarNow); 
                    // 获取上个月最大的一天 
                    int maxDayOfLastMonth = calendarNow.getActualMaximum(Calendar.DAY_OF_MONTH); 
                    if (maxDayOfLastMonth > dayOfBirth) { 
                        diffDays = maxDayOfLastMonth - dayOfBirth + dayOfNow; 
                    } else { 
                        diffDays = dayOfNow; 
                    } 
                } 
            } 
        } 
        // 计算月份时，没有考虑年 
        diffYears = diffMonths / 12; 
        diffMonths = diffMonths % 12; 
        return new int[] { diffYears, diffMonths, diffDays }; 
    } 
        /**  
        * 获取两个日历的月份之差  
        *   
        * @param calendarBirth  
        * @param calendarNow  
        * @return  
        */ 
    public static int getMonthsOfAge(Calendar calendarBirth,  
                Calendar calendarNow) {  
        return (calendarNow.get(Calendar.YEAR) - calendarBirth  
                    .get(Calendar.YEAR))* 12+ calendarNow.get(Calendar.MONTH)  
                    - calendarBirth.get(Calendar.MONTH);  
    } 
    
    /**
     * 今天的剩余时间.
     * @return
     */
    public static int getLeftTimeToday() {
        DateTime beginDt = new DateTime();  //当前时间
        DateTime endDt =
            new DateTime(beginDt.getYear(), beginDt.getMonthOfYear(), beginDt.getDayOfMonth(), 23, 59, 59);

        return Math.abs(Seconds.secondsBetween(beginDt, endDt).getSeconds());
    }
    
    /**
     * 比较两个时间是否是同一个月
     * */
    public static boolean verifySameMonth(Date date1, Date date2){
        int month1 = date1.getMonth();
        int month2 = date2.getMonth();
        return month1 == month2?true:false;
    }
    
    
    /**
     * 计算两个时间之间的天数,包括首尾两天，即闭区间
     * @Description date2 - date1
     * */
    public static long getDays(Date date1, Date date2) {
        int cnt = 0;
        
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        
        int day1 = calendar1.get(Calendar.DATE);       //日
        int month1 = calendar1.get(Calendar.MONTH) + 1;//月
        int year1 = calendar1.get(Calendar.YEAR);      //年

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        
        int day2 = calendar2.get(Calendar.DATE);       //日
        int month2 = calendar2.get(Calendar.MONTH) + 1;//月
        int year2 = calendar2.get(Calendar.YEAR);      //年
        
        if(year1 == year2){
            if(month1==month2){
                cnt = day2 - day1 + 1;
            }else{
                //获取month1这个月的天数
                int days = getDay(month1, year1);
                cnt = days - day1 + 1 + day2;
            }
        }else{
            //计算第一年时间
            cnt = getDay(month1, year1) - day1 + 1;  
            for(int i=1+month1; i<13; i++){
                cnt += getDay(month1, year1);
            }
            //计算两个时间的年份天数
            int countYear = year1+1;
            while(countYear==year2){
                if(isLeapYear(countYear)){
                    cnt += 366;
                }else{
                    cnt += 365;
                }
                countYear++;
            }
            //计算最后一年时间天数
            cnt += day2;
            for(int i=month2-1; i>0; i--){
                cnt += getDay(month2, year2);
            }
        }
        return cnt;
    }

    
    /**  
    * 判断这一天是否是月底  
    *   
    * @param calendar  
    * @return  
    */ 
    public static boolean isEndOfMonth(Calendar calendar) {  
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);  
        if (dayOfMonth == calendar.getActualMaximum(Calendar.DAY_OF_MONTH)){
            return true; 
        } 
        return false;  
    } 
    /**
     * @param date1
     * @param date2
     * @return
     */
    public static String caculateSecond(Date date1, Date date2){
        long nDay = 1000*24*60*60;//一天的毫秒数
        long nHour = 1000*60*60;//一小时的毫秒数
        long nMinite = 1000*60;//一分钟的毫秒数
        long nSecond = 1000;//一秒钟的毫秒数

        long diff = 1800000 - (date2.getTime() - date1.getTime());
        long min = diff%nDay%nHour/nMinite; //计算差多少分钟
        long sec = diff%nDay%nHour%nMinite/nSecond; //计算差多少秒
        return min+"分"+sec+"秒";
    }
    
    private static String createActivities(){
        Activities activities = new Activities();
        activities.setName("ee\"><h1>ddd");
        activities.setActivityId("test");
        
        System.out.println(JSONObject.toJSONString(activities));
        return JSONObject.toJSONString(activities);
    }
    
    
    public static void main(String[] args) {
        try {
            String value = createActivities();
            value = value.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
                    .replaceAll(">", "&gt;")
                    .replaceAll("'", "&apos;");
            
            JSONObject object = JSONObject.parseObject(value);
            object.size();
            System.out.println("size === " + object.size());
            Set<String> it = object.keySet();
            Iterator<String> its = it.iterator();
            
            while(its.hasNext()){
                String key = (String)its.next();
                String item = (String)object.get(key);
                System.out.println("item === "+ item);
                item = item.replaceAll("\"", "&quot;");
                System.out.println("item === "+ item);
                
                object.put(key, item);
            }
            System.out.println("object==="+JSONObject.toJSONString(object));
            
            /*Date today = parse("yyyy-MM-dd HH:mm:ss", "2017-10-08 00:23:11");
            
            //获得本月月初
            String begin = dateToString(getBeginMonthOfDate(getBeginOfDay(today)), "yyyy-MM-dd HH:mm:ss");
            System.out.println(begin);
            
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            int month = calendar.get(Calendar.MONTH) + 1;//月
            int year = calendar.get(Calendar.YEAR);      //年
            System.out.println("month:"+month + "  ;year:"+year);
            
            String date = dateToString(getEndTimeOfMonth(year, month), "yyyy-MM-dd HH:mm:ss");
            System.out.println(date);*/
            
            //Date endDate = parse("yyyy-MM-dd HH:mm:ss", "2017-07-01 23:59:59");
            //boolean flag = verifySameMonth(new Date(), endDate);
            //long day = getDays(endDate, new Date());
            //String str = dateToString(new Date(), "HH:mm");
            //String str = "qrscene_787587";
            //String[] strArray = str.split("_");
            //System.out.println(strArray.length);
            
            /*String result = "true";
            Date timeDate = new Date();
            
            String time1 = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
            String limitEndTime = "17:30:00";
            
            String startTimeStr = time1 + " "+ limitEndTime;
            
            if(timeDate.getTime() <= DateUtil.parse("yyyy-MM-dd HH:mm:ss", startTimeStr).getTime()){
                result = "false";
            }
            
            System.out.println("hh");*/
            
/*            String date = dateToString(getBeginOfDay(new Date()), "yyyy-MM-dd HH:mm:ss");
            System.out.println(date);*/

  
            //String id1 = UUID.randomUUID().toString();
            //String id2 = UUID.randomUUID().toString();
            //System.out.println("id1="+id1);
            //System.out.println("id2="+id2);
            
            /*Date date = parse("yyyyMMdd","20170430");
            System.out.println(getEndNextMonthOfDate(date));*/
            //Date startTime = DateUtil.getBeginOfDay(new Date());
            //Date endTime = DateUtil.getEndTimeOfDate(new Date());
            //map.put("startTime", DateUtil.dateToString(startTime, "YYYY-MM-DD HH:mm:ss"));
            //map.put("endTime", DateUtil.dateToString(endTime, "YYYY-MM-DD HH:mm:ss"));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
