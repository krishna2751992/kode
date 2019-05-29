/**
 * 
 */
package gov.hhs.cms.desy.service.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jagannathan.Narashim
 *
 */
public class DateFunctions {
	private static final Logger log = LoggerFactory.getLogger(DateFunctions.class);
	
	final public static String DAY = "d";
	final public static String MONTH = "M";
	final public static String MONTHNAME = "MMM";
	final public static String YEAR = "yyyy";
	final public static String SHORT_MONTH_DAY_YEAR = "MM/dd/yy";
	final public static String LONG_MONTH_DAY_YEAR = "MM/dd/yyyy";
	final public static String MONTHNAME_DAY_YEAR = "MMMM dd, yyyy";
	final public static String SHORT_MONTHNAME_DAY_YEAR = "MMM dd, yyyy";
	final public static String MONTHNAME_DAY_YEAR_TIMEZONE = "MMM dd, yyyy zz";
	final public static String MONTHNAME_DAY_YEAR_HOUR_MINUTE_AMPM_TIMEZONE = "MMM dd, yyyy hh:mm a zz";
	final public static String YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_TIMESTAMP = "yyyy-MM-dd-HH.mm.ss";
	final public static String YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_TIMESTAMP_WOH = "yyyy-MM-dd HH:mm:ss";
	final public static String YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_TIMESTAMP_WOH_TIMEZONE = "yyyy-MM-dd HH:mm:ss zz";
	final public static String YEAR_MONTH_DAY_HOUR_TIMESTAMP = "yyyy-MM-dd";
	/**
	 * Simple Date utilities that return various formated date and time
	 */
	public DateFunctions()
	{
	}

	/**
	 * returns server's time zone in EST, PST etc.. format
	 */
	public static String getTimeZone()
	{
		SimpleDateFormat df = new SimpleDateFormat();
		TimeZone tz = df.getTimeZone();
		return tz.getDisplayName(false, TimeZone.SHORT);
	}

	/**
	 * returns short date 9/12/2000
	 */
	public static String getShortDate()
	{
		Date d = new Date();
		String date = DateFormat.getDateInstance(DateFormat.SHORT).format(d);
		return date;
	}

	/**
	 * returns medium date; Sep 22, 2000
	 */
	public static String getMediumDate()
	{
		Date d = new Date();
		String date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(d);
		return date;
	}

	/**
	 * returns long date; September 22, 2000
	 */
	public static String getLongDate()
	{
		Date d = new Date();
		String date = DateFormat.getDateInstance(DateFormat.LONG).format(d);
		return date;
	}

	/**
	 * returns local time AM/PM; 8:01 AM
	 */
	public static String getTimeAmPm()
	{
		Date d = new Date();
		String ti = DateFormat.getTimeInstance(DateFormat.SHORT).format(d);
		return ti;
	}

	/**
	 * returns local time AM/PM; 8:01 AM for given Date
	 */
	public static String formatTime(Date dte)
	{
		return DateFormat.getTimeInstance(DateFormat.SHORT).format(dte);
	}

	/**
	 * Retrieve the last day of the month for the specified year and month.
	 * 
	 * @param year The year.
	 * @param month The month constant defined in Calendar object for month of
	 *            year.
	 * @return The last day for the specified parameters.
	 */
	public static int getLastDayOfMonth(int year, int month)
	{
		GregorianCalendar gc = new GregorianCalendar(year, month, 1);
		return gc.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Get the year from the date. *
	 * 
	 * @param dt The date.
	 * @return The year.
	 */
	public static int getYear(Date dt)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(dt);
		return gc.get(Calendar.YEAR);
	}

	/**
	 * Get the month from the date.
	 * 
	 * @param dt The date.
	 * @return The month.
	 */
	public static int getMonth(Date dt)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(dt);
		return gc.get(Calendar.MONTH);
	}

	/**
	 * Get the day from the date.
	 * 
	 * @param dt The date.
	 * @return The day.
	 */
	public static int getDay(Date dt)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(dt);
		return gc.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Get the day of the month from the date.
	 * 
	 * @param dt The date.
	 * @return The day of the month.
	 */
	public static int getDayOfTheMonth(Date dt)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(dt);
		return gc.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Get the date plus the specified amount of specified field. Example: Date
	 * dt = DateFunctions.addMonth(dt, -1, Calendar.MONTH);
	 * 
	 * @param dt The date.
	 * @param amount The amount to increment (may be negative).
	 * @param field The Calendar field to increment.
	 * @return The date.
	 */
	public static Date add(Date dt, int amount, int field)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(dt);
		gc.add(field, amount);
		return gc.getTime();
	}

	/**
	 * Return a Date object based on specified parameters.
	 * 
	 * @param month The month, a constant defined in the Calendar object, e.g.
	 *            Calendar.JANUARY.
	 * @param day The day.
	 * @param year The year.
	 * @return The date object.
	 */
	public static Date makeDate(int month, int day, int year)
	{
		GregorianCalendar gc = new GregorianCalendar(year, month, day);
		return gc.getTime();
	}

	public static Date makeDate(String sDate)
	{
		StringTokenizer token = new StringTokenizer(sDate, "/");
		Date nDate = null;
		if (token.countTokens() == 3)
		{
			int month = Integer.parseInt(token.nextToken().trim()) - 1;
			int day = Integer.parseInt(token.nextToken().trim());
			int year = Integer.parseInt(token.nextToken().trim());
			nDate = makeDate(month, day, year);
		}
		return nDate;
	}

	public static Date makePartialDateFromTS(String ts)
	{
		Date date = null;
		if (ts != null)
		{
			try
			{
				date = new SimpleDateFormat(YEAR_MONTH_DAY_HOUR_TIMESTAMP)
					.parse(ts);
			}
			catch (ParseException e)
			{
				System.out.println("Parsing exception in converting timestamp "
					+ ts + " to date in makeDateFromTS");
			}
		}
		return date;
	}

	public static Date makeDateFromTS(String ts)
	{
		Date date = null;
		if (ts != null)
		{
			try
			{
				date = new SimpleDateFormat(
					YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_TIMESTAMP).parse(ts);
			}
			catch (ParseException e)
			{
				System.out.println("Parsing exception in converting timestamp "
					+ ts + " to date in makeDateFromTS");
			}
			if (date != null)
			{
				System.out.println("Converting timestamp " + ts + " to date "
					+ date.toString() + " in makeDateFromTS");
				System.out.println("Converting timestamp to date "
					+ formatTimeStampDate(date) + " in makeDateFromTS");
			}
		}
		return date;
	}

	/**
	 * Return a string in the format yyyy-MM-dd HH:mm:ss.
	 * 
	 * @param dt The date.
	 * @return A formatted date string.
	 */
	public static String formatTimeStampDate(Date dt)
	{
		if (dt != null)
		{
			SimpleDateFormat df = new SimpleDateFormat(
				YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_TIMESTAMP_WOH);
			return df.format(dt);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Return a string in the format yyyy-MM-dd HH:mm:ss.
	 * 
	 * @param dt The date.
	 * @return A formatted date string.
	 */
	public static String formatTimeStampDateTimeZone(Date dt)
	{
		if (dt != null)
		{
			SimpleDateFormat df = new SimpleDateFormat(
				YEAR_MONTH_DAY_HOUR_MINUTE_SECOND_TIMESTAMP_WOH_TIMEZONE);
			return df.format(dt);
		}
		else
		{
			return null;
		}
	}
		
	public static Date parseDashDateYMD(String sDate)
	{
		StringTokenizer token = new StringTokenizer(sDate, "-");
		Date nDate = null;
		if (token.countTokens() == 3)
		{
			int year = Integer.parseInt(token.nextToken().trim());
			int month = Integer.parseInt(token.nextToken().trim()) - 1;
			int day = Integer.parseInt(token.nextToken().trim());
			nDate = makeDate(month, day, year);
		}
		return nDate;
	}

	/**
	 * Return a string in the format MM/dd/yyyy.
	 * 
	 * @param dt The date.
	 * @return A formatted date string.
	 */
	public static String formatDate(Date dt)
	{
		if (dt != null)
		{
			SimpleDateFormat df = new SimpleDateFormat(LONG_MONTH_DAY_YEAR);
			return df.format(dt);
		}
		else
		{
			return null;
		}
	}

	public static String getDateFormater(Date d, String formater)
	{
		SimpleDateFormat df = new SimpleDateFormat(formater);
		return df.format(d).toString();
	}

	/**
	 * get the current quarter beginning as a mm/dd string based on the current
	 * month
	 */
	public static Date getQuarterBegin(Date dt)	{

		int dateMonth = getMonth(dt);
		int quarterMonth = 0;
		switch (dateMonth)
		{
			case 0:
			case 1:
			case 2:
				quarterMonth = Calendar.JANUARY;
				break;
			case 3:
			case 4:
			case 5:
				quarterMonth = Calendar.APRIL;
				break;
			case 6:
			case 7:
			case 8:
				quarterMonth = Calendar.JULY;
				break;
			case 9:
			case 10:
			case 11:
				quarterMonth = Calendar.OCTOBER;
				break;
		}

		return makeDate(quarterMonth, 1, getYear(dt));
	}

/*	public static void main(String[] args)
	{
		Date d = new Date();
		//DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
		//System.out.println(getDateFormater(d,"MMM, dd yyyy zz"));
		//System.out.println(getTimeAmpm());
		//System.out.println("last day="+ getLastDayOfMonth(2000,
		// Calendar.FEBRUARY));
		Date dt = makeDate(3, 5, 2001);
	}*/

	/**
	 * Return abrieviated month names array.
	 */
	public final static String[] getShortMonths = { "", "Jan", "Feb", "Mar",
		"Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

	/**
	 * Return normal month integer.
	 * 
	 * @param calendarMonth The static value from the Calendar object.
	 * @return The normal month int.
	 */
	public static int convertMonth(int calendarMonth)
	{
		int result = 0;
		switch (calendarMonth)
		{
			case Calendar.JANUARY:
				result = 1;
				break;
			case Calendar.FEBRUARY:
				result = 2;
				break;
			case Calendar.MARCH:
				result = 3;
				break;
			case Calendar.APRIL:
				result = 4;
				break;
			case Calendar.MAY:
				result = 5;
				break;
			case Calendar.JUNE:
				result = 6;
				break;
			case Calendar.JULY:
				result = 7;
				break;
			case Calendar.AUGUST:
				result = 8;
				break;
			case Calendar.SEPTEMBER:
				result = 9;
				break;
			case Calendar.OCTOBER:
				result = 10;
				break;
			case Calendar.NOVEMBER:
				result = 11;
				break;
			case Calendar.DECEMBER:
				result = 12;
				break;
		}
		return result;
	}

	public static boolean compareDates(Date compDate1, Date compDate2)
	{
		return DateFunctions.getDateFormater(compDate1, SHORT_MONTH_DAY_YEAR)
			.equals(
				DateFunctions.getDateFormater(compDate2, SHORT_MONTH_DAY_YEAR));
	}

	/**
	 * Convert date string ("MMYYYY") to Calendar.
	 */
	public static Date getDateMMYYYY(String monthYear, boolean isMonthEnd)
	{
		int year = Integer.parseInt(monthYear.substring(
			(monthYear.length() - 4), monthYear.length()));
		int month = Integer.parseInt(monthYear.substring(0, 2)) - 1;
		if (isMonthEnd)
			return new GregorianCalendar(year, month, getLastDayOfMonth(year,
				month)).getTime();
		else
			return new GregorianCalendar(year, month, 1).getTime();
	}
	public static Date parseDate (String db2TimeStamp)
	{
		java.util.Date dt = new java.util.Date();
		String db2Date = db2TimeStamp.substring(0,19); //"2006-06-09-11.03.06";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss");
		try
		{
			dt = formatter.parse(db2Date);
		}
		catch (Exception c)
		{
			
		}
		return dt;
		
	}
	
	public static String formatXmlDateStr(String xmlDateStr) {
		String formattedDate = "";

		try {
	        SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSSSSS");	        
	        SimpleDateFormat s2 = new SimpleDateFormat("MM/dd/yyyy");
	        Date d= s1.parse( xmlDateStr );
	        formattedDate = s2.format( d);
	        log.info("formattedDate :" + formattedDate);			
		}catch(Exception e){
			log.error("xmlDateStr Date conversion issue :", e);	
		}

		
		return formattedDate;
	}
}
