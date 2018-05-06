package matricula.ui.page;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import matricula.model.RegisterIndex;
import matricula.model.RegisterIndexEntry;
import matricula.model.RegisterPage;

public class RegisterPageIndexView {

	private final RegisterIndex model;
	private final RegisterPage page;

	public RegisterPageIndexView(RegisterPage page) {
		this.page = page;
		this.model = page.getRegister().getRegisterIndex();
	}

	public void paint(Graphics g, int width, int height) {
		final int w = 160;
		final int gap = 20;
		g.setColor(Color.WHITE);
		g.fill3DRect(width - w - gap, gap, 160, 160, true);
		g.setColor(Color.DARK_GRAY);
		int y = 40;

		List<RegisterIndexEntry> entriesOfPage = model.getEntriesOfPage(page);
		for (RegisterIndexEntry e : entriesOfPage) {
			String str = e.toString();
			g.drawString(str, width - w - (gap / 2), y);
			y = y + 20;
		}

	}

}
