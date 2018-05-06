package matricula.ui.page;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class ImageGraphics2D {

	private final int offsetX;
	private final int offsetY;
	private final double scale;
	private final Graphics2D g;

	public ImageGraphics2D(int offsetX, int offsetY, double scale, Graphics2D g) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.scale = scale;
		this.g = g;
	}

	public Color getColor() {
		return g.getColor();
	}

	public void setColor(Color c) {
		g.setColor(c);
	}

	public void fillRect(int x, int y, int width, int height) {
		g.fillRect(toGraphcisX(x), toGraphcisY(y), scale(width), scale(height));
	}

	public void drawRect(int x, int y, int width, int height) {
		g.drawRect(toGraphcisX(x), toGraphcisY(y), scale(width), scale(height));
	}

	public void drawOval(int x, int y, int width, int height) {
		g.drawOval(toGraphcisX(x), toGraphcisY(y), scale(width), scale(height));
	}

	public int scale(int dimension) {
		return (int) (scale * dimension);
	}

	public void drawLine(int x1, int y1, int x2, int y2) {
		g.drawLine(toGraphcisX(x1), toGraphcisY(y1), toGraphcisX(x2), toGraphcisY(y2));
	}

	public int toGraphcisX(int imageCoordinate) {
		final int result = offsetX + (int) (scale * imageCoordinate);
		return result;
	}

	public int toGraphcisY(int imageCoordinate) {
		final int result = offsetY + (int) (scale * imageCoordinate);
		return result;
	}

	public Font getFont() {
		return g.getFont();
	}

	public void setFont(Font newFont) {
		g.setFont(newFont);
	}

	public void drawString(String str, int x, int y) {
		g.drawString(str, toGraphcisX(x), toGraphcisY(y));

	}

	public Graphics2D getGraphics2D() {
		return g;
	}

	public double getScale() {
		return scale;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

}
