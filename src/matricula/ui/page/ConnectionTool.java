package matricula.ui.page;

import java.awt.Color;
import java.awt.Component;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import matricula.model.PersonTag;
import matricula.model.RegisterPage;
import matricula.model.Tag;
import matricula.model.TagRegion;

public class ConnectionTool extends EditTool {

	private static final Color dimColor = new Color(40, 40, 40, 40);

	private int endX;
	private int endY;
	private final Tag startTag;
	private Tag endTag;

	public ConnectionTool(Component parent, RegisterPage page, Tag startTag) {
		super(parent, page);
		this.startTag = startTag;
	}

	public void updateTo(int x, int y) {
		this.endX = x;
		this.endY = y;
	}

	public void paint(ImageGraphics2D p) {
		final Color oldColor = p.getColor();

		final TagRegion r = startTag.getRegion();
		p.setColor(dimColor);
		p.fillRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());

		if (endTag != null) {
			final TagRegion r2 = endTag.getRegion();
			p.fillRect(r2.getX(), r2.getY(), r2.getWidth(), r2.getHeight());
		}

		p.setColor(Color.red);
		if (endTag != null) {
			final TagRegion r2 = endTag.getRegion();
			p.drawLine(r.getCenterX(), r.getCenterY(), r2.getCenterX(), r2.getCenterY());
		} else {
			p.drawLine(r.getCenterX(), r.getCenterY(), endX, endY);
		}

		p.setColor(oldColor);
	}

	@Override
	public void updateTo(Tag tag) {
		// The start tag is not a valid end tag.
		if (tag == startTag) {
			tag = null;
		}
		endTag = tag;
	}

	public boolean isEmpty() {
		return false;
	}

	public TagRegion getTagRegion() {
		return startTag.getRegion();
	}

	@Override
	public void fillPopupMenu(PopupMenu popupMenu) {
		final boolean twoPersons = (startTag instanceof PersonTag) && (endTag instanceof PersonTag);
		if (!twoPersons) {
			return;
		}

		final PersonTag first = (PersonTag) startTag;
		final PersonTag second = (PersonTag) endTag;
		final boolean sameSex = first.isMalePerson() == second.isMalePerson();

		// father oder mother
		if (second.isMalePerson()) {
			popupMenu.add("Vater");
		} else {
			popupMenu.add("Mutter");
		}

		if (!sameSex) {
			popupMenu.add("Trauung");
		}

		popupMenu.addActionListener(getMarriageAction());
	}

	private ActionListener getMarriageAction() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if ("Trauung".equals(e.getActionCommand())) {

					JTextField dateField = new JTextField();
					dateField.addAncestorListener(new RequestFocusListener());
					JTextField addressField = new JTextField();
					Object[] message = { "Datum:", dateField, "Ort:", addressField };

					int option = JOptionPane.showConfirmDialog(getParentComponent(), message, "Trauung", JOptionPane.OK_CANCEL_OPTION);
					if (option == JOptionPane.OK_OPTION && !dateField.getText().isEmpty()) {
						String dateOfMarriage = dateField.getText();
						String placeOfMarriage = addressField.getText();

						// Normaly, user connects bridegroom (start) with bride
						// (end).
						PersonTag bridegroom = (PersonTag) startTag;
						PersonTag bride = (PersonTag) endTag;
						if (bride.isMalePerson()) {
							bride = bridegroom;
							bridegroom = (PersonTag) endTag;
						}

						getPage().addMarriage(dateOfMarriage, placeOfMarriage, bridegroom, bride);
						getPage().save();
					}
				} else if ("Vater".equals(e.getActionCommand()) || "Mutter".equals(e.getActionCommand())) {
					PersonTag child = (PersonTag) startTag;
					PersonTag parent = (PersonTag) endTag;
					getPage().addParent(child, parent);
					getPage().save();
				}

			}

		};
	}

}
