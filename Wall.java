package AP_Project;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This class is a subclass for Tokens and represents the walls token in the
 * game.
 *
 */
public class Wall extends Token implements java.io.Serializable {

	/**
	 * default constructor to initialize the wall and passes the node to the
	 * constructor of the superclass.
	 * 
	 * @param n
	 */
	public Wall(Node n) {
		super(n);
	}

	/**
	 * helps the wall to move down and sets the speed according to the length of the
	 * snake.
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
	}

	/**
	 * This function creates the String version of the object which is used in
	 * serialisation and deserialisation.
	 */
	public String toString() {
		Rectangle R = (Rectangle) node;
		double x = R.getX() + R.getTranslateX();
		double y = R.getY() + R.getTranslateY();
		Color color = (Color) R.getFill();
		String col = String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
				(int) (color.getBlue() * 255));

		return x + " " + y + " " + R.getWidth() + " " + R.getHeight() + "\n";
	}
}
