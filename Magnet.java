package AP_Project;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This class is a subclass for Tokens and represents the magnet token in the
 * game.
 *
 */
public class Magnet extends Token implements java.io.Serializable {
	private double time;
	private boolean active;

	public Magnet(Node n) {
		super(n);
		active = false;
		time = 0;

	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * This function creates the String version of the object which is used in
	 * serialisation and deserialisation.
	 */
	public String toString() {
		ImageView R = (ImageView) node;
		double x = R.getX() + R.getTranslateX();
		double y = R.getY() + R.getTranslateY();
		return x + " " + y + " " + active + "\n";

	}
}
