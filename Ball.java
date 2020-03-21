package AP_Project;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 * This class is a subclass for Tokens and represents the magnet token in the
 * game.
 *
 *
 */
public class Ball extends Token implements java.io.Serializable {
	private Label num;
	int points;

	/**
	 * This is a constructor which initializes the values of node and points and the
	 * label for the points.
	 * 
	 * @param n
	 *            this node represents a token which is passed to the constructor of
	 *            the superclass
	 * @param p
	 *            this p is the points that a ball has.
	 */
	public Ball(Node n, int p) {
		super(n);
		points = p;
		num = new Label();
		num.setText(Integer.toString(p));
		num.setTextFill(Color.BLACK);
		num.setFont(Font.font("centauri", 15));
	}

	/**
	 * @return the points of the current ball
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * This function sets the points of the ball
	 * 
	 * @param points
	 *            points is the number on the ball
	 */
	public void setPoints(int points) {
		this.points = points;
	}

	/**
	 * Default constructor to initialize the node n
	 * 
	 * @param n
	 */
	public Ball(Node n) {
		super(n);
	}

	/**
	 * 
	 * @return the label of the points
	 */
	public Label getNum() {
		return num;
	}

	/**
	 * sets the label for the points
	 * 
	 * @param num
	 */
	public void setNum(Label num) {
		this.num = num;
	}

	@Override
	/**
	 * this is a function that makes the balls move down and sets their speed
	 * according to the length of the snake.
	 */
	public void moveDown(int length) {
		double speed = 0;
		if (length < 20)
			speed = 0;
		else if (length <= 40)
			speed = 0.5;
		else if (length <= 60)
			speed = 1;
		else
			speed = 1.5;
		node.setTranslateY(node.getTranslateY() + 4 + speed);
		num.setTranslateY(num.getTranslateY() + 4 + speed);

	}

	/**
	 * This function creates the String version of the object which is used in
	 * serialisation and deserialisation.
	 */
	public String toString() {
		Circle R = (Circle) node;
		double x = R.getCenterX() + R.getTranslateX();
		double y = R.getCenterY() + R.getTranslateY();
		Color color = (Color) R.getFill();
		String col = String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
				(int) (color.getBlue() * 255));

		return x + " " + y + " " + col + " " + points + "\n";
	}
}
