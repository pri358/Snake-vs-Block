package AP_Project;

import javafx.scene.Node;

/**
 * This class is used for the bursting animation of the blocks and the snake
 *
 */
public class Entity {

	Node R;
	double dy, dx;
	double time;

	/**
	 * Default constructor initializes the value of the node, x coordinate and the y
	 * coordinate and the time by the present time.
	 * 
	 * @param r
	 *            node on which the bursting effect is applied.
	 * @param x
	 *            x-coordinate of the node
	 * @param y
	 *            y-coordinate of the node
	 */
	public Entity(Node r, double x, double y) {
		R = r;
		dx = x;
		dy = y;
		time = System.currentTimeMillis();
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public Node getR() {
		return R;
	}

	public void setR(Node r) {
		R = r;
	}

	public double getDy() {
		return dy;
	}

	public void setDy(double dy) {
		this.dy = dy;
	}

	public double getDx() {
		return dx;
	}

	public void setDx(double dx) {
		this.dx = dx;
	}
}
