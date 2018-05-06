package matricula.report;

import genealogy.model.RegisterDate;

import java.util.ArrayList;
import java.util.List;

import matricula.Community;
import matricula.model.Matricula;
import matricula.model.PersonTag;

public class BirthMonthReport {

	public static void main(String[] args) {

		String[] registerFileNames = Community.Hollenstein.baptismalRegisterFileNames();
		Matricula m = Matricula.loadRegisters(registerFileNames);

		List<PersonTag> personList = m.getPersonList();
		List<PersonTag> birthList = filter(personList);

		StringFrequency f = new StringFrequency();
		for(PersonTag e : birthList){
			RegisterDate date = RegisterDate.parse(e.getDateOfBirth());
			String month = date.monthAsString();
			f.add(month);
		}
		for(String e : RegisterDate.monthName){
			int count = f.getCount(e);
			System.out.format("%s;%d;",e,count);
			System.out.println();
		}
	}

	private static List<PersonTag> filter(List<PersonTag> personList) {
		List<PersonTag> result = new ArrayList<PersonTag>();

		for (PersonTag e : personList) {
			if (e.getDateOfBirth() != null && e.getPlaceOfBirth() != null) {
				result.add(e);
			}
		}

		return result;
	}

}
