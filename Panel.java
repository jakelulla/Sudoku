import java.awt.*;

public class Panel
{
	private int number;
	private boolean editable;
	private boolean given;
	private Color color;
	private final Color givenColor = Color.LIGHT_GRAY; // Color for given squares

	public Panel(int number, int width, int height, boolean editable, boolean given)
	{
		this.number = number;
		this.editable = editable;
		this.given = given;
		this.color = given ? givenColor : Color.WHITE; // Use given color if the square is given
	}

	public int getNumber()
	{
		return number;
	}

	public void setNumber(int number)
	{
		this.number = number;
	}

	public boolean isEditable()
	{
		return editable;
	}

	public void setEditable(boolean editable)
	{
		this.editable = editable;
	}

	public boolean isGiven()
	{
		return given;
	}

	public void setGiven(boolean given)
	{
		this.given = given;
		this.color = given ? givenColor : Color.WHITE; // Update color if the square is given
	}

	public Color getColor()
	{
		return color;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}

	public void draw(Graphics2D g2, int x, int y, int width, int height, boolean critRow, boolean critCol)
	{
		g2.setStroke(new BasicStroke(2));
		g2.setColor(color);

		// Adjust rectangle coordinates to avoid overlapping with borders
		int rectX = x + 1;
		int rectY = y + 1;
		int rectWidth = width - 2;
		int rectHeight = height - 2;

		// Fill the cell with the appropriate color
		if (!editable)
		{
			g2.setColor(new Color(215, 200, 185));
			g2.fillRect(rectX, rectY, rectWidth, rectHeight);
		} else if (color == Color.YELLOW && editable)
		{
			g2.fillRect(rectX, rectY, rectWidth, rectHeight);
		}

		else
		{
			g2.setColor(Color.WHITE);
			g2.fillRect(rectX, rectY, rectWidth, rectHeight);
		}

		// Draw the number if applicable
		if ((!color.equals(givenColor) || !editable) && number != -1)
		{
			g2.setColor(Color.BLACK);
			Font stringFont = new Font("SansSerif", Font.PLAIN, 22);
			g2.setFont(stringFont);
			g2.drawString(number + "", x + width / 2 - 5, y + height / 2 + 5);
		}

		// Draw the borders
		g2.setStroke(new BasicStroke(2));
		g2.setColor(Color.GRAY);
		g2.drawRect(x, y, width, height);

		// Draw thicker lines for critical rows and columns
		g2.setStroke(new BasicStroke(4));
		g2.setColor(Color.BLACK);
		if (critRow)
		{
			g2.drawLine(x, y + height, x + width, y + height);
		}
		if (critCol)
		{
			g2.drawLine(x + width, y, x + width, y + height);
		}

		// Restore default stroke for non-critical lines
		g2.setStroke(new BasicStroke(2));
		g2.setColor(Color.GRAY);
		if (critRow || critCol)
		{
			g2.drawRect(x, y, width, height);
		}
	}

}
