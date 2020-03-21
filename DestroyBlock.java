package AP_Project;

import javafx.scene.Node;
import javafx.scene.image.ImageView;

/**
 * This class is a subclass for Tokens and represents the blocks token in the
 * game.
 *
 */
public class DestroyBlock extends Token implements java.io.Serializable {
	
	/**
	 * This is a constructor which initialises the value of node.
	 * 
	 * @param n
	 *            this represents a token and is passed to the constructor of the
	 *            superclass
	 */
	public DestroyBlock(Node n) {
		super(n);
	}

	/**
	 * This function creates the String version of the object which is used in
	 * serialisation and deserialisation.
	 */
	public String toString() {
		ImageView R = (ImageView) node;
		double x = R.getX() + R.getTranslateX();
		double y = R.getY() + R.getTranslateY();
		return x + " " + y + "\n";

	}

	@Override
	/**
	 * this is a function that makes the token move down and sets their speed
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
		

	}
}
