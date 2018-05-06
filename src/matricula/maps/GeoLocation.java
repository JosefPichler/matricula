package matricula.maps;

public class GeoLocation implements Comparable<GeoLocation> {

	public static final String TypeRoofTop = "ROOFTOP";
	public static final String TypeRangeInterpolated = "RANGE_INTERPOLATED";

	private final String streetName;
	private final String streetNumber;
	private final double lat;
	private final double lng;
	private final String locationType;

	GeoLocation(String streetName, String streetNumber, double lat, double lng, String locationType) {
		this.streetName = streetName;
		this.streetNumber = streetNumber;
		this.lat = lat;
		this.lng = lng;
		this.locationType = locationType;
	}

	// E.g 'Dornleiten 28' or "Gaflenz".
	public String getStreet() {
		if (streetNumber.length() == 0) {
			return streetName;
		}
		return streetName + " " + streetNumber;
	}

	public String getLocationType() {
		return locationType;
	}

	@Override
	public String toString() {
		return streetName + " " + streetNumber + " (lat: " + lat + ", lng: " + lng + ") " + locationType;
	}

	@Override
	public int compareTo(GeoLocation o) {
		if (streetName.equals(o.streetName)) {
			try {
				int a = Integer.parseInt(streetNumber);
				int b = Integer.parseInt(o.streetNumber);
				return a - b;
			} catch (NumberFormatException e) {
				return streetNumber.compareTo(o.streetNumber);
			}
		}
		return streetName.compareTo(o.streetName);
	}

	public double getLng() {
		return lng;
	}

	public double getLat() {
		return lat;
	}
}
