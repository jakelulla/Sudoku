import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;

public class Grid
{
	private final int rows;
	private final int cols;
	private final Panel[][] grid;
	private final int x;
	private final int y;
	private final int w;
	private final int h;

	public Grid(int rows, int cols, int x, int y, int w, int h)
	{
		this.rows = rows;
		this.cols = cols;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		grid = new Panel[rows][cols];
		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < cols; j++)
			{
				grid[i][j] = new Panel(-1, w, h, false, false);
			}
		}
	}

	public Panel getPanel(int row, int col)
	{
		return grid[row][col];
	}

	public void setPanel(int row, int col, int number)
	{
		grid[row][col].setNumber(number);
	}

	public void draw(Graphics2D g2)
	{
		int xCoord = x;
		int yCoord = y;
		boolean critRow;
		boolean critCol;
		for (int r = 0; r < rows; r++)
		{
			for (int c = 0; c < cols; c++)
			{
				critRow = r == 2 || r == 5;
				critCol = c == 2 || c == 5;
				grid[r][c].draw(g2, xCoord, yCoord, w, h, critRow, critCol);
				xCoord += w;
			}
			yCoord += h;
			xCoord = x;
		}
	}

//	public boolean boardIsSolvable() {
//		if (boardFilled()) {
//			for (int r = 0; r < rows; r++) {
//				for (int c = 0; c < cols; c++) {
//					if (!moveIsValid(r, c, grid[r][c].getNumber())) {
//						return false;
//					}
//				}
//			}
//			return true;
//		}
//
//		int nextRow = -1;
//		int nextCol = -1;
//		for (int r = 0; r < rows; r++) {
//			for (int c = 0; c < cols; c++) {
//				if (grid[r][c].getNumber() == -1) {
//					nextRow = r;
//					nextCol = c;
//					break;
//				}
//			}
//		}
//
//		for (int num = 1; num <= 9; num++) {
//			if (moveIsValid(nextRow, nextCol, num)) {
//				grid[nextRow][nextCol].setNumber(num);
//				if (boardIsSolvable()) {
//					return true;
//				}
//				grid[nextRow][nextCol].setNumber(-1);
//			}
//		}
//
//		return false;
//	}

	public boolean moveIsValid(int row, int col, int number)
	{
		for (int i = 0; i < 9; i++)
		{
			if (grid[row][i].getNumber() == number && i != col)
				return false;
			if (grid[i][col].getNumber() == number && i != row)
				return false;
		}
		int startRow = row - row % 3;
		int startCol = col - col % 3;
		for (int r = startRow; r < startRow + 3; r++)
		{
			for (int c = startCol; c < startCol + 3; c++)
			{
				if (grid[r][c].getNumber() == number)
					return false;
			}
		}
		return true;
	}

	public boolean boardFilled()
	{
		for (int r = 0; r < rows; r++)
		{
			for (int c = 0; c < cols; c++)
			{
				if (grid[r][c].getNumber() == -1)
					return false;
			}
		}
		return true;
	}

	public void fillGrid()
	{
		fillGrid(0, 0);
	}

	private boolean fillGrid(int row, int col)
	{
		if (row == rows)
		{
			return true; // Grid is fully filled
		}

		int nextRow = (col == cols - 1) ? row + 1 : row;
		int nextCol = (col + 1) % cols;

		ArrayList<Integer> numbers = new ArrayList<>();
		for (int i = 1; i <= 9; i++)
		{
			numbers.add(i);
		}
		Collections.shuffle(numbers); // Shuffle the numbers randomly

		for (int number : numbers)
		{
			if (moveIsValid(row, col, number))
			{
				grid[row][col].setNumber(number);
				if (fillGrid(nextRow, nextCol))
				{
					return true;
				}
				grid[row][col].setNumber(-1);
				grid[row][col].setEditable(false);
			}
		}

		return false;
	}

	public void removeSquares(int missingSquares)
	{
		int removed = 0;
		while (removed < missingSquares)
		{
			int row = (int) (Math.random() * rows);
			int col = (int) (Math.random() * cols);
			if (grid[row][col].getNumber() != 0)
			{
				grid[row][col].setNumber(-1);
				grid[row][col].setEditable(true);
				removed++;
			}
		}
	}

	

}
