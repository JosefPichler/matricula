package matricula.maps;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AddressGenerator {

	public static void main(String[] args) {
		AddressGenerator main = new AddressGenerator();

		GeoLocations loc = new GeoLocations();
		List<GeoLocation> locactionList = loc.getAllLocations();
		sort(locactionList);
		main.write(locactionList, "maps/address-data.js");
		System.out.println("Done.");
	}

	private static void sort(List<GeoLocation> locationList) {

		Collections.sort(locationList, new Comparator<GeoLocation>() {

			@Override
			public int compare(GeoLocation o1, GeoLocation o2) {
				return o1.getStreet().compareTo(o2.getStreet());
			}
		});

	}

	private void write(List<GeoLocation> locactionList, String outFileName) {

		try (PrintStream out = new PrintStream(outFileName)) {
			generate(out, locactionList);
		} catch (FileNotFoundException e) {
			System.out.println("Fehler beim Schreiben der Datei " + outFileName);
		}

	}

	private void generate(PrintStream out, List<GeoLocation> locactionList) {

		for (GeoLocation e : locactionList) {
			String street = e.getStreet();
			if (street.startsWith("Dorf")){
				street = street.replace("Dorf", "Hollenstein");
			}
			street = street.replace("ß", "ss");
			
			
			String key = JavaScript.encode(street);
			String lat = JavaScript.encode(e.getLat());
			String lng = JavaScript.encode(e.getLng());

			out.format("var %s = {lat: %s, lng: %s};", key, lat, lng);
			out.println();
		}

	}

}
