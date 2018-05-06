package matricula.model;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Register {

	private final String gemeinde;
	private final String buch;
	private final String laufzeit;
	private final List<RegisterPage> page;
	private String registerFileName;
	private Matricula root;
	private RegisterIndex index;
	private final String externalImagePathRoot;

	public Register(String gemeinde, String buch, String laufzeit, String externalImagePathRoot) {
		this.gemeinde = gemeinde;
		this.buch = buch;
		this.laufzeit = laufzeit;
		this.externalImagePathRoot = externalImagePathRoot;
		this.page = new ArrayList<RegisterPage>();
	}

	@Override
	public String toString() {
		return buch + " (" + laufzeit + ")";
	}

	public void addPage(RegisterPage page) {
		page.setRegister(this);
		this.page.add(page);
	}

	public RegisterPage getFirstPage() {
		if (page.isEmpty()) {
			return null;
		}
		return page.get(0);
	}

	public String getTitle() {
		return buch;
	}

	public String getTimePeriod() {
		return laufzeit;
	}

	public String getCommunity() {
		return gemeinde;
	}

	public void saveAll() {
		try (PrintStream out = new PrintStream(registerFileName)) {
			out.print("register;");
			out.print(gemeinde);
			out.print(";");
			out.print(buch);
			out.print(";");
			out.print(laufzeit);
			out.print(";");
			out.println();
			for (RegisterPage e : page) {
				e.save(out);
			}
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Matricula getMatricula() {
		return root;
	}

	public void setMatricula(Matricula root) {
		this.root = root;
	}

	public List<RegisterPage> getPageList() {
		return page;
	}

	public void setRegisterFileName(String registerFileName) {
		this.registerFileName = registerFileName;
	}

	public RegisterIndex getRegisterIndex() {
		return index;
	}

	public void setIndex(RegisterIndex index) {
		this.index = index;
	}

	public String getExternalImagePathRoot() {
		return externalImagePathRoot;
	}

}
