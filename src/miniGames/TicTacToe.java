package miniGames;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TicTacToe {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TicTacToe window = new TicTacToe();
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
	public TicTacToe() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	enum C {PLAYING, LOSE, WIN, TIE};
	C cond = C.PLAYING;
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 450);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		int w = frame.getWidth()/3;
		int h = frame.getHeight()/3;
		gridBagLayout.columnWidths = new int[] {w, w, w, 0};
		gridBagLayout.rowHeights = new int[]{h, h, h, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setBackground(Color.LIGHT_GRAY);
		frame.getContentPane().setLayout(gridBagLayout);

		JButton[][] buttons = new JButton[3][3];
		for (int x = 0; x<3; x++)
			for (int y = 0; y<3; y++) {
				JButton btn = new JButton(" ");
				btn.setFocusPainted(false);
				btn.setBackground(Color.WHITE);
				btn.setFont(new Font("Arial", Font.PLAIN, 80));
				btn.setForeground(Color.GRAY);
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.insets = new Insets(0,0,5,5);
				gbc.gridx = x;
				gbc.gridy = y;
				gbc.fill = 1;
				buttons[x][y] = btn;
				frame.getContentPane().add(buttons[x][y], gbc);

				btn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JButton self = (JButton) e.getSource();
						self.setText("X");
						self.setEnabled(false);
						if(!CheckVictory("X", buttons)) TurnAI(buttons);
					}
				});
			}
	}
	void TurnAI(JButton[][] buttons) {
		ArrayList<Integer> emptyX = new ArrayList<>();
		ArrayList<Integer> emptyY = new ArrayList<>();
		if (cond == C.PLAYING) {
			for (int x = 0; x<3; x++)
				for (int y=0; y<3; y++) {
					String val = buttons[x][y].getText();
					if (val == " ") {
						emptyX.add(x);
						emptyY.add(y);
					}
				}

			int chosen = new Random().nextInt(emptyX.size());
			buttons[emptyX.get(chosen)][emptyY.get(chosen)].setText("O");
			buttons[emptyX.get(chosen)][emptyY.get(chosen)].setEnabled(false);
			CheckVictory("O", buttons);
		}
	}
	boolean CheckVictory(String side, JButton[][] buttons) {
		// check each vertical row
		for (int i=0; i<3; i++)
			if (buttons[0][i].getText() == side && buttons[1][i].getText() == side && buttons[2][i].getText() == side) {
				if (side == "X") cond = C.WIN;
				else if (side == "O") cond = C.LOSE;
			}
		// check each horizontal row
		for (int i=0; i<3; i++)
			if (buttons[i][0].getText() == side && buttons[i][1].getText() == side && buttons[i][2].getText() == side) {
				if (side == "X") cond = C.WIN;
				else if (side == "O") cond = C.LOSE;
			}
		// check diagonal
		if (buttons[0][0].getText() == side && buttons[1][1].getText() == side && buttons[2][2].getText() == side) {
			if (side == "X") cond = C.WIN;
			else if (side == "O") cond = C.LOSE;
		}
		if (buttons[0][2].getText() == side && buttons[1][1].getText() == side && buttons[2][0].getText() == side) {
			if (side == "X") cond = C.WIN;
			else if (side == "O") cond = C.LOSE;
		}
		
		boolean tie = true;
		for (int x=0; x<3; x++)
			for (int y=0; y<3; y++)
				if (buttons[x][y].getText() == " ") {
					tie = false;
					break;
				}
		if (cond == C.PLAYING && tie == true) cond = C.TIE;

		if (cond != C.PLAYING) {
			for (int x=0; x<3; x++)
				for (int y=0; y<3; y++)
					buttons[x][y].setEnabled(false);
		}

		if (cond == C.WIN) JOptionPane.showMessageDialog(frame, "Congratulations! You win!", "Victory", JOptionPane.INFORMATION_MESSAGE);
		else if (cond == C.LOSE) JOptionPane.showMessageDialog(frame, "Sorry, but you suck at this!", "Defeat", JOptionPane.INFORMATION_MESSAGE);
		else if (cond == C.TIE) JOptionPane.showMessageDialog(frame, "A tie! happens a lot in Tic-Tac-Toe!", "Tie", JOptionPane.INFORMATION_MESSAGE);
		return tie;
	}
}
