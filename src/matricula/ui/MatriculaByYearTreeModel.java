package matricula.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import matricula.model.Matricula;
import matricula.model.Register;
import matricula.model.RegisterPage;
import matricula.model.RegisterYear;

public class MatriculaByYearTreeModel implements TreeModel, ITreeModelFilter {

	private final Matricula model;
	private final List<TreeModelListener> listenerList;
	private List<RegisterYear> yearList;

	MatriculaByYearTreeModel(Matricula model) {
		this.model = model;
		this.listenerList = new ArrayList<TreeModelListener>();
	}

	@Override
	public Object getRoot() {
		return model;
	}

	@Override
	public Object getChild(Object parent, int index) {
		System.out.println("getchild");
		if (parent == model) {
			return yearList().get(index);
		} else if (parent instanceof RegisterYear) {
			return ((RegisterYear) parent).getRegisterPageList().get(index);
		}
		return null;
	}

	private List<RegisterYear> yearList() {
		if (yearList == null) {
			Map<Integer, RegisterYear> cache = new HashMap<Integer, RegisterYear>();
			for (Register r : model.getRegisterList()) {
				for (RegisterPage p : r.getPageList()) {
					int firstYearOfPage = p.firstYear();
					System.out.println(firstYearOfPage);

					if (cache.containsKey(firstYearOfPage)) {
						cache.get(firstYearOfPage).addRegisterPage(p);
					} else {
						RegisterYear year = new RegisterYear(firstYearOfPage);
						cache.put(firstYearOfPage, year);
						year.addRegisterPage(p);
					}
				}
			}

			List<Integer> yearNumberList = new ArrayList<Integer>(cache.keySet());
			Collections.sort(yearNumberList);

			yearList = new ArrayList<RegisterYear>();
			for (Integer i : yearNumberList) {
				yearList.add(cache.get(i));
			}

		}
		return yearList;
	}

	@Override
	public int getChildCount(Object parent) {
		System.out.println("getChildCount");
		if (parent == model) {
			return yearList().size();
		} else if (parent instanceof RegisterYear) {
			return ((RegisterYear) parent).getRegisterPageList().size();
		}
		return 0;
	}

	@Override
	public boolean isLeaf(Object node) {
		if (node == model) {
			return yearList().isEmpty();
		} else if (node instanceof RegisterYear) {
			return ((RegisterYear) node).getRegisterPageList().isEmpty();
		}
		return true;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		System.out.println("getIndexOfChild");
		if (parent == model) {
			return yearList().indexOf(child);
		} else if (parent instanceof RegisterYear) {
			return ((RegisterYear) parent).getRegisterPageList().indexOf(child);
		}
		return 0;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		listenerList.add(l);

	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		listenerList.remove(l);

	}

	@Override
	public void setFilter(String filterString) {
		// TODO Auto-generated method stub

	}

}
