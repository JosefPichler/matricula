package matricula.ui.page;

import java.awt.Component;
import java.awt.PopupMenu;

import matricula.model.RegisterPage;
import matricula.model.Tag;
import matricula.model.TagRegion;

public abstract class EditTool {

	private final Component parentComponent;
	private final RegisterPage page;

	public EditTool(Component parentComponent, RegisterPage page) {
		this.parentComponent = parentComponent;
		this.page = page;
	}

	protected Component getParentComponent() {
		return parentComponent;
	}

	protected RegisterPage getPage() {
		return page;
	}

	public abstract boolean isEmpty();

	public abstract TagRegion getTagRegion();

	public abstract void updateTo(int x, int y);

	public abstract void updateTo(Tag tag);

	public abstract void paint(ImageGraphics2D p);

	public void fillPopupMenu(PopupMenu popupMenu) {
		// Empty hook.
	}

}
