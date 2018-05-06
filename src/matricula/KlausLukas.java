package matricula;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import matricula.model.Matricula;
import matricula.model.PersonTag;

public class KlausLukas {
	public static void main(String[] args) {

		Matricula m = Matricula.loadRegisters(Community.Hollenstein.registerFileNames());

		List<PersonTag> personList = m.getPersonList();
		List<String> nameList = extractGivenNames(personList);
	
		
		Set<String> uniqueNameSet = new HashSet<String>();
		uniqueNameSet.addAll(nameList);
		nameList.clear();
		nameList.addAll(uniqueNameSet);
		Collections.sort(nameList);

		printList(nameList);
	}

	private static void printList(List<String> nameList) {
		for(String e : nameList){
			System.out.println(e);
		
		}
		
	}

	private static List<String> extractGivenNames(List<PersonTag> personList) {
		List<String> result = new ArrayList<String>();
		for (PersonTag e : personList) {
		    String givenName = extractGivenName(e.getFullName());
			if (givenName != null) {
				result.add(givenName);
			}
		}
		return result;
	}

	private static String extractGivenName(String fullName) {
		int count = 0;
		int i = fullName.indexOf(' ');
		while (i > -1) {
			count++;
			i = fullName.indexOf(' ', i+1);
		}
		
		if (count > 1){
			String result = fullName.substring(fullName.indexOf(' '));
			return result;
		}
		return null;
	}
}
