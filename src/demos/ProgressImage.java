package demos;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ProgressImage {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					ProgressImage window = new ProgressImage();
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
	public ProgressImage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		MyLabel label = new MyLabel();
		label.setBounds(10, 11, 764, 539);
		frame.getContentPane().add(label);
		
		frame.addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {}
			@Override
			public void componentResized(ComponentEvent e) {
				JFrame f = (JFrame) e.getSource();
				Dimension d = f.getSize();
				d.setSize(d.getWidth() - 36, d.getHeight() - 61);
				label.setSize(d);
			}
			@Override
			public void componentMoved(ComponentEvent e) {}
			@Override
			public void componentHidden(ComponentEvent arg0) {}
		});
	}
	@SuppressWarnings("serial")
	class MyLabel extends JLabel {
		BufferedImage image = null;
		BufferedImage original = null;
		int centerX = 0;
		int step = 0;
		Timer timer;
		public MyLabel() {
			setHorizontalAlignment(SwingConstants.CENTER);
			setVerticalAlignment(SwingConstants.CENTER);
			setText("Click to load image...");

			new DropTarget(this, DnDConstants.ACTION_COPY, new DropTargetListener() {	
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
//							for (int i = 0; i < list.size(); i++)
//								System.out.println(i + " " + list.get(i).getAbsolutePath());
						} catch (UnsupportedFlavorException | IOException e) {};
					}
					else dtde.rejectDrag();
				}
			},true);
			
			addMouseListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {}
				@Override
				public void mousePressed(MouseEvent e) {}
				@Override
				public void mouseExited(MouseEvent e) {}
				@Override
				public void mouseEntered(MouseEvent e) {}
				@Override
				public void mouseClicked(MouseEvent e) {
					openImage();
				}
			});
			timer = new Timer(10, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					frame.setTitle(step + "%");
					repaint();
					step += 1;
					if (step > 1000) ((Timer) e.getSource()).stop();
				}
			});

		}

		@Override
		public void paint(Graphics g) {
			//			super.paint(g);
			if (image != null) {
				//				g.drawImage(image, centerX, 0, null);
				//				100 rows x 10 cols
				int width = image.getWidth()/10;
				int height = image.getHeight()/100 + 1;
				int x = width * (step % 10);
				int y = height * (step / 10);
				if (image.getHeight() - y < height) {
					y = image.getHeight() - height;
					x = image.getWidth() - width;
				}
				// scan style
				//				Image chunk = image.getSubimage(x, y, width, height);
				//				g.drawImage(chunk, centerX + x, y, null);

				// draw line by line
				if (y > 0) {
					Image prevRow = image.getSubimage(0, 0, image.getWidth(), y);
					g.drawImage(prevRow, centerX, 0, null);
				}
				Image chunk = image.getSubimage(0, y, x + width, height);
				g.drawImage(chunk, centerX + 0, y, null);
				//				System.out.println(step + " x=" + x + " y=" + y + " " + image.getHeight() + " " + height);
			}
		}

		private void openImage() {
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "bmp", "gif");
			fc.addChoosableFileFilter(filter);
			fc.setAcceptAllFileFilterUsed(false);
			fc.setMultiSelectionEnabled(false);
			fc.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));

			int result = fc.showOpenDialog(frame);
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				loadImage(file);
				setText(null);
			}
		}

		private void loadImage(File file) {
			try {
				// Load Image
				original = ImageIO.read(file);

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

				// Center and Repaint
				centerX = (getWidth()-image.getWidth(null))/2;

				// Start timer
				step = 0;
				timer.start();
			} catch (Exception e) {setText("Failed to load image.");}
		}
	}
}
