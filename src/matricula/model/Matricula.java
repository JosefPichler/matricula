package matricula.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class Matricula {

	private final List<Register> registerList;

	private Register defaultRegister;

	public Matricula() {
		this.registerList = new ArrayList<Register>();
	}

	public Register getLastRegister() {
		return defaultRegister;
	}

	public void setLastRegister(Register r) {
		defaultRegister = r;
	}

	private void addRegister(Register r) {
		r.setMatricula(this);
		setLastRegister(r);
		registerList.add(r);
	}

	public List<Register> getRegisterList() {
		return registerList;
	}

	public void save() {
		defaultRegister.saveAll();
	}

	public static Matricula loadRegisters(String[] registerFileName) {
		return loadRegisters(registerFileName, null);
	}
	
	public static Matricula loadRegisters(String[] registerFileName, String externalImagePathRoot) {
		final Matricula result = new Matricula();
		for (String e : registerFileName) {
			Matricula m = loadRegister(e, externalImagePathRoot);
			result.addRegister(m.getLastRegister());

		}
		return result;
	}

	private static Matricula loadRegister(String registerFileName, String externalImagePathRoot) {
		Register r = null;
		RegisterPage page = null;
		try (BufferedReader in = new BufferedReader(new FileReader(registerFileName))) {
			String line = in.readLine();
			while (line != null) {
				if (line.startsWith("register;")) {
					r = loadRegister(line, in, externalImagePathRoot);
					r.setRegisterFileName(registerFileName);
				} else if (line.startsWith("page;")) {
					connectTags(page);
					page = loadRegisterPage(line);
					if (r != null && page != null) {
						r.addPage(page);
					} else {
						error("missing register or page");
					}
				} else if (line.startsWith("tag;")) {
					Tag tag = loadTag(line);
					if (page != null && tag != null) {
						page.addTag(tag);
					} else {
						error("illegal tag " + line);
					}
				}
				line = in.readLine();
			}
			connectTags(page);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Matricula result = new Matricula();
		result.addRegister(r);
		return result;
	}

	private static void connectTags(RegisterPage page) {
		if (page != null) {
			page.connectTags();
		}

	}

	private static Tag loadTag(String line) {
		StringTokenizer scanner = new StringTokenizer(line, ";");
		// 1. Ignore tag;
		if (!scanner.hasMoreTokens()) {
			error("missing tag;");
			return null;
		}
		scanner.nextToken();

		// 2. Tag kind
		if (!scanner.hasMoreTokens()) {
			error("missing tag kind");
			return null;
		}
		final String kind = scanner.nextToken();

		// 3. Title
		if (!scanner.hasMoreTokens()) {
			error("missing title");
			return null;
		}
		final String title = scanner.nextToken();
		int x = loadInteger(scanner);
		int y = loadInteger(scanner);
		int w = loadInteger(scanner);
		int h = loadInteger(scanner);

		Tag result = null;
		if (x != -1 && y != -1 && w != -1 && h != -1) {
			if ("male".equals(kind)) {
				result = new PersonTag(title, true, new TagRegion(x, y, w, h));
				loadPersonTagKeyValue((PersonTag) result, scanner);
			} else if ("female".equals(kind)) {
				result = new PersonTag(title, false, new TagRegion(x, y, w, h));
				loadPersonTagKeyValue((PersonTag) result, scanner);
			} else if ("address".equals(kind)) {
				result = new AddressTag(title, new TagRegion(x, y, w, h));
			} else if ("marriage".equals(kind)) {
				PersonTag p1 = new PersonTag("Placeholder", true, new TagRegion(x, y, 0, 0));
				PersonTag p2 = new PersonTag("Placeholder", false, new TagRegion(w, h, 0, 0));
				result = new MarriageTag(title, p1, p2);
				loadMarriageTagKeyValue((MarriageTag) result, scanner);
			} else if ("parent".equals(kind)) {
				PersonTag child = new PersonTag("Placeholder Child", true, new TagRegion(x, y, 0, 0));
				PersonTag parent = new PersonTag("Paceholder Parent", true, new TagRegion(w, h, 0, 0));
				result = new ParentTag(child, parent);
			}
		}
		return result;
	}

	private static void loadPersonTagKeyValue(PersonTag result, StringTokenizer scanner) {
		while (scanner.hasMoreTokens()) {
			String key = null;
			String value = null;
			String keyValue = scanner.nextToken();
			StringTokenizer keyValueScanner = new StringTokenizer(keyValue, "=");
			if (keyValueScanner.hasMoreTokens()) {
				key = keyValueScanner.nextToken();
			}
			if (keyValueScanner.hasMoreTokens()) {
				value = keyValueScanner.nextToken();
			}
			if (key != null && value != null) {
				if ("pob".equals(key)) {
					result.setPlaceOfBirth(value);
				} else if ("dob".equals(key)) {
					result.setDateOfBirth(value);
				} else if ("job".equals(key)) {
					result.setJob(value);
				} else if ("age".equals(key)) {
					result.setAge(value);
				}
			}
		}

	}

	private static void loadMarriageTagKeyValue(MarriageTag result, StringTokenizer scanner) {
		while (scanner.hasMoreTokens()) {
			String key = null;
			String value = null;
			String keyValue = scanner.nextToken();
			StringTokenizer keyValueScanner = new StringTokenizer(keyValue, "=");
			if (keyValueScanner.hasMoreTokens()) {
				key = keyValueScanner.nextToken();
			}
			if (keyValueScanner.hasMoreTokens()) {
				value = keyValueScanner.nextToken();
			}
			if (key != null && value != null) {
				if ("place".equals(key)) {
					result.setPlace(value);
				}
			}
		}

	}

	private static void error(String msg) {
		System.out.println("Parsing error " + msg);

	}

	private static int loadInteger(StringTokenizer scanner) {
		final int error = -1;

		if (!scanner.hasMoreTokens()) {
			return error;
		}
		String str = scanner.nextToken();
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			error("expected number but found " + str);
			return error;
		}

	}

	private static RegisterPage loadRegisterPage(String line) {
		StringTokenizer scanner = new StringTokenizer(line, ";");
		// 1. Ignore page;
		if (!scanner.hasMoreTokens()) {
			return null;
		}
		scanner.nextToken();

		// 2. Page ID
		if (!scanner.hasMoreTokens()) {
			return null;
		}
		final String pageID = scanner.nextToken();

		// 3. Page URL
		if (!scanner.hasMoreTokens()) {
			return null;
		}
		final String pageURL = scanner.nextToken();

		RegisterPage result = new RegisterPage(pageID, pageURL);
		return result;
	}

	private static Register loadRegister(String line, BufferedReader in, String externalImagePathRoot) {
		StringTokenizer scanner = new StringTokenizer(line, ";");
		// 1. Ignore register;
		if (!scanner.hasMoreTokens()) {
			return null;
		}
		scanner.nextToken();

		// 2. Gemeinde
		if (!scanner.hasMoreTokens()) {
			return null;
		}
		final String gemeinde = scanner.nextToken();

		// 3. ID
		if (!scanner.hasMoreTokens()) {
			return null;
		}
		final String buch = scanner.nextToken();

		// 4. Laufzeit
		if (!scanner.hasMoreTokens()) {
			return null;
		}
		final String laufzeit = scanner.nextToken();

		Register result = new Register(gemeinde, buch, laufzeit, externalImagePathRoot);
		return result;
	}

	public void printPersonsSorted() {
		List<String> personList = new ArrayList<String>();
		for (RegisterPage e : defaultRegister.getPageList()) {
			for (Tag e2 : e.getTagList()) {
				if (e2 instanceof PersonTag) {
					PersonTag p = (PersonTag) e2;
					personList.add(String.format("%-25s  %s", p.getTitle(), e.getPageNumber()));
				}
			}
		}

		Collections.sort(personList);
		for (String e : personList) {
			System.out.println(e);
		}
	}

	public void printIndex01() {
		List<PersonTag> personList = getPersonList();

		Collections.sort(personList, new Comparator<PersonTag>() {
			@Override
			public int compare(PersonTag o1, PersonTag o2) {
				char firstA = ' ';
				char firstB = ' ';
				if (!o1.getFullName().isEmpty()) {
					firstA = o1.getFullName().charAt(0);
				}
				if (!o2.getFullName().isEmpty()) {
					firstB = o2.getFullName().charAt(0);
				}
				int result = firstA - firstB;
				if (result == 0) {
					result = DateTag.parse(o1.getDateOfBirth()).compareTo(DateTag.parse(o2.getDateOfBirth()));
				}
				return result;
			}
		});

		char ch = 'A';
		System.out.println(ch);
		for (PersonTag e : personList) {
			if (e.getFullName().charAt(0) != ch) {
				ch = e.getFullName().charAt(0);
				System.out.println();
				System.out.println(ch);
			}
			String yearOfBirth = DateTag.parse(e.getDateOfBirth()).getYear();
			String pod = e.getPlaceOfBirth();
			if (pod == null) {
				pod = "";
			}
			System.out.println(String.format("%-28s  %4s  %4s   %s ", e.getFullName(), yearOfBirth, e.getPageNumberShort(), pod));
		}

	}

	public void printIndex02() {
		List<String> mList = new ArrayList<String>();
		for (RegisterPage e : defaultRegister.getPageList()) {
			for (Tag e2 : e.getTagList()) {
				if (e2 instanceof MarriageTag) {
					MarriageTag p = (MarriageTag) e2;
					String namePair = String.format("%s - %s", p.getHim().getFullName(), p.getHer().getFullName());
					String date = p.getTitle();
					String page = e.getPageNumber();
					String result = String.format("%-50s %-15s  %s", namePair, date, page);
					mList.add(result);
				}
			}
		}

		Collections.sort(mList);
		char ch = 'A';
		System.out.println(ch);
		for (String e : mList) {
			if (e.charAt(0) != ch) {
				ch = e.charAt(0);
				System.out.println();
				System.out.println(ch);
			}
			System.out.println(e);
		}

	}

	public void printAddressList() {
		List<String> mList = new ArrayList<String>();
		for (RegisterPage e : defaultRegister.getPageList()) {
			for (Tag e2 : e.getTagList()) {
				if (e2 instanceof PersonTag) {
					PersonTag p = (PersonTag) e2;
					if (p.getPlaceOfBirth() != null) {
						String s = String.format("%-25s * %s", p.getPlaceOfBirth(), p.getFullName());
						mList.add(s);
					}
				}
			}
		}
		Collections.sort(mList);
		for (String e : mList) {
			System.out.println(e);
		}

	}

	public List<PersonTag> getPersonList() {
		List<PersonTag> result = new ArrayList<PersonTag>();
		for (Register e : registerList) {
			for (RegisterPage e2 : e.getPageList()) {
				for (Tag e3 : e2.getTagList()) {
					if (e3 instanceof PersonTag) {
						PersonTag person = (PersonTag) e3;
						person.setPageNumber(e2.getPageNumber());
						person.setRegisterPage(e2);
						result.add(person);
					}
				}
			}
		}
		return result;
	}

	public List<ParentTag> getParentList() {
		List<ParentTag> result = new ArrayList<ParentTag>();
		for (Register e : registerList) {
			for (RegisterPage e2 : e.getPageList()) {
				for (Tag e3 : e2.getTagList()) {
					if (e3 instanceof ParentTag) {
						ParentTag parent = (ParentTag) e3;
						result.add(parent);
					}
				}
			}
		}
		return result;
	}

	public List<MarriageTag> getMarriageList() {
		List<MarriageTag> result = new ArrayList<MarriageTag>();
		for (Register e : registerList) {
			for (RegisterPage e2 : e.getPageList()) {
				for (Tag e3 : e2.getTagList()) {
					if (e3 instanceof MarriageTag) {
						MarriageTag parent = (MarriageTag) e3;
						result.add(parent);
					}
				}
			}
		}
		return result;
	}

	public void printYearReport() {
		Map<String, Integer> yearCount = new TreeMap<String, Integer>();
		for (PersonTag e : getPersonList()) {
			String dob = e.getDateOfBirth();
			if (dob != null) {
				String year = extractYear(dob);
				if (year != null) {
					if (yearCount.containsKey(year)) {
						yearCount.put(year, yearCount.get(year) + 1);
					} else {
						yearCount.put(year, 1);
					}
				}
			}
		}

		List<String> sortedYearList = new ArrayList<String>(yearCount.keySet());
		Collections.sort(sortedYearList);
		System.out.println("Jahr     Geburten");
		for (String e : sortedYearList) {
			System.out.println(String.format("%s    %d", e, yearCount.get(e)));
		}

	}

	private String extractYear(String date) {
		int i = date.lastIndexOf('.');
		if (i > 0) {
			return date.substring(i + 1);
		}

		return null;
	}

	public static RegisterIndex loadIndex(String registerIndexFileName) {
		RegisterIndex result = new RegisterIndex();
		try (BufferedReader in = new BufferedReader(new FileReader(registerIndexFileName))) {
			String line = in.readLine();
			while (line != null) {
				RegisterIndexEntry entry = loadRegisterIndexEntry(line);
				if (entry != null) {
					result.add(entry);
				}
				line = in.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	private static RegisterIndexEntry loadRegisterIndexEntry(String line) {
		String name = null;
		int pageNr = 0;
		String year = null;
		StringTokenizer scanner = new StringTokenizer(line, ";");
		if (scanner.hasMoreTokens()) {
			name = scanner.nextToken();
		}
		if (scanner.hasMoreTokens()) {
			year = scanner.nextToken();
		}
		pageNr = loadInteger(scanner);

		// Missing year?
		if (pageNr == -1 && year != null) {
			try {
				pageNr = Integer.parseInt(year);
			} catch (NumberFormatException e) {
				// Ignore.
			}
		}
		return new RegisterIndexEntry(name, pageNr, year);
	}

}
