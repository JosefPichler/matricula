package matricula.report;

import genealogy.model.Address;
import genealogy.model.Genealogy;
import genealogy.model.Marriage;
import genealogy.model.Person;
import genealogy.model.RegisterDate;

import java.util.HashMap;
import java.util.Map;

import matricula.model.MarriageTag;
import matricula.model.Matricula;
import matricula.model.ParentTag;
import matricula.model.PersonTag;

public class GenealogyFactory {

	public static GenealogyFactory createFactory() {
		GenealogyFactory f = new GenealogyFactory();
		return f;
	}

	private final Map<PersonTag, Person> personTable;
	private final Map<String, Address> addressTable;

	private GenealogyFactory() {
		this.personTable = new HashMap<PersonTag, Person>();
		this.addressTable = new HashMap<String, Address>();
	}

	public Genealogy createGenealogy(Matricula m) {
		Genealogy result = new Genealogy();

		for (PersonTag pTag : m.getPersonList()) {
			Person p = createPerson(pTag);
			personTable.put(pTag, p);
			result.addPerson(p);

			Address a = null;
			if (addressTable.containsKey(pTag.getPlaceOfBirth())) {
				a = addressTable.get(pTag.getPlaceOfBirth());
			} else {
				a = new Address(pTag.getPlaceOfBirth());
				addressTable.put(pTag.getPlaceOfBirth(), a);
				result.addAddress(a);
			}
			p.setAddress(a);
			a.addPerson(p);
		}

		for (ParentTag pTag : m.getParentList()) {
			Person child = personTable.get(pTag.getChild());
			Person parent = personTable.get(pTag.getParent());
			parent.children().add(child);
			if (parent.isMale()) {
				child.setFather(parent);
			} else {
				child.setMother(parent);
			}
		}

		for (MarriageTag mTag : m.getMarriageList()) {
			Person bridge = personTable.get(mTag.getHer());
			Person bridegroom = personTable.get(mTag.getHim());
			bridegroom.setMarriedWith(bridge);
			bridge.setMarriedWith(bridegroom);
			Marriage ma = new Marriage(RegisterDate.parse(mTag.getDateOfMarriage()), bridge, bridegroom);
			result.addMarriage(ma);

			if (mTag.getPlace() != null) {
				Address a = null;
				if (addressTable.containsKey(mTag.getPlace())) {
					a = addressTable.get(mTag.getPlace());
				} else {
					a = new Address(mTag.getPlace());
					addressTable.put(mTag.getPlace(), a);
					result.addAddress(a);
				}
				a.addMarriage(ma);

			} else {

				Address a = null;
				if (addressTable.containsKey(mTag.getHim().getPlaceOfBirth())) {
					a = addressTable.get(mTag.getHim().getPlaceOfBirth());
				} else {
					a = new Address(mTag.getHim().getPlaceOfBirth());
					addressTable.put(mTag.getHim().getPlaceOfBirth(), a);
					result.addAddress(a);
				}
				a.addMarriage(ma);
			}

		}

		return result;
	}

	private int personCount = 0;

	private Person createPerson(PersonTag pTag) {

		Person result = new Person(personCount++, pTag.isMalePerson(), pTag.getFullName());
		result.setDateOfBirth(RegisterDate.parse(pTag.getDateOfBirth()));
		result.setJob(pTag.getJob());
		return result;
	}

}
