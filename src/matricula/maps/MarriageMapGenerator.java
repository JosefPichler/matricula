package matricula.maps;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;

import matricula.Community;
import matricula.model.MarriageTag;
import matricula.model.Matricula;
import matricula.report.StringFrequency;

public class MarriageMapGenerator {

	public static void main(String[] args) {
		MarriageMapGenerator generator = new MarriageMapGenerator();
		generator.generate("maps/trauung-data.js");
		generator.printSummary();
	}

	private final GeoCoding geo;
	private final StringFrequency missingLocations;
	private int marriageInMapCount;
	private int marriageCount;

	public MarriageMapGenerator() {
		this.geo = new GeoCoding();
		this.missingLocations = new StringFrequency();
	}

	private void printSummary() {
		System.out.println(missingLocations);

		System.out.format("    %5d Trauungen erfasst", marriageCount);
		System.out.println();
		System.out.format("    %5d Trauungen in der Karte", marriageInMapCount);
		System.out.println();
	}

	private void generate(String outFileName) {
		Matricula registerModel = Matricula.loadRegisters(Community.Hollenstein.registerFileNames());
		List<MarriageTag> marriageList = registerModel.getMarriageList();

		try (PrintStream out = new PrintStream(outFileName)) {
			generateHeader(out);
			generateLocationData(marriageList, out);
		} catch (FileNotFoundException e) {
			System.out.println("Fehler bei Schreiben der Datei " + outFileName);
		}
	}

	private void generateLocationData(List<MarriageTag> marriageList, PrintStream out) {
		out.println("var relocationData = [");
		for (MarriageTag e : marriageList) {
			marriageCount++;
			out.println("// " + e.getDateOfMarriage() + ", " + e.getHim().getFullName() + " oo " + e.getHer().getFullName());
			GeoLocation from = geocode(e.getHer().getPlaceOfBirth());
			GeoLocation to = geocode(e.getHim().getPlaceOfBirth());

			if (from != null && to != null) {
				marriageInMapCount++;
				out.print("   [");
				String fromStr = normalizeLocationName(e.getHer().getPlaceOfBirth());

				out.print(JavaScript.encode(fromStr));
				out.print(", ");

				String toStr = normalizeLocationName(e.getHim().getPlaceOfBirth());
				out.print(JavaScript.encode(toStr));
				out.println("],");
			} else {
				if (from == null) {
					out.format("  // TODO: %s", e.getHer().getPlaceOfBirth());
					out.println();
					missingLocations.add(e.getHer().getPlaceOfBirth());
				}
				if (to == null) {
					out.format("  // TODO: %s", e.getHim().getPlaceOfBirth());
					out.println();
					missingLocations.add(e.getHim().getPlaceOfBirth());
				}
			}

		}

		out.println("];");
	}

	private String normalizeLocationName(String placeOfBirth) {
		String result = placeOfBirth;

		result = result.replace("ß", "ss");
		result = result.replace("Than ", "Thann ");
		result = result.replace("Dorf","Hollenstein");
		int indexSlash = result.indexOf("/Opponitz");
		if (indexSlash > 0) {
			result = result.substring(0, indexSlash);
		}

		return result;
	}

	private GeoLocation geocode(String address) {
		if (address != null) {
			return geo.geocode(address);
		}
		return null;
	}

	private void generateHeader(PrintStream out) {

	}

}
