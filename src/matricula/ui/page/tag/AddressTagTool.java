package matricula.ui.page.tag;

import java.awt.Color;
import java.awt.Font;

import matricula.model.AddressTag;
import matricula.model.TagRegion;
import matricula.ui.page.ImageGraphics2D;

public class AddressTagTool extends TagTool {

	private static String adr = "\u2302";

	private final AddressTag tag;

	public AddressTagTool(AddressTag tag) {
		super(tag);
		this.tag = tag;

	}

	@Override
	public void paint(ImageGraphics2D p) {
		super.paint(p);

		TagRegion r = tag.getRegion();

		final Color oldColor = p.getColor();
		p.setColor(Color.black);
		Font currentFont = p.getFont();
		float fontSize = 20f;
		if (p.getScale() < 0.5) {
			fontSize = 12f;
		}
		Font newFont = currentFont.deriveFont(fontSize);
		p.setFont(newFont);

		p.drawString(adr, r.getX() + r.getWidth() - 8, r.getY() + (int) (0.3 * r.getHeight()));

		p.setColor(oldColor);

	}

}
