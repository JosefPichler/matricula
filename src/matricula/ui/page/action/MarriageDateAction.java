package matricula.ui.page.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import matricula.model.MarriageTag;
import matricula.model.RegisterPage;

public class MarriageDateAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private final MarriageTag tag;
	private final RegisterPage page;
	private final JPanel parent;

	public MarriageDateAction(JPanel parent, MarriageTag tag, RegisterPage page) {
		super(createTitle(tag.getDateOfMarriage(), "<Datum>"));
		this.tag = tag;
		this.page = page;
		this.parent = parent;
	}

	private static String createTitle(String value, String emptyTitle) {
		if (value == null) {
			return emptyTitle;
		}
		return value;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String newValue = JOptionPane.showInputDialog(parent, "Datum der Trauung ", tag.getDateOfMarriage());
		if (newValue != null) {
			if (newValue.length() == 0) {
				newValue = null;
			}
			tag.setDateOfMarriage(newValue);
			page.save();
			parent.repaint();
		}

	}

}
