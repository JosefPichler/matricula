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

public class SelectionTool extends EditTool {

	private static final Color dimColor = new Color(40, 40, 40, 40);
	private final int startX;
	private final int startY;
	private int endX;
	private int endY;

	public SelectionTool(Component parentComponent, RegisterPage page, int startX, int startY) {
		super(parentComponent, page);
		this.startX = startX;
		this.startY = startY;
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

	@Override
	public void paint(ImageGraphics2D p) {
		if (endX == 0 && endY == 0) {
			return;
		}

		final int maxX = 7524;
		final int maxY = 5652;
		int x = Math.min(startX, endX);
		int y = Math.min(startY, endY);
		int width = Math.abs(endX - startX);
		int height = Math.abs(endY - startY);
		final Color oldColor = p.getColor();

		// 1. Dim background (left, right, top, bottom).
		p.setColor(dimColor);
		p.fillRect(0, 0, x, maxY);
		p.fillRect(x + width, 0, maxX, maxY);
		p.fillRect(x, 0, width, y);
		p.fillRect(x, y + height, width, maxY);

		// 2. Red rectangle.
		p.setColor(Color.red);
		p.drawRect(x, y, width, height);

		p.setColor(oldColor);

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
		// NOthing to do.

	}

	@Override
	public void fillPopupMenu(PopupMenu popupMenu) {
		super.fillPopupMenu(popupMenu);

		popupMenu.add("Mann");
		popupMenu.add("Frau");
		popupMenu.add("Adresse");
		popupMenu.addSeparator();
		popupMenu.add("Löschen");
		popupMenu.addActionListener(getActionListener());
	}

	protected ActionListener getActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if ("Mann".equals(e.getActionCommand())) {
					createPerson("Mann");
				} else if ("Frau".equals(e.getActionCommand())) {
					createPerson("Frau");
				} else if ("Adresse".equals(e.getActionCommand())) {
					final String address = JOptionPane.showInputDialog(getParentComponent(), "Adresse (Straße Nr):", null);
					if (address != null) {
						getPage().addAddress(address, getTagRegion());
						getPage().save();
					}
				}
			}
		};
	}

	private void createPerson(String title) {
		JTextField nameInputField = new JTextField();
		nameInputField.addAncestorListener(new RequestFocusListener());
		JTextField dateOfBirthInputField = new JTextField();
		JTextField placeOfBirthInputField = new JTextField();

		Object[] message = { "Nach- und Vorname:", nameInputField, "Geburtsdatum:", dateOfBirthInputField, "Geburtsort:",
				placeOfBirthInputField };

		int option = JOptionPane.showConfirmDialog(getParentComponent(), message, title, JOptionPane.OK_CANCEL_OPTION);

		if (option == JOptionPane.OK_OPTION && !nameInputField.getText().isEmpty()) {
			String name = nameInputField.getText();
			PersonTag newPerson = null;
			if ("Mann".equals(title)) {
				newPerson = getPage().addMale(name, getTagRegion());
			} else {
				newPerson = getPage().addFemale(name, getTagRegion());
			}
			if (!dateOfBirthInputField.getText().isEmpty()) {
				newPerson.setDateOfBirth(dateOfBirthInputField.getText());
			}
			if (!placeOfBirthInputField.getText().isEmpty()) {
				newPerson.setPlaceOfBirth(placeOfBirthInputField.getText());
			}
			getPage().save();
		}
	}

}
