package com.uakgul.mvvm.todotask.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

    static SimpleDateFormat DATE       = new SimpleDateFormat("yyyyMMdd");

    static SimpleDateFormat DATEDOT    = new SimpleDateFormat("yyyy.MM.dd");

    static SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd");

    static SimpleDateFormat TIME       = new SimpleDateFormat("HHmmss");

    static SimpleDateFormat TIMEFORMAT = new SimpleDateFormat("HH:mm:ss");

    static SimpleDateFormat DATETIMEm  = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    static SimpleDateFormat DATETIME   = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static SimpleDateFormat DATETIMES3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    static SimpleDateFormat DATETIMES5 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSS");

    // __________________________________________________________________________________________ \\



    /**
     *
     * @return systemDate : yyyyMMdd
     */
    public static String getSystemDate() {
        return DATE.format(new Date() );
    }

    /**
     *
     * @return systemDate : yyyy-MM-dd
     */
    public static String getSystemDateWithStriped() {
        return DATEFORMAT.format(new Date() );
    }

    /**
     *
     * @return systemDate : yyyy.MM.dd
     */
    public static String getSystemDateWithDotted() {
        return DATEDOT.format(new Date() );
    }


    /**
     *
     * @return systemTime : HHmmss
     */
    public static String getSystemTime() {
        return TIME.format(new Date() );
    }


    /**
     *
     * @return systemTime : HH:mm:ss
     */
    public static String getSystemTimeWithDotted() {
        return TIMEFORMAT.format(new Date() );
    }

    /**
     *
     * @return systemTime : yyyy-MM-dd HH:mm
     */
    public static String getTimeStampMinute() {
        return DATETIMEm.format(new Date() );
    }

    /**
     *
     * @return systemTime : yyyy-MM-dd HH:mm:ss
     */
    public static String getTimeStamp() {
        return DATETIME.format(new Date() );
    }

    /**
     *
     * @return systemTime : yyyy-MM-dd HH:mm:ss.SSS
     */
    public static String getTimeStampS3() {
        return DATETIMES3.format(new Date() );
    }

    /**
     *
     * @return systemTime : yyyy-MM-dd HH:mm:ss.SSSSS
     */
    public static String getTimeStampS5() {
        return DATETIMES5.format(new Date() );
    }



    /**
     *
     * @param diff
     * @return timeStamp + diff Hour : yyyy-MM-dd HH:mm:ss
     */
    public static String getTimeStampAddHour(int diff) {
        Calendar cal         = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY , diff ); // adds diff hour
        return DATETIME.format(cal.getTime());
    }


    /**
     *
     * @param diff
     * @return timeStamp + diff Day : yyyy-MM-dd HH:mm:ss
     */
    public static String getTimeStampAddDay(int diff) {
        Calendar cal         = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR , diff ); // adds diff day
        return DATETIME.format(cal.getTime());
    }



    /**
     *
     * @return systemTime : 1539771364644
     */
    public static Long getTimeMiliSecond() {
        return new Date().getTime();
    }


    /**
     *
     * @param date1
     * @param date2
     * @return which one greater than another
     */
    public static String compareTwoDate(String date1, String date2){

        String whichDate="";

        try {
            Date fisrtDate = DATETIME.parse( date1 );
            Date secondDate = DATETIME.parse( date2 );

            if ( fisrtDate.before( secondDate )   ) {
                whichDate = "2";
            }else{
                whichDate = "1";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if( whichDate.equals("1") ) {
            whichDate = date1;
        }else{
            whichDate = date2;
        }

        return whichDate;
    }

    /**
     *
     * @param date1
     * @return true if date bigger than today
     *         false if date expired than today
     */
    public static boolean isExpireDate(String date1){
        boolean isBigger = false;
        try {
            Date fisrtDate = DATEFORMAT.parse( date1 );
            Date secondDate = DATEFORMAT.parse( getTimeStamp() );
            if ( fisrtDate.before( secondDate )   ) {
                isBigger = true;
            }else{
                isBigger = false;
            }
            //for same date but differnet time
            String today =  getSystemDateWithStriped();
            if( date1.equalsIgnoreCase( today ) ){
                isBigger = false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isBigger;
    }






    /**
     *
     * @param timestamp
     * @return timeStamp with string : yyyy-MM-dd HH:mm:ss --> yyyyMMdd
     */
    public static String getClassicDateWithTimeStamp( String timestamp ) {

//        Date date = new Date();
        String reformattedStr = "";
        try {
            reformattedStr = DATE.format( DATETIME.parse( timestamp ) );
//            System.out.println( "DATE.format( DATETIME.parse( time ) ) => DATETIME --> DATE ( "+time+" ) : " + reformattedStr );
//            date = DATETIME.parse( time );
//            System.out.println( "DATETIME --> formeat2 ( "+time+" ) : " + DATE.format( date ) );
        } catch (ParseException ex) {
            System.out.println("getClassicFormatedDate ParseException : " + ex.getMessage());
        }
//        return DATE.format( date );

        return reformattedStr;
    }



    /**
     *
     * @param longTime like 1539771364644
     * @return string timeStamp with Long milisecondUnixTime : yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTimeFromLongMilisecond(Long longTime){
        return DATETIME.format( new Date( longTime ) );
    }// end of getDateTimeFromLongMilisecond



    /**
     *
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @return Long milisecondUnixTime with string timeStamp : 1539771364644
     */
    public static Long getMilisecondFromDate( String dateTime ) {

        Long milisecond = 0L;

        try {
            milisecond = DATETIME.parse( dateTime ).getTime();
//            System.out.println(  "getMilisecondFromDate ( "+dateTime+" ) --> : " + milisecond );
        } catch (ParseException ex) {
            System.out.println("getMilisecondFromDate ParseException : " + ex.getMessage());
        }
        return milisecond;

    }



    /**
     *
     * @return String GregorianCalendar.DAY_OF_YEAR  : 066 / 151
     */
    public static String getJulianDate() {

        GregorianCalendar gc = new GregorianCalendar();

        gc.setTime( Calendar.getInstance().getTime() );

        int julianDate = gc.get( GregorianCalendar.DAY_OF_YEAR );

        String result = "001";

        if( julianDate <100 ){
            result = "0" + julianDate;
        }else{
            result = ""  + julianDate;
        }

        System.out.println( "getJulianDate : " + result );

        return result;
    }


    /**
     *
     * @param dayOfYear
     * @return String GregorianCalendar.DAY_OF_YEAR  : 066 / 151
     * @throws Exception
     */
    public static String getDateFromJulianDate(String dayOfYear) throws Exception{

        Calendar calendar = Calendar.getInstance();

        calendar.set( Calendar.DAY_OF_YEAR , Integer.parseInt( dayOfYear ) );

//        Log.i( "DUtils", "..... getDateFromJulianDate : getTime : " + calendar.getTime() + "..... !!!" );

        String result =  DATE.format( calendar.getTime());

//        Log.i( "DUtils", "..... getDateFromJulianDate : result : " + result );

        return result;
    }


    /**
     *
     * @return String GregorianCalendar.DAY_OF_YEAR  : 066 / 151
     */
    public static String getDateFromJulianDate(){

        Calendar calendar = Calendar.getInstance();

        calendar.set( Calendar.DAY_OF_YEAR , Integer.parseInt( getJulianDate() ) );

        String result =  DATE.format( calendar.getTime());

//        System.out.println("getDateFromJulianDate result : " + result);

        return result;
    }





    /**
     *
     * @param beforeDays
     * @return  yyyy-MM-dd - beforeDays
     */
    public static String getFewDaysAgoDate(int beforeDays){
        Long diffMilisecond = (long) (beforeDays * 24 * 60 * 60 * 1000); // for one week : 7 * 24 * 60 * 60 * 1000
        Long beforeDiff = getTimeMiliSecond() - diffMilisecond;
        return DATEFORMAT.format( new Date( beforeDiff ) );
    }

    /**
     *
     * @param afterDays
     * @return  yyyy-MM-dd + afterDays
     */
    public static String getFewDaysAfterDate(int afterDays){
        Long diffMilisecond = (long) (afterDays * 24 * 60 * 60 * 1000); // for one week : 7 * 24 * 60 * 60 * 1000
        Long afterDiff = getTimeMiliSecond() - diffMilisecond;
        return DATEFORMAT.format( new Date( afterDiff ) );
    }

    /**
     *
     * @param beforeDays
     * @return  1542783095429 - 604800000L (7days)
     */
    public static Long getFewDaysAgoMilisecond(int beforeDays){
        Long before = (long) (beforeDays * 24 * 60 * 60 * 1000); // for one week : 7 * 24 * 60 * 60 * 1000
        Long difference = getTimeMiliSecond() - before;
        return ( difference );
    }


    /**
     *
     * @param afterDays
     * @return  1542783095429 + 604800000L (7days)
     */
    public static Long getFewDaysAfterMilisecond(int afterDays){
        Long after = (long) (afterDays * 24 * 60 * 60 * 1000); // for one week : 7 * 24 * 60 * 60 * 1000
        Long difference = getTimeMiliSecond() + after;
        return ( difference );
    }




}
