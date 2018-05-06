package matricula.ui.page;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import matricula.image.OtsuBinarize;
import matricula.model.AddressTag;
import matricula.model.MarriageTag;
import matricula.model.PersonTag;
import matricula.model.RegisterPage;
import matricula.model.Tag;
import matricula.model.search.MatriculaSearch;
import matricula.ui.page.action.MarriageDateAction;
import matricula.ui.page.action.MarriagePlaceAction;
import matricula.ui.page.tag.TagTool;
import matricula.ui.page.tag.TagToolFactory;

/**
 * Implementation nodes:
 * https://docs.oracle.com/javase/tutorial/2d/images/loadimage.html
 * 
 * @author jpichler
 * 
 */
public class RegisterPageView extends JPanel {

	private static final long serialVersionUID = 1L;
	private RegisterPage page;
	private BufferedImage image;
	private int x;
	private int y;
	private double scale;
	private int dragStartX;
	private int dragStartY;
	private int oldX;
	private int oldY;
	private EditTool selection;
	private PopupMenu finishActionPopup;
	private JPopupMenu contextMenu;
	private RegisterPageIndexView pageIndexView;

	public RegisterPageView() {
		this.x = 0;
		this.y = 0;
		this.scale = 0.2;
		this.setFocusable(true);
		this.addMouseListener(getMouseListener());
		this.addMouseWheelListener(getMouseWhellListener());
		this.addMouseMotionListener(getMouseMotionListener());
		this.contextMenu = new JPopupMenu();
		this.addMouseListener(getContextMenuListener());
		// Must set a dummy text here; otherwise no tool tip appears.
		this.setToolTipText("dummy");
	}

	private MouseListener getContextMenuListener() {
		return new MouseAdapter() {
			public void mousePressed(MouseEvent ev) {
				if (ev.isPopupTrigger()) {
					if (fillMenu(ev)) {
						contextMenu.show(ev.getComponent(), ev.getX(), ev.getY());
					}
				}
			}

			private boolean fillMenu(MouseEvent ev) {
				Tag tag = page.findTagAt(toImagePixel(x, ev.getX()), toImagePixel(y, ev.getY()));
				if (tag instanceof PersonTag) {
					PersonTag p = (PersonTag) tag;
					contextMenu.removeAll();
					// contextMenu.add(p.getFullName());
					contextMenu.add(new JMenuItem(createPersonNameAction(p)));
					contextMenu.addSeparator();
					contextMenu.add(new JMenuItem(createDateOfBirthAction(p)));
					contextMenu.add(new JMenuItem(createPlaceOfBirthAction(p)));
					contextMenu.add(new JMenuItem(createJobAction(p)));
					contextMenu.add(new JMenuItem(createAgeAction(p)));
					contextMenu.addSeparator();
					contextMenu.add(new JMenuItem(createPersonSearchAction(p)));
					return true;
				} else if (tag instanceof MarriageTag) {
					MarriageTag m = (MarriageTag) tag;
					contextMenu.removeAll();
					contextMenu.add(new JMenuItem(new MarriageDateAction(RegisterPageView.this, m, page)));
					contextMenu.add(new JMenuItem(new MarriagePlaceAction(RegisterPageView.this, m, page)));
					return true;
				}
				return false;
			}

			protected Action createPersonNameAction(final PersonTag p) {
				String title = p.getFullName();

				return new AbstractAction(title) {
					private static final long serialVersionUID = 1L;

					@Override
					public void actionPerformed(ActionEvent e) {
						String newFullName = JOptionPane.showInputDialog(RegisterPageView.this, "Name des " + p.getFullName(),
								p.getFullName());
						if (newFullName != null) {
							if (newFullName.length() == 0) {
								newFullName = null;
							}
							p.setFullName(newFullName);
							page.save();
							repaint();
						}
					}

				};
			}

			protected Action createDateOfBirthAction(final PersonTag p) {
				String title = "<geb. am>";
				if (p.getDateOfBirth() != null) {
					title = "* " + p.getDateOfBirth();
				}

				return new AbstractAction(title) {
					private static final long serialVersionUID = 1L;

					@Override
					public void actionPerformed(ActionEvent e) {
						String dob = JOptionPane.showInputDialog(RegisterPageView.this, "Geburtsdatum des " + p.getFullName(),
								p.getDateOfBirth());
						if (dob != null) {
							if (dob.length() == 0) {
								dob = null;
							}
							p.setDateOfBirth(dob);
							page.save();
							repaint();
						}
					}

				};
			}

			protected Action createPlaceOfBirthAction(final PersonTag p) {
				String title = "<geb. in>";
				if (p.getPlaceOfBirth() != null) {
					title = "* " + p.getPlaceOfBirth();
				}

				return new AbstractAction(title) {
					private static final long serialVersionUID = 1L;

					@Override
					public void actionPerformed(ActionEvent e) {
						String pob = JOptionPane.showInputDialog(RegisterPageView.this, "Geburtsort des " + p.getFullName(),
								p.getPlaceOfBirth());
						if (pob != null) {
							if (pob.length() == 0) {
								pob = null;
							}
							p.setPlaceOfBirth(pob);
							page.save();
							repaint();
						}
					}

				};
			}

			protected Action createJobAction(final PersonTag p) {
				String title = "<Beruf>";
				if (p.getJob() != null) {
					title = p.getJob();
				}

				return new AbstractAction(title) {
					private static final long serialVersionUID = 1L;

					@Override
					public void actionPerformed(ActionEvent e) {
						String dob = JOptionPane.showInputDialog(RegisterPageView.this, "Beruf des/der " + p.getFullName(), p.getJob());
						if (dob != null) {
							if (dob.length() == 0) {
								dob = null;
							}
							p.setJob(dob);
							page.save();
							repaint();
						}
					}

				};
			}

			protected Action createAgeAction(final PersonTag p) {
				String title = "<Alter>";
				if (p.getAge() != null) {
					title = "Alter: " + p.getAge();
				}

				return new AbstractAction(title) {
					private static final long serialVersionUID = 1L;

					@Override
					public void actionPerformed(ActionEvent e) {
						String dob = JOptionPane.showInputDialog(RegisterPageView.this, "Alter " + p.getFullName(), p.getAge());
						if (dob != null) {
							if (dob.length() == 0) {
								dob = null;
							}
							p.setAge(dob);
							page.save();
							repaint();
						}
					}

				};
			}

			public void mouseReleased(MouseEvent ev) {
				if (ev.isPopupTrigger()) {
					if (fillMenu(ev)) {
						contextMenu.show(ev.getComponent(), ev.getX(), ev.getY());
					}
				}
			}

		};

	}

	protected Action createPersonSearchAction(final PersonTag p) {
		return new AbstractAction("Search") {

			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				StringBuilder tooltip = new StringBuilder();
				MatriculaSearch search = new MatriculaSearch(page.getRegister().getMatricula());
				if (p.getDateOfBirth() != null) {
					Object result = search.searchForDate(p.getDateOfBirth());
					tooltip.append(p.getDateOfBirth() + ":\n");
					tooltip.append(result);
					tooltip.append("\n");
				}

				if (p.getFullName() != null) {
					Object result = search.searchFullname(p.getFullName());
					tooltip.append(p.getFullName() + ":\n");
					tooltip.append(result);
					tooltip.append("\n");
				}
				if (p.getPlaceOfBirth() != null) {
					Object result = search.searchPlaceOfBirth(p.getPlaceOfBirth());
					tooltip.append(p.getPlaceOfBirth() + ":\n");
					tooltip.append(result);
					tooltip.append("\n");
				}
				JOptionPane.showMessageDialog(RegisterPageView.this, tooltip.toString());
			}

		};
	}

	public void setRegisterPage(RegisterPage page) {
		if (this.page != null) {
			this.page = null;
			System.gc();
		}

		this.page = page;
		if (page.getRegister().getRegisterIndex() != null) {
			this.pageIndexView = new RegisterPageIndexView(page);
		} else {
			this.pageIndexView = null;
		}
		this.image = loadBufferedImage(page);

		
		// Book cover is always centered.
		if (page == page.getRegister().getFirstPage()) {
			this.scale = scaleImageToView(image);
			centerImage(true);
			return;
		}
		
		boolean scaleImageToView = true;
		if (scaleImageToView) {
			this.scale = scaleImageToView(image);
			centerImage(true);
			return;
		}
		
		// For reading 01-13 we start with -140/-200
		// For reading 01-12 we start with -100/-200 and scale = 0.35;
		// For full register page start with -100/0, scale = 0.3
		// Settings for 02-09: x = -130, y= 0, scale = 0.45
		if (page.getRegister().getTitle().contains("02/09")) {
			x = -130;
			y = 0;
			scale = 0.45;
			centerImage(false);
			return;
		}
		if (page.getRegister().getTitle().contains("01/01")) {
			System.out.println("01/01");
			scale = 0.35;
			centerImage(true);
			return;
		}
		if (page.getRegister().getTitle().contains("01/02")) {
			System.out.println("01/01");
			scale = 0.35;
			centerImage(true);
			return;
		}
		this.x = -30;
		this.y = 0;
		this.scale = 0.8;
		this.centerImage(false);
	}

	private double scaleImageToView(BufferedImage img) {
		double scaleX = (double)getWidth() / (double)img.getWidth();
		double scaleY = (double)getHeight() / (double)img.getHeight();
		return 0.98*Math.min(scaleX, scaleY);
	}

	private MouseListener getMouseListener() {
		return new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (!SwingUtilities.isLeftMouseButton(e)) {
					return;
				}
				// Reset mouse interaction
				if (finishActionPopup != null) {
					remove(finishActionPopup);
					finishActionPopup = null;
					selection = null;
					repaint();
				}

				// Init drag data.
				dragStartX = e.getX();
				dragStartY = e.getY();
				oldX = x;
				oldY = y;

				// Start selection.
				if (!e.isControlDown()) {
					// Avoid selection starting within tags.
					final Tag tag = page.findTagAt(toImagePixel(x, e.getX()), toImagePixel(y, e.getY()));
					if (tag == null) {
						selection = new SelectionTool(RegisterPageView.this, page, toImagePixel(x, e.getX()), toImagePixel(y, e.getY()));
					} else {
						selection = new ConnectionTool(RegisterPageView.this, page, tag);
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (!SwingUtilities.isLeftMouseButton(e)) {
					return;
				}
				if (selection != null && !selection.isEmpty() && !e.isControlDown()) {
					finishActionPopup = new PopupMenu();
					selection.fillPopupMenu(finishActionPopup);
					if (finishActionPopup.getItemCount() > 0) {
						finishActionPopup.addActionListener(getActionListener());
						add(finishActionPopup);
						finishActionPopup.show(RegisterPageView.this, e.getX(), e.getY());
					} else {
						finishActionPopup = null;
						selection = null;
					}
				} else if (finishActionPopup != null) {
					remove(finishActionPopup);
					finishActionPopup = null;
					selection = null;
				} else if (e.getX() == dragStartX && e.getY() == dragStartY) {
					selection = null;
				}
				repaint();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		};
	}

	protected int toImagePixel(int offset, int a) {
		int d = a - offset;
		return (int) (d / scale);
	}

	private MouseMotionListener getMouseMotionListener() {
		return new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				// Move image.
				if (e.isControlDown()) {
					int dx = e.getX() - dragStartX;
					int dy = e.getY() - dragStartY;
					x = oldX + dx;
					y = oldY + dy;

					if (x > 0) {
						x = 0;
					}
					if (y > 0) {
						y = 0;
					}
					if (x + scale * getImageWidth() < getWidth()) {
						x = getWidth() - (int) (scale * getImageWidth());
					}
					if (y + scale * getImageHeight() < getHeight()) {
						y = getHeight() - (int) (scale * getImageHeight());
					}

					repaint();
					return;
				}

				// Draw selected region.
				if (selection != null) {
					final Tag tag = page.findTagAt(toImagePixel(x, e.getX()), toImagePixel(y, e.getY()));
					selection.updateTo(tag);
					selection.updateTo(toImagePixel(x, e.getX()), toImagePixel(y, e.getY()));

					repaint();
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {

			}
		};
	}

	protected double getImageHeight() {
		return image.getHeight();
	}

	protected double getImageWidth() {
		return image.getWidth();
	}

	private MouseWheelListener getMouseWhellListener() {
		return new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {

				if (e.isControlDown()) {
					// Zoom
					if (e.getWheelRotation() < 0) {
						scale += 0.05;
						if (scale > 1.0) {
							scale = 1.0;
						}
					} else {
						scale -= 0.05;
						if (scale < 0.2) {
							scale = 0.2;
						}
					}
					centerImage(false);
					repaint();
				} else {
					// Move vertically.
					final int deltaY = 100;
					if (e.getWheelRotation() < 0) {
						y -= deltaY;
						// getHeight() - (int) (scale * getImageHeight())
						if (y + (int) (scale * getImageHeight()) < getHeight()) {
							y = getHeight() - (int) (scale * getImageHeight());
						}
					} else {
						y += deltaY;
						if (y > 0) {
							y = 0;
						}
					}
					repaint();
				}
			}
		};
	}

	private void centerImage(boolean centerIfImageLarger) {
		final int imageWidthInPixel = (int) (scale * getImageWidth());
		if (imageWidthInPixel < getWidth()) {
			x = (getWidth() - imageWidthInPixel) / 2;
		} else if (centerIfImageLarger) {
			x = (getWidth() - imageWidthInPixel) / 2;
		}
		final int imageHeightInPixel = (int) (scale * getImageHeight());
		if (imageHeightInPixel < getHeight()) {
			y = (getHeight() - imageHeightInPixel) / 2;
		} else if (centerIfImageLarger) {
			y = (getHeight() - imageHeightInPixel) / 2;
		}
	}

	protected ActionListener getActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				selection = null;
				repaint();
			}
		};
	}

	private BufferedImage loadBufferedImage(RegisterPage page) {

		File imageFile = new File(page.getImageURL());
		if (!imageFile.exists() && page.getRegister().getExternalImagePathRoot() != null) {
			System.out.println("No local file; try external ...");
			File externalImagePathRoot = new File(page.getRegister().getExternalImagePathRoot());
			imageFile = new File(externalImagePathRoot, page.getImageURL());
		}

		BufferedImage result = null;
		try {
			result = ImageIO.read(imageFile);
		} catch (IOException e) {
			System.out.println("Cannot read file " + imageFile);
			e.printStackTrace();
		}
		return result;
	}

	public void paint(Graphics g) {
		if (page == null) {
			g.drawString("Keine Seite ausgewählt", 100, 100);
			return;
		}
		Graphics2D g2 = (Graphics2D) g;

		g.setColor(Color.gray);
		g.fillRect(0, 0, getWidth(), getHeight());

		// 1. Image
		AffineTransform op = AffineTransform.getScaleInstance(scale, scale);
		g2.drawImage(image, new AffineTransformOp(op, null), x, y);

		ImageGraphics2D p = new ImageGraphics2D(x, y, scale, g2);
		// 2. Tags
		final TagToolFactory factory = new TagToolFactory();
		for (Tag e : this.page.getTagList()) {
			TagTool tool = factory.create(e);
			tool.paint(p);
		}

		// 3. Selection
		if (selection != null) {
			selection.paint(p);
		}

		// 4. Print index in right upper corner.
		if (pageIndexView != null) {
			pageIndexView.paint(g, getWidth(), getHeight());
		}

	}

	@Override
	public String getToolTipText(MouseEvent e) {
		final Tag tag = page.findTagAt(toImagePixel(x, e.getX()), toImagePixel(y, e.getY()));
		if (tag != null) {
			if (tag instanceof PersonTag) {
				PersonToolTipBuilder b = new PersonToolTipBuilder(page.getTagList());
				return b.toString((PersonTag) tag);
			}
			if (tag instanceof AddressTag) {
				AddressToolTipBuilder b = new AddressToolTipBuilder(page);
				return b.toString((AddressTag) tag);
			}
			return tag.getTitle();
		}
		return null;
	}

	public void setUseFilter(boolean useFilter) {
		if (image != null) {
			if (useFilter) {
				BufferedImage grayscale = OtsuBinarize.toGray(image);
				image = OtsuBinarize.binarize(grayscale);
			} else {
				image = loadBufferedImage(page);
			}
			repaint();
		}
	}
}
