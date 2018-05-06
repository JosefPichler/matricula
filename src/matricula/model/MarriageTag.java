package matricula.model;

import java.io.PrintStream;

public class MarriageTag extends Tag {

	private String dateOfMarriage;
	private String placeOfMarriage;
	private PersonTag bridegroom;
	private PersonTag bride;

	public MarriageTag(String dateOfMarriage, PersonTag bridegroom, PersonTag bride) {
		super(null);
		this.dateOfMarriage = dateOfMarriage;
		this.bridegroom = bridegroom;
		this.bride = bride;
	}

	public void setDateOfMarriage(String date) {
		this.dateOfMarriage = date;
	}

	public String getDateOfMarriage() {
		return dateOfMarriage;
	}

	@Override
	public String getTitle() {
		return dateOfMarriage;
	}

	@Override
	public TagRegion getRegion() {
		int x1 = bridegroom.getRegion().getX() - 20;
		int x2 = bride.getRegion().getX() - 20;
		int x = Math.min(x1, x2);
		int y = Math.min(bridegroom.getRegion().getCenterY(), bride.getRegion().getCenterY());
		y += Math.abs(bridegroom.getRegion().getCenterY() - bride.getRegion().getCenterY()) / 2;

		int dx = 100;
		int w = 50;

		TagRegion result = new TagRegion(x - dx - w, y - w, 2 * w, 2 * w);
		return result;
	}

	@Override
	public void save(PrintStream out) {
		// Must not call super.save because we passed 'null' to super
		// constructor.
		out.print("tag;marriage;");
		out.print(dateOfMarriage);
		out.print(";");

		// We save the upper left corner of person tags; bridegroom first.
		out.print(bridegroom.getRegion().getX());
		out.print(";");
		out.print(bridegroom.getRegion().getY());
		out.print(";");
		out.print(bride.getRegion().getX());
		out.print(";");
		out.print(bride.getRegion().getY());
		out.print(";");

		if (placeOfMarriage != null) {
			out.print("place=");
			out.print(placeOfMarriage);
			out.print(";");
		}
		out.println();

	}

	public void setBridegroom(PersonTag bridegroom) {
		this.bridegroom = bridegroom;
	}

	public PersonTag getHim() {
		return bridegroom;
	}

	public PersonTag getHer() {
		return bride;
	}

	public void setBride(PersonTag bride) {
		this.bride = bride;
	}

	public void setPlace(String place) {
		this.placeOfMarriage = place;
	}

	public String getPlace() {
		return placeOfMarriage;
	}

}
