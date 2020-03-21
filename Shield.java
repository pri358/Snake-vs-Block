package AP_Project;

import javafx.scene.Node;

import javafx.scene.image.ImageView;

/**
 * This class is a subclass for Tokens and represents the Shield token in the
 * game.
 */
public class Shield extends Token implements java.io.Serializable {
	private boolean active;
	private double time;

	public Shield(Node n) {
		super(n);
		active = false;
		time = 0;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public String toString() {
		ImageView R = (ImageView) node;
		double x = R.getX() + R.getTranslateX();
		double y = R.getY() + R.getTranslateY();
		return x + " " + y + " " + active + "\n";

	}

}
