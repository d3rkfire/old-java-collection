package miniGames;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

public class MagicClicker {	

	private JFrame frame;

	/**
	 * Launch the application.
	 */	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MagicClicker window = new MagicClicker();
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
	public MagicClicker() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	boolean victory = false;
	int value = 10;
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 400, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblMagic = new JLabel(" ");
		lblMagic.setHorizontalAlignment(SwingConstants.CENTER);
//		lblMagic.setBounds(65, 11, 250, 14);
		frame.getContentPane().add(lblMagic);

		JProgressBar progressBar = new JProgressBar();
//		progressBar.setBounds(65, 37, 250, 14);
		frame.getContentPane().add(progressBar);

		JButton btnMagic = new JButton("Magic");
//		btnMagic.setBounds(147, 62, 89, 23);
		frame.getContentPane().add(btnMagic);
		
		String imagePath = "assets\\running.gif";
		
		ArrayList<BufferedImage> images = getGif(imagePath);
		MagicLabel lblImage = new MagicLabel(images);
		lblImage.setHorizontalAlignment(SwingConstants.CENTER);
//		lblImage.setBounds(10, 96, 364, 153);
		frame.getContentPane().add(lblImage);

		
		btnMagic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(progressBar.getValue() < progressBar.getMaximum()) progressBar.setValue(progressBar.getValue() + value);
				if(progressBar.getValue() >= progressBar.getMaximum()) {
					victory = true;
					lblImage.setDelay(10);
					lblMagic.setText("YOU WIN!!!");
				}
			}
		});
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(!victory) {
					int pval = progressBar.getValue();
					int max = progressBar.getMaximum();
					if(pval > progressBar.getMinimum()) progressBar.setValue(pval - 10);

					if (pval == 0) {
						if (!lblImage.isPaused()) lblImage.pause();
					} else {
						if (lblImage.isPaused()) lblImage.resume();
					}
					if (pval < max/5) {
						lblImage.setDelay(200);
						lblMagic.setText("Are you even trying?");
					}
					else if (pval < max * 2/5) {
						lblImage.setDelay(150);
						lblMagic.setText("You can do better than that...");
					}
					else if (pval < max * 3/5) {
						lblImage.setDelay(100);
						lblMagic.setText("Try harder...");
						value = 10;
					}
					else if (pval < max * 4/5) {
						lblImage.setDelay(50);
						lblMagic.setText("So near yet so far...");
						value = 9;
					}
					else if (pval < max) {
						lblImage.setDelay(25);
						lblMagic.setText("GO GO GO!!!");
						value = 9;
					}

					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {}
				}
			}
		}).start();
		
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				lblMagic.setBounds((frame.getWidth()-250)/2, 11, 250, 14);
				progressBar.setBounds((frame.getWidth()-250)/2, 37, 250, 14);
				btnMagic.setBounds((frame.getWidth()-89)/2, 62, 89, 23);
				lblImage.setBounds(10, 96, frame.getWidth()-20, frame.getHeight() - 300 + 153);
			}
		});
	}
	
	public static ArrayList<BufferedImage> getGif(String filename) {
		File file = new File(filename);
		ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
		ArrayList<BufferedImage> list = new ArrayList<>();
		try {
			// Getting Image Data //
			reader.setInput(ImageIO.createImageInputStream(file));
			BufferedImage prev = reader.read(0);
			list.add(prev);
			
			// First Metadata
			IIOMetadata imageMetaData = reader.getImageMetadata(0);
	        String metaFormatName = imageMetaData.getNativeMetadataFormatName();
			
			int num = reader.getNumImages(true);
			for (int i=1; i<num; i++) {
				int leftPos = 0, topPos = 0;
				// Get Metadata
	        	IIOMetadataNode root = (IIOMetadataNode) reader.getImageMetadata(i).getAsTree(metaFormatName);
	        	int nNodes = root.getLength();
	        	for (int j=0; j<nNodes; j++) {
	        		IIOMetadataNode node = (IIOMetadataNode) root.item(j);
	        		if (node.getNodeName().equalsIgnoreCase("ImageDescriptor")) {
	        			leftPos = Integer.parseInt(node.getAttribute("imageLeftPosition"));
	        			topPos = Integer.parseInt(node.getAttribute("imageTopPosition"));
	        			break;
	        		}
	        		// List all nodes for debug
//	        		NamedNodeMap attrs = node.getAttributes();
//	        		for (int k=0; k<attrs.getLength()-1; k++) {
//	        			System.out.println(j + " " + node.getNodeName() + " -> " + attrs.item(k).getNodeName() + ":" + attrs.item(k).getNodeValue());
//	        		}
	        	}
				
	        	// Set each frame
				BufferedImage frame = reader.read(i);
				BufferedImage combined = new BufferedImage(prev.getWidth(), prev.getHeight(), BufferedImage.TYPE_INT_ARGB);
				
				Graphics g = combined.getGraphics();
				g.drawImage(prev, 0, 0, null);
				g.drawImage(frame, leftPos, topPos, null);
				
				prev = combined;
				list.add(combined);
			}
		} catch (IOException e) {}
		return list;
	}
	
	@SuppressWarnings("serial")
	class MagicLabel extends JLabel {
		ArrayList<BufferedImage> images = new ArrayList<>();
		ArrayList<ImageIcon> list = new ArrayList<>();
		int cur = 0;
		int max = 0;
		boolean pause = true;
		javax.swing.Timer timer;
		MagicLabel(ArrayList<BufferedImage> l) {
			images = l;
			max = images.size() - 1;
			ActionListener animate = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (!pause) {
						setIcon(list.get(cur));
						if (cur < max) cur += 1;
						else cur = 0;
						repaint();
					}
				}
			};
			timer = new javax.swing.Timer(50, animate);
			timer.start();
		}
		
		void setDelay(int delay) {timer.setDelay(delay);}
		int getDelay() {return timer.getDelay();}
		void pause() {pause = true;}
		void resume() {pause = false;}
		boolean isPaused() {return pause;}
		
		@Override
		public void setBounds(int x, int y, int width, int height) {
			super.setBounds(x, y, width, height);
			list.clear();
			for (int i=0; i<=max; i++) {
				ImageIcon icon = new ImageIcon(images.get(i));
				icon.setImage(icon.getImage().getScaledInstance(getHeight() * icon.getIconWidth()/icon.getIconHeight(), getHeight(), Image.SCALE_DEFAULT));
				list.add(icon);
			}
			// First Frame
			setIcon(list.get(0));
		}
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
		}
	}
}
