package com.fusionalliance.internal.interview.sharedutility.core;

import static java.util.Calendar.DATE;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * This utility class contains methods for transforming between a UTC Calendar and an MDY date String. The Calendar always represents the date as
 * midnight UTC, which facilitates storing the value in a database.
 */
public final class CalendarStringUtility {

	public static void main(final String... args) {
		final Calendar calendar = nowAsUtcCalendar();
		System.out.println(calendarToMdy(calendar));
	}

	/**
	 * Convert to mm/dd/yyyy String to a UTC calendar.
	 * 
	 * @param mdyDateParm
	 *            single digit month/day is OK
	 * @return
	 */
	public static Calendar mdyToUtcCalendar(final String mdyDateParm) {
		ValidationUtility.checkStringNotBlank("The M/D/Y date passed is blank or null.", mdyDateParm);
		ValidationUtility.checkGoodConditionMet("The M/D/Y date passed is not a valid format: " + mdyDateParm,
				mdyDateParm.matches("\\d{1,2}/\\d{1,2}/\\d{4}"));

		final String[] dateParts = mdyDateParm.split("/");

		final GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

		calendar.set(YEAR, Integer.parseInt(dateParts[2]));
		calendar.set(MONTH, Integer.parseInt(dateParts[0]) - 1);
		calendar.set(DATE, Integer.parseInt(dateParts[1]));
		calendar.set(HOUR_OF_DAY, 0);
		calendar.set(MINUTE, 0);
		calendar.set(SECOND, 0);
		calendar.set(MILLISECOND, 0);

		return calendar;
	}

	/**
	 * Convert a Calendar to a MM/DD/YYYY String. Return empty if null.
	 * 
	 * @param calendarParm
	 * @return
	 */
	public static String calendarToMdy(final Calendar calendarParm) {
		if (calendarParm == null) {
			return "";
		}

		return String.format("%1$tm/%1$td/%1$tY", calendarParm);
	}

	/**
	 * Return the current local date as a UTC Calendar.
	 * 
	 * @return
	 */
	public static Calendar nowAsUtcCalendar() {
		final LocalDate localDate = LocalDate.now();

		final GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

		calendar.set(YEAR, localDate.getYear());
		calendar.set(MONTH, localDate.getMonthValue() - 1);
		calendar.set(DATE, localDate.getDayOfMonth());
		calendar.set(HOUR_OF_DAY, 0);
		calendar.set(MINUTE, 0);
		calendar.set(SECOND, 0);
		calendar.set(MILLISECOND, 0);

		return calendar;
	}

	/**
	 * Hidden constructor
	 */
	private CalendarStringUtility() {
		// Do nothing
	}
}
