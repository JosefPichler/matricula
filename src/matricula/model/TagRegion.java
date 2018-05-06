package matricula.model;

/**
 * Describes a region in an image with its origin in the left upper corner (x,
 * y).
 */
public class TagRegion {

	private final int x;
	private final int y;
	private final int width;
	private final int height;

	public TagRegion(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean contains(int px, int py) {
		final boolean result = (x <= px && px <= (x + width)) && (y <= py && py <= (y + height));
		return result;
	}

	public int getCenterY() {
		return y + (int) (0.5 * height);
	}

	public int getCenterX() {
		return x + (int) (0.5 * width);
	}
}
