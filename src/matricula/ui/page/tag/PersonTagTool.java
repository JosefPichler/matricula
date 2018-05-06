package matricula.ui.page.tag;

import java.awt.Color;
import java.awt.Font;

import matricula.model.PersonTag;
import matricula.model.TagRegion;
import matricula.ui.page.ImageGraphics2D;

public class PersonTagTool extends TagTool {

	private static String male = "\u2642";
	private static String female = "\u2640";
	private static String adr = "\u2302";

	private final PersonTag tag;

	public PersonTagTool(PersonTag tag) {
		super(tag);
		this.tag = tag;

	}

	private boolean hasWarnings() {
		return tag.getPlaceOfBirth() == null || tag.getDateOfBirth() == null;
	}

	@Override
	public void paint(ImageGraphics2D p) {
		TagRegion r = tag.getRegion();
		
		final Color oldColor = p.getColor();

		if (hasWarnings()) {
			super.paint(p);
			if (tag.isMalePerson()) {
				p.setColor(Color.blue);
			} else {
				p.setColor(Color.cyan);
			}

			// Draw decorators for sex, place of birth and date of birth.
			Font currentFont = p.getFont();
			float fontSize = 14f;
			Font newFont = currentFont.deriveFont(fontSize);
			p.setFont(newFont);
			String str = tag.isMalePerson() ? male : female;
			if (tag.getPlaceOfBirth() != null) {
				str += adr;
			}
			if (tag.getDateOfBirth() != null) {
				str += "*";
			}
			int x = r.getCenterX() - 50;
			int y = r.getY() + 20;
			p.drawString(str, x, y);
		} else {
			// Draw corners of rectangle.
			final int d = 30;
			p.setColor(Color.DARK_GRAY);
			p.drawLine(r.getX(), r.getY(), r.getX() + d, r.getY());
			p.drawLine(r.getX(), r.getY(), r.getX(), r.getY() + d);

			p.drawLine(r.getX() + r.getWidth() - d, r.getY(), r.getX() + r.getWidth(), r.getY());
			p.drawLine(r.getX() + r.getWidth(), r.getY(), r.getX() + r.getWidth(), r.getY() + d);

			p.drawLine(r.getX() + r.getWidth(), r.getY() + r.getHeight() - d, r.getX() + r.getWidth(), r.getY() + r.getHeight());
			p.drawLine(r.getX() + r.getWidth() - d, r.getY() + r.getHeight(), r.getX() + r.getWidth(), r.getY() + r.getHeight());

			p.drawLine(r.getX(), r.getY() + r.getHeight(), r.getX() + d, r.getY() + r.getHeight());
			p.drawLine(r.getX(), r.getY() + r.getHeight(), r.getX(), r.getY() + r.getHeight() - d);
		}
		p.setColor(oldColor);

	}

}
