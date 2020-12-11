package miniGames;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class EllieMascot {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EllieMascot window = new EllieMascot();
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
	public EllieMascot() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		int screenW = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int screenH = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

		frame = new JFrame();
		frame.setBounds(0, 0, screenW, screenH);
		frame.setUndecorated(true);
		frame.setBackground(new Color(1f,1f,1f,0f));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setAlwaysOnTop(true);

		MagicPanel mp = new MagicPanel();
		mp.setBounds(0 , 0, frame.getWidth(), frame.getHeight());
		mp.setBackground(new Color(1f,1f,1f,0f));
		frame.add(mp);

		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setUseParentHandlers(false);
		logger.setLevel(Level.OFF);

		try {
			GlobalScreen.registerNativeHook();
			GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
				@Override
				public void nativeKeyTyped(NativeKeyEvent e) {
				}

				@Override
				public void nativeKeyReleased(NativeKeyEvent e) {
					int k = e.getKeyCode();
					if (mp.getEllieState() == STATE.AIMED && k == NativeKeyEvent.VC_SHIFT) mp.setEllieState(STATE.STOP_AIMING);
					else if (mp.getEllieState() == STATE.STOP_AIMING) mp.setEllieState(STATE.STOP_AIMING);
					else if (mp.getEllieState() == STATE.SHOOTING && k == NativeKeyEvent.VC_SPACE) mp.setEllieState(STATE.AIMED);
					else mp.setEllieState(STATE.IDLE);
					System.out.println("RELEASED: " + mp.getEllieState() + " " + NativeKeyEvent.getKeyText(k));
				}

				@Override
				public void nativeKeyPressed(NativeKeyEvent e) {
					int k = e.getKeyCode();
					if (mp.getEllieState() != STATE.AIMED && mp.getEllieState() != STATE.SHOOTING) {
						if (k == NativeKeyEvent.VC_SHIFT && mp.getEllieState() != STATE.START_AIMING) {
							mp.setEllieState(STATE.START_AIMING);
						}
						if (k == NativeKeyEvent.VC_A) {
							mp.setX(-10);
							mp.setEllieState(STATE.RUNNING);
						} else if (k == NativeKeyEvent.VC_D) {
							mp.setX(10);
							mp.setEllieState(STATE.RUNNING);
						} else if (k == NativeKeyEvent.VC_W) {
							mp.setY(-10);
							mp.setEllieState(STATE.RUNNING);
						} else if (k == NativeKeyEvent.VC_S) {
							mp.setY(10);
							mp.setEllieState(STATE.RUNNING);
						}
					} else {
						if (k == NativeKeyEvent.VC_SPACE) mp.setEllieState(STATE.SHOOTING);
					}
					System.out.println("PRESSED: " + mp.getEllieState() + " " + NativeKeyEvent.getKeyText(k));
				}
			});
		} catch (NativeHookException e) {}

		frame.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) {}
			@Override
			public void windowClosed(WindowEvent e) {
				try {GlobalScreen.unregisterNativeHook();} catch (NativeHookException e1) {}
			}
			@Override
			public void windowActivated(WindowEvent e) {}
		});
	}

	enum STATE {START_AIMING, STOP_AIMING, AIMED, IDLE, SHOOTING, RUNNING, DYING, DEATH};
	@SuppressWarnings("serial")
	class MagicPanel extends JPanel implements ActionListener {
		int tick = 100;
		javax.swing.Timer timer;	
		STATE ellieState = STATE.IDLE;
		int curPos = 0;
		// first, last
		int[][] pos = {
				{0,7},		// aim
				{8,11},		// idle
				{12,15},	// shoot
				{16,29},	// run
				{30,37}		// death
		};
		ArrayList<BufferedImage> ellieSpr = new ArrayList<>();
		int x=0, y=0;
		boolean facingLeft = false;

		MagicPanel() {
			loadEllie();
			timer = new javax.swing.Timer(tick, this);
			timer.start();
		}

		@Override
		protected void paintComponent(Graphics g) {
			//			super.paintComponent(g);
			drawEllie(g);
		}
		@Override
		public boolean isOpaque() {
			return false;
		}

		void drawEllie(Graphics g) {
			// Set Draw Pos
			if (ellieState == STATE.START_AIMING) {
				if (curPos < pos[0][0] || curPos > pos[0][1]) curPos = pos[0][0];
			} else if (ellieState == STATE.STOP_AIMING) {
				if (curPos < pos[0][0] || curPos > pos[0][1]) curPos = pos[0][1];
			} else if (ellieState == STATE.AIMED) {
				curPos = 7;
			} else if (ellieState == STATE.IDLE) {
				if (curPos < pos[1][0] || curPos > pos[1][1]) curPos = pos[1][0];
			} else if (ellieState == STATE.SHOOTING) {
				if (curPos < pos[2][0] || curPos > pos[2][1]) curPos = pos[2][0];
			} else if (ellieState == STATE.RUNNING) {
				if (curPos < pos[3][0] || curPos > pos[3][1]) curPos = pos[3][0];
			} else if (ellieState == STATE.DYING) {
				if (curPos < pos[4][0] || curPos > pos[4][1]) curPos = pos[4][0];
			} else if (ellieState == STATE.DEATH) {
				curPos = 37;
			}
			
			if (!facingLeft) g.drawImage(ellieSpr.get(curPos), x, y, null);
			else {
				AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
				tx.translate(-240, 0);
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				g.drawImage(op.filter(ellieSpr.get(curPos), null), x, y, null);
			}
			
			if (ellieState != STATE.STOP_AIMING) curPos++;
			else curPos--;
			
			// Change State
			if (ellieState == STATE.START_AIMING && curPos > pos[0][1]) ellieState = STATE.AIMED;
			else if (ellieState == STATE.STOP_AIMING && curPos < pos[0][0]) ellieState = STATE.IDLE;
//			else if (ellieState == STATE.SHOOTING && curPos > pos[2][1]) ellieState = STATE.AIMED;
			else if (ellieState == STATE.DYING && curPos > pos[4][1]) ellieState = STATE.DEATH;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// Game Logic Here
			repaint();
		}

		void loadEllie() {
			BufferedImage spr = null;
			try{
				spr = ImageIO.read(new File("assets\\ellie_spritesheet.png"));
			} catch (IOException e) {}
			final int width = 240, height = 240;

			for (int r=0; r<5; r++)
				for (int c=0; c<8; c++)
					ellieSpr.add(spr.getSubimage(c * width, r * height, width, height));
		}
		void setEllieState(STATE s) {ellieState = s;}
		STATE getEllieState() {return ellieState;}
		void setX(int relativeX) {
			x += relativeX;
			if (relativeX > 0) facingLeft = false;
			else facingLeft = true;
		}
		void setY(int relativeY) {y += relativeY;}
	}
}