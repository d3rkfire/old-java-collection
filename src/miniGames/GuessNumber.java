package miniGames;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class GuessNumber {

	private JFrame frmNumberGuesserPro;
	private JButton btnNext;
	private JLabel lblInstruction;
	
	private int index = 0;
	private String table = null;
	private String result = null;
	private String instruction[] = new String[] {
			"<html>Please choose a 2-digits number that you like<br>Eg: 79</html>",
			"<html>Now add these two digits together<br>Eg: 7 + 9 = 16</html>",
			"<html>Then substract the second number from your first number<br>Eg: 79 - 16 = 63</html>",
			"<html>After you click \"Next\", a table will be shown.<br>Please look carefully at your result number and its sign.</html>",
			"", // Show Table
			"<html>Now we will guess your sign</html>",
			"<html>And your sign is...</html>",
			"" // Show Sign
	};
	private String signs[] = new String[] {"♈", "♉", "♊", "♋", "♌", "♍", "♎", "♏", "♐", "♑", "♒", "♓"};
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuessNumber window = new GuessNumber();
					window.frmNumberGuesserPro.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GuessNumber() {
		initialize();
		btnNext.addActionListener(nextAL);
	}

	private void generateTable() {
		Random r = new Random();
		result = signs[r.nextInt(signs.length)];
		
		table = "<html>";
		for (int i = 0; i <= 89; i++) {
			if (i % 9 == 0 && i != 0) table += i + " = " + result + ", ";
			else table+= i + " = " + signs[r.nextInt(signs.length)] + ", ";
			if (i % 7 == 0 && i != 0) table += "<br>";
		}
		table += "</html>";
	}
	
	private ActionListener nextAL = new ActionListener() {	
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (index == 4) {
				generateTable();
				lblInstruction.setText(table);
				index++;
				return;
			}
			if (index == 7) {
				lblInstruction.setFont(lblInstruction.getFont().deriveFont(lblInstruction.getFont().getSize() * 8f));
				lblInstruction.setText(result);
				btnNext.setVisible(false);
				return;
			}
			
			lblInstruction.setText(instruction[index++]);
			if (index == instruction.length - 1) btnNext.setText("Show");
		}
	};
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmNumberGuesserPro = new JFrame();
		frmNumberGuesserPro.setResizable(false);
		frmNumberGuesserPro.setTitle("Number Guesser Pro");
		frmNumberGuesserPro.setBounds(100, 100, 450, 300);
		frmNumberGuesserPro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmNumberGuesserPro.getContentPane().setLayout(null);
		
		lblInstruction = new JLabel(instruction[index++]);
		lblInstruction.setHorizontalAlignment(SwingConstants.CENTER);
		lblInstruction.setBounds(10, 11, 424, 215);
		frmNumberGuesserPro.getContentPane().add(lblInstruction);
		
		btnNext = new JButton("Next");
		btnNext.setBounds(178, 237, 89, 23);
		frmNumberGuesserPro.getContentPane().add(btnNext);
	}
}
