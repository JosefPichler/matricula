package matricula.model;

import java.io.PrintStream;

public class AddressTag extends Tag {

	private final String address;

	public AddressTag(String address, TagRegion tagRegion) {
		super(tagRegion);
		this.address = address;
	}

	@Override
	public String getTitle() {
		return address;
	}

	public void save(PrintStream out) {
		out.print("tag;");
		out.print("address;");
		out.print(getTitle());
		out.print(";");
		super.save(out);
		out.println();
	}
}
