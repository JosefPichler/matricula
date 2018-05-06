package matricula.model.search;

import java.util.ArrayList;
import java.util.List;

import matricula.model.Matricula;
import matricula.model.PersonTag;
import matricula.model.Register;
import matricula.model.RegisterPage;
import matricula.model.Tag;

public class MatriculaSearch {

	private final Matricula model;

	public MatriculaSearch(Matricula model) {
		this.model = model;
	}

	public Object searchForDate(final String dateOfBirth) {
		List<Tag> result = iterate(model.getRegisterList(), new SearchCriteria() {

			@Override
			protected boolean acceptPerson(PersonTag t) {
				return dateOfBirth.equals(t.getDateOfBirth());
			}

		});
		return toString(result);
	}

	public Object searchFullname(final String fullName) {
		List<Tag> result = iterate(model.getRegisterList(), new SearchCriteria() {

			@Override
			protected boolean acceptPerson(PersonTag t) {
				return fullName.equals(t.getFullName());
			}

		});
		return toString(result);
	}

	public Object searchPlaceOfBirth(final String placeOfBirth) {
		List<Tag> result = iterate(model.getRegisterList(), new SearchCriteria() {

			@Override
			protected boolean acceptPerson(PersonTag t) {
				return placeOfBirth.equals(t.getPlaceOfBirth());
			}

		});
		return toString(result);
	}

	private List<Tag> iterate(List<Register> registerList, SearchCriteria searchCriteria) {
		List<Tag> result = new ArrayList<Tag>();
		for (Register r : registerList) {
			for (RegisterPage p : r.getPageList()) {
				for (Tag t : p.getTagList()) {
					if (searchCriteria.accept(t)) {
						result.add(t);
						if (t instanceof PersonTag) {
							((PersonTag) t).setPageNumber(createPageNr(r, p));
						}

					}
				}
			}
		}
		return result;
	}

	private String createPageNr(Register r, RegisterPage p) {
		return r.getTitle() + ", Seite " + p.getPageNumberAsInteger();
	}

	private String toString(List<Tag> tagList) {
		StringBuilder result = new StringBuilder();
		for (Tag t : tagList) {
			result.append(t);
			if (t instanceof PersonTag) {
				PersonTag p = (PersonTag) t;
				if (p.getDateOfBirth() != null){
					result.append(" (*");
					result.append(p.getDateOfBirth());
					result.append(")");
				}
				
				if (p.getPageNumber() != null) {
					result.append(" - ");
					result.append(((PersonTag) t).getPageNumber());
				}
			}
			result.append("\n");
		}
		return result.toString();
	}
}
