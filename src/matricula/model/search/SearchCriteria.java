package matricula.model.search;

import matricula.model.PersonTag;
import matricula.model.Tag;

public abstract class SearchCriteria {

	public boolean accept(Tag t) {
		if (t instanceof PersonTag) {
			return acceptPerson((PersonTag) t);
		}
		return false;
	}

	protected abstract boolean acceptPerson(PersonTag t);
}
