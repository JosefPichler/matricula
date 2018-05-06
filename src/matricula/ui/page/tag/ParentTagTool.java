package matricula.ui.page.tag;

import matricula.model.ParentTag;
import matricula.model.TagRegion;
import matricula.ui.page.ImageGraphics2D;

public class ParentTagTool extends TagTool {

	private final ParentTag model;

	public ParentTagTool(ParentTag model) {
		super(model);
		this.model = model;
	}

	@Override
	public void paint(ImageGraphics2D p) {

		// Child
		TagRegion r = model.getChild().getRegion();

		int x = r.getX() + r.getWidth() + 20;
		int y = r.getCenterY();

		p.drawLine(x, y, x + 50, y);
	}

}
