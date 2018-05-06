package matricula.ui.page;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import matricula.image.OtsuBinarize;
import matricula.model.Tag;
import matricula.model.TagRegion;

public class SelectionToolOtsu extends EditTool {

	private final BufferedImage image;
	private final int startX;
	private final int startY;
	private int endX;
	private int endY;

	public SelectionToolOtsu(int startX, int startY, BufferedImage image) {
		super(null, null);
		this.startX = startX;
		this.startY = startY;
		this.image = image;
	}

	public void updateTo(int x, int y) {
		this.endX = x;
		this.endY = y;
	}

	public int getWidth() {
		return endX - startX;
	}

	public int getHeight() {
		return endY - startY;
	}

	public int getX() {
		return startX;
	}

	public int getY() {
		return startY;
	}

	public boolean paintNeedsRegionImage() {
		return true;
	}

	public void paint(int offsetX, int offsetY, double scale, Graphics2D g) {
		int x = offsetX + (int) (scale * Math.min(startX, endX));
		int y = offsetY + (int) (scale * Math.min(startY, endY));
		int width = (int) (scale * Math.abs(endX - startX));
		int height = (int) (scale * Math.abs(endY - startY));
		final Color oldColor = g.getColor();

		BufferedImage original = image.getSubimage(Math.min(startX, endX), Math.min(startY, endY), 1+Math.abs(startX - endX),
				1+Math.abs(startY - endY));

		BufferedImage grayscale = OtsuBinarize.toGray(original);
		BufferedImage binarized = OtsuBinarize.binarize(grayscale);

		AffineTransform op = AffineTransform.getScaleInstance(scale, scale);
		g.drawImage(binarized, new AffineTransformOp(op, null), x, y);

		// 2. Red rectangle.
		g.setColor(Color.red);
		g.drawRect(x, y, width, height);

		g.setColor(oldColor);

	}

	public boolean isEmpty() {
		return (Math.abs(startX - endX) < 5) || endX == 0;
	}

	@Override
	public String toString() {
		String result = String.format("(%d, %d) - (%d, %d)", startX, startY, endX, endY);
		return result;
	}

	public TagRegion getTagRegion() {
		final int x = Math.min(startX, endX);
		final int y = Math.min(startY, endY);
		final int w = Math.abs(startX - endX);
		final int h = Math.abs(startY - endY);
		return new TagRegion(x, y, w, h);
	}

	@Override
	public void updateTo(Tag tag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void paint(ImageGraphics2D p) {
		// TODO Auto-generated method stub
		
	}
}
