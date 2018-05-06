package matricula.ui.page;

import java.util.List;

import matricula.model.MarriageTag;
import matricula.model.ParentTag;
import matricula.model.PersonTag;
import matricula.model.Tag;

public class PersonToolTipBuilder {

	private final List<Tag> tagList;
	private final StringBuilder out;

	public PersonToolTipBuilder(List<Tag> tagList) {
		this.tagList = tagList;
		this.out = new StringBuilder();
	}

	public String toString(PersonTag tag) {
		out.append("<html><b>");
		person(tag, true);
		out.append("</html>");
		return out.toString();
	}

	private void person(PersonTag person, boolean includeChildren) {
		out.append(person.getFullName());
		out.append("</b>");
		if (person.getDateOfBirth() != null) {
			out.append("<p>");
			out.append("* ");
			out.append(person.getDateOfBirth());
			out.append("</p");
		}
		if (person.getPlaceOfBirth() != null) {
			out.append("<p>");
			out.append("* ");
			out.append(person.getPlaceOfBirth());
			out.append("</p");
		}

		boolean ul = false;
		for (Tag e : tagList) {
			PersonTag parent = extractParent(person, e);
			if (parent != null) {
				ul = lazyTag(ul, "ul");
				String s = parent.isMalePerson() ? "Vater:" : "Mutter: ";
				out.append("<li>");
				out.append(s);
				person(parent, false);
				out.append("</li>");
			}

			PersonTag married = extractMarried(person, e);
			if (married != null) {
				ul = lazyTag(ul, "ul");
				out.append("<li>");
				out.append("Verheiratet mit " + married.getFullName());
				out.append("</li>");
			}
			if (includeChildren) {
				ul = lazyTag(ul, "ul");
				PersonTag child = extractChild(person, e);
				if (child != null) {
					out.append("<li>");
					out.append("Kind: " + child.getFullName());
					out.append("</li>");
				}
			}
		}
		if (ul) {
			out.append("</ul>");
		}
	}

	private boolean lazyTag(boolean ul, String tagName) {
		if (ul) {
			return true;
		}
		out.append("<");
		out.append(tagName);
		out.append(">");
		return true;
	}

	private PersonTag extractChild(PersonTag tag, Tag e) {
		if (e instanceof ParentTag) {
			ParentTag p = (ParentTag) e;
			if (tag == p.getParent()) {
				return ((ParentTag) e).getChild();
			}
		}
		return null;
	}

	private PersonTag extractMarried(PersonTag tag, Tag e) {
		if (e instanceof MarriageTag) {
			MarriageTag m = (MarriageTag) e;
			if (tag == m.getHer()) {
				return m.getHim();
			}
			if (tag == m.getHim()) {
				return m.getHer();
			}
		}
		return null;
	}

	private PersonTag extractParent(PersonTag tag, Tag e) {
		if (e instanceof ParentTag) {
			ParentTag p = (ParentTag) e;
			if (tag == p.getChild()) {
				return ((ParentTag) e).getParent();
			}
		}
		return null;
	}
}
