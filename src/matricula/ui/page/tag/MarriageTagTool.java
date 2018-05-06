package matricula.ui.page.tag;

import matricula.model.MarriageTag;
import matricula.model.TagRegion;
import matricula.ui.page.ImageGraphics2D;

public class MarriageTagTool extends TagTool {

	private final MarriageTag tag;

	public MarriageTagTool(MarriageTag tag) {
		super(tag);
		this.tag = tag;
	}

	@Override
	public void paint(ImageGraphics2D p) {
		int x1 = tag.getHim().getRegion().getX() - 20;
		int y1 = tag.getHim().getRegion().getCenterY();

		int x2 = tag.getHer().getRegion().getX() - 20;
		int y2 = tag.getHer().getRegion().getCenterY();

		int dy = 20;
		TagRegion r = tag.getRegion();
		p.drawOval(r.getX(), r.getY() + dy, r.getWidth(), r.getHeight());
		p.drawOval(r.getX(), r.getY() - dy, r.getWidth(), r.getHeight());

		p.drawLine(r.getCenterX(), r.getY() - dy, r.getCenterX(), y1);
		p.drawLine(r.getCenterX(), y1, x1, y1);

		p.drawLine(r.getCenterX(), r.getY() + r.getHeight() + dy, r.getCenterX(), y2);
		p.drawLine(r.getCenterX(), y2, x2, y2);

		p.drawLine(r.getX() - 5, r.getCenterY(), r.getX() - r.getWidth() / 2 - dy, r.getCenterY());

	}
}
