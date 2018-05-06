package matricula.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import matricula.Community;
import matricula.maps.AddressGenerator;
import matricula.maps.MapGenerator;
import matricula.maps.MarriageMapGenerator;
import matricula.model.Matricula;
import matricula.model.Register;
import matricula.model.RegisterIndex;
import matricula.model.RegisterPage;
import matricula.model.RegisterYear;
import matricula.model.Tag;
import matricula.report.AddressReport;
import matricula.ui.page.RegisterPageView;

/**
 * This is the main program for collecting and browsing personal data (name,
 * date of birth, place of birth) and relations between person data from scanned
 * register pages.
 * <p>
 * The development of this software is driven by genealoge work based on the
 * <a href="http://www.data.matricula.info/php/main.php/">Matricula</a> project.
 * 
 * @author Josef Pichler
 */
public class MatriculaUI {

	private static boolean useIndex = false;

	public static void main(String[] args) {
		final String externalImagePathRoot = extractExternalImagePathRoot(args);
		final Matricula m = Matricula.loadRegisters(Community.Hollenstein.registerFileNames(), externalImagePathRoot);
		if (useIndex) {
			RegisterIndex index = Matricula.loadIndex("files-01-12/01-12 Taufbuch Index.csv");
			m.getRegisterList().get(0).setIndex(index);
		}

		MatriculaUI.setLookAndFeel();
		MatriculaUI ui = new MatriculaUI(m);
		ui.createFrameAndRun();
	}

	private static String extractExternalImagePathRoot(String[] args) {
		if (args.length == 1 && new File(args[0]).isDirectory()) {
			return args[0];
		}
		return null;
	}

	/**
	 * Installs the preferred Swing look and feel. It is important that this is done
	 * before any other Swing specific UI action.
	 */
	private static void setLookAndFeel() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// Ignore it; the interested user will notice that the Nimbus look
			// and feel is not used; other users does not care at all.
		}
	}

	/** The entry point to the data model explored and changed by this software. */
	private final Matricula model;
	/** The Swing frame for updating the title after selection change. */
	private JFrame frame;
	/** The left-placed tree showing register, register pages, and persons. */
	private JTree pageTreeView;
	/** The main view displaying scanned register images. */
	private RegisterPageView pageDetailView;
	/** The search field. */
	private JTextField searchField;

	/**
	 * Constructs new UI object and initializes the model.
	 * 
	 * @param model
	 *            the data model used by the software
	 */
	public MatriculaUI(Matricula model) {
		this.model = model;
	}

	/**
	 * Create Swing frame, open frame in Swing thread.
	 */
	private void createFrameAndRun() {
		frame = createJFrame();
		fillFrameContentPane(frame);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				model.printYearReport();
			}
		});

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				pageDetailView.setRegisterPage(model.getLastRegister().getFirstPage());
			}
		});
	}

	/**
	 * Fills Swing frame with left-placed tree view (and search field) and main view
	 * showing scanned register images.
	 * 
	 * @param frame
	 *            the Swing frame of this software program
	 */
	private void fillFrameContentPane(JFrame frame) {
		final boolean useByYearTree = false;
		TreeModel treeModel = null;
		if (useByYearTree) {
			treeModel = new MatriculaByYearTreeModel(model);
		} else {
			treeModel = new MatriculaTreeModelFiltered(model);
		}
		frame.getContentPane().setLayout(new BorderLayout());

		// Left tree and search field.
		pageTreeView = new JTree(treeModel);
		pageTreeView.setRootVisible(false);
		pageTreeView.getSelectionModel().addTreeSelectionListener(getTreeSelectionListener());
		JScrollPane pageTreeScrollPane = new JScrollPane(pageTreeView);
		searchField = new JTextField();
		searchField.getDocument().addDocumentListener(createDocumentListener((ITreeModelFilter) treeModel));

		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add("North", searchField);
		leftPanel.add("Center", pageTreeScrollPane);

		pageTreeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		pageTreeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		// Main view.
		JPanel mainPanel = new JPanel(new BorderLayout());
		pageDetailView = new RegisterPageView();
		mainPanel.add("Center", pageDetailView);
		JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		fillToolbarPanel(toolbarPanel);
		mainPanel.add("North", toolbarPanel);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, mainPanel);
		splitPane.setDividerLocation(180);
		frame.getContentPane().add(splitPane);
	}

	private void fillToolbarPanel(JPanel toolbarPanel) {
		final JToggleButton imageFilter = new JToggleButton("Schwarz/Weiß");
		imageFilter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pageDetailView.setUseFilter(imageFilter.isSelected());
			}
		});
		toolbarPanel.add(imageFilter);

		final JButton generateAddresses = new JButton("Generiere Hausberichte");
		generateAddresses.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AddressReport.main(null);
			}
		});
		toolbarPanel.add(generateAddresses);

		final JButton generateMap = new JButton("Generiere Google Maps");
		generateMap.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				class GenerateMapTask extends SwingWorker<Object, Object> {

					@Override
					public Object doInBackground() {
						AddressGenerator.main(new String[0]);
						MapGenerator.main(new String[0]);
						MarriageMapGenerator.main(new String[0]);
						return null;
					}

				}

				new GenerateMapTask().execute();

			}
		});
		toolbarPanel.add(generateMap);

	}

	/**
	 * Listen to search field's document and forward search string to provided tree
	 * model. Additionally, handle collapse/expand tree nodes according to search
	 * string.
	 * 
	 * @see MatriculaTreeModelFiltered#setFilter(String)
	 * @param treeModel
	 *            the filter-aware tree model
	 * @return a new created document listener with the described behavior
	 */
	private DocumentListener createDocumentListener(final ITreeModelFilter treeModel) {
		return new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				String filter = searchField.getText();
				if (filter == null || filter.isEmpty()) {
					treeModel.setFilter(null);
				} else {
					treeModel.setFilter(filter);
					for (int i = 0; i < pageTreeView.getRowCount(); i++) {
						pageTreeView.expandRow(i);
					}
				}

			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String filter = searchField.getText();
				if (filter == null || filter.isEmpty()) {
					treeModel.setFilter(null);
				} else {
					treeModel.setFilter(filter);
					for (int i = 0; i < pageTreeView.getRowCount(); i++) {
						pageTreeView.expandRow(i);
					}
				}

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// Nothing to do.
			}
		};
	}

	/**
	 * Listen to selection in the tree view and open main view with selected
	 * register page. Also update title of the main frame (after image is completely
	 * loaded).
	 * 
	 * @return a new listener object implementing the described behavior
	 */
	private TreeSelectionListener getTreeSelectionListener() {
		return new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				Object selectedNode = pageTreeView.getLastSelectedPathComponent();
				if (selectedNode == null) {
					return;
				}
				if (selectedNode instanceof Matricula) {
					return;
				}
				if (selectedNode instanceof Register) {
					Register register = (Register)selectedNode;
					selectedNode = register.getFirstPage();
				}
				if (selectedNode instanceof RegisterYear) {
					return;
				}
				if (selectedNode instanceof Tag) {
					TreePath path = pageTreeView.getSelectionPath();
					selectedNode = path.getPathComponent(path.getPathCount() - 2);
				}

				final RegisterPage selectedPage = (RegisterPage) selectedNode;
				class OpenRegisterPageTask extends SwingWorker<Object, Object> {

					@Override
					public Object doInBackground() {
						pageDetailView.setRegisterPage(selectedPage);
						return null;
					}

					@Override
					protected void done() {
						frame.setTitle(createTitle(selectedPage));
						pageDetailView.repaint();
					}

				}

				new OpenRegisterPageTask().execute();
			}

		};
	}

	/**
	 * Create a new Swing frame with an appropriate icon in full screen size (on
	 * notebook) or with half of screen dimensions.
	 * 
	 * @return the Swing frame of this software program
	 */
	protected JFrame createJFrame() {
		JFrame result = new JFrame(createTitle(model.getLastRegister().getFirstPage()));

		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		result.setIconImage(kit.getImage(getClass().getResource("icon.png")));

		// Per default:
		result.setSize(screenWidth / 2, screenHeight / 2);

		// This is my notebook; I like it full screen:
		if (screenHeight < 1000) {
			result.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
		// This is my iMac, put it to the right:
		else if (screenWidth > 2500) {
			result.setLocation(screenWidth * 1 / 4, 0);
			result.setSize(screenWidth * 3 / 4, screenHeight);
		}

		result.setLocationByPlatform(false);
		return result;
	}

	/**
	 * Constructs a string that is used as Swing frame title.
	 * 
	 * @param page
	 *            the actual register page
	 * @return a string to be used as frame title
	 */
	private String createTitle(RegisterPage page) {
		final Register register = page.getRegister();
		final String result = String.format("Matricula - %s (%s) Seite %s", register.getTitle(), register.getTimePeriod(),
				page.getPageNumber());
		return result;
	}

}
