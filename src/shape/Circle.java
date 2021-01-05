package shape;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


/**
 * Creates a circle object with methods for controlling it's point locale,
 * background color, border color, font color, and number id.
 * @author Eric Canull
 * @version 1.0
 */
public final class Circle {
	
	/**
	 * The font for the numbers inside the circle.
	 */
	final Font font =  Font.font("Cooper Black", FontWeight.BOLD, 16);
	final Font varFont =  Font.font("Cooper Black", FontWeight.LIGHT, 12);
	final FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(font);

	/**
	 * The radius of the circle.
	 */
	public static final int RADIUS = 26;
	
	/**
	 * The search key for searching and deleting circles.
	 */
	private final String searchKey;
	
	// The circle attributes
	private Point2D point;
	private Color backgroundColor;
	private Color borderColor;
	private Color fontColor;
	private Line newLine;

	/**
	 * Creates a circle object with methods for controlling it's point locale,
	 * background color, border color, font color, search key.
	 * @param searchKey a <code>Integer</code> search key for searching and deleting within an index.
	 */
	public Circle(String searchKey) {
		this.searchKey = searchKey;
		this.newLine = new Line();
		this.backgroundColor = Color.web("#FCFCFC");
	}
	
	/**
	 * 
	 * @param searchKey a integer id number for searching and deleting from an index.
	 * @param point a Cartesian coordinate using x and y float numbers.
	 */
	public Circle(String searchKey, Point2D point) {
		this.searchKey = searchKey;
		this.point = point;
		this.backgroundColor = Color.rgb(49, 116, 222);
		this.setBorderColor(Color.rgb(99, 99, 99));
		this.fontColor = Color.web("#FCFCFC");
		
	}

	/**
	 * Draws the circle at a new position
	 * @param gc The graphics object to use for drawing to a component
	 */
	public void draw(GraphicsContext gc, int shape) {
		//(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gc.setLineWidth(3); // Sets the width of the lines
		//System.out.println("shape = "+shape);
		// Create a circle 
		if(shape == 0){
			gc.setFill(backgroundColor);
			gc.fillOval(point.getX() - RADIUS, point.getY() - RADIUS, 2 * RADIUS, 2 * RADIUS);

			// Outline the circle border
			gc.setStroke(borderColor);
			//gc.strokeOval(point.getX() - (RADIUS-10-5), point.getY() - (RADIUS-10-5), 2 * (RADIUS-10-5), 2 * (RADIUS-10-5));
			gc.strokeOval(point.getX() - (RADIUS), point.getY() - (RADIUS), 2 * (RADIUS), 2 * (RADIUS));
		}
		else if(shape == 1){
			gc.setLineWidth(4); // Sets the width of the lines
			gc.setFill(backgroundColor);
			gc.fillRect(point.getX() - RADIUS, point.getY() - RADIUS, 2 * RADIUS, 2 * RADIUS);
			// Fill the line
			gc.setStroke(borderColor);
			gc.strokeLine((point.getX()-RADIUS), (point.getY()+RADIUS), point.getX()-RADIUS, point.getY()-RADIUS);
			gc.strokeLine((point.getX()-RADIUS), (point.getY()+RADIUS), point.getX()+RADIUS, point.getY()+RADIUS);
			gc.strokeLine((point.getX()-RADIUS), (point.getY()-RADIUS), point.getX()+RADIUS, point.getY()-RADIUS);
			gc.strokeLine((point.getX()+RADIUS), (point.getY()-RADIUS), point.getX()+RADIUS, point.getY()+RADIUS);

		}

		// Draw the id number inside the circle
		String[] key = getKey().split("\n");
		gc.setFont(font);
		gc.setFill(getFontColor());
		gc.fillText(key[0],
				 point.getX() - (fm.computeStringWidth(key[0]) / 2),
				 point.getY() + (fm.getAscent() / 4));
		gc.setFont(varFont);
		//gc.setFill(Color.NAVAJOWHITE);
		if (key.length>1)
			{gc.fillText('\n'+key[1],
					point.getX() - (fm.computeStringWidth(key[1]) / 3),
					point.getY() + (fm.getAscent() / 4));}
	}

	private String getKey() {
		return getSearchKey();
	}
	/**
	 * Get the search key number.
	 * @return A integer of the circle index value. 
	 */
	public String getSearchKey() {
		return this.searchKey;
	}
	
	/**
	 * Gets the border color.
	 * @return A color for the circle border
	 */
	public Color getBorderColor() {
		return borderColor;
	}
	
	/**
	 * Sets the border color.
	 * @param borderColor
	 */
	private void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}
	
	/**
	 * Gets the point coordinates.
	 * @return 
	 */
	public Point2D getPoint() {
		return point;
	}
	
	/**
	 * Sets the point coordinates.
	 * @param point
	 */
	public void setPoint(Point2D point) {
		this.point = point;
	}
	
	/**
	 * Gets the background color.
	 * @return
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
	/**
	 * Sets the background color.
	 * @param color 
	 */
	private void setBackgroundColor(Color color) {
		this.backgroundColor = color;
	}
	
	/**
	 * Gets the circle radius.
	 * @return
	 */
	public int getRadius() {
		return RADIUS;
	}
	
	/**
	 * Gets the font color.
	 * @return
	 */
	public Color getFontColor() {
		return this.fontColor;
	}
	
	/**
	 * Sets the font color.
	 * @param fontColor
	 */
	private void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}
	
	/**
	 * Sets the circle to use highlighted colors if the parameter 
	 * is true.
	 * @param highlight a boolean value for switching on/off highlighted colors
	 */
	public void setHighlighter(boolean highlight) {
		if (highlight) {
			setFontColor(Color.rgb(49, 116, 222));
			setBackgroundColor(Color.rgb(155, 244, 167));
			setBorderColor(Color.rgb(49, 116, 222));
	
		} else {
			setFontColor(Color.web("#FCFCFC"));
			setBackgroundColor(Color.rgb(49, 116, 222));
			setBorderColor(Color.rgb(99, 99, 99));
		}
	}

	/**
	 * Overrides the default toString method and gets the String representation
	 * of a circle.
	 * @return A String representation of the circle object.
	 */
	@Override
	public String toString() {
		
		return "Search Key# " + searchKey  + 
				" (x,y) = ("  + point.getX() + ", " + point.getY() + ")";
	}
}
