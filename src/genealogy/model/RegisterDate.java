package genealogy.model;

import java.util.StringTokenizer;

public class RegisterDate {

	public static String[] monthName = new String[] {"<UNKNOWN>", "Jänner", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September",
			"Oktober", "November", "Dezember" };

	public static RegisterDate parse(String dateAsString) {
		int day = -1;
		int month = -1;
		int year = -1;

		if (dateAsString != null) {
			StringTokenizer t = new StringTokenizer(dateAsString, ".");
			day = nextInteger(t);
			month = nextInteger(t);
			year = nextInteger(t);
		}
		return new RegisterDate(day, month, year);
	}

	private static int nextInteger(StringTokenizer t) {
		int result = -1;
		if (t.hasMoreElements()) {
			try {
				result = Integer.parseInt(t.nextToken());
			} catch (NumberFormatException e) {
				result = -1;
			}
		}
		return result;
	}

	private final int day;
	private final int month;
	private final int year;

	public RegisterDate(int day, int month, int year) {
		this.day = day;
		this.month = month;
		this.year = year;
	}

	public int year() {
		return year;
	}

	@Override
	public String toString() {
		String result = String.format("%d.%d.%d", day, month, year);
		return result;
	}

	public int hashCode(){
		return year + month + day;
	}
	
	public boolean equals(RegisterDate other){
		return compareTo(other) == 0;
	}
	
	public int compareTo(RegisterDate b) {
		if (this == b || b == null) {
			return 0;
		}

		RegisterDate a = this;

		if (a.year != b.year) {
			return a.year - b.year;
		}
		if (a.month != b.month) {
			return a.month - b.month;
		}
		return a.day - b.day;
	}

	public String monthAsString() {
		if (month >= 1 && month <= 12) {
			return monthName[month];
		}
		return monthName[0];
	}

	public boolean isValid() {
		return year() != -1;
	}

}
