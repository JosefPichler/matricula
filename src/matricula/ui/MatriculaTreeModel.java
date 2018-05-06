package matricula.ui;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import matricula.model.Matricula;
import matricula.model.Register;
import matricula.model.RegisterPage;

public class MatriculaTreeModel implements TreeModel {

	private final Matricula model;

	MatriculaTreeModel(Matricula model) {
		this.model = model;
	}

	@Override
	public Object getRoot() {
		return model;
	}

	@Override
	public Object getChild(Object parent, int index) {
		if (parent == model) {
			return model.getRegisterList().get(index);
		} else if (parent instanceof Register) {
			Register r = (Register) parent;
			return r.getPageList().get(index);
		} else if (parent instanceof RegisterPage) {
			RegisterPage page = (RegisterPage) parent;
			return page.getTagList().get(index);
		}

		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		if (parent == model) {
			return model.getRegisterList().size();
		} else if (parent instanceof Register) {
			Register r = (Register) parent;
			return r.getPageList().size();
		} else if (parent instanceof RegisterPage) {
			RegisterPage page = (RegisterPage) parent;
			return page.getTagList().size();
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
			return page.getTagList().isEmpty();
		}
		return true;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		System.out.println("valueForPathChanged> " + path);
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		System.out.println("getIndexOfChild> "+child);
		if (parent == model) {
			return model.getRegisterList().indexOf(child);
		} else if (parent instanceof Register) {
			Register r = (Register) parent;
			return r.getPageList().indexOf(child);
		} else if (parent instanceof RegisterPage) {
			RegisterPage page = (RegisterPage)parent;
			return page.getTagList().indexOf(child);
		}
		return 0;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		System.out.println("addTreeModelListener> ");
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		System.out.println("removeTreeModelListener> ");
	}

}
