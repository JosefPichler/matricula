package matricula.model;

import java.io.PrintStream;

public abstract class Tag {

	protected static final TagRegion NoRegion = new TagRegion(-1, -1, -1, -1);
	private final TagRegion region;

	public Tag(TagRegion region) {
		this.region = region;
	}

	public abstract String getTitle();

	public TagRegion scale(double scale) {
		final int x = (int) (region.getX() * scale);
		final int y = (int) (region.getY() * scale);
		final int w = (int) (region.getWidth() * scale);
		final int h = (int) (region.getHeight() * scale);

		TagRegion result = new TagRegion(x, y, w, h);
		return result;
	}

	public void save(PrintStream out) {
		out.print(region.getX());
		out.print(";");
		out.print(region.getY());
		out.print(";");
		out.print(region.getWidth());
		out.print(";");
		out.print(region.getHeight());
		out.print(";");
	}

	public TagRegion getRegion() {
		return region;
	}
}
