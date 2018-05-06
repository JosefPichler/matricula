package matricula.ui.page;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public class RequestFocusListener implements AncestorListener {
	@Override
	public void ancestorAdded(final AncestorEvent e) {
		final AncestorListener al = this;
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				final JComponent component = e.getComponent();
				component.requestFocusInWindow();
				component.removeAncestorListener(al);
			}
		});
	}

	@Override
	public void ancestorMoved(final AncestorEvent e) {
	}

	@Override
	public void ancestorRemoved(final AncestorEvent e) {
	}
}
