package matricula.model;

import genealogy.model.RegisterDate;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class RegisterPage {

	private final String pageID;
	private final String pageURL;
	private final List<Tag> tagList;
	private Register register;

	public RegisterPage(String pageID, String pageURL) {
		this.pageID = pageID;
		this.pageURL = pageURL;
		this.tagList = new ArrayList<Tag>();
	}

	public String getPageNumber() {
		return pageID;
	}

	public int getPageNumberAsInteger() {
		int i = pageID.lastIndexOf('-');
		if (i > 0) {
			try {
				int result = Integer.parseInt(pageID.substring(i + 1));
				return result;
			} catch (NumberFormatException e) {
				return 0;
			}
		}

		return 0;
	}

	public String getImageURL() {
		return pageURL;
	}

	public PersonTag addMale(String name, TagRegion region) {
		final PersonTag newTag = new PersonTag(name, true, region);
		tagList.add(newTag);
		return newTag;
	}

	public PersonTag addFemale(String name, TagRegion region) {
		final PersonTag newTag = new PersonTag(name, false, region);
		tagList.add(newTag);
		return newTag;
	}

	public void addAddress(String address, TagRegion tagRegion) {
		final Tag newTag = new AddressTag(address, tagRegion);
		tagList.add(newTag);
	}

	public void addMarriage(String dateOfMarriage, String placeOfMarriage, PersonTag bridegroom, PersonTag bride) {
		final MarriageTag newTag = new MarriageTag(dateOfMarriage, bridegroom, bride);
		newTag.setPlace(placeOfMarriage);
		tagList.add(newTag);
	}

	// Connects child with father of mother (depending on the sex of the
	// parent).
	public void addParent(PersonTag child, PersonTag parent) {
		final ParentTag newTag = new ParentTag(child, parent);
		tagList.add(newTag);
	}

	public List<Tag> getTagList() {
		return tagList;
	}

	public List<Tag> getPersonTagList() {
		List<Tag> result = new ArrayList<Tag>();
		for (Tag e : tagList) {
			if (e instanceof PersonTag) {
				result.add(e);
			}
		}
		return result;
	}

	public void save() {
		register.saveAll();
	}

	public void setRegister(Register register) {
		this.register = register;
	}

	public Register getRegister() {
		return register;
	}

	public void save(PrintStream out) {
		out.print("page;");
		out.print(pageID);
		out.print(";");
		out.print(pageURL);
		out.print(";");
		out.println();
		for (Tag e : tagList) {
			e.save(out);
		}

	}

	public void addTag(Tag tag) {
		tagList.add(tag);
	}

	public Tag findTagAt(int x, int y) {
		for (Tag e : tagList) {
			if (e.getRegion().contains(x, y)) {
				return e;
			}
		}

		return null;
	}

	@Override
	public String toString() {
		int countMale = 0;
		int countFemale = 0;
		int countMarriage = 0;
		for (Tag e : tagList) {
			if (e instanceof PersonTag) {
				PersonTag p = (PersonTag) e;
				if (p.isMalePerson()) {
					countMale++;
				} else {
					countFemale++;
				}
			} else if (e instanceof MarriageTag) {
				countMarriage++;
			}
		}

		// For concise labels in tree views, we try to show only the page number
		// (i.e. the last four digits).
		String pageNr = pageID;
		int i = pageNr.lastIndexOf("-");
		if (i > 0 && i < pageNr.length() - 3) {
			pageNr = pageNr.substring(i + 2);
		}

		final String result = String.format("%s (%d, \u2642\u2640 %d)", pageNr, countMarriage, countMale + countFemale);
		return result;
	}

	public void connectTags() {
		for (Tag e : tagList) {
			if (e instanceof MarriageTag) {
				MarriageTag m = (MarriageTag) e;
				Tag p1 = findTagAt(m.getHim().getRegion().getX(), m.getHim().getRegion().getY());
				if (p1 instanceof PersonTag) {
					m.setBridegroom((PersonTag) p1);
				} else {
					System.out.println("Missing person for marriage on " + m.getTitle());
				}
				Tag p2 = findTagAt(m.getHer().getRegion().getX(), m.getHer().getRegion().getY());
				if (p2 instanceof PersonTag) {
					m.setBride((PersonTag) p2);
				} else {
					System.out.println("Missing person for marriage on " + m.getTitle());
				}
			} else if (e instanceof ParentTag) {
				ParentTag p = (ParentTag) e;
				Tag childCandidate = findTagAt(p.getChild().getRegion().getX(), p.getChild().getRegion().getY());
				if (childCandidate instanceof PersonTag) {
					p.setChild((PersonTag) childCandidate);
				} else {
					System.out.println("Missing child for parent " + p);
				}
				Tag parentCandidate = findTagAt(p.getParent().getRegion().getX(), p.getParent().getRegion().getY());
				if (parentCandidate instanceof PersonTag) {
					p.setParent((PersonTag) parentCandidate);
				} else {
					System.out.println("Missing parent for parent " + p);
				}
			}
		}

	}

	public int firstYear() {
		for (Tag e : tagList) {
			if (e instanceof PersonTag) {
				PersonTag p = (PersonTag) e;
				if (p.getDateOfBirth() != null) {
					RegisterDate date = RegisterDate.parse(p.getDateOfBirth());
					return date.year();
				}
			}
		}
		return -1;
	}

}
