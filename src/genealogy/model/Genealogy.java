package genealogy.model;

import java.util.ArrayList;
import java.util.List;

public class Genealogy {

	private final List<Person> personList;
	private final List<Address> addressList;
	private final List<Marriage> marriageList;

	public Genealogy() {
		this.personList = new ArrayList<Person>();
		this.addressList = new ArrayList<Address>();
		this.marriageList = new ArrayList<Marriage>();
	}

	public void addPerson(Person person) {
		personList.add(person);

	}

	public void addAddress(Address address) {
		addressList.add(address);

	}

	public void addMarriage(Marriage marriage) {
		marriageList.add(marriage);
	}

	public Address findAddress(String key) {
		for (Address e : addressList) {
			if (key.equalsIgnoreCase(e.getAddressName())) {
				return e;
			}
		}
		return null;
	}

	public List<Person> findPerson(String key) {
		List<Person> result = new ArrayList<Person>();
		for (Person e : personList) {
			String personName = e.getFullName();
			if (personName.equalsIgnoreCase(key)) {
				result.add(e);
			}
		}

		return result;

	}

}
