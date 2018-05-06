package genealogy.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import matricula.Community;
import matricula.model.Matricula;
import matricula.report.GenealogyFactory;

public class WhoAmI {

	public static void main(String[] args) {

		String key = makeName(args);
		System.out.println("Find " + key);

		Matricula m = Matricula.loadRegisters(Community.Hollenstein.registerFileNames());
		Genealogy g = GenealogyFactory.createFactory().createGenealogy(m);
		WhoAmI main = new WhoAmI(g);
		main.run(key);
	}

	private static String makeName(String[] args) {
		StringBuilder result = new StringBuilder();

		for (String e : args) {
			result.append(e);
			result.append(" ");
		}
		return result.toString().trim();
	}

	private final Genealogy g;

	public WhoAmI(Genealogy g) {
		this.g = g;
	}

	private void run(String key) {
		List<Person> result = findPerson(key);
		mergePersons(result);
		printResult(result, 0);
	}

	private List<Person> findPerson(String key) {
		List<Person> result = g.findPerson(key);
		mergePersons(result);
		Collections.sort(result, new Comparator<Person>() {

			@Override
			public int compare(Person a, Person b) {
				return b.getScore() - a.getScore();
			}
		});

		return result;
	}

	// Merge two persons with same name and same date of birth.
	private void mergePersons(List<Person> list) {
		List<Person> workList = new ArrayList<Person>();
		workList.addAll(list);
		boolean merged = true;
		while (merged && workList.size() > 1) {
			merged = false;
			Person p = workList.get(0);
			for (int i = 1; i < workList.size(); i++) {
				Person other = workList.get(i);
				if (p.isSamePerson(other)) {
					workList.remove(other);
					merged = true;
					break;
				}
			}
		}

		list.clear();
		list.addAll(workList);
	}

	private void printResult(List<Person> result, int level) {
		if (level > 5) {
			return;
		}

		for (Person e : result) {
			// Print person in one line
			for (int i = 0; i < level; i++) {
				System.out.print("   ");
			}
			System.out.format("%2d ", e.getScore());
			printPerson(e);

			if (e.getFather() != null) {
				printPerson(e.getFather());
			}

			if (e.getMother() != null) {
				printPerson(e.getMother());
			}

			if (e.getMarriedWith() != null) {
				printPerson(e.getMarriedWith());
			}
			System.out.println();

			// Now start parents...
			if (e.getFather() != null) {
				List<Person> fathersList = findPerson(e.getFather().getFullName());
				removePerson(e, fathersList);
				filterAndMerge(fathersList);
				printResult(fathersList, level + 1);
			}
			if (e.getMother() != null) {
				List<Person> mothersList = findPerson(e.getMother().getFullName());
				removePerson(e, mothersList);
				filterAndMerge(mothersList);
				printResult(mothersList, level + 1);
			}

		}
	}

	private void removePerson(Person p, List<Person> list) {
		list.remove(p);

		if (p.getDateOfBirth().isValid()) {
			// Remove all persons younger than person p.
			List<Person> inputList = new ArrayList<Person>();
			inputList.addAll(list);
			for (Person e : inputList) {
				int yearDiff = p.getDateOfBirth().year() - e.getDateOfBirth().year();
				// Eltern sind mindestens 15 Jahre alt.
				if (yearDiff < 15) {
					list.remove(e);
				}
			}
		}

	}

	private void filterAndMerge(List<Person> list) {
		List<Person> result = new ArrayList<Person>();

		for (int i = 0; i < 1 && i < list.size(); i++) {
			result.add(list.get(i));
		}
		list.clear();
		list.addAll(result);
	}

	private void printPerson(Person e) {
		String name = e.getFullName();
		String addr = (e.getAddress().getAddressName() != null) ? e.getAddress().getAddressName() : "-";
		String dob = e.getDateOfBirth().year() != -1 ? e.getDateOfBirth().toString() : "-";

		System.out.format("%-15s %10s %-20s", name, dob, addr);

	}
}
