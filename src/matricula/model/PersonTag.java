package matricula.model;

import java.io.PrintStream;

public class PersonTag extends Tag {

	private String name;
	private final boolean male;
	private String dateOfBirth;
	private String placeOfBirth;
	private String pageNr;
	private String job;

	// Bei Hochzeiten steht teilweise das Alter der Brautleute.
	private String age;

	private RegisterPage registerPage;

	public PersonTag(String name, boolean male, TagRegion region) {
		super(region);
		this.name = name;
		this.male = male;
	}

	@Override
	public String getTitle() {
		StringBuffer result = new StringBuffer();
		result.append("<html><b>");
		result.append(name);
		result.append("</b>");

		if (dateOfBirth != null) {
			result.append("<p>");
			result.append("* ");
			result.append(dateOfBirth);
			result.append("</p");
		}
		if (placeOfBirth != null) {
			result.append("<p>");
			result.append("* ");
			result.append(placeOfBirth);
			result.append("</p");
		}
		result.append("</html>");
		return result.toString();
	}

	@Override
	public String toString() {
		return name;
	}

	public void save(PrintStream out) {
		out.print("tag;");
		if (male) {
			out.print("male;");
		} else {
			out.print("female;");
		}
		out.print(getFullName());
		out.print(";");

		// Super writes tag region.
		super.save(out);
		if (placeOfBirth != null) {
			out.print("pob=");
			out.print(placeOfBirth);
			out.print(";");
		}
		if (dateOfBirth != null) {
			out.print("dob=");
			out.print(dateOfBirth);
			out.print(";");
		}
		if (job != null) {
			out.print("job=");
			out.print(job);
			out.print(";");
		}
		if (age != null) {
			out.print("age=");
			out.print(age);
			out.print(";");
		}
		out.println();
	}

	public boolean isMalePerson() {
		return male;
	}

	public String getFullName() {
		return name;
	}

	public String getFamilyName() {
		int i = name.indexOf(" ");
		if (i > 0) {
			return name.substring(0, i);
		}
		return name;
	}

	public String getFirstName() {
		int i = name.indexOf(" ");
		if (i > 0) {
			return name.substring(i + 1);
		}
		return name;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setPlaceOfBirth(String placeOfBirth) {
		this.placeOfBirth = placeOfBirth;
	}

	public String getPlaceOfBirth() {
		return placeOfBirth;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getJob() {
		return job;
	}

	public void setPageNumber(String pageNr) {
		this.pageNr = pageNr;
	}

	public String getPageNumber() {
		return pageNr;
	}

	public String getPageNumberShort() {
		if (pageNr == null) {
			return pageNr;
		}
		int i = pageNr.lastIndexOf('-');
		if (i > 0) {
			String str = pageNr.substring(i + 1);
			try {
				int result = Integer.parseInt(str);
				return new Integer(result).toString();
			} catch (NumberFormatException e) {
				return pageNr;
			}
		}
		return null;
	}

	public void setFullName(String newFullName) {
		this.name = newFullName;
	}

	public void setRegisterPage(RegisterPage page) {
		this.registerPage = page;
	}

	public RegisterPage getRegisterPage() {
		return registerPage;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

}
