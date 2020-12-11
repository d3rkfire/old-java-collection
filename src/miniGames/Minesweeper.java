package miniGames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Minesweeper {

	private JFrame frame;
	private JTextField txtSize;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					Minesweeper window = new Minesweeper();
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
	public Minesweeper() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private JButton[][] buttons;
	private boolean[][] hasBomb;
	private String[][] values;

	private Color unturnedBackground = new Color(220, 220, 220);
	private Color turnedBackground = Color.WHITE;
	private Color turnedForeground = Color.BLACK;
	private Color unsureBackground = Color.YELLOW;
	private Color bombBackground = Color.RED;

	private int size = 16;
	private int bombCount = size * 2;
	private int currentBombCount = bombCount;

	private MouseListener listener;

	private void initialize() {
		buttons = new JButton[size][size];
		hasBomb = new boolean[size][size];
		values = new String[size][size];

		frame = new JFrame();
		frame.setBounds(100, 100, 800, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		
		JLabel lblSize = new JLabel("Size:");
		lblSize.setBounds(10, 15, 30, 14);
		frame.getContentPane().add(lblSize);

		txtSize = new JTextField();
		txtSize.setText("16");
		txtSize.setBounds(50, 12, 86, 20);
		frame.getContentPane().add(txtSize);
		txtSize.setColumns(10);
		txtSize.setEnabled(false);

		JButton btnNew = new JButton("New");
		btnNew.setBounds(146, 11, 60, 23);
		frame.getContentPane().add(btnNew);
		//		btnNew.setEnabled(false);
		btnNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int row = 0; row < size; row++)
					for (int col = 0; col < size; col++) {
						hasBomb[row][col] = false;
						values[row][col] = null;
						
						buttons[row][col].setEnabled(true);
						buttons[row][col].setText(null);
						buttons[row][col].setBackground(unturnedBackground);
						
						// Length = 1 because 1 default listener + our listeners
						if (buttons[row][col].getMouseListeners().length == 1) buttons[row][col].addMouseListener(listener);
					}
				
				generateBombs();
				generateNumbers();
			}
		});

		JLabel lblRemaining = new JLabel("Remaining: 32/32");
		lblRemaining.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRemaining.setBounds(645, 15, 100, 14);
		frame.getContentPane().add(lblRemaining);

		JPanel panel = new JPanel();
		panel.setBounds(35, 40, 710, 710);
		frame.getContentPane().add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		panel.setLayout(gbl_panel);

		// Create Buttons
		int width = panel.getWidth()/size;
		for (int row = 0; row<size; row++)
			for (int col = 0; col<size; col++) {
				//				JButton btn = new JButton("" + (i + 16 * j));
				JButton btn = new JButton();
				btn.setMargin(new Insets(0, 0, 0, 0));
				btn.setFocusPainted(false);
				btn.setBackground(unturnedBackground);
				//				btn.setForeground(unturnedBackground);
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.insets = new Insets(0, 0, 0, 0);
				gbc.gridx = col;
				gbc.gridy = row;
				gbc.fill = 1;
				btn.setPreferredSize(new Dimension(width, width));
				buttons[row][col] = btn;
				panel.add(btn, gbc);
			}

		generateBombs();
		generateNumbers();

		// Mouse listener
		listener = new MouseListener() {
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
				JButton self = (JButton) e.getSource();
				GridBagConstraints gbc = gbl_panel.getConstraints(self);
				int col = gbc.gridx;
				int row = gbc.gridy;
				//				System.out.println(row + " " + col);

				if (e.getButton() == MouseEvent.BUTTON1) {
					// Left Click
					if (hasBomb[row][col]) {
						self.setBackground(turnedBackground);
						self.setForeground(turnedForeground);
						self.setText(values[row][col]);
						for (int row1 = 0; row1 < size; row1++)
							for (int col1 = 0; col1 < size; col1++) {
								buttons[row1][col1].setEnabled(false);
								buttons[row1][col1].removeMouseListener(listener);
							}
						JOptionPane.showMessageDialog(null, "You lose.");
					}
					else if (values[row][col] == null) {
						floodFill(buttons, values, row, col);
						checkWin(buttons, hasBomb, size, bombCount);
					}
					else if (values[row][col] != null) {
						self.setBackground(turnedBackground);
						self.setForeground(turnedForeground);
						self.setText(values[row][col]);
						checkWin(buttons, hasBomb, size, bombCount);
					}
				} else {
					// Right Click
					if (self.getBackground() == unturnedBackground) {
						self.setBackground(bombBackground);
						lblRemaining.setText("Remaining: " + --currentBombCount + "/" + bombCount);
					} else if (self.getBackground() == bombBackground) {
						self.setBackground(unsureBackground);
						lblRemaining.setText("Remaining: " + ++currentBombCount + "/" + bombCount);
					} else if (self.getBackground() == unsureBackground) {
						self.setBackground(unturnedBackground);
					}
				}

			}
		};

		for (int row = 0; row < size; row++)
			for (int col = 0; col < size; col++)
				buttons[row][col].addMouseListener(listener);
	}

	private void floodFill(JButton[][] buttons, String[][] values, int row, int col) {
		if (buttons[row][col].getBackground() == turnedBackground) return;
		buttons[row][col].setBackground(turnedBackground);
		buttons[row][col].setForeground(turnedForeground);
		if (values[row][col] != null) {
			buttons[row][col].setText(values[row][col]);
			return;
		}

		if (row-1 >= 0) floodFill(buttons, values, row - 1, col);	//up
		if (row+1 < size) floodFill(buttons, values, row + 1, col);	//down
		if (col-1 >= 0) floodFill(buttons, values, row, col-1);		//left
		if (col+1 < size) floodFill(buttons, values, row, col+1);	//right

		if (row-1 >= 0 && col-1 >= 0) floodFill(buttons, values, row-1, col-1);	//up-left
		if (row-1 >= 0 && col+1 < size) floodFill(buttons, values, row-1, col+1);	//up-right
		if (row+1 < size && col-1 >= 0) floodFill(buttons, values, row+1, col-1);	//down-left
		if (row+1 < size && col+1 < size) floodFill(buttons, values, row+1, col+1);	//down-left
	}

	private void checkWin(JButton[][] buttons, boolean[][] hasBomb, int size, int bombCount) {
		int count = 0;
		for (int row = 0; row < size; row++)
			for (int col = 0; col < size; col++)
				if (buttons[row][col].getBackground() == turnedBackground) count += 1;
		System.out.println(count + "/" + (size * size - bombCount));
		if (count == size * size - bombCount) {
			for (int row = 0; row < size; row++)
				for (int col = 0; col < size; col++) {
					buttons[row][col].setEnabled(false);
					buttons[row][col].removeMouseListener(listener);
				}
			JOptionPane.showMessageDialog(null, "You win!");
		}
	}

	private void generateBombs() {
		// Create Bombs
		Random r = new Random();
		for (int i = 0; i < bombCount; i++) {
			int row = r.nextInt(size);
			int col = r.nextInt(size);
			if (hasBomb[row][col]) i--;
			else hasBomb[row][col] = true;
			values[row][col] = "B";
			//					buttons[row][col].setText("B");
		}
	}

	private void generateNumbers() {
		// Create Numbers
		for (int row = 0; row < size; row++)
			for (int col = 0; col < size; col++) {
				if (hasBomb[row][col]) continue;
				else {
					// Check Surrounding
					int top = (row-1 >= 0) ? row-1 : 0,
							bottom = (row+1 < size) ? row+1 : size-1,
									left = (col-1 >= 0) ? col-1 : 0,
											right = (col+1 < size) ? col+1 : size-1;
					int count = 0;
					for (int row2 = top; row2 <= bottom; row2++)
						for (int col2 = left; col2 <= right; col2++)
							if (hasBomb[row2][col2]) count += 1;
					if (count > 0) values[row][col] = String.valueOf(count);
					//					System.out.println((i+1 < size) + " " + i + " " + j + " top = " + top + " bottom = " + bottom + " left = " + left + " right = " + right);
				}
			}
	}
}
