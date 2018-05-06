package matricula.maps;

import java.io.File;

public class GeoCodingLoader {

	private static String placeholder = "<<>>";
	private static String urlTemplate = "https://maps.googleapis.com/maps/api/geocode/xml?address=<<>>&key=AIzaSyCE9TjBdssr5zADjmle-kn9Pwwvnph0-mc";

	public static void main(String[] args) {
		GeoCodingLoader main = new GeoCodingLoader("locs/temp");
		main.loadGeoLocations("Dorf", 1, 2, "3343+Hollenstein");
	}

	private final String outDirectory;

	public GeoCodingLoader(String outDirectory) {
		this.outDirectory = outDirectory;
	}

	private void loadGeoLocations(String streetName, int firstStreetNumber, int lastStreetNumber, String city) {

		for (int i = firstStreetNumber; i <= lastStreetNumber; i++) {
			String address = streetName + " " + i + "+" + city;
			String url = urlTemplate.replace(placeholder, address);
			File outFile = new File(outDirectory + "/" + streetName + " " + i);
			HttpsClient client = new HttpsClient();

			client.loadAndSave(url, outFile);

			System.out.println(url);
		}

	}

}
