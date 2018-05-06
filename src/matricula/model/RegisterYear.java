package matricula.model;

import java.util.ArrayList;
import java.util.List;

public class RegisterYear {

	private int year;
	private List<RegisterPage> pageList;

	public RegisterYear(int year) {
		this.year = year;
		this.pageList = new ArrayList<RegisterPage>();
	}

	public void addRegisterPage(RegisterPage p) {
		pageList.add(p);
	}

	public List<RegisterPage> getRegisterPageList() {
		return pageList;
	}

	@Override
	public String toString() {
		return Integer.toString(year);
	}

}
