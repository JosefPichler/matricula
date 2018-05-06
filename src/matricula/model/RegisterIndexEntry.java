package matricula.model;

public class RegisterIndexEntry {

	private final String name;
	private final String year;
	private final int pageNr;

	public RegisterIndexEntry(String name, int pageNr, String year) {
		this.name = name;
		this.year = year;
		this.pageNr = pageNr;
	}

	public int getPageNr() {
		return pageNr;
	}

	@Override
	public String toString() {
		String result = String.format("%s (%s)", name, year);
		return result;
	}

}
