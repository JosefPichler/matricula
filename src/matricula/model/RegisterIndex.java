package matricula.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RegisterIndex {

	private final List<RegisterIndexEntry> entryList;
	private final Map<Integer, List<RegisterIndexEntry>> entryMap;

	public RegisterIndex() {
		this.entryList = new ArrayList<RegisterIndexEntry>();
		this.entryMap = new TreeMap<Integer, List<RegisterIndexEntry>>();
	}

	public List<RegisterIndexEntry> getEntriesOfPage(RegisterPage page) {
		int pageNr = page.getPageNumberAsInteger();

		if (entryMap.containsKey(pageNr)) {
			return entryMap.get(pageNr);
		}

		return new ArrayList<RegisterIndexEntry>();
	}

	public void add(RegisterIndexEntry entry) {
		entryList.add(entry);

		int pageNr = entry.getPageNr();
		if (!entryMap.containsKey(pageNr)) {
			entryMap.put(pageNr, new ArrayList<RegisterIndexEntry>());
		}
		entryMap.get(pageNr).add(entry);
	}

}
