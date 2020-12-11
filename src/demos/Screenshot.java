package demos;

import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Screenshot {

	private JFrame frame;
	private MyLabel lblImage;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Screenshot window = new Screenshot();
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
	public Screenshot() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Instant Screenshot");
		frame.getContentPane().setLayout(null);
		
		lblImage = new MyLabel();
		lblImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblImage.setBounds(10, 11, 764, 490);
		frame.getContentPane().add(lblImage);
		
		JButton btnScreenshot = new JButton("Screenshot");
		btnScreenshot.setBounds(344, 509, 89, 23);
		frame.getContentPane().add(btnScreenshot);
		
		btnScreenshot.addActionListener(ScreenshotAL);
	}
	
	@SuppressWarnings("serial")
	static class MyLabel extends JLabel {
		BufferedImage image;
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			if (image != null) {
				int w = (int) (this.getHeight() * (image.getWidth()/(double) image.getHeight()));
				g.drawImage(image, 0, 0, w, this.getHeight(), null);
			}
		}
		
		public void loadImage(BufferedImage img) {
			image = img;
			repaint();
		}
	}
	
	ActionListener ScreenshotAL = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				Robot robot = new Robot();
				Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
				BufferedImage img = robot.createScreenCapture(screenRect);
				lblImage.loadImage(img);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
	};
}
