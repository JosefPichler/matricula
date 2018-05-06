package matricula.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import matricula.model.Matricula;
import matricula.model.PersonTag;
import matricula.model.Register;
import matricula.model.RegisterPage;
import matricula.model.Tag;

public class MatriculaTreeModelFiltered implements TreeModel, ITreeModelFilter {

	private final List<TreeModelListener> listenerList;

	private String filter;
	private final Matricula model;

	MatriculaTreeModelFiltered(Matricula model) {
		this.model = model;
		this.listenerList = new ArrayList<TreeModelListener>();
	}

	public void setFilter(String filter) {
		this.filter = filter;
		if (this.filter != null) {
			this.filter = filter.toLowerCase();
		}

		for (TreeModelListener l : listenerList) {
			TreeModelEvent e = new TreeModelEvent(this, new Object[] { model });
			l.treeStructureChanged(e);
		}

	}

	@Override
	public Object getRoot() {
		return model;
	}

	@Override
	public Object getChild(Object parent, int index) {
		if (parent == model) {
			return filterRegister(model.getRegisterList()).get(index);
		} else if (parent instanceof Register) {
			Register r = (Register) parent;
			return filterRegisterPage(r.getPageList()).get(index);
		} else if (parent instanceof RegisterPage) {
			RegisterPage page = (RegisterPage) parent;
			return filterTag(page.getPersonTagList()).get(index);
		}

		return null;
	}

	private boolean filterString(Tag tag) {
		// 1. Person name or address of address tag.
		String str = tag.toString();
		if (filterString(str)) {
			return true;
		}

		if (tag instanceof PersonTag) {
			PersonTag person = (PersonTag) tag;
			// 2. Address of person.
			if (person.getPlaceOfBirth() != null) {
				boolean result = filterString(person.getPlaceOfBirth());
				if (result) {
					return result;
				}
			}
			// 3. Date of birth (year only).
			String dob = person.getDateOfBirth();
			if (dob != null && dob.length() >= 5) {
				boolean result = dob.endsWith(filter);
				if (result) {
					return result;
				}
			}
		}

		return false;
	}

	private boolean filterString(String str) {
		if (str == null) {
			return true;
		}

		str = str.toLowerCase();
		return str.startsWith(filter);
	}

	private List<Tag> filterTag(List<Tag> tagList) {
		if (filter == null) {
			return tagList;
		}
		List<Tag> result = new ArrayList<Tag>();
		for (Tag e : tagList) {
			if (filterString(e)) {
				result.add(e);
			}
		}

		return result;
	}

	private List<RegisterPage> filterRegisterPage(List<RegisterPage> pageList) {
		if (filter == null) {
			return pageList;
		}
		List<RegisterPage> result = new ArrayList<RegisterPage>();
		for (RegisterPage e : pageList) {
			List<Tag> tagList = filterTag(e.getPersonTagList());
			if (tagList.size() > 0) {
				result.add(e);
			}
		}
		return result;
	}

	private List<Register> filterRegister(List<Register> registerList) {
		if (filter == null) {
			return registerList;
		}
		List<Register> result = new ArrayList<Register>();
		for (Register e : registerList) {
			List<RegisterPage> pageList = filterRegisterPage(e.getPageList());
			if (pageList.size() > 0) {
				result.add(e);
			}
		}
		return result;
	}

	@Override
	public int getChildCount(Object parent) {
		if (parent == model) {
			return filterRegister(model.getRegisterList()).size();
		} else if (parent instanceof Register) {
			Register r = (Register) parent;
			return filterRegisterPage(r.getPageList()).size();
		} else if (parent instanceof RegisterPage) {
			RegisterPage page = (RegisterPage) parent;
			return filterTag(page.getPersonTagList()).size();
		}
		return 0;
	}

	@Override
	public boolean isLeaf(Object node) {
		if (node == model) {
			return false;
		} else if (node instanceof Register) {
			Register r = (Register) node;
			return r.getPageList().isEmpty();
		} else if (node instanceof RegisterPage) {
			RegisterPage page = (RegisterPage) node;
			return page.getPersonTagList().isEmpty();
		}
		return true;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		System.out.println("valueForPathChanged> " + path);
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		System.out.println("getIndexOfChild> " + child);
		if (parent == model) {
			return model.getRegisterList().indexOf(child);
		} else if (parent instanceof Register) {
			Register r = (Register) parent;
			return r.getPageList().indexOf(child);
		} else if (parent instanceof RegisterPage) {
			RegisterPage page = (RegisterPage) parent;
			return page.getPersonTagList().indexOf(child);
		}
		return 0;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		if (listenerList.contains(l)) {
			return;
		}
		listenerList.add(l);
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		listenerList.remove(l);
	}

}
