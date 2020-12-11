package demos;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.SwingConstants;

public class MemeGenerator {

	private JFrame frame;
	private ImagePanel imagePanel;
	private JPanel controlPanel;
	private JTextField txtText;
	private JTextField txtColor;
	private JComboBox<String> cboFont;
	private JTextField txtSize;
	
	private JButton btnExport;
	private File exportedFile;
	
	private JLabel lblText, lblColor, lblFont, lblSize;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					MemeGenerator window = new MemeGenerator();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MemeGenerator() {
		initialize();
		frame.addComponentListener(memeResizeListener);
		txtText.addKeyListener(textChangedListener);
		txtColor.addKeyListener(colorChangedListener);
		cboFont.addActionListener(fontChangedListener);
		txtSize.addKeyListener(sizeChangedListener);
		
		btnExport.setTransferHandler(exportHandler);
		
		JLabel lblHowto = new JLabel("<html>\r\nHow to use:\r\n<br>1. Drag image onto the empty panel\r\n<br>2. Edit and position text\r\n<br>3. Drag result from \"Export (Drag)\"\r\n<br>4. Profit!\r\n</html>");
		lblHowto.setVerticalAlignment(SwingConstants.TOP);
		lblHowto.setBounds(420, 0, 334, 125);
		lblHowto.setFont(new Font(lblHowto.getFont().getName(), Font.PLAIN, 10));
		controlPanel.add(lblHowto);
		btnExport.addMouseMotionListener(exportMotionListener);
		
		toggleControls(false);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Meme Generator");

		imagePanel = new ImagePanel();
		imagePanel.setBounds(10, 11, 764, 428);
		frame.getContentPane().add(imagePanel);
		imagePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		controlPanel = new JPanel();
		controlPanel.setBounds(10, 450, 764, 100);
		frame.getContentPane().add(controlPanel);
		controlPanel.setLayout(null);

		lblText = new JLabel("Text:");
		lblText.setBounds(10, 14, 35, 14);
		controlPanel.add(lblText);

		txtText = new JTextField();
		txtText.setBounds(55, 11, 150, 20);
		controlPanel.add(txtText);
		txtText.setColumns(10);

		lblColor = new JLabel("Color:");
		lblColor.setBounds(10, 42, 35, 14);
		controlPanel.add(lblColor);

		txtColor = new JTextField();
		txtColor.setBounds(55, 39, 150, 20);
		controlPanel.add(txtColor);
		txtColor.setText("#FFFFFF");
		txtColor.setColumns(10);

		lblFont = new JLabel("Font:");
		lblFont.setBounds(10, 70, 35, 14);
		controlPanel.add(lblFont);

		cboFont = new JComboBox<String>();
		cboFont.setBounds(55, 67, 150, 20);
		initializeFonts(cboFont);
		controlPanel.add(cboFont);
		
		lblSize = new JLabel("Size:");
		lblSize.setBounds(215, 14, 35, 14);
		controlPanel.add(lblSize);
		
		txtSize = new JTextField();
		txtSize.setText("42");
		txtSize.setEnabled(false);
		txtSize.setColumns(10);
		txtSize.setBounds(260, 11, 150, 20);
		controlPanel.add(txtSize);
		
		btnExport = new JButton("Export (Drag)");
		btnExport.setBounds(215, 39, 194, 48);
		controlPanel.add(btnExport);
	}
	private void toggleControls(boolean enabled) {
		txtText.setEnabled(enabled);
		txtColor.setEnabled(enabled);
		cboFont.setEnabled(enabled);
		txtSize.setEnabled(enabled);
		btnExport.setEnabled(enabled);
	}

	private void initializeFonts(JComboBox<String> cbo) {
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		for (String f : fonts) cbo.addItem(f);
		cbo.setSelectedItem("Times New Roman");
	}

	private Timer resizeTimer = new Timer(100, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			imagePanel.redraw();
			txtSize.setText(String.valueOf(imagePanel.getFontSize()));
			((Timer) e.getSource()).stop();
		}
	});
	private ComponentListener memeResizeListener = new ComponentListener() {
		@Override
		public void componentShown(ComponentEvent e) {}
		@Override
		public void componentMoved(ComponentEvent e) {}
		@Override
		public void componentHidden(ComponentEvent e) {}
		@Override
		public void componentResized(ComponentEvent e) {
			int w = frame.getWidth(), h = frame.getHeight();
			imagePanel.setSize(w - 36, h - 172);
			controlPanel.setBounds(10, h - 150, w - 36, controlPanel.getHeight());

			if (resizeTimer.isRunning()) resizeTimer.restart();
			else resizeTimer.start();
		}
	};

	private KeyListener textChangedListener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) {
			String text = txtText.getText();
			if (e.getKeyChar() != 8) text += e.getKeyChar();
			Color color = Color.WHITE;
			try {
				color = Color.decode(txtColor.getText());
			} catch (NumberFormatException e1) {}
			int size = 42;
			try {
				size = Integer.parseInt(txtSize.getText());
			} catch (NumberFormatException e2) {}
			imagePanel.setText(text, color, (String) cboFont.getSelectedItem(), size);
		}
		@Override
		public void keyReleased(KeyEvent e) {}
		@Override
		public void keyPressed(KeyEvent e) {}
	};

	private KeyListener colorChangedListener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) {
			String text = txtColor.getText();
			if (e.getKeyChar() != 8) text += e.getKeyChar();
			Color color = Color.WHITE;
			try {
				color = Color.decode(text);
			} catch (NumberFormatException e1) {}
			int size = 42;
			try {
				size = Integer.parseInt(txtSize.getText());
			} catch (NumberFormatException e2) {}
			imagePanel.setText(txtText.getText(), color, (String) cboFont.getSelectedItem(), size);
		}
		@Override
		public void keyReleased(KeyEvent e) {}
		@Override
		public void keyPressed(KeyEvent e) {}
	};
	
	private KeyListener sizeChangedListener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) {
			String text = txtSize.getText();
			if (e.getKeyChar() != 8) text += e.getKeyChar();
			Color color = Color.WHITE;
			try {
				color = Color.decode(txtColor.getText());
			} catch (NumberFormatException e1) {}
			int size = 42;
			try {
				size = Integer.parseInt(text);
			} catch (NumberFormatException e2) {}
			imagePanel.setText(txtText.getText(), color, (String) cboFont.getSelectedItem(), size);
		}
		@Override
		public void keyReleased(KeyEvent e) {}
		@Override
		public void keyPressed(KeyEvent e) {}
	};

	private ActionListener fontChangedListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			Color color = Color.WHITE;
			try {
				color = Color.decode(txtColor.getText());
			} catch (NumberFormatException e1) {}
			int size = 42;
			try {
				size = Integer.parseInt(txtSize.getText());
			} catch (NumberFormatException e2) {}
			imagePanel.setText(txtText.getText(), color, (String) cboFont.getSelectedItem(), size);
		}
	};
	
	@SuppressWarnings("serial")
	private TransferHandler exportHandler = new TransferHandler() {
		
		@Override
		protected Transferable createTransferable(JComponent c) {
			List<File> files = new ArrayList<>();
			files.add(exportedFile);
			return new FileTransferable(files);
		}
		
		@Override
		public int getSourceActions(JComponent c) {
			return MOVE;
		}
	};
	
	class FileTransferable implements Transferable {
		private List<File> files;
		public FileTransferable(List<File> files) {
			this.files = files;
		}
		@Override
		public List<File> getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			if (!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
			return files;
		}
		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] {DataFlavor.javaFileListFlavor};
		}
		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return flavor.equals(DataFlavor.javaFileListFlavor);
		}
	}
	
	private MouseMotionListener exportMotionListener = new MouseMotionListener() {
		@Override
		public void mouseMoved(MouseEvent e) {}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			TransferHandler th  = btnExport.getTransferHandler();
			exportedFile = imagePanel.generateExportFile();
			th.setDragImage(imagePanel.getDragImage());
			th.exportAsDrag((JComponent) btnExport, e, DnDConstants.ACTION_MOVE);
		}
	};

	@SuppressWarnings("serial")
	class ImagePanel extends JPanel {
		private BufferedImage image = null;
		private BufferedImage original = null;
		private BufferedImage export = null;
		private int centerX = 0;

		private String text = null;
		private Color color = Color.WHITE;
		private Font font = new Font("Times New Roman", Font.BOLD, 42);
		private int textX = 100, textY = 100;
		
		private String fileName;
		private String fileExtension;
		
		public ImagePanel() {
			setDropTarget(dropTarget);
			addMouseListener(mouseListener);
		}
		
		public Image getDragImage() {
			if (export == null) return null;
			double height = export.getHeight() * 200 / (double) export.getWidth();
			return export.getScaledInstance(200, (int) height, Image.SCALE_FAST);
		}

		public void setText(String string, Color color, String font, int size) {
			text = string;
			this.color = color;
			this.font = new Font(font, Font.BOLD, size);
			repaint();
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			if (image != null) g.drawImage(image, centerX, 0, null);
			if (text != null) {
				g.setFont(font);
				g.setColor(color);
				g.drawChars(text.toCharArray(), 0, text.length(), textX, textY);
			}
		}

		public int getFontSize() {
			return font.getSize();
		}
		
		public void exportFile() {
			generateExport();
			
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Current Image Format (." + fileExtension + ")", fileExtension);
			fc.addChoosableFileFilter(filter);
			fc.setAcceptAllFileFilterUsed(false);
			fc.setMultiSelectionEnabled(false);
			fc.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));

			int result = fc.showSaveDialog(frame);
			if (result == JFileChooser.APPROVE_OPTION) {
				File f;
				if (fc.getSelectedFile().getAbsolutePath().endsWith(fileExtension)) f = fc.getSelectedFile();
				else f = new File(fc.getSelectedFile().getAbsolutePath() + "." + fileExtension);
				try {
					ImageIO.write(export, fileExtension, f);
				} catch (IOException e) {e.printStackTrace();}
			}
		}
		
		private Timer exportFileCleaner = new Timer(10000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File file = new File(System.getProperty("user.home") + "/AppData/Local/Temp/Meme - " + fileName + "." + fileExtension);
				file.delete();
				((Timer) e.getSource()).stop();
			}
		});
		public File generateExportFile() {
			generateExport();
			if (export == null) return null;
			File file = new File(System.getProperty("user.home") + "/AppData/Local/Temp/Meme - " + fileName + "." + fileExtension);
			try {
				ImageIO.write(export, fileExtension, file);
			} catch (IOException e) {e.printStackTrace();}
			exportFileCleaner.start();
			return file;
		}
		
		public void redraw() {
			if (original != null) {
				// previous Text X, Y, Font
				double tXRatio = (textX - centerX)/(double) image.getWidth();
				double tYRatio = textY/(double) image.getHeight();
				double fontRatio = font.getSize()/(double) image.getHeight();
				
				// Scale
				AffineTransform at = new AffineTransform();
				double ratio = (double) original.getWidth(null) / (double) original.getHeight(null);
				double width = getHeight() * ratio;

				double sx = width/(double) original.getWidth();
				double sy = (double) getHeight()/(double) original.getHeight();

				at.scale(sx, sy);
				AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
				image = null;
				image = scaleOp.filter(original, image);

				// Center, Text Position and Repaint
				centerX = (getWidth()-image.getWidth(null))/2;
				textX = centerX + (int) (tXRatio * image.getWidth());
				textY = (int) (tYRatio * image.getHeight());
				font = font.deriveFont((float) (fontRatio * image.getHeight()));
				repaint();
				toggleControls(true);
			}	
		}

		private void loadImage(File file) {
			try {
				// Load Image
				original = ImageIO.read(file);
				fileName = file.getName().substring(0, file.getName().lastIndexOf('.'));
				fileExtension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf('.') + 1);

				// Scale
				AffineTransform at = new AffineTransform();
				double ratio = (double) original.getWidth(null) / (double) original.getHeight(null);
				double width = getHeight() * ratio;

				double sx = width/(double) original.getWidth();
				double sy = (double) getHeight()/(double) original.getHeight();

				at.scale(sx, sy);
				AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
				image = null;
				image = scaleOp.filter(original, image);

				// Center, Text Position and Repaint
				centerX = (getWidth()-image.getWidth(null))/2;
				textX = centerX + 10;
				textY = getHeight() - 30;
				repaint();
				toggleControls(true);
			} catch (Exception e) {e.printStackTrace();}
		}
		
		private void generateExport() {
			if (original != null) {
				// Copy Original
				ColorModel cm = original.getColorModel();
				WritableRaster raster = original.copyData(null);
				boolean isRasterPremultiplied = cm.isAlphaPremultiplied();
				export = new BufferedImage(cm, raster, isRasterPremultiplied, null);
				
				// Draw Text
				Graphics2D g2d = export.createGraphics();
				if (text != null) {
					// previous Text X, Y, Font
					double tXRatio = (textX - centerX)/(double) image.getWidth();
					double tYRatio = textY/(double) image.getHeight();
					double fontRatio = font.getSize()/(double) image.getHeight();
					
					// Center, Text Position and Repaint
					int posX = (int) (tXRatio * export.getWidth());
					int posY = (int) (tYRatio * export.getHeight());
					float fontSize = (float) (fontRatio * export.getHeight());
					
					// Draw
					Font exportFont = font.deriveFont(fontSize);
					g2d.setFont(exportFont);
					g2d.setColor(color);
					g2d.drawChars(text.toCharArray(), 0, text.length(), posX, posY);
				}
			}
		}

		private DropTarget dropTarget = new DropTarget(this, DnDConstants.ACTION_COPY, new DropTargetListener() {	
			@Override
			public void dropActionChanged(DropTargetDragEvent dtde) {}
			@Override
			public void drop(DropTargetDropEvent dtde) {
				try {
					dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					@SuppressWarnings("unchecked")
					List<File> list = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					loadImage(list.get(0));
				} catch (UnsupportedFlavorException | IOException e) {}
			}
			@Override
			public void dragOver(DropTargetDragEvent dtde) {}
			@Override
			public void dragExit(DropTargetEvent dte) {}
			@Override
			public void dragEnter(DropTargetDragEvent dtde) {
				Transferable t = dtde.getTransferable();
				if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					try {
						@SuppressWarnings("unchecked")
						List<File> list = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);

						if (list.size() == 1) {
							boolean jpg = list.get(0).getAbsolutePath().endsWith("jpg");
							boolean jpeg = list.get(0).getAbsolutePath().endsWith("jpeg");
							boolean png = list.get(0).getAbsolutePath().endsWith("png");
							boolean bmp = list.get(0).getAbsolutePath().endsWith("bmp");
							boolean gif = list.get(0).getAbsolutePath().endsWith("gif");
							if (jpg || jpeg || png || bmp || gif) dtde.acceptDrag(DnDConstants.ACTION_COPY);
							else dtde.rejectDrag();
						} else dtde.rejectDrag();
						//						for (int i = 0; i < list.size(); i++)
						//							System.out.println(i + " " + list.get(i).getAbsolutePath());
					} catch (UnsupportedFlavorException | IOException e) {};
				}
				else dtde.rejectDrag();
			}
		},true);

		private MouseListener mouseListener = new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				textX = e.getX();
				textY = e.getY();
				repaint();
			}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) {}
		};
	}
}