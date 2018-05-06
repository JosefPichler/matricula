package genealogy.model;

import java.util.ArrayList;
import java.util.List;

public class Address {

	private final String adr;
	private final List<Marriage> marriageList;
	private final List<Person> personList;

	public Address(String adr) {
		this.adr = adr;
		this.personList = new ArrayList<Person>();
		this.marriageList = new ArrayList<Marriage>();
	}

	public void addPerson(Person p) {
		personList.add(p);

	}

	public List<Person> getPersonList() {
		return personList;
	}

	public String getAddressName() {
		return adr;
	}

	public void addMarriage(Marriage ma) {
		marriageList.add(ma);
	}

	public List<Marriage> getMarriageList() {
		return marriageList;
	}

	public boolean isValid() {
		return adr != null;
	}

}
