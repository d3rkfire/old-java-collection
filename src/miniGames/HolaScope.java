package miniGames;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class HolaScope {

	private JFrame frame;
	private int screenW = 0, screenH = 0;
	private int prevX = 0, prevY = 0; // previous Mouse pos
	private int centerX = 0, centerY = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HolaScope window = new HolaScope();
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
	public HolaScope() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		screenW = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		screenH = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		centerX = screenW / 2;
		centerY = screenH / 2;
		// Frame
		frame = new JFrame();
		frame.setBounds(0, 0, screenW, screenH);
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setUndecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Blank cursor
		BufferedImage cursorImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), null);
		frame.setCursor(blankCursor);
		
		// Background
		File[] panoramaDir = new File("assets\\Panorama\\").listFiles();
		Random rand = new Random();
		int chosen = rand.nextInt(panoramaDir.length);
		DrawPanel dp = new DrawPanel(panoramaDir[chosen]);
		dp.setBounds(0, 0, screenW, screenH);
		dp.setBackground(Color.BLACK);
		frame.getContentPane().add(dp, null);
		dp.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				MyMouseMove(e, dp);
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				MyMouseMove(e, dp);
			}
		});
	}
	
	private void MyMouseMove(MouseEvent e, DrawPanel dp) {
		// Adjust blur
		int curX = e.getX(), curY = e.getY();
		int movement = curX - prevX + curY - prevY;
		if (movement > 25 || movement < -25) dp.setAlpha(1f);
		else if (movement > 15 || movement < -15) dp.setAlpha(dp.getAlpha() + 0.2f);
		else if (movement > 2 || movement < -2) dp.setAlpha(dp.getAlpha() + 0.01f);
		if (dp.getAlpha()>1) dp.setAlpha(1f);
//		System.out.println(movement + " " + dp.getAlpha());
		
		// Set Scope (Circle) Position
		dp.setX(curX);
		dp.setY(curY);
		prevX = curX;
		prevY = curY;
		
		// Set Adjustment
//		float adjust = ((curX - centerX) / screenW) * dp.getBuffer();
		float posToScreenRatioX = (float) (curX - centerX)/screenW;
		float adjustSide = posToScreenRatioX * 2 * dp.getSideBuffer();
		dp.setAdjustmentSide((int) adjustSide);
		
		float posToScreenRatioY = (float) (curY - centerY)/screenH;
		float adjustTop = posToScreenRatioY * 2 * dp.getTopBuffer();
		dp.setAdjustmentTop((int) adjustTop);
		
		dp.repaint();
	}
	
	@SuppressWarnings("serial")
	class DrawPanel extends JPanel {
		private BufferedImage background = null, blur = null;
		private float size = 250; // Circle Diameter
		private int posX = -500, posY = 0; // Current Mouse Position
		private float alpha = 0f;
		private float getAlpha() {return alpha;}
		private void setAlpha(float a) {alpha = a;}
		private int adjustmentSide = 0, adjustmentTop = 0;
		private int backgroundSideBuffer = 0;
		private int backgroundTopBuffer = 0;
		// X = (screenW - image.getWidth())/2
		// adjustment range = -X to X (Left to Right)
		
		public DrawPanel(String imagePath) {
			loadBackground(imagePath);
			init();
		}
		
		public DrawPanel(File image) {
			loadBackground(image);
			init();
		}
		
		void init() {
			Timer timer = new Timer(100, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (alpha > 0.8) alpha -= 0.02f;
					else alpha -= 0.05f;
					if (alpha < 0) alpha = 0;
					repaint();
				}
			});
			timer.start();
			backgroundSideBuffer = (screenW - background.getWidth())/2;
			backgroundTopBuffer =  (screenH - background.getHeight())/2;
		}
		
		@Override
		public void paint(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			drawImage(g2d, background, adjustmentSide, adjustmentTop);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			drawImage(g2d, blur, adjustmentSide, adjustmentTop);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			drawCircle(g2d);
		}
		
		private void drawCircle(Graphics2D g2d) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Shape innerCircle = new Ellipse2D.Float(posX - size/2, posY - size/2, size, size);
			Area blackBox = new Area(new Rectangle(0, 0, screenW, screenH));
			blackBox.subtract(new Area(innerCircle));
			g2d.setColor(Color.BLACK);
			g2d.fill(blackBox);
		}
		
		private void drawImage(Graphics2D g2d, BufferedImage image, int adjustSide, int adjustTop) {
			// Without scaling background to screen
//			int imageWidth = screenH * image.getWidth() / image.getHeight();
//			g2d.drawImage(image, (screenW - imageWidth)/2, 0, imageWidth, screenH, null);
			// After scaling background to screen
			g2d.drawImage(image, (screenW - image.getWidth())/2 + adjustSide, (screenH - image.getHeight())/2 + adjustTop, image.getWidth(), image.getHeight(), null);
			
			// Before Scale Y
//			g2d.drawImage(image, (screenW - image.getWidth())/2 + adjust, 0, image.getWidth(), screenH, null);
		}
		
		private void loadBackground(String path) {
			try {
				background = ImageIO.read(new File(path));

				float ratio = (float) screenH/background.getHeight() + 0.1f;
				AffineTransform at = new AffineTransform();
				at.scale(ratio, ratio);
				AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
				background = scaleOp.filter(background, null);
				blur = blur(10, 10);
			} catch (IOException e) {e.printStackTrace();}
		}
		
		private void loadBackground(File image) {
			try {
				background = ImageIO.read(image);

				float ratio = (float) screenH/background.getHeight() + 0.1f;
				AffineTransform at = new AffineTransform();
				at.scale(ratio, ratio);
				AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
				background = scaleOp.filter(background, null);
				blur = blur(10, 10);
			} catch (IOException e) {e.printStackTrace();}
		}
		
		private BufferedImage blur(int vert, int horz) {
			float[] data = new float[vert*horz];
			for (int i=0; i<vert * horz; i++) data[i] = 1f / (vert*horz);
			Kernel k = new Kernel(vert, horz, data);
			ConvolveOp cop = new ConvolveOp(k);
			return cop.filter(background, null);
		}
		
		void setX(int x) {posX = x;}
		void setY(int y) {posY = y;}
		void setAdjustmentSide(int x) {adjustmentSide = x;}
		void setAdjustmentTop(int y) {adjustmentTop = y;}
		int getSideBuffer() {return backgroundSideBuffer;}
		int getTopBuffer() {return backgroundTopBuffer;}
	}
}
