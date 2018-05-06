package matricula.report;

import genealogy.model.Address;
import genealogy.model.Genealogy;
import genealogy.model.IRecordWithRegisterDate;
import genealogy.model.Marriage;
import genealogy.model.Person;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import matricula.Community;
import matricula.model.Matricula;

public class AddressReport {

	// Test driver for Sattel 1 - 11.
	public static void main(String[] args) {

		List<String> reg = new ArrayList<String>(Arrays.asList(Community.Hollenstein.registerFileNames()));
		reg.remove("files-01-08/01-08.txt");
		String[] registerForGeneration = new String[reg.size()];
		reg.toArray(registerForGeneration);
		Matricula m = Matricula.loadRegisters(registerForGeneration);

		Genealogy g = GenealogyFactory.createFactory().createGenealogy(m);

		for (int i = 1; i <= 11; i++) {
			String adr = "Sattel " + i;
			generate(g, adr);
		}
		generate(g, "Oberkirchen 13");
		generate(g, "Thalbauer 3");
		generate(g, "Thalbauer 9");
		generate(g, "Thomasberg 1");
		generate(g, "Thomasberg 4");
		generate(g, "Thomasberg 10");
		generate(g, "Reingrub 2");
		generate(g, "Berg 10");
		generate(g, "Hollenstein 1"); // Trogbach
		generate(g, "Hollenstein 2"); // Mittendorf
		generate(g, "Hollenstein 3"); // Badhaus
		generate(g, "Hollenstein 4"); // Steinhaus
		generate(g, "Hollenstein 5"); // Altenhaus
		generate(g, "Hollenstein 6"); // Dingstatt
	}

	private static void generate(Genealogy g, String adr) {
		Address sattel10 = g.findAddress(adr);
		AddressReport report = new AddressReport(sattel10);
		try {
			report.generate("maps/detail/" + adr + ".html");
		} catch (FileNotFoundException e) {
			System.out.println("Error during report generation.");
			e.printStackTrace();
		}
	}

	private final Address address;
	private final Toggle cellBackgroundChild;
	private final Toggle cellBackgroundFather;
	private final Toggle cellBackgroundMother;
	private final Toggle cellBackgroundMarriage;

	private final Set<Person> guard;

	public AddressReport(Address address) {
		this.address = address;
		this.cellBackgroundChild = new Toggle(new String[] { "d0" });
		this.cellBackgroundFather = new Toggle(new String[] { "d1", "d2" });
		this.cellBackgroundMother = new Toggle(new String[] { "d1", "d2" });
		this.cellBackgroundMarriage = new Toggle(new String[] { "d3" });

		this.guard = new HashSet<Person>();
	}

	private void generate(String outFilePath) throws FileNotFoundException {
		try (PrintWriter out = new PrintWriter(outFilePath)) {
			printHeader(out);
			printTable(out);
		}
	}

	private void printHeader(PrintWriter out) {
		out.println("<meta charset=\"UTF-8\">");
		out.println("<head>");
		out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"design.css\">");
		out.println("</head>");
		out.println("<h1>");
		out.println(address.getAddressName());
		out.println("</h1>");
	}

	private void printTable(PrintWriter out) {
		out.println("<table>");
		out.format("<col width=\"%d\">", 50);
		out.format("<col width=\"%d\">", 200);
		out.format("<col width=\"%d\">", 280);
		out.format("<col width=\"%d\">", 280);

		out.println("<tr><th>Jahr</th><th>Taufe</th><th>Vater</th><th>Mutter</th></tr>");

		List<IRecordWithRegisterDate> recordList = new ArrayList<IRecordWithRegisterDate>();
		recordList.addAll(address.getPersonList());
		recordList.addAll(address.getMarriageList());
		recordList.sort(new RecordDateComparator());

		for (IRecordWithRegisterDate e : recordList) {
			printRecord(e, out);
		}
		out.println("</table>");
	}

	private void printRecord(IRecordWithRegisterDate record, PrintWriter out) {
		if (record instanceof Person) {
			printPerson((Person) record, out);
		}
		if (record instanceof Marriage) {
			printMarriage((Marriage) record, out);
		}

	}

	private void printMarriage(Marriage m, PrintWriter out) {
		out.print("<tr>");

		out.print("<td class=\"d3\">");
		out.print(m.getDateOfMarriage().year());
		out.print("</td>");
		out.print("<td class=\"d3\">&#9901;");
		out.print("</td>");

		printPersonCell(m.getBridegroom(), cellBackgroundMarriage, true, out);
		printPersonCell(m.getBridge(), cellBackgroundMarriage, true, out);

		out.println("</tr>");
	}

	private void printPerson(Person p, PrintWriter out) {
		if (p.getDateOfBirth().year() == -1) {
			return;
		}
		out.print("<tr>");

		out.print("<td>");
		out.print(p.getDateOfBirth().year());
		out.print("</td>");

		printPersonCell(p, cellBackgroundChild, false, out);
		printPersonCell(p.getFather(), cellBackgroundFather, false, out);
		printPersonCell(p.getMother(), cellBackgroundMother, false, out);

		out.println("</tr>");
	}

	private void printPersonCell(Person p, Toggle t, boolean personDetail, PrintWriter out) {
		if (p == null || t == null) {
			out.print("<td></td>");
			return;
		}

		String tdClass = t.getValue(p.getFullName());

		out.format("<td class=\"%s\">", tdClass);
		guard.clear();
		printPersonParents(p, true, personDetail, out);
		out.print("</td>");
	}

	private void printPersonParents(Person p, boolean personBold, boolean personDetail, PrintWriter out) {
		if (guard.contains(p)) {
			System.out.println("Error in data model: person " + p + " already printed");
			System.out.println("  " + p.getFullName());
			return;
		}
		guard.add(p);

		out.print(p.getFirstName());
		out.print(" ");
		if (personBold) {
			out.print("<b>");
		}
		out.print(p.getFamilyName());
		if (personBold) {
			out.print("</b>");
		}

		if (p.getJob() != null) {
			out.print("<span style=\"font-size:95%;\">");
			out.print(" ");
			out.print(p.getJob());
			out.print("</span>");
		}

		boolean parent = personDetail && (p.getFather() != null || p.getMother() != null);
		if (!parent) {
			return;
		}

		out.print("<span style=\"font-size:95%;\">");

		if (p.isMale()) {
			out.print(", Sohn");
		} else {
			out.print(", Tochter");
		}

		if (p.getFather() != null && p.getMother() != null) {
			out.print(" des ");
			printPersonParents(p.getFather(), false, personDetail, out);
			out.print(" und der ");
			printPersonParents(p.getMother(), false, personDetail, out);
		} else if (p.getFather() != null) {
			out.print(" des ");
			printPersonParents(p.getFather(), false, personDetail, out);
		} else {
			out.print(" der ");
			printPersonParents(p.getMother(), false, personDetail, out);
		}
		out.print("</span>");
	}
}
