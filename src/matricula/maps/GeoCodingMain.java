package matricula.maps;

import java.util.Collections;
import java.util.List;

public class GeoCodingMain {

	public static void main(String[] args) {
		List<GeoLocation> all = GeoLocations.Local.getAllLocations();
		Collections.sort(all);
		for (GeoLocation e : all) {
			System.out.println(e);
		}
		System.out.println("Size: " + all.size());

	}
}
