package matricula.maps;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import matricula.model.Matricula;
import matricula.model.PersonTag;
import matricula.model.Register;
import matricula.model.RegisterPage;
import matricula.model.Tag;

public class JavaScriptDataGenerator {

	public static void main(String[] args) {
		JavaScriptDataGenerator generator = new JavaScriptDataGenerator();
		generator.generate(new String[] { "files-01-10/01-10.txt", "files-01-11/01-11.txt", "files-01-12/01-12.txt", "files-01-13/01-13.txt", "files-01-14/01-14.txt" }, "maps/data.js");
	}

	private void generate(String[] registerFileNames, String outFileName) {
		Matricula registerModel = Matricula.loadRegisters(registerFileNames);

		List<PersonTag> personList = filterPersonTagsWithGeoLocation(registerModel);
		List<String> uniquePlaceOfBirthList = createUniquePlaceOfBirthList(personList);

		try (PrintStream out = new PrintStream(outFileName)) {
			generateHeader(out);
			generateLocationData(uniquePlaceOfBirthList, out);
			generatePersonData(personList, uniquePlaceOfBirthList, out);
		} catch (FileNotFoundException e) {
			System.out.println("Fehler bei Schreiben der Datei " + outFileName);
		}

		printSummary(uniquePlaceOfBirthList.size(), personList.size());
	}

	private void generateLocationData(List<String> uniquePlaceOfBirthList, PrintStream out) {
		GeoCoding geo = new GeoCoding();
		// Write rotten as variables
		for (String e : uniquePlaceOfBirthList) {
			String key = GeoCoding.placeOfBirthKey(e);
			GeoLocation loc = geo.geocode(e);
			String var = String.format("var %s = {lat: %s, lng: %s};", JavaScript.encode(key), JavaScript.encode(loc.getLat()),
					JavaScript.encode(loc.getLng()));
			out.println(var);
		}

		// Write rotten as array
		out.println("var routs = [");
		for (String e : uniquePlaceOfBirthList) {
			String key = GeoCoding.placeOfBirthKey(e);
			String var = String.format("%s,", JavaScript.encode(key));
			out.println(var);
		}
		out.println("];");

		out.println("var rottenPersonCount = [");
		for (int i = 0; i < uniquePlaceOfBirthList.size(); i++) {
			out.println("    0, ");
		}
		out.println("];");

	}

	private void generatePersonData(List<PersonTag> personList, List<String> uniquePlaceOfBirthList, PrintStream out) {
		out.println("var persons = [");
		for (PersonTag e : personList) {
			String name = e.getFullName();
			String dob = e.getDateOfBirth();
			String pob = e.getPlaceOfBirth();
			String keyPob = GeoCoding.placeOfBirthKey(pob);

			int rpcIdx = uniquePlaceOfBirthList.indexOf(keyPob);

			// {name:"Josef Pichler", rout: SattelX1, rpcIdx: 2},
			out.println(String.format("{name:'%s', dateOfBirth: '%s', rpcIdx: %d}, ", name, dob, rpcIdx));
		}

		out.println("];");

		out.println(String.format("// number of persons: %d", personList.size()));
	}

	private void printSummary(int nrOfAddress, int nrOfPerson) {
		System.out.println("Summary ");
		System.out.println(String.format("    %5d Adressen", nrOfAddress));
		System.out.println(String.format("    %5d Personen", nrOfPerson));
	}

	private List<String> createUniquePlaceOfBirthList(List<PersonTag> personList) {
		Set<String> resultSet = new HashSet<String>();
		for (PersonTag e : personList) {
			String key = GeoCoding.placeOfBirthKey(e.getPlaceOfBirth());
			resultSet.add(key);
		}
		ArrayList<String> result = new ArrayList<String>();
		result.addAll(resultSet);
		Collections.sort(result);
		return result;
	}

	private List<PersonTag> filterPersonTagsWithGeoLocation(Matricula registerModel) {
		GeoCoding geo = new GeoCoding();
		ArrayList<PersonTag> result = new ArrayList<PersonTag>();
		for (Register r : registerModel.getRegisterList()) {
			for (RegisterPage e : r.getPageList()) {
				for (Tag e2 : e.getTagList()) {
					if (e2 instanceof PersonTag) {
						PersonTag p = (PersonTag) e2;
						if (p.getPlaceOfBirth() != null) {
							GeoLocation loc = geo.geocode(p.getPlaceOfBirth());
							if (loc != null) {
								result.add(p);
							}
						}
					}
				}
			}
		}
		return result;
	}

	private void generateHeader(PrintStream out) {

	}

}
