package matricula.maps;

public class GeoCoding {

	public static final String placeholder = "<<>>";
	public static final String url = "https://maps.googleapis.com/maps/api/geocode/xml?address=Dornleiten 1+3343+Hollenstein&key=AIzaSyCE9TjBdssr5zADjmle-kn9Pwwvnph0-mc";

	private final GeoLocations locs;

	public GeoCoding() {
		this.locs = new GeoLocations();
	}

	public GeoLocation geocode(String address) {
		// Addresses of Opponitz are search by street name only.
		int indexSlash = address.indexOf("/Opponitz");
		if (indexSlash > 0) {
			address = address.substring(0, indexSlash);
		}

		// Suffixes a and b.
		if (address.endsWith("a") || address.endsWith("b")) {
			address = address.substring(0, address.length() - 1);
		}

		// Oberoisberg -> Oisberg
		
		if (address.startsWith("Oberoisberg ")) {
			address = address.replace("Oberoisberg ", "Oisberg ");
		}

		// Hollenstein -> Dorf
		if (address.startsWith("Hollenstein ")) {
			address = address.replace("Hollenstein ", "Dorf ");
		}

		// Thalbauern -> Thalbauer
		// Talbauer -> Thalbauer
		if (address.startsWith("Thalbauern")) {
			address = address.replace("Thalbauern", "Thalbauer");
		}
		if (address.startsWith("Talbauer")) {
			address = address.replace("Talbauer", "Thalbauer");
		}

		// Than -> Thann
		if (address.startsWith("Than ")) {
			address = address.replace("Than", "Thann");
		}
		// Addresses of St. Georgen am Reith
		indexSlash = address.indexOf("/St. Georgen am Reith");
		if (indexSlash > 0) {
			address = address.substring(0, indexSlash);
		}
		
		if (address.startsWith("Reith ")){
			address = address.replace("Reith", "Sankt Georgen am Reith");
		}
		
		// Aschenmoos
		if (address.startsWith("Reingrub 10")){
			address = "Königsberg 8";
		}

		GeoLocation result = locs.find(address);
		// We need some normalization because Google GeoCoding sometimes uses
		// Grießau and sometime Griessau.
		if (result == null) {
			address = address.replace("ß", "ss");
			result = locs.find(address);
		}
		return result;
	}

	public static String placeOfBirthKey(String address) {
		// Suffixes a and b.
		if (address.endsWith("a") || address.endsWith("b")) {
			address = address.substring(0, address.length() - 1);
		}
		return address;
	}
}
