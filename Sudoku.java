import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Sudoku extends JPanel implements MouseListener, KeyListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	public static final int PREF_W = 800;
	public static final int PREF_H = 600;

	private final Grid grid;
	private final int gridX;
	private final int gridY;
	private final int panelW;
	private final int panelH;
	private int editSquareRow;
	private int editSquareCol;
	private boolean gameWon;
	private JButton restartButton;

	public enum Difficulty
	{
		EASY(50), MEDIUM(81), HARD(90);

		private final int squaresToRemove;

		Difficulty(int squaresToRemove)
		{
			this.squaresToRemove = squaresToRemove;
		}

		public int getSquaresToRemove()
		{
			return squaresToRemove;
		}
	}

	public Sudoku(Difficulty difficulty)
	{
		this.addMouseListener(this);
		this.addKeyListener(this);
		this.setFocusable(true);
		this.requestFocusInWindow();
		gridX = (PREF_W - 9 * 50) / 2;
		gridY = (PREF_H - 9 * 50) / 2;
		panelW = 50;
		panelH = 50;
		editSquareRow = editSquareCol = -1;
		grid = new Grid(9, 9, gridX, gridY, panelW, panelH);
		grid.fillGrid();
		grid.removeSquares(difficulty.getSquaresToRemove());
		gameWon = false;

		restartButton = new JButton("Restart");
		restartButton.setBounds(PREF_W - 120, 20, 100, 30);
		restartButton.addActionListener(this);
		this.setLayout(null);
		this.add(restartButton);

		Timer timer = new Timer(10, e ->
		{
			if (!gameWon && grid.boardFilled())
			{
				gameWon = true;
				repaint();
			}
		});
		timer.start();
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		grid.draw(g2);
		if (gameWon)
		{
			g2.setFont(new Font("Arial", Font.BOLD, 36));
			g2.setColor(Color.GREEN);
			g2.drawString("You Win!", PREF_W / 2 - 80, PREF_H / 2);
			g2.drawString("Press 'R' to Restart", PREF_W / 2 - 150, PREF_H / 2 + 50);
		}
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(PREF_W, PREF_H);
	}

	public static void createAndShowGUI()
	{
		Difficulty difficulty = (Difficulty) JOptionPane.showInputDialog(null, "Select difficulty level:", "Difficulty",
				JOptionPane.QUESTION_MESSAGE, null, Difficulty.values(), Difficulty.EASY);

		JFrame frame = new JFrame("Sudoku");
		JPanel gamePanel = new Sudoku(difficulty);
		gamePanel.setFocusable(true);
		gamePanel.requestFocusInWindow();
		frame.getContentPane().add(gamePanel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(Sudoku::createAndShowGUI);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (gameWon)
			return;
		requestFocusInWindow();
		int x = e.getX();
		int y = e.getY();
		if (x < gridX || x > gridX + panelW * 9 || y < gridY || y > gridY + panelH * 9)
		{
			return;
		}
		x -= gridX;
		y -= gridY;
		x /= panelW;
		y /= panelH;

		if (editSquareRow != -1)
		{
			grid.getPanel(editSquareRow, editSquareCol).setColor(Color.WHITE);
		}

		editSquareRow = y;
		editSquareCol = x;
		if (grid.getPanel(y, x).isEditable())
			grid.getPanel(y, x).setColor(Color.YELLOW);

		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// Unused mouse event
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// Unused mouse event
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// Unused mouse event
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// Unused mouse event
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		// Unused key event
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (gameWon)
		{
			if (e.getKeyCode() == KeyEvent.VK_R)
			{
				createAndShowGUI();
				((Window) SwingUtilities.getWindowAncestor(this)).dispose();
			}
			return;
		}
		if (editSquareRow == -1)
			return;
		if (e.getKeyCode() >= KeyEvent.VK_1 && e.getKeyCode() <= KeyEvent.VK_9)
		{
			int number = e.getKeyCode() - KeyEvent.VK_0;
			if (grid.moveIsValid(editSquareRow, editSquareCol, number))
			{
				grid.getPanel(editSquareRow, editSquareCol).setNumber(number);
			} else
			{
				grid.getPanel(editSquareRow, editSquareCol).setColor(Color.RED);
			}
		}
		grid.getPanel(editSquareRow, editSquareCol).setColor(Color.WHITE);
		editSquareRow = editSquareCol = -1;
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		// Unused key event
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == restartButton)
		{
			createAndShowGUI();
			((Window) SwingUtilities.getWindowAncestor(this)).dispose();
		}
	}
}
