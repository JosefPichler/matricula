package matricula.ui.page.tag;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import matricula.model.Tag;
import matricula.model.TagRegion;
import matricula.ui.page.ImageGraphics2D;

public class TagTool {

	private final Tag tag;

	public TagTool(Tag tag) {
		this.tag = tag;
	}

	public void paint(ImageGraphics2D p) {
		final Graphics2D g = p.getGraphics2D();
		final TagRegion t = tag.scale(p.getScale());
		final int x = p.getOffsetX() + t.getX();
		final int y = p.getOffsetY() + t.getY();
		final int width = t.getWidth();
		final int height = t.getHeight();

		final Color oldColor = g.getColor();

		// Draw error line.
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHints(rh);

		int basey = y + height - 2;
		int ix = x;
		int dy = 1;
		int previx = ix;
		int prevdy = dy;
		g.setColor(new Color(174, 90, 79));
		while (ix < (x + width)) {
			previx = ix;
			prevdy = dy;
			ix = ix + 2;
			dy = -dy;
			g.drawLine(previx, basey + prevdy, ix, basey + dy);
		}

		g.setColor(oldColor);
	}

}
