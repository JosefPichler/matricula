package matricula.ui.page;

import java.util.ArrayList;
import java.util.List;

import matricula.model.AddressTag;
import matricula.model.Matricula;
import matricula.model.PersonTag;
import matricula.model.Register;
import matricula.model.RegisterPage;
import matricula.model.Tag;

public class AddressToolTipBuilder {

	private final RegisterPage page;

	public AddressToolTipBuilder(RegisterPage page) {
		this.page = page;
	}

	public String toString(AddressTag tag) {
		StringBuilder out = new StringBuilder();
		out.append("<html>");

		String adr = tag.getTitle();

		List<PersonTag> personList = filterPersonTag();
		for (PersonTag p : personList) {
			if (adr.equalsIgnoreCase(p.getPlaceOfBirth())) {
				out.append("<p>");
				out.append(p.getFullName());
				out.append("</p>");
			}
		}
		out.append("</html>");
		return out.toString();
	}

	private List<PersonTag> filterPersonTag() {
		List<PersonTag> personList = new ArrayList<PersonTag>();
		Matricula m = page.getRegister().getMatricula();

		for (Register r : m.getRegisterList()) {
			for (RegisterPage p : r.getPageList()) {
				for (Tag t : p.getTagList()) {
					if (t instanceof PersonTag) {
						personList.add((PersonTag) t);

					}
				}
			}
		}
		return personList;
	}
}
