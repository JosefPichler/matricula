package matricula.maps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import matricula.Community;
import matricula.model.Matricula;
import matricula.model.PersonTag;
import matricula.model.Register;
import matricula.model.RegisterPage;
import matricula.model.Tag;

public class MapGenerator {

	private static final String placeholder = "PLACEHOLDER";

	public static void main(String[] args) {
		if (0 < args.length) {
			generate(args);
		} else {
			generate(Community.Hollenstein.baptismalRegisterFileNames());
		}
	}

	public static void generate(String[] args) {
		Matricula matricula = Matricula.loadRegisters(args);
		MapGenerator main = new MapGenerator(matricula);

		String markerCode = main.generateJavaScriptCode();
		try {
			main.generateMap("maps/step6template.html", "maps/step6.html", markerCode);
			main.generatePersonDataArray("maps/data7.txt");
		} catch (FileNotFoundException e) {
			System.out.println("Problem writing HTML file");
		} catch (IOException e) {
			System.out.println("Problem writing HTML file");
		}
		main.printSummary();
	}

	private final Matricula matricula;
	private final List<String> missingLoc;
	private final List<String> dataArray;
	private int streetNameInMapCount;
	private int personCount;
	private int personInMapCount;

	public MapGenerator(Matricula matricula) {
		this.matricula = matricula;
		this.missingLoc = new ArrayList<String>();
		this.dataArray = new ArrayList<String>();
		this.streetNameInMapCount = 0;
		this.personCount = 0;
		this.personInMapCount = 0;
	}

	private void printSummary() {
		final Map<String, Integer> missingLocFrequency = frequencyFromList(missingLoc);
		List<String> f = new ArrayList<String>(missingLocFrequency.keySet());

		Collections.sort(f, new Comparator<String>() {

			@Override
			public int compare(String a, String b) {
				int af = missingLocFrequency.get(a);
				int bf = missingLocFrequency.get(b);
				return bf - af;
			}

		});

		for (String e : f) {
			String line = String.format("%2dx  %s", missingLocFrequency.get(e), e);
			System.out.println(line);
		}

		Set<String> missingLocSet = new HashSet<String>(missingLoc);

		System.out.println();
		System.out.println("Summary");
		System.out.println(String.format("%6d persons in registers", personCount));
		System.out.println(String.format("%6d persons in map", personInMapCount));
		System.out.println();
		System.out.println(String.format("%6d street names", streetNameInMapCount + missingLocSet.size()));
		System.out.println(String.format("%6d street names in map", streetNameInMapCount));
	}

	private static Map<String, Integer> frequencyFromList(List<String> list) {
		Map<String, Integer> result = new TreeMap<String, Integer>();

		for (String e : list) {
			if (result.containsKey(e)) {
				int freq = result.get(e) + 1;
				result.put(e, freq);
			} else {
				result.put(e, new Integer(1));
			}
		}

		return result;
	}

	private void generatePersonDataArray(String outFileName) {
		try (PrintStream out = new PrintStream(new File(outFileName))) {

			for (String e : dataArray) {
				out.println(e);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void generateMap(String htmlTemplateFile, String outFileName, String markerCode) throws IOException {
		PrintStream out = new PrintStream(new File(outFileName));
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(htmlTemplateFile)));

		String line = in.readLine();
		while (line != null) {
			if (line.startsWith(placeholder)) {
				out.println(markerCode);
			} else {
				out.println(line);
			}

			line = in.readLine();
		}
		in.close();
		out.close();
	}

	private String generateJavaScriptCode() {
		Map<String, String> title = new HashMap<String, String>();
		Map<String, Integer> label = new HashMap<String, Integer>();

		// 1. Collect person tags with address.
		List<PersonTag> personList = new ArrayList<PersonTag>();
		for (Register r : matricula.getRegisterList()) {
			for (RegisterPage e : r.getPageList()) {
				for (Tag e2 : e.getTagList()) {
					if (e2 instanceof PersonTag) {
						personCount++;
						PersonTag p = (PersonTag) e2;
						if (p.getPlaceOfBirth() != null) {
							personList.add(p);
						}
					}
				}
			}
		}

		// 2. Generate JavaScript code
		StringBuffer result = new StringBuffer();
		GeoCoding geo = new GeoCoding();
		
		for (PersonTag p : personList) {
			GeoLocation loc = geo.geocode(p.getPlaceOfBirth());
			if (loc != null) {
				personInMapCount++;
				String key = GeoCoding.placeOfBirthKey(p.getPlaceOfBirth());
				String var = String.format("var %s = {lat: %s, lng: %s};", javaScript(key), javaScript(loc.getLat()),
						javaScript(loc.getLng()));
				result.append(var);
				result.append("\n");

				if (title.containsKey(key)) {
					title.put(key, title.get(key) + "\\n* " + p.getDateOfBirth() + " " + p.getFullName());
					label.put(key, label.get(key) + 1);
				} else {
					title.put(key, p.getPlaceOfBirth() + "\\n* " + p.getDateOfBirth() + " " + p.getFullName());
					label.put(key, 1);
				}
			} else {
				missingLoc.add(p.getPlaceOfBirth());
			}
		}

		for (String e : title.keySet()) {
			streetNameInMapCount++;
			String t = title.get(e);
			Integer l = label.get(e);

			String marker = String.format("var marker = new google.maps.Marker({map: map, label: '%d', position: %s, title: '%s' });", l, javaScript(e), t);
			result.append(marker);
			result.append("\n");

			// // {name:'Oberkirchen 11', loc:OberkirchenX11},

			String dataStr = String.format("{name:'%s', loc:%s},", l, javaScript(e));
			dataArray.add(dataStr);
		}

		return result.toString();
	}

	private String javaScript(double value) {
		String str = Double.toString(value);
		String result = str.replace(',', '.');
		return result;
	}

	private String javaScript(String str) {
		String result = str.replace(" ", "X");
		result = result.replace("/", "X");
		result = result.replace(".", "X");
		return result;

	}
}
