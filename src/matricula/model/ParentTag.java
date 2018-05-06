package matricula.model;

import java.io.PrintStream;

public class ParentTag extends Tag {

	private PersonTag child;
	private PersonTag parent;

	public ParentTag(PersonTag child, PersonTag parent) {
		super(null);
		this.child = child;
		this.parent = parent;
	}

	@Override
	public TagRegion getRegion() {
		return Tag.NoRegion;
	}

	@Override
	public String getTitle() {
		return null;
	}

	public PersonTag getChild() {
		return child;
	}

	public void setChild(PersonTag child) {
		this.child = child;
	}

	public PersonTag getParent() {
		return parent;
	}

	public void setParent(PersonTag parent) {
		this.parent = parent;
	}

	@Override
	public void save(PrintStream out) {
		// Must not call super.save because we passed 'null' to super
		// constructor.
		out.print("tag;parent;dummy;");

		// We save the upper left corner of person tags; bridegroom first.
		out.print(child.getRegion().getX());
		out.print(";");
		out.print(child.getRegion().getY());
		out.print(";");
		out.print(parent.getRegion().getX());
		out.print(";");
		out.print(parent.getRegion().getY());
		out.print(";");
		out.println();
	}

}
