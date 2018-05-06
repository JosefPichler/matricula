package matricula.model;

import java.util.StringTokenizer;

public class DateTag implements Comparable<DateTag> {

	private static final DateTag NullObject = new DateTag("", 0, 0, 0);

	private final String dateAsString;
	private final int day;
	private final int month;
	private final int year;

	public static DateTag parse(String dateAsString) {
		if (dateAsString == null) {
			return DateTag.NullObject;
		}
		StringTokenizer scanner = new StringTokenizer(dateAsString, ".");
		int day = nextToken(scanner);
		int month = nextToken(scanner);
		int year = nextToken(scanner);
		return new DateTag(dateAsString, year, month, day);
	}

	private static int nextToken(StringTokenizer scanner) {
		int result = 0;
		if (scanner.hasMoreTokens()) {
			String str = scanner.nextToken();
			try {
				result = Integer.parseInt(str);
			} catch (NumberFormatException e) {
				result = 0;
			}
		}
		return result;
	}

	// Expected format 13.2.1897
	public DateTag(String dateAsString, int year, int month, int day) {
		this.dateAsString = dateAsString;
		this.year = year;
		this.month = month;
		this.day = day;
	}

	@Override
	public int compareTo(DateTag other) {
		int result = year - other.year;
		if (result == 0) {
			result = month - other.month;
		}
		if (result == 0) {
			result = day - other.day;
		}
		return result;
	}

	@Override
	public String toString() {
		return dateAsString;
	}

	public String getYear() {
		if (year == 0) {
			return "    ";
		}

		return new Integer(year).toString();
	}

}
