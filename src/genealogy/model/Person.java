package genealogy.model;

import java.util.ArrayList;
import java.util.List;

public class Person implements IRecordWithRegisterDate {

	private final int personID;
	private final boolean isMale;
	private final String fullName;
	private Address address;
	private RegisterDate dateOfBirth;
	private String job;
	private Person father;
	private Person mother;
	private Person marriedWith;
	private final List<Person> children;

	public Person(int personID, boolean isMale, String fullName) {
		this.personID = personID;
		this.isMale = isMale;
		this.fullName = fullName;
		this.children = new ArrayList<Person>();
	}

	public int getPersonID() {
		return personID;
	}

	public List<Person> children() {
		return children;
	}

	public boolean isMale() {
		return isMale;
	}

	public void setFather(Person father) {
		this.father = father;
	}

	public void setMother(Person mother) {
		this.mother = mother;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public RegisterDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(RegisterDate dob) {
		this.dateOfBirth = dob;
	}

	public String getFullName() {
		return fullName;
	}

	public Address getAddress() {
		return address;
	}

	public Person getFather() {
		return father;
	}

	public Person getMother() {
		return mother;
	}

	public String getFamilyName() {
		int i = fullName.indexOf(" ");
		if (i > 0) {
			return fullName.substring(0, i);
		}
		return fullName;
	}

	public String getFirstName() {
		int i = fullName.indexOf(" ");
		if (i > 0) {
			return fullName.substring(i + 1);
		}
		return fullName;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getJob() {
		return job;
	}

	public void setMarriedWith(Person marriedWith) {
		this.marriedWith = marriedWith;
	}

	public Person getMarriedWith() {
		return marriedWith;
	}

	@Override
	public RegisterDate getRegisterDate() {
		return dateOfBirth;
	}

	public int getScore() {
		int result = 0;

		if (dateOfBirth != null && dateOfBirth.isValid()) {
			result += 1;
		}

		if (address != null && address.isValid()) {
			result += 1;
		}
		if (father != null) {
			result += 1 + father.getScore();
		}
		if (mother != null) {
			result += 1 + mother.getScore();
		}

		return result;
	}

	public boolean isSamePerson(Person other) {
		boolean result = this.fullName.equalsIgnoreCase(other.fullName);
		result &= this.dateOfBirth.equals(other.dateOfBirth);
		return result;
	}
	
	public String toString(){
		return fullName + "("+dateOfBirth+")";
	}

}
